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

        for (Positioned<Tile> tile : placement.getTiles()) {
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
            Optional<Tile> t = gameBag.drawTile();
            t.ifPresent(tile -> this.getPlayer().getTileHand().add(tile));
        }
        this.turns.add(placement);
        this.update();
    }

    /**
     * Allows a player to pass a turn without playing a word
     * @author Colin Mandeville, 101140289
     */
    public void pass() {
        this.turns.add(new TilePlacement(new ArrayList<>()));
        this.update();
    }

    private void update() {
        for (GameView view : this.views) {
            view.update();
        }
        if (this.getPlayer().isAI()) {
            this.AITurn();
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
        Player activePlayer = this.players.get(this.turns.size() % this.players.size());

        StringBuilder word = new StringBuilder();

        for (Positioned<Tile> tilePositioned : tiles) {
            var chr = tilePositioned.value().chr();
            if (chr > 64 && chr < 91) {
                word.append(chr);
            }
        }

        Tile matchTile;

        for (int i = 0; i < word.length(); i++) {
            matchTile = null;
            for (Tile tile : activePlayer.getTileHand()) {
                if (matchTile == null && tile.chr() == word.charAt(i)) {
                    matchTile = tile;
                }
            }
            if (matchTile == null) {
                return false;
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
            for (Tile tile: activePlayer.getTileHand()) {
                if (tile.chr() == c) {
                    activePlayer.getTileHand().remove(tile);
                    break;
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

    /**
     * Checks for the first valid horizontal placement of a string on a Board object
     * @param word String representing the word to be played
     * @return Returns the starting index at which to place tiles
     */
    private Optional<TilePlacement> checkHorizontal(String word) {
        HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
        for (int i = 0; i < Board.getROW_NUMBER() * Board.getCOLUMN_NUMBER() - word.length(); i++) {
            if (i % Board.getROW_NUMBER() > Board.getROW_NUMBER() - word.length()) {
                i += word.length();
                continue;
            }
            ArrayList<Positioned<Tile>> tiles = new ArrayList<>();
            for (int j = 0; j < word.length(); j++) {
                char c = word.toCharArray()[j];
                Optional<Position> cPos = Position.FromIndex(j + i);
                cPos.ifPresent(position -> tiles.add(new Positioned<Tile>(tbs.get(c).tile(), position)));
            }
            Optional<TilePlacement> tp = TilePlacement.FromTiles(tiles);
            if (tp.isPresent()) {
                try {
                    this.previewPlacement(tp.get());
                    return tp;
                } catch (PlacementException ignored) {}
            }
        }
        return Optional.empty();
    }

    /**
     * Checks for the first valid vertical placement of a string on a Board object
     * @param word String representing the word to be played
     * @return Returns the starting index at which to place tiles
     */
    private Optional<TilePlacement> checkVertical(String word) {
        ArrayList<Positioned<Tile>> tiles = new ArrayList<>();
        HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
        for (int i = 0; i < Board.getCOLUMN_NUMBER() * Board.getROW_NUMBER(); i++) {
            tiles.clear();
            for (int j = 0; j < word.length(); j++) {
                char c = word.toCharArray()[j];
                Optional<Position> cPos = Position.FromInts(i, j);
                cPos.ifPresent(position -> tiles.add(new Positioned<Tile>(tbs.get(c).tile(), position)));
            }
            Optional<TilePlacement> tp = TilePlacement.FromTiles(tiles);
            if (tp.isPresent()) {
                try {
                    this.previewPlacement(tp.get());
                    return tp;
                } catch (PlacementException ignored) {}
            }
        }
        return Optional.empty();
    }

    /**
     * Determines if there is a placement for a passed word on the current board state of the game object
     * @param word A word the AI player is trying to play
     * @return Returns an optional of a TilePlacement object, which is empty if no placement of the given word is
     * possible
     */
    private Optional<TilePlacement> boardPlacement(String word) {

        if (this.wordsPlayed.size() == 0) {
            ArrayList<Positioned<Tile>> tiles = new ArrayList<>();
            ArrayList<String> words = this.getPlayer().getPossibleWords(0);
            words.sort(new PointComparator());
            String first = "";
            if (words.size() > 0) {
                first = words.get(0);
            }
            HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
            for (int i = 0; i < first.length(); i++) {
                char c = first.toUpperCase().toCharArray()[i];
                tiles.add(new Positioned<Tile>(tbs.get(c).tile(), Position.FromInts(Math.floorDiv(Board.getROW_NUMBER(), 2), Math.floorDiv(Board.getCOLUMN_NUMBER(), 2) + i).get()));
            }
            return TilePlacement.FromTiles(tiles);
        }
        Random r = new Random();
        if (r.nextBoolean()) {
            Optional<TilePlacement> tp = checkHorizontal(word);
            if (tp.isEmpty()) {
                tp = checkVertical(word);
            }
            return tp;
        } else {
            Optional<TilePlacement> tp = checkVertical(word);
            if (tp.isEmpty()) {
                tp = checkHorizontal(word);
            }
            return tp;
        }
    }

    /**
     * Function call to make a move for the AI player, either playing their highest scoring word, or passing if no word
     * is possible to play
     */
    public void AITurn() {
        ArrayList<String> possibleWords = this.getPlayer().getPossibleWords(1);
        boolean hasPlayed = false;
        possibleWords.sort(new PointComparator());
        for (String word : possibleWords) {
            Optional<TilePlacement> tp = this.boardPlacement(word);
            if (tp.isPresent()) {
                try {
                    this.place(tp.get());
                    hasPlayed = true;
                    break;
                } catch (PlacementException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (!hasPlayed) {
            this.pass();
        }
    }

    public static class PointComparator implements java.util.Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
            int score1 = 0;
            int score2 = 0;
            for (int i = 0; i < o1.length(); i++) {
                score1 += tbs.get(o1.toUpperCase().toCharArray()[i]).tile().pointValue();
            }
            for (int i = 0; i < o2.length(); i++) {
                score2 += tbs.get(o2.toUpperCase().toCharArray()[i]).tile().pointValue();
            }
            if (score1 > score2) {
                return -1;
            } else if (score2 > score1) {
                return 1;
            }
            return 0;
        }
    }

}
