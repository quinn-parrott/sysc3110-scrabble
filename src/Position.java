import java.util.Optional;

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
     * @param x Input for the column, Should be a Letter (Not case-sensitive)
     * @param y Input for the row, Should be a number less than 16
     * @author Colin Mandeville, 101140289
     */
    private Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Optional<Position> FromStrings(String x, String y) {
        int column;
        int row = -1;
        column = x.toUpperCase().charAt(0) - 65;
        try{
            row = Integer.parseInt(y) - 1;
        } catch (IllegalArgumentException _ignored) {
            return Optional.empty();
        }
        if (column >= 0 && column < Board.getCOLUMN_NUMBER() && row >= 0 && row < Board.getROW_NUMBER()) {
            return Optional.of(new Position(column, row));
        }
        return Optional.empty();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
