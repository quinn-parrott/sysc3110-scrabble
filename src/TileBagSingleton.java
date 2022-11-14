import java.util.HashMap;

/**
 * The default tile bag data
 *
 * @author Colin Mandeville 101140289
 */
public class TileBagSingleton {

    private static final HashMap<Character, TileBagDetails> bagDetails = new HashMap<>();

    /**
     * Declares a HashMap with the standard set of Scrabble tiles in a tileBag
     * @return Returns a reference to the same HashMap declared on first call
     */
    public static HashMap<Character, TileBagDetails> getBagDetails() {
        if (bagDetails.size() == 0) {
            bagDetails.put('A', new TileBagDetails(new Tile('A', 1), 9));
            bagDetails.put('B', new TileBagDetails(new Tile('B', 3), 2));
            bagDetails.put('C', new TileBagDetails(new Tile('C', 3), 2));
            bagDetails.put('D', new TileBagDetails(new Tile('D', 2), 4));
            bagDetails.put('E', new TileBagDetails(new Tile('E', 1), 12));
            bagDetails.put('F', new TileBagDetails(new Tile('F', 4), 2));
            bagDetails.put('G', new TileBagDetails(new Tile('G', 2), 3));
            bagDetails.put('H', new TileBagDetails(new Tile('H', 4), 2));
            bagDetails.put('I', new TileBagDetails(new Tile('I', 1), 9));
            bagDetails.put('J', new TileBagDetails(new Tile('J', 8), 1));
            bagDetails.put('K', new TileBagDetails(new Tile('K', 5), 1));
            bagDetails.put('L', new TileBagDetails(new Tile('L', 1), 4));
            bagDetails.put('M', new TileBagDetails(new Tile('M', 3), 2));
            bagDetails.put('N', new TileBagDetails(new Tile('N', 1), 6));
            bagDetails.put('O', new TileBagDetails(new Tile('O', 1), 8));
            bagDetails.put('P', new TileBagDetails(new Tile('P', 3), 2));
            bagDetails.put('Q', new TileBagDetails(new Tile('Q', 10), 1));
            bagDetails.put('R', new TileBagDetails(new Tile('R', 1), 6));
            bagDetails.put('S', new TileBagDetails(new Tile('S', 1), 4));
            bagDetails.put('T', new TileBagDetails(new Tile('T', 1), 6));
            bagDetails.put('U', new TileBagDetails(new Tile('U', 1), 4));
            bagDetails.put('V', new TileBagDetails(new Tile('V', 4), 2));
            bagDetails.put('W', new TileBagDetails(new Tile('W', 4), 2));
            bagDetails.put('X', new TileBagDetails(new Tile('X', 8), 1));
            bagDetails.put('Y', new TileBagDetails(new Tile('Y', 4), 2));
            bagDetails.put('Z', new TileBagDetails(new Tile('Z', 10), 1));
        }
        return bagDetails;
    }
}
