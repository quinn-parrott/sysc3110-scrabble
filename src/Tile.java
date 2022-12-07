import java.io.Serializable;

/**
 * The representation of a single tile
 *
 * @author Colin Mandeville, 101140289
 */
public record Tile(char chr, int pointValue) implements Serializable {
}
