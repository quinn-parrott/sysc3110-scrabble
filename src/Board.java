import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

/**
 * @author Colin Mandeville, 101140289
 */
public class Board {
    private static final int COLUMN_NUMBER = 15;
    private static final int ROW_NUMBER = 15;

    private final ArrayList<Tile> board; // TODO: Rename to something more descriptive

    /**
     * Board Constructor, defines ArrayList to contain Board and sets each square to an arbitrary _ on each tile,
     * with * marking the center square
     *
     * @author Colin Mandeville, 101140289
     */
    public Board() {
        this.board = new ArrayList<>();
        for (int i = 0; i < COLUMN_NUMBER * ROW_NUMBER; i++) {
            if (i == getCenterTilePos()) {
                this.board.add(new Tile('*', 0));
            } else {
                this.board.add(new Tile('_', 0));
            }
        }
    }

    public static int getCenterTilePos() {
        return Math.floorDiv(COLUMN_NUMBER * ROW_NUMBER, 2);
    }
// TODO: CLEAN UP GETTERS AND SETTERS TO ALL USE POSITION
    public void setTile(Tile tile, Position pos) {
        setTile(tile, pos.getX(), pos.getY());
    }

    public void setTile(Tile tile, int x, int y) {
        if (Position.FromInts(x, y).isPresent()) {
            setTile(tile, Position.FromInts(x, y).get().getIndex());
        }
    }

    public void setTile(Tile tile, int index) {
        this.board.set(index, tile);
    }

    public Optional<Tile> getTile(Position pos) {
        return getTile(pos.getX(), pos.getY());
    }

    public Optional<Tile> getTile(int x, int y) {
        Optional<Position> i = Position.FromInts(x, y);
        return i.map(position -> this.board.get(position.getIndex()));
    }

    public static int getCOLUMN_NUMBER() {
        return COLUMN_NUMBER;
    }

    public static int getROW_NUMBER() {
        return ROW_NUMBER;
    }

    /**
     * Place a TilePlacement on the board
     *
     * @author Quinn Parrott, 101169535
     */
    public void placeTiles(TilePlacement tilePlacement) throws PlacementException {
        // Check that all tiles are placeable
        for (var tile : tilePlacement.getTiles()) {
            var tileOpt = getTile(tile.pos());
            if (tileOpt.isEmpty()) {
                throw new PlacementException(String.format("'%s' is outside the board", tile.pos()), tilePlacement, Optional.of(this));
            }
            if (tileOpt.get().isFilledWithLetter()) {
                throw new PlacementException(String.format("'%s' already has a letter", tile.pos()), tilePlacement, Optional.of(this));
            }
        }

        // Place the tiles
        for (var tile : tilePlacement.getTiles()) {
            setTile(tile.tile(), tile.pos());
        }
    }

    /**
     * Prints the current board state to console
     *
     * @author Colin Mandeville, 101140289
     */
    public void printBoard() {
        // TODO: This print function seems to print the wrong coordinate system (x/y backwards)?
        // Print each row of the board and the Vertical Legend
        for (int i = 0; i < ROW_NUMBER; i++) {
            System.out.printf("%2s ", i + 1);

            // Print each tile in row i
            for (int j = 0; j < COLUMN_NUMBER; j++) {
                System.out.print(this.board.get(15 * i + j).chr() + "  ");
            }

            System.out.println();
        }

        // Print the Horizontal Legend
        System.out.print("   ");
        for (int i = 0; i < COLUMN_NUMBER; i++) {
            int j = i + 'A';
            char c = (char) j;
            System.out.print(c + "  ");
        }
        System.out.println();
    }

    /**
     * Get positioned tiles
     *
     * @author Quinn Parrott, 101169535
     */
    public ArrayList<TilePositioned> getTiles() {
        // TODO: This could be much more efficient if a data structure such as a HashMap was used instead of ArrayList
        var result = new ArrayList<TilePositioned>(this.board.size());

        int i = 0;
        for (var tile : this.board) {
            if (tile.isFilledWithLetter()) { // Ignore empty tiles
                result.add(new TilePositioned(tile, Position.FromIndex(i).get()));
            }
            i++;
        }

        return result;
    }

    /**
     * Get all vertical and horizontal sequences of characters on the board.
     *
     * @author Quinn Parrott, 101169535
     */
    public HashSet<String> collectCharSequences() {
        var results = new HashSet<String>();

        for (int x = 0; x < COLUMN_NUMBER; x++) {
            var seq = "";
            for (int y = 0; y < ROW_NUMBER; y++) {
                var pos = Position.FromInts(x, y).get();
                var tile = getTile(pos).get();
                if (tile.isFilledWithLetter()) {
                    seq += tile.chr();
                } else {
                    if (seq.length() > 0) {
                        results.add(seq);
                        seq = "";
                    }
                }
            }

            // TODO: Is there a way to get rid of this final check (dedup with inner loop)
            if (seq.length() > 0) {
                results.add(seq);
                seq = "";
            }
        }

        // TODO: Dedup with other loop
        for (int y = 0; y < ROW_NUMBER; y++) {
            var seq = "";
            for (int x = 0; x < COLUMN_NUMBER; x++) {
                var pos = Position.FromInts(x, y).get();
                var tile = getTile(pos).get();
                if (tile.isFilledWithLetter()) {
                    seq += tile.chr();
                } else {
                    if (seq.length() > 0) {
                        results.add(seq);
                        seq = "";
                    }
                }
            }

            // TODO: Is there a way to get rid of this final check (dedup with inner loop)
            if (seq.length() > 0) {
                results.add(seq);
                seq = "";
            }
        }

        return results;
    }

    /**
     * Clone the board instance
     *
     * @author Quinn Parrott, 101169535
     */
    public Board clone() {
        // TODO: Can this be done in a way that's compile time
        //  checked and generally assumes less about the implementation?
        var newBoard = new Board();

        int i = 0;
        for (var tile : this.board) {
            newBoard.setTile(tile, i);
            i += 1;
        }

        return newBoard;
    }
}
