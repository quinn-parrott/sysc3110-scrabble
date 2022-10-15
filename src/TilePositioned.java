public class TilePositioned {
    // Written by Colin Mandeville, 101140289
    private Tile tile;
    private Position pos;

    public TilePositioned(Tile tile, Position pos) {
        this.tile = tile;
        this.pos = pos;
    }

    public Tile getTile() {
        return tile;
    }

    public Position getPos() {
        return pos;
    }
}
