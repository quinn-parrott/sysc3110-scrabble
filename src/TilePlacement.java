import java.util.ArrayList;

public class TilePlacement {
    // Written by Colin Mandeville, 101140289
    // This represents a single player turn
    private ArrayList<TilePositioned> tiles;
    private int score;

    /**
     * Constructor for TilePlacement, Will handle placing word onto board in future
     * @param tiles ArrayList of TilePositioned objects, which contain a Tile object, and a Position object
     * @Author Colin Mandeville, 101140289
     */
    public TilePlacement(ArrayList<TilePositioned> tiles) {
        this.tiles = tiles;
        for (TilePositioned tile : this.tiles) {

            // TODO Implement putting words onto board, need to wait on CLI and Game being implemented

            this.score += tile.getTile().getPointValue();
        }
    }
}
