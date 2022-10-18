/**
 * @author Colin Mandeville, 101140289
 */
public record Tile(char chr, int pointValue) {
    public boolean isFilledWithLetter() {
        // TODO: Make it impossible to have empty tiles
        return chr != '_' && chr != '*';
    }
}
