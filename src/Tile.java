/**
 * @author Colin Mandeville, 101140289
 */
public record Tile(char chr, int pointValue) {

    /**
     * Verifies if the Tile is not an arbitrary entry value.
     * @return Returns the boolean value of if the Tile object has a letter
     * @author Quinn Parrott
     */
    public boolean isFilledWithLetter() {
        // TODO: Make it impossible to have empty tiles
        return chr != '_' && chr != '*';
    }
}
