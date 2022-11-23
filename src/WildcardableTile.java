/**
 * The representation of a single tile but can also be a wildcard
 *
 * @author Quinn Parrott, 101169535
 */
public record WildcardableTile(char chr, int pointValue) {
    public static final char WILDCARD_CHAR = '&';

    public boolean isWildcard() {
        return WILDCARD_CHAR == chr();
    }
}
