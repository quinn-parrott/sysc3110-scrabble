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

    private final List<Player> players;
    private final List<TilePlacement> turns;
    private TileBag gameBag;
    private HashMap<Integer, Character> gamePremiumSquares;

    /**
     * Constructor for the Game class
     * @param players players playing the game
     * @param wordList wordList of legal words for the game
     * @author Quinn Parrott, 101169535
     */
    public Game(List<Player> players, WordList wordList) {
        this.players = players;
        this.turns = new ArrayList<>();
        this.wordList = wordList;
        this.views = new ArrayList<>();
        this.gameBag = new TileBag();
        this.gamePremiumSquares = PremiumSquares.getPremiumSquares();
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
                if (gamePremiumSquares.containsKey(update.playedWords().get(word).get(i))) {
                    var chr = gamePremiumSquares.get(update.playedWords().get(word).get(i));
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

                    gamePremiumSquares.remove(update.playedWords().get(word).get(i));

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
            Optional<WildcardableTile> t = gameBag.drawTile();
            if (t.isPresent()) {
                if (t.get().isWildcard() && this.getPlayer().isAI()) {
                    t = Optional.of(new WildcardableTile('E', 0));
                }
                t.ifPresent(tile -> this.getPlayer().getTileHand().add(tile));
            }
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
        getBoard().printBoard();
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
        var board = new Board();
        for (var placement : this.turns) {
            try {
                board.placeTiles(placement);
            } catch (PlacementException e) {
                throw new RuntimeException("Invalid turns prevented board reconstruction. This should never happen", e);
            }
        }
        return board;
    }

    /**
     * Checks for the first valid horizontal placement of a string on a Board object
     * @param word String representing the word to be played
     * @return Returns the starting index at which to place tiles
     * @author Colin Mandeville, 101140289
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
                Tile tile;
                if (tbs.get(c).tile().isWildcard()) {
                    tile = new Tile ('E', 0);
                } else {
                    tile = new Tile (tbs.get(c).tile().chr(), tbs.get(c).tile().pointValue());
                }
                cPos.ifPresent(position -> tiles.add(new Positioned<>(tile, position)));
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
     * @Author Colin Mandeville, 101140289
     */
    private Optional<TilePlacement> checkVertical(String word) {
        ArrayList<Positioned<Tile>> tiles = new ArrayList<>();
        HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
        for (int i = 0; i < Board.getCOLUMN_NUMBER() * Board.getROW_NUMBER(); i++) {
            tiles.clear();
            for (int j = 0; j < word.length(); j++) {
                char c = word.toCharArray()[j];
                Optional<Position> cPos = Position.FromInts(i, j);
                Tile tile;
                if (tbs.get(c).tile().isWildcard()) {
                    tile = new Tile ('E', 0);
                } else {
                    tile = new Tile (tbs.get(c).tile().chr(), tbs.get(c).tile().pointValue());
                }
                cPos.ifPresent(position -> tiles.add(new Positioned<>(tile, position)));
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
     * @Author Colin Mandeville, 101140289
     */
    private Optional<TilePlacement> boardPlacement(String word) {

        if (getBoard().collectCharSequences().size() == 0) {
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
                Tile tile;
                if (tbs.get(c).tile().isWildcard()) {
                    tile = new Tile ('E', 0);
                } else {
                    tile = new Tile (tbs.get(c).tile().chr(), tbs.get(c).tile().pointValue());
                }
                tiles.add(new Positioned<Tile>(tile, Position.FromInts(Math.floorDiv(Board.getROW_NUMBER(), 2), Math.floorDiv(Board.getCOLUMN_NUMBER(), 2) + i).get()));
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
     * @Author Colin Mandeville, 101140289
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
                } catch (PlacementException ignored) {}
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
