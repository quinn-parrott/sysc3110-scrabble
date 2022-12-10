import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Class representing a scrabble board with tiles on it
 *
 * @author Colin Mandeville, 101140289
 */
public class Board implements Cloneable {
    private static final int COLUMN_NUMBER = 15;
    private static final int ROW_NUMBER = 15;
    private boolean isCustomBoard;

    private final ArrayList<Optional<Tile>> board; // TODO: Rename to something more descriptive

    /**
     * Board Constructor, defines ArrayList to contain Board and sets each square to an arbitrary _ on each tile,
     * with * marking the center square
     * @author Colin Mandeville, 101140289
     */
    public Board() {
        this(false);
    }

    public Board(boolean isCustomBoard) {
        this.board = new ArrayList<>();
        this.isCustomBoard = isCustomBoard;
        for (int i = 0; i < COLUMN_NUMBER * ROW_NUMBER; i++) {
            this.board.add(Optional.empty());
        }
    }

    public boolean isCustomBoard() {
        return isCustomBoard;
    }

    /**
     * Calculates the middle square of the board ArrayList
     * @return int equal to the index of the center tile
     * @author Colin Mandeville, 101140289
     */
    public static int getCenterTilePos() {
        return Math.floorDiv(COLUMN_NUMBER * ROW_NUMBER, 2);
    }

    /**
     * Sets a tile object to be contained at a specific pos
     * @param tile tile to be set to the position
     * @param pos pos where the tile will be set
     * @author Quinn Parrott, 101169535
     */
    public void setTile(Tile tile, Position pos) {
        setTile(tile, pos.getX(), pos.getY());
    }

    /**
     * Sets a tile object to be contained at a specific x and y coordinate, where y
     * represents row and x represents column
     * @param tile tile to be set to the x and y coordinate
     * @param x x coordinate representing the column number
     * @param y y coordinate representing the row number
     * @author Quinn Parrott, 101169535
     */
    public void setTile(Tile tile, int x, int y) {
        if (Position.FromInts(x, y).isPresent()) {
            setTile(tile, Position.FromInts(x, y).get().getIndex());
        }
    }

    /**
     * Sets a tile object to be contained at a specific index of the ArrayList
     * @param tile tile to be set to a specific index of board
     * @param index index of board
     * @author Quinn Parrott, 101169535
     */
    public void setTile(Tile tile, int index) {
        this.board.set(index, Optional.of(tile));
    }

    /**
     * Getter method for tiles contained at a set Position on the board
     * @param pos pos is a Position object referencing an index on the board
     * @return Returns the tile object at pos Position on the board
     * @author Quinn Parrott, 101169535
     */
    public Optional<Tile> getTile(Position pos) {
        return this.board.get(pos.getIndex());
    }

    /**
     * @return Returns the constant COLUMN_NUMBER
     * @author Quinn Parrott, 101169535
     */
    public static int getCOLUMN_NUMBER() {
        return COLUMN_NUMBER;
    }

    /**
     * @return Returns the constant ROW_NUMBER
     * @author Quinn Parrott, 101169535
     */
    public static int getROW_NUMBER() {
        return ROW_NUMBER;
    }

    /**
     * Place a TilePlacement on the board
     *
     * @param tilePlacement tilePlacement to be placed on the board
     * @author Quinn Parrott, 101169535
     */
    public void placeTiles(TilePlacement tilePlacement) throws PlacementException {
        int i = 0;
        // Check that all tiles are placeable
        for (var tile : tilePlacement.getTiles()) {
            var tileOpt = getTile(tile.pos());
            if (tileOpt.isPresent()) {
                throw new PlacementException(String.format("'%s' already has a letter", tile.pos()), tilePlacement, Optional.of(this));
            }

            // TODO Make sure words don't wrap to other rows
//            int x = tile.pos().getX();
//            if (Math.floorDiv(x, ROW_NUMBER) != Math.floorDiv(x + tilePlacement.getTiles().size(), ROW_NUMBER)) {
//                throw new PlacementException(String.format("'%s' is outside the board", tile.pos()), tilePlacement, Optional.of(this));
//            }
            i++;
        }

        // Place the tiles
        for (var tile : tilePlacement.getTiles()) {
            setTile(tile.value(), tile.pos());
        }
    }

    /**
     * Prints the current board state to console
     * @author Colin Mandeville, 101140289
     */
    public void printBoard() {
        System.out.println(render(true));
    }

    /**
     * Visual representation of the board
     * @author Colin Mandeville, 101140289
     */
    public String render(Boolean showPremiumSquares) {
        StringBuilder builder = new StringBuilder();

        // Print each row of the board and the Vertical Legend
        for (int i = 0; i < ROW_NUMBER; i++) {
            builder.append(String.format("%2s  ", i + 1));

            // Print each tile in row i
            for (int j = 0; j < COLUMN_NUMBER; j++) {
                var pos = Position.FromInts(j, i).get();
                var index = pos.getIndex();
                if(showPremiumSquares) {
                    builder.append(this.board.get(index).map(Tile::chr).orElse(pos.getBackgroundChar())).append("  ");
                }else {
                    builder.append(this.board.get(index).map(Tile::chr).orElse('_')).append("   ");
                }
            }

            builder.append('\n');
        }

        // Print the Horizontal Legend
        if(showPremiumSquares){
            builder.append("   \t");
        }else{
            builder.append("      \t");
        }
        for (int i = 0; i < COLUMN_NUMBER; i++) {
            int j = i + 'A';
            char c = (char) j;
            builder.append(c).append("  ");
        }
        return builder.toString();
    }

    /**
     * Get positioned tiles
     * @return Returns an ArrayList<Positioned<Tile>> of all tiles on the board
     * @author Quinn Parrott, 101169535
     */
    public ArrayList<Positioned<Tile>> getTiles() {
        // TODO: This could be much more efficient if a data structure such as a HashMap was used instead of ArrayList
        var result = new ArrayList<Positioned<Tile>>(this.board.size());

        int i = 0;
        for (var tile : this.board) {
            if (tile.isPresent()) { // Ignore empty tiles
                result.add(new Positioned<Tile>(tile.get(), Position.FromIndex(i).get()));
            }
            i++;
        }

        return result;
    }

    /**
     * Get all vertical and horizontal sequences of characters on the board.
     * @return Returns a HashSet<String> of all words on the board
     * @author Quinn Parrott, 101169535
     */
    public HashMap<String, ArrayList<Integer>> collectCharSequences() {
        var results = new HashMap<String, ArrayList<Integer>>();

        for (int x = 0; x < COLUMN_NUMBER; x++) {
            var seq = "";
            ArrayList<Integer> indexPosSeq = new ArrayList<>();

            for (int y = 0; y < ROW_NUMBER; y++) {
                var pos = Position.FromInts(x, y).get();
                var tile = getTile(pos);
                if (tile.isPresent()) {
                    seq += tile.get().chr();
                    indexPosSeq.add(getIndexPositionFromPoint(new Point(pos.getX(),pos.getY())));
                } else {
                    if (seq.length() > 1) {
                        results.put(seq, indexPosSeq);
                    }
                    seq = "";
                    indexPosSeq = new ArrayList<>();
                }
            }
            // TODO: Is there a way to get rid of this final check (dedup with inner loop)
            if (seq.length() > 1) {
                results.put(seq, indexPosSeq);
            }
        }

        // TODO: Dedup with other loop
        for (int y = 0; y < ROW_NUMBER; y++) {
            var seq = "";
            ArrayList<Integer> indexPosSeq = new ArrayList<>();

            for (int x = 0; x < COLUMN_NUMBER; x++) {
                var pos = Position.FromInts(x, y).get();
                var tile = getTile(pos);
                if (tile.isPresent()) {
                    seq += tile.get().chr();
                    indexPosSeq.add(getIndexPositionFromPoint(new Point(pos.getX(),pos.getY())));
                } else {
                    if (seq.length() > 1) {
                        results.put(seq, indexPosSeq);
                    }
                    seq = "";
                    indexPosSeq = new ArrayList<>();
                }
            }
            // TODO: Is there a way to get rid of this final check (dedup with inner loop)
            if (seq.length() > 1) {
                results.put(seq, indexPosSeq);
            }
        }


        return results;
    }

    /**
     * Clone the board instance
     * @return Returns a duplicate Board instance of the object on which this method is called
     * @author Quinn Parrott, 101169535
     */
    public Board clone() {
        try {
            Board clone = (Board) super.clone();

            int i = 0;
            for (var tile : this.board) {
                if (tile.isPresent()) {
                    clone.setTile(tile.get(), i);
                }
                i += 1;
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to return position index from the board
     * @param p point(x,y)
     * @return int position index
     *
     * @author Tao Lufula, 101164153
     */
    private static int getIndexPositionFromPoint(Point p){
        return (int) (getROW_NUMBER() * ((p.getY())) + (p.getX()));
    }
}
