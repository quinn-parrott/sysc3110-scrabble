import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Colin Mandeville, 101140289
 */
public class Position {
    // This will represent the placement on the board x is received as letter input, y as a number
    private final int x;
    private final int y;

    /**
     * Constructor for Position Class. Verifies that input x is a letter (not case-sensitive) and
     * y is a positive integer less than 16.
     *
     * @param x Input for the column, Should be a Letter (Not case-sensitive)
     * @param y Input for the row, Should be a number less than 16
     * @author Colin Mandeville, 101140289
     */
    private Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @author Colin Mandeville, 101140289
     */
    public static Optional<Position> FromStrings(String x, String y) {
        int column;
        int row = -1;
        column = x.toUpperCase().charAt(0) - 65;
        try {
            row = Integer.parseInt(y) - 1;
        } catch (IllegalArgumentException _ignored) {
            return Optional.empty();
        }
        if (column >= 0 && column < Board.getCOLUMN_NUMBER() && row >= 0 && row < Board.getROW_NUMBER()) {
            return Optional.of(new Position(column, row));
        }
        return Optional.empty();
    }

    /**
     * @author Quinn Parrott, 101169535
     */
    public static Optional<Position> FromString(String pos) {
        if (pos.length() >= 2) {
            // TODO: This doesn't handle "AA1" (2 letter long x)
            return FromStrings(pos.substring(0, 1), pos.substring(1));
        }
        return Optional.empty();
    }

    /**
     * @author Colin Mandeville, 101140289
     */
    public int getX() {
        return x;
    }

    /**
     * @author Colin Mandeville, 101140289
     */
    public int getY() {
        return y;
    }

    /**
     * @author Quinn Parrott, 101169535
     */
    public static Optional<List<Position>> Interpolate(Position pos1, Position pos2) {
        if (pos1.x == pos2.x) {
            // Vertical
            var delta = Math.abs(pos1.y - pos2.y);
            var first = Integer.min(pos1.y, pos2.y);
            return Optional.of(
                    Arrays.stream(IntStream.rangeClosed(first, first + delta).toArray())
                            .mapToObj(pos -> new Position(pos1.x, pos))
                            .collect(Collectors.toList()
                            )
            );
        }

        if (pos1.y == pos2.y) {
            // Horizontal
            var delta = Math.abs(pos1.x - pos2.x);
            var first = Integer.min(pos1.x, pos2.x);
            return Optional.of(
                    Arrays.stream(IntStream.rangeClosed(first, first + delta).toArray())
                            .mapToObj(pos -> new Position(pos, pos1.y))
                            .collect(Collectors.toList()
                            )
            );
        }

        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
