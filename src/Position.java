import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Colin Mandeville, 101140289
 */
public class Position implements Serializable {
    // This will represent the placement on the board x is received as letter input, y as a number
    private final int x;
    private final int y;
    private static HashMap<Integer, Character> premiumSquares;

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

    /**
     * Defines a Position object using an index value in the Board object using the bounds on its row and column limits
     * @param index index referring to a position on the Board object
     * @return Returns the created Position object
     * @author Quinn Parrott, 101169535
     */
    public static Optional<Position> FromIndex(int index) {
        return Position.FromInts(index % Board.getROW_NUMBER(), Integer.divideUnsigned(index, Board.getROW_NUMBER()));
    }

    /**
     * Defines a Position object using a set of int x and y coordinates
     * @param x x integer referring to the column number on the board
     * @param y y integer referring to the row number on the board
     * @return Returns the created Position object
     * @author Colin Mandeville, 101140289
     * @author Quinn Parrott, 101169535
     */
    public static Optional<Position> FromInts(int x, int y) {
        if (x >= 0 && x < Board.getCOLUMN_NUMBER() && y >= 0 && y < Board.getROW_NUMBER()) {
            return Optional.of(new Position(x, y));
        }
        return Optional.empty();
    }

    /**
     * Defines a Position object using a set of String x and y coordinates
     * @param x x refers to the String of the x coordinate on the board as a Letter
     * @param y y refers to the String of the y coordinate on the board as a Number
     * @return Returns the created Position object
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
        return FromInts(column, row);
    }

    /**
     * Defines a Position object using a String position using
     * @param pos pos is a String to be parsed to get Position values
     * @return Returns the created Position object
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
     * Getter method for the x coordinate
     * @return Returns the x coordinate of the Position
     * @author Colin Mandeville, 101140289
     */
    public int getX() {
        return x;
    }

    /**
     * Getter method for the y coordinate
     * @return Returns the y coordinate of the Position
     * @author Colin Mandeville, 101140289
     */
    public int getY() {
        return y;
    }

    /**
     * Getter method for the index of Board equivalent to the Position x and y coordinates
     * @return Returns the index equivalent to the Position
     * @author Quinn Parrott, 101169535
     */
    public int getIndex() {
        return Board.getROW_NUMBER() * y + x;
    }

    /**
     * Calculates the pythagorean distance between 2 Position objects
     * @param pos1 The first point to compare
     * @param pos2 The point to be compared to pos1
     * @return Returns the difference between 2 Position objects
     * @author Quinn Parrott, 101169535
     */
    public static double Distance(Position pos1, Position pos2) {
        var e = Math.ceil(Math.sqrt(Math.pow(pos1.getY() - pos2.getY(), 2) + Math.pow(pos1.getX() - pos2.getX(), 2)));
        return e;
    }

    /**
     * Defines an inclusive list of Positions between two positions on the same axis
     * @param pos1 The first point to compare
     * @param pos2 The point to be compared to pos1
     * @return Returns the inclusive List of positions between two Positions on the same axis
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

    /**
     * Returns a list of positions that are in bounds and adjacent to the current position.
     * @return List of adjacent positions to the current tile.
     * @author Quinn Parrott, 101169535
     */
    public List<Position> adjacentPositions() {
        int x = this.getX();
        int y = this.getY();

        var pos = new ArrayList<Position>(4);

        if (0 <= (x - 1)) {
            pos.add(new Position(x - 1, y));
        }

        if ((x + 1) <= (Board.getCOLUMN_NUMBER() - 1)) {
            pos.add(new Position(x + 1, y));
        }

        if (0 <= (y - 1)) {
            pos.add(new Position(x, y - 1));
        }

        if ((y + 1) <= (Board.getROW_NUMBER()) - 1) {
            pos.add(new Position(x, y + 1));
        }

        return pos;
    }

    public char getBackgroundChar() {
        this.premiumSquares = PremiumSquares.getPremiumSquares();
        int i =  getIndex();
        if(Board.getCenterTilePos() == i){
            return '*';
        }else{
            if(premiumSquares.containsKey(i)){
                return premiumSquares.get(i);
            }else{
                return '_';
            }
        }
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

    @Override
    public String toString() {
        return String.format("%c%d", 'A' + this.getX(), this.getY() + 1);
    }

    public String toXMLString() {
        return "x=\"" + x + "\" y=\"" + y + "\"";
    }
}
