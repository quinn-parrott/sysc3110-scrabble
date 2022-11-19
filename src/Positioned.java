/**
 * A tile that also has a position (used for storing on board)
 *
 * @author Colin Mandeville, 101140289
 */
public record Positioned<T>(T value, Position pos) {
}
