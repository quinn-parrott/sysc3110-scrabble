import java.util.*;

/**
 * Core game engine. This implements all game state.
 *
 * @author Quinn Parrott, 101169535
 */
public class Game {
    private final List<Player> players;
    private final ArrayList<String> wordsPlayed;
    private final ArrayList<String> newWords;
    private final List<TilePlacement> turns;
    private ArrayList<GameView> views;
    private TileBag gameBag;
    public WordList wordList;
    private Board board; // TODO: Can be removed if reconstructed each round (superfluous)?

    /**
     * Constructor for the Game class
     * @param players players playing the game
     * @param wordList wordList of legal words for the game
     * @author Quinn Parrott, 101169535
     */
    public Game(List<Player> players, WordList wordList) {
        this.players = players;
        this.wordsPlayed = new ArrayList<>();
        this.newWords = new ArrayList<>();
        this.turns = new ArrayList<>();
        this.board = new Board();
        this.wordList = wordList;
        this.views = new ArrayList<>();
        this.gameBag = new TileBag();
        int PLAYER_HAND_SIZE = 7;
        for (Player player : this.players) {
            for (int i = 0; i < PLAYER_HAND_SIZE; i++) {
                player.getTileHand().add(gameBag.drawTile().get());
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
    public Board previewPlacement(TilePlacement placement) throws PlacementException {
        var nextBoard = this.board.clone();
        newWords.clear();

        if (wordsPlayed.size() == 0) {
            // First turn
            var p = Position.FromIndex(Board.getCenterTilePos()).orElseThrow();
            var distanceFromCenter = (int) placement.minTileDistance(p);
            if (distanceFromCenter > 0) {
                throw new PlacementException("At least one tile must intercept with the center on the first turn",
                        placement, Optional.of(this.board));
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
                                "tile there", tile.pos()), placement, Optional.of(this.board));
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

        nextBoard.placeTiles(placement);

        for (String word : nextBoard.collectCharSequences()) {
            if (!this.wordsPlayed.contains(word)) {
                newWords.add(word);
            }
            if (word.length() > 1 && !this.wordList.isValidWord(word)) {
                throw new PlacementException(String.format("'%s' is not a valid word", word),
                        placement, Optional.of(this.board));
            }
        }

        if (!this.playerHasNeededTiles(placement.getTiles())) {
            throw new PlacementException("You do not have all needed tiles",
                    placement, Optional.of(board));
        }

        return nextBoard;
    }

    /**
     * Places a TilePlacement object on the board
     * @param placement placement object to be applied to the current board
     * @author Quinn Parrott, 101169535, and Colin Mandeville, 101140289
     */
    public void place(TilePlacement placement) throws PlacementException {
        this.board = this.previewPlacement(placement);

        StringBuilder tilesUsed = new StringBuilder();

        for (var tile : placement.getTiles()) {
            tilesUsed.append(tile.value().chr());
        }

        this.removeTilesFromHand(tilesUsed.toString());

        HashMap<Character, TileBagDetails> tileDetails = TileBagSingleton.getBagDetails();
        int score = 0;

        for (String word : newWords) {
            for (int i = 0; i < word.length(); i++) {
                score += tileDetails.get(word.charAt(i)).tile().pointValue();
            }
            this.wordsPlayed.add(word);
        }
        this.players.get(this.turns.size() % this.players.size()).addPoints(score);
        int PLAYER_HAND_SIZE = 7;
        for (int i = 0; i < PLAYER_HAND_SIZE; i++) {
            Optional<WildcardableTile> t = gameBag.drawTile();
            t.ifPresent(tile -> this.getPlayer().getTileHand().add(tile));
        }
        this.turns.add(placement);
        for (GameView view : this.views) {
            view.update();
        }
    }

    /**
     * Allows a player to pass a turn without playing a word
     * @author Colin Mandeville, 101140289
     */
    public void pass() {
        this.turns.add(new TilePlacement(new ArrayList<>()));
        for (GameView view : this.views) {
            view.update();
        }
    }

    /**
     * Checks if the active Player has the required tiles to make their move.
     * @param tiles tiles being used to create the word
     * @return Returns a boolean representing if the player has all required tiles to make their word
     * @author Colin Mandeville, 101140289
     */
    private boolean playerHasNeededTiles(List<Positioned<Tile>> tiles) {
        Player activePlayer = this.players.get(this.turns.size() % this.players.size());

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
        Player activePlayer = this.players.get(this.turns.size() % this.players.size());

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
        this.board.printBoard();
        System.out.println("Scores (Player : Points)");
        for (Player player : this.players) {
            System.out.println(player.getName() + " : " + player.getPoints());
        }
    }

    public int getNumTurns() {
        return this.turns.size();
    }

    public Player getPlayer() {
        return this.players.get(this.turns.size() % this.players.size());
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Board getBoard() {
        return this.board;
    }
}
