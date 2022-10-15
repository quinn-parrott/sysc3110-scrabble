public class Position {
    // Written by Colin Mandeville, 101140289
    // This will represent the placement on the board x is received as letter input, y as a number
    private int x = -1;
    private int y = -1;
    private boolean valid;

    /**
     * Constructor for Position Class. Verifies that input x is a letter (not case-sensitive) and y is a number which is
     * (less than 16)
     * @param x Input for the column, Should be a Letter (Not case-sensitive)
     * @param y Input for the row, Should be a number less than 16
     * @Author Colin Mandeville, 101140289
     */
    public Position(String x, String y) {
        char c = x.toUpperCase().charAt(0);
        if (c >= 65 && c <= 79) {
            this.x = c - 65;
        }
        if (y.length() == 1) {
            c = y.charAt(0);
            if (c >= 49 && c <= 57) {
                this.y = c - 49;
            }
        } else if (y.length() == 2) {
            c = y.charAt(0);
            if (c >= 49 && c <= 57) {
                this.y = (c - 48) * 10;
            }
            c = y.charAt(1);
            if (c >= 49 && c <= 53) {
                this.y += c - 49;
            } else {
                this.y = -1;
            }
        }
        this.valid = (this.x != 0) && (this.y != 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
