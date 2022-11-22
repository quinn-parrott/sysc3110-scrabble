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
    private int numPassesSequentially;

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
        this.numPassesSequentially = 0;
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
        this.turns.add(placement);
        this.numPassesSequentially = 0;
        for (GameView view : this.views) {
            view.update();
        }
        int PLAYER_HAND_SIZE = 7;
        for (int i = 0; i < PLAYER_HAND_SIZE; i++) {
            Optional<Tile> t = gameBag.drawTile();
            t.ifPresent(tile -> this.getPlayer().getTileHand().add(tile));
        }
    }

    /**
     * Allows a player to pass a turn without playing a word
     * @author Colin Mandeville, 101140289
     */
    public void pass() {
        this.numPassesSequentially++;
        this.turns.add(new TilePlacement(new ArrayList<>()));
        for (GameView view : this.views) {
            view.update();
        }
        if (this.numPassesSequentially == this.players.size()) {
            ArrayList<Player> winners = new ArrayList();
            winners.add(this.players.get(0));
            for (int i = 1; i < this.players.size(); i++) {
                if (winners.get(0).getPoints() < this.players.get(i).getPoints()) {
                    winners.clear();
                    winners.add(this.players.get(i));
                } else if (winners.get(0).getPoints() == this.players.get(i).getPoints()) {
                    winners.add(this.players.get(i));
                }
            }
            for (GameView view : this.views) {
                view.endGame(winners);
            }
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
}
