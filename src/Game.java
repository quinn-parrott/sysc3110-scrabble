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

        public GameMutableState clone() {
            // Beware ye who seek to enter below:
            // beyond lay cloning evils so vile that the faint of heart may perish.
            try {
                // The only reliable way to make a _true_ deep copy in java is to serialize
                // then deserialize the objects.
                var stream = new ByteArrayOutputStream();
                var output = new ObjectOutputStream(stream);
                output.writeObject(this);

                var input = new ObjectInputStream(new ByteArrayInputStream(stream.toByteArray()));
                return (GameMutableState) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
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

        var playersTemp = new ArrayList<>(players);
        var tileBag = new TileBag();

        int PLAYER_HAND_SIZE = 7;
        for (Player player : playersTemp) {
            for (int i = 0; i < PLAYER_HAND_SIZE; i++) {
                player.getTileHand().add(tileBag.drawTile().get());
            }
        }

        this.state = new Transactionable<>(new GameMutableState(
            playersTemp,
            new ArrayList<>(),
            tileBag,
            PremiumSquares.getPremiumSquares()
        ));
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
                if (this.state.state(GameMutableState::clone).gamePremiumSquares.containsKey(update.playedWords().get(word).get(i))) {
                    var chr = this.state.state(GameMutableState::clone).gamePremiumSquares.get(update.playedWords().get(word).get(i));
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

                    this.state.state(GameMutableState::clone).gamePremiumSquares.remove(update.playedWords().get(word).get(i));

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
            Optional<WildcardableTile> t = this.state.state(GameMutableState::clone).gameBag.drawTile();
            if (t.isPresent()) {
                if (t.get().isWildcard() && this.getPlayer().isAI()) {
                    t = Optional.of(new WildcardableTile('E', 0));
                }
                t.ifPresent(tile -> this.getPlayer().getTileHand().add(tile));
            }
        }

        this.state.state(GameMutableState::clone).turns.add(placement);
        this.update();
    }

    /**
     * Allows a player to pass a turn without playing a word
     * @author Colin Mandeville, 101140289
     */
    public void pass() {
        this.state.state(GameMutableState::clone).turns.add(new TilePlacement(new ArrayList<>()));
        this.update();
    }

    public void commit() {
        this.state.commitWorking(GameMutableState::clone); // Commit the state change
    }

    public void runAi() {
        while (this.getPlayer().isAI()) {
            (new AiPlayer(this)).AiTurn();
        }
    }

    private void update() {
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
        return this.state.state(GameMutableState::clone).turns.size();
    }

    public Player getPlayer() {
        return getPlayers().get(getNumTurns() % getPlayers().size());
    }

    public List<Player> getPlayers() {
        return this.state.state(GameMutableState::clone).players;
    }

    /**
     * Method to save current board state into a file
     * @param filename String representing the name of the file being saved
     */
    public void saveGame(String filename) throws IOException {
        File file = new File(filename);
        FileOutputStream f = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            f = new FileOutputStream(file.getName());
        } catch (Exception ignored) {}
        try {
            assert f != null;
            f.write(this.toXML().toString().getBytes());
            f.close();
        } catch (Exception ignored) {}
    }

    /**
     * Converts the Game instance into XML data
     * @return Returns a String representing the XML data of the Game instance
     */
    private String toXML() {
        int numTabs = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("<Game>\n");
        for (Player p : players) {
            sb.append(p.toXML(numTabs + 1));
        }
        for (TilePlacement tp : turns) {
            sb.append(tp.toXML(numTabs + 1));
        }
        sb.append("    ").append("<TileBag>\n").append(gameBag.toXML(numTabs + 1));
        sb.append("    </TileBag>\n").append("    <PremiumSquares>\n");
        for (int key : gamePremiumSquares.keySet()) {
            sb.append("        ").append("<Premium index=\"").append(key).append("\" char=\"");
            sb.append(gamePremiumSquares.get(key)).append("\"></Premium>\n");
        }
        sb.append("    </PremiumSquares>\n").append("</Game>");
        return sb.toString();
    }

    public void loadGame(String filename) {

    }

    public Board getBoard() {
        var board = new Board();
        for (var placement : this.state.state(GameMutableState::clone).turns) {
            try {
                board.placeTiles(placement);
            } catch (PlacementException e) {
                throw new RuntimeException("Invalid turns prevented board reconstruction. This should never happen", e);
            }
        }
        return board;
    }

    public void popCommitToWorking() {
        this.state.popCommitToWorking();
        this.update();
    }

    public boolean canUndo() {
        return this.state.canUndo();
    }

    public boolean canRedo() {
        return this.state.canRedo();
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

    public static void main(String[] args) throws PlacementException {
        Player p = new Player("Colin", false);
        p.addTile(new WildcardableTile('W', 4));
        p.addTile(new WildcardableTile('E', 1));
        p.addTile(new WildcardableTile('F', 4));
        Player p2 = new Player("Quinn", false);
        List<Player> l = new ArrayList<>();
        l.add(p);
        l.add(p2);
        Game g = new Game(l, new WordList());
        List<Positioned<Tile>> turn = new ArrayList<>();
        turn.add(new Positioned<>(new Tile(p.getTileHand().get(2).chr(), p.getTileHand().get(2).pointValue()), Position.FromIndex(Board.getCenterTilePos()).get()));
        turn.add(new Positioned<>(new Tile(p.getTileHand().get(1).chr(), p.getTileHand().get(1).pointValue()), Position.FromIndex(Board.getCenterTilePos() + 1).get()));
        turn.add(new Positioned<>(new Tile(p.getTileHand().get(0).chr(), p.getTileHand().get(0).pointValue()), Position.FromIndex(Board.getCenterTilePos() + 2).get()));
        g.place(TilePlacement.FromTiles(turn).get());
        System.out.println(g.toXML());
    }
}
