package ca.carleton.sysc3110.group13.scrabble;

public class Tile {

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
