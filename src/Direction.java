import java.util.Optional;

/**
 * @author Quinn Parrott, 101169535
 */
public enum Direction {
    Vertical,
    Horizontal;

    /**
     * @author Quinn Parrott, 101169535
     */
    public static Optional<Direction> FromString(String directionString) {
        if (directionString.length() > 0) {
            char firstChar = Character.toUpperCase(directionString.charAt(0));
            return switch (firstChar) {
                case 'V' -> Optional.of(Direction.Vertical);
                case 'H' -> Optional.of(Direction.Horizontal);
                default ->  Optional.empty();
            };
        }
        return Optional.empty();
    }
}
