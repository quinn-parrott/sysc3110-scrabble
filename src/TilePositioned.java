/**
 * A tile that also has a position (used for storing on board)
 *
 * @author Colin Mandeville, 101140289
 */
public record TilePositioned(Tile tile, Position pos) {
}
