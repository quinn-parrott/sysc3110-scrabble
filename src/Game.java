import java.util.*;

/**
 * @author Quinn Parrott, 101169535
 */
public class Game {
    private final ArrayList<Player> players;
    private final ArrayList<String> wordsPlayed;
    private final ArrayList<String> newWords;
    private final List<TilePlacement> turns;
    public WordList wordList;
    private Board board; // TODO: Can be removed if reconstructed each round (superfluous)?

    /**
     * Constructor for the Game class
     * @author Quinn Parrott, 101169535
     */
    public Game(List<Player> players, WordList wordList) {
        this.players = (ArrayList<Player>) players;
        this.wordsPlayed = new ArrayList<>();
        this.newWords = new ArrayList<>();
        this.turns = new ArrayList<>();
        this.board = new Board();
        this.wordList = wordList;
    }

    /**
     * Verifies if the TilePlacement object can be placed on the board
     * @author Quinn Parrott, 101169535, and Colin Mandeville, 101140289
     */
    public Board previewPlacement(TilePlacement placement) throws PlacementException {
        var nextBoard = this.board.clone();
        newWords.clear();

        if (turns.size() == 0) {
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
            for (var tile : tiles) {
                int distance = (int) Math.round(placement.minTileDistance(tile.pos()));

                if (distance == 0) {
                    throw new PlacementException(String.format("Can't place tile at %s since there is already a tile" +
                                    " there", tile.pos()), placement, Optional.of(this.board));
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

        for (String next : nextBoard.collectCharSequences()) {
            if (!this.wordsPlayed.contains(next)) {
                newWords.add(next);
            }
        }
        return nextBoard;
    }

    /**
     * Places a TilePlacement object on the board
     * @author Quinn Parrott, 101169535, and Colin Mandeville, 101140289
     */
    public void place(TilePlacement placement) throws PlacementException {
        this.board = this.previewPlacement(placement);
        this.turns.add(placement);

        HashMap<Character, TileBagDetails> tileDetails = TileBagSingleton.getBagDetails();
        int score = 0;

        for (String word : newWords) {
            if (word.length() > 1 && !this.wordList.isValidWord(word)) {
                throw new PlacementException(String.format("'%s' is not a valid word", word),
                        placement, Optional.of(this.board));
            } else {
                for (int i = 0; i < word.length(); i++) {
                    score += tileDetails.get(word.charAt(i)).tile().pointValue();
                }
                this.wordsPlayed.add(word);
            }
        }
        this.players.get(this.turns.size() % this.players.size() - 1).addPoints(score);
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
}
