import java.util.ArrayList;

/**
 * @author Colin Mandeville, 101140289
 */
public class TilePlacement {
    // This class represents a single player turn
    private final ArrayList<TilePositioned> tiles;
    private int score = 0;

    /**
     * Constructor for TilePlacement, Will handle placing word onto board in future
     * @param tiles ArrayList of TilePositioned objects, which contain a Tile object, and a Position object
     * @author Colin Mandeville, 101140289
     */
    public TilePlacement(ArrayList<TilePositioned> tiles) {
        this.tiles = tiles;
        for (TilePositioned tile : this.tiles) {

            // TODO Implement putting words onto board, need to wait on CLI and Game being implemented

            this.score += tile.tile().pointValue();
        }
    }
}
