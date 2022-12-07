import java.io.*;
import java.util.*;

/**
 * Core game engine. This implements all game state.
 *
 * @author Quinn Parrott, 101169535
 */
public class Game {
    public record GameUpdateState(Board board, ArrayList<String> newWords, HashMap<String, ArrayList<Integer>> playedWords) {};

    private ArrayList<GameView> views;
    private WordList wordList;


    private static class GameMutableState implements Serializable {
        public ArrayList<Player> players;
        public ArrayList<TilePlacement> turns;
        public TileBag gameBag;
        public HashMap<Integer, Character> gamePremiumSquares;

        public GameMutableState(
            ArrayList<Player> players,
            ArrayList<TilePlacement> turns,
            TileBag gameBag,
            HashMap<Integer, Character> gamePremiumSquares
        ) {
            this.players = players;
            this.turns = turns;
            this.gameBag = gameBag;
            this.gamePremiumSquares = gamePremiumSquares;
        }

        @Override
        public GameMutableState clone() {
            try {
                GameMutableState clone = (GameMutableState) super.clone();
                clone.players = (ArrayList<Player>) this.players.clone();
                clone.turns = (ArrayList<TilePlacement>) this.turns.clone();
                clone.gameBag = this.gameBag.clone();
                clone.gamePremiumSquares = (HashMap<Integer, Character>) this.gamePremiumSquares.clone();
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    private Transactionable<GameMutableState> state;

    /**
     * Constructor for the Game class
     * @param players players playing the game
     * @param wordList wordList of legal words for the game
     * @author Quinn Parrott, 101169535
     */
    public Game(List<Player> players, WordList wordList) {
        this.wordList = wordList;
        this.views = new ArrayList<>();

        this.state = new Transactionable<>(new GameMutableState(
                 new ArrayList<>(players),
                 new ArrayList<>(),
                 new TileBag(),
                 PremiumSquares.getPremiumSquares()
        ));

        int PLAYER_HAND_SIZE = 7;
        for (Player player : this.state.state().players) {
            for (int i = 0; i < PLAYER_HAND_SIZE; i++) {
                player.getTileHand().add(this.state.state().gameBag.drawTile().get());
            }
        }
    }

    /**
     * Setting the view for model
     *
     * @param gv GameView
     */
    public void addGameView(GameView gv){
        views.add(gv);
    }

    /**
     * Verifies if the TilePlacement object can be placed on the board
     * @param placement placement which will be applied to the current board state
     * @return Returns a Board object with the TilePlacement parameter placed on it
     * @author Quinn Parrott, 101169535, and Colin Mandeville, 101140289
     */
    public GameUpdateState previewPlacement(TilePlacement placement) throws PlacementException {
        var board = getBoard();
        var playedWords = board.collectCharSequences();

        if (playedWords.size() == 0) {
            // First turn
            var p = Position.FromIndex(Board.getCenterTilePos()).orElseThrow();
            var distanceFromCenter = (int) placement.minTileDistance(p);
            if (distanceFromCenter > 0) {
                throw new PlacementException("At least one tile must intercept with the center on the first turn",
                        placement, Optional.of(board));
            }
        } else {
            enum MinResult {
                TooFar,
                Adjacent,
            }

            var minResult = MinResult.TooFar;

            var tiles = board.getTiles();

            boolean firstOverlap = true;
            for (var tile : tiles) {
                int distance = (int) Math.round(placement.minTileDistance(tile.pos()));

                if (distance == 0) {
                    if (board.getTile(tile.pos()).isPresent() || !firstOverlap) {
                        throw new PlacementException(String.format("Can't place tile at %s since there is already a " +
                                "tile there", tile.pos()), placement, Optional.of(board));
                    } else {
                        firstOverlap = false;
                    }
                }

                if (distance == 1) {
                    minResult = MinResult.Adjacent;
                }
            }

            if (minResult != MinResult.Adjacent) {
                throw new PlacementException("New tiles aren't adjacent to existing tiles",
                        placement, Optional.of(board));
            }
        }

        // TODO: Probably don't need to clone this?
        var nextBoard = board.clone();

        nextBoard.placeTiles(placement);

        // Check that the TilePlacement does not contain any spaces
        {
            var tiles = placement.getTiles();
            var firstTile = tiles.get(0);
            var lastTile = tiles.get(tiles.size() - 1);
            for (var pos : Position.Interpolate(firstTile.pos(), lastTile.pos()).get()) {
                if (nextBoard.getTile(pos).isEmpty()) {
                    throw new PlacementException("There can't be spaces in the placed word",
                            placement, Optional.of(board));
                }
            }
        }

        var nextWords = nextBoard.collectCharSequences();
        ArrayList<String> newWords = new ArrayList<>();

        for (String word : nextWords.keySet()) {
            if (!playedWords.containsKey(word)) {
                newWords.add(word);
            }
            if (word.length() > 1 && !this.wordList.isValidWord(word)) {
                throw new PlacementException(String.format("'%s' is not a valid word", word),
                        placement, Optional.of(board));
            }
        }

        if (!this.playerHasNeededTiles(placement.getTiles())) {
            throw new PlacementException("You do not have all needed tiles",
                    placement, Optional.of(board));
        }

        return new GameUpdateState(nextBoard, newWords, nextWords);
    }

    /**
     * Places a TilePlacement object on the board
     * @param placement placement object to be applied to the current board
     * @author Quinn Parrott, 101169535, and Colin Mandeville, 101140289
     */
    public void place(TilePlacement placement) throws PlacementException {
        var update = this.previewPlacement(placement);

        StringBuilder tilesUsed = new StringBuilder();

        for (var tile : placement.getTiles()) {
            tilesUsed.append(tile.value().chr());
        }

        this.removeTilesFromHand(tilesUsed.toString());

        HashMap<Character, TileBagDetails> tileDetails = TileBagSingleton.getBagDetails();
        int score = 0;

        for (String word : update.newWords()) {
            int tileScore = 0;
            int wordMultiplier = 1;
            int wordscore = 0;
            for (int i = 0; i < word.length(); i++) {
                if (this.state.state().gamePremiumSquares.containsKey(update.playedWords().get(word).get(i))) {
                    var chr = this.state.state().gamePremiumSquares.get(update.playedWords().get(word).get(i));
                    switch (chr){

                        case '$':
                            tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue()) * 2; break;
                        case '%':
                            tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue()) * 3; break;
                        case '@':
                            wordMultiplier *= 2;
                            tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue()); break;
                        case '#':
                            wordMultiplier *= 3;
                            tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue()); break;

                    }

                    this.state.state().gamePremiumSquares.remove(update.playedWords().get(word).get(i));

                }else{
                    tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue());
                }
            }
            wordscore = tileScore * wordMultiplier;
            score += wordscore;

        }

        this.getPlayer().addPoints(score);
        int currentPlayerHandSize= this.getPlayer().getTileHand().size();
        int PLAYER_HAND_SIZE = 7;
        int numTilesToAdd = PLAYER_HAND_SIZE - currentPlayerHandSize;
        for (int i = 0; i < numTilesToAdd; i++) {
            Optional<WildcardableTile> t = this.state.state().gameBag.drawTile();
            if (t.isPresent()) {
                if (t.get().isWildcard() && this.getPlayer().isAI()) {
                    t = Optional.of(new WildcardableTile('E', 0));
                }
                t.ifPresent(tile -> this.getPlayer().getTileHand().add(tile));
            }
        }

        this.state.state().turns.add(placement);
        this.state.pushCopy(this.state.state().clone()); // Commit the state change
        this.update();
    }

    /**
     * Allows a player to pass a turn without playing a word
     * @author Colin Mandeville, 101140289
     */
    public void pass() {
        this.state.state().turns.add(new TilePlacement(new ArrayList<>()));
        this.state.pushCopy(this.state.state().clone()); // Commit the state change
        this.update();
    }

    private void update() {
        for (GameView view : this.views) {
            view.update();
        }
        if (this.getPlayer().isAI()) {
            (new AiPlayer(this)).AiTurn();
            this.update();
        }
    }

    /**
     * Checks if the active Player has the required tiles to make their move.
     * @param tiles tiles being used to create the word
     * @return Returns a boolean representing if the player has all required tiles to make their word
     * @author Colin Mandeville, 101140289
     */
    private boolean playerHasNeededTiles(List<Positioned<Tile>> tiles) {
        Player activePlayer = this.getPlayer();

        var letters = new ArrayList<>();

        int wildcards = 0;
        for (WildcardableTile tile : activePlayer.getTileHand()) {
            if (tile.isWildcard()) {
                wildcards++;
            } else {
                Character chr = tile.chr();
                letters.add(chr);
            }
        }

        for (var tile : tiles) {
            Character chr = tile.value().chr();
            if (!letters.remove(chr)) {
                wildcards--;
                if (wildcards < 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Removes all used tiles from the players hand of tiles
     * @param letters The string of letters being used by the player
     * @author Colin Mandeville, 101140289
     */
    private void removeTilesFromHand(String letters) {
        Player activePlayer = this.getPlayer();

        for (char c : letters.toCharArray()) {

            boolean found = false;
            for (WildcardableTile tile: activePlayer.getTileHand()) {
                if (!tile.isWildcard() && tile.chr() == c) {
                    activePlayer.getTileHand().remove(tile);
                    found = true;
                    break;
                }
            }

            if (!found) {
                for (WildcardableTile tile: activePlayer.getTileHand()) {
                    if (tile.isWildcard()) {
                        activePlayer.getTileHand().remove(tile);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Prints the current board and scoreboard
     * @author Colin Mandeville, 101140289
     */
    public void printBoardState() {
        getBoard().printBoard();
        System.out.println("Scores (Player : Points)");
        for (Player player : getPlayers()) {
            System.out.println(player.getName() + " : " + player.getPoints());
        }
    }

    public int getNumTurns() {
        return this.state.state().turns.size();
    }

    public Player getPlayer() {
        return getPlayers().get(getNumTurns() % getPlayers().size());
    }

    public List<Player> getPlayers() {
        return this.state.state().players;
    }

    public Board getBoard() {
        var board = new Board();
        for (var placement : this.state.state().turns) {
            try {
                board.placeTiles(placement);
            } catch (PlacementException e) {
                throw new RuntimeException("Invalid turns prevented board reconstruction. This should never happen", e);
            }
        }
        return board;
    }

    public void discardLastTurnFromUndo() {
        this.state.discardLastCommit();
    }

    public boolean undo() {
        var result = this.state.undo();
        this.update();
        return result;
    }

    public boolean redo() {
        var result = this.state.redo();
        this.update();
        return result;
    }

}
