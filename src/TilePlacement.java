import java.util.ArrayList;

public class TilePlacement {
    // Written by Colin Mandeville, 101140289
    // This represents a single player turn
    private ArrayList<TilePositioned> tiles;
    private int score;

    public TilePlacement(ArrayList<TilePositioned> tiles) {
        this.tiles = tiles;
        for (TilePositioned tile : this.tiles) {

            // TODO Implement putting words onto board, need to wait on CLI and Game being implemented

            this.score += tile.getTile().getPointValue();
        }
    }
}
