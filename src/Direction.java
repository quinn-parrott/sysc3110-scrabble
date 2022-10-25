import java.util.Optional;

/**
 * @author Quinn Parrott, 101169535
 */
public enum Direction {
    Vertical,
    Horizontal;

    /**
     * Interprets the direction to be either v=Vertical, h=Horizontal, or any other input as empty
     * @param directionString directionString will be scanned at index 0 to determine which Direction it refers to
     * @return Returns an Optional of the Direction Enum
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
