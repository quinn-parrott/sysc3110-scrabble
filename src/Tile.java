public class Tile {
    // Written by Colin Mandeville, 101140289
    private char chr;
    private int pointValue;

    public Tile(char chr, int pointValue) {
        this.chr = chr;
        this.pointValue = pointValue;
    }

    public char getChr() {
        return chr;
    }

    public int getPointValue() {
        return pointValue;
    }
}
