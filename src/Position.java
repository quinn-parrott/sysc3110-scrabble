public class Position {
    // Written by Colin Mandeville, 101140289
    // This will represent the placement on the board x is received as letter input, y as a number
    private int x = 0;
    private int y = 0;
    private boolean valid;

    public Position(String x, String y) {
        char c = x.toUpperCase().charAt(0);
        if (c >= 65 && c <= 79) {
            this.x = c - 64;
        }
        if (y.length() == 1) {
            c = y.charAt(0);
            if (c >= 49 && c <= 57) {
                this.y = c - 48;
            }
        } else if (y.length() == 2) {
            c = y.charAt(0);
            if (c >= 49 && c <= 57) {
                this.y = (c - 48) * 10;
            }
            c = y.charAt(1);
            if (c >= 49 && c <= 53) {
                this.y += c - 48;
            } else {
                this.y = 0;
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

    public static void main(String[] args) {
        System.out.println("TEST");

    }
}
