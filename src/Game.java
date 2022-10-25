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

        StringBuilder entry = new StringBuilder();
        for (TilePositioned tile : placement.getTiles()) {
            if (tile.tile().chr() > 64 && tile.tile().chr() < 91) {
                entry.append(tile.tile().chr());
            } else if (tile.tile().chr() == board.getTile(tile.pos()).get().chr()) {
                entry.append(tile.tile().chr());
            }
        }
        if (entry.length() > 1 && !this.wordList.isValidWord(entry.toString())) {
            throw new PlacementException(String.format("'%s' is not a valid word", entry),
                    placement, Optional.of(this.board));
        }

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

            boolean firstOverlap = true;
            for (var tile : tiles) {
                int distance = (int) Math.round(placement.minTileDistance(tile.pos()));

                if (distance == 0) {
                    if (tile.tile().chr() != board.getTile(tile.pos()).get().chr() || !firstOverlap) {
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
        for (TilePositioned tile : placement.getTiles()) {
            if (board.getTile(tile.pos()).get().chr() == tile.tile().chr()) {
                this.players.get(this.turns.size() % this.players.size()).getTileHand().add(TileBagSingleton.getBagDetails().get(tile.tile().chr()).tile());
            }
        }

        if (!this.playerHasNeededTiles(placement.getTiles())) {
            throw new PlacementException("You do not have all needed tiles",
                    placement, Optional.of(board));
        }

        this.board = this.previewPlacement(placement);

        StringBuilder tilesUsed = new StringBuilder();

        for (TilePositioned tile : placement.getTiles()) {
            tilesUsed.append(tile.tile().chr());
        }

        this.removeTilesFromHand(tilesUsed.toString());

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
        this.players.get(this.turns.size() % this.players.size()).addPoints(score);
        this.turns.add(placement);
    }

    /**
     * Allows a player to pass a turn without playing a word
     * @author Colin Mandeville, 101140289
     */
    public void pass() {
        this.turns.add(new TilePlacement(new ArrayList<>()));
    }

    private boolean playerHasNeededTiles(List<TilePositioned> tiles) {
        Player activePlayer = this.players.get(this.turns.size() % this.players.size());

        StringBuilder word = new StringBuilder();

        for (TilePositioned tile : tiles) {
            if (tile.tile().chr() > 64 && tile.tile().chr() < 91) {
                word.append(tile.tile().chr());
            }
        }

        Tile matchTile;

        for (int i = 0; i < word.length(); i++) {
            matchTile = null;
            for (Tile tile : activePlayer.getTileHand()) {
                if (tile.chr() == word.charAt(i) && matchTile == null) {
                    matchTile = tile;
                }
            }
            if (matchTile == null) {
                return false;
            }
        }
        return true;
    }

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
}
