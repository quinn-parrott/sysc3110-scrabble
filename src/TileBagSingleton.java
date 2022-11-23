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
            bagDetails.put('A', new TileBagDetails(new WildcardableTile('A', 1), 9));
            bagDetails.put('B', new TileBagDetails(new WildcardableTile('B', 3), 2));
            bagDetails.put('C', new TileBagDetails(new WildcardableTile('C', 3), 2));
            bagDetails.put('D', new TileBagDetails(new WildcardableTile('D', 2), 4));
            bagDetails.put('E', new TileBagDetails(new WildcardableTile('E', 1), 12));
            bagDetails.put('F', new TileBagDetails(new WildcardableTile('F', 4), 2));
            bagDetails.put('G', new TileBagDetails(new WildcardableTile('G', 2), 3));
            bagDetails.put('H', new TileBagDetails(new WildcardableTile('H', 4), 2));
            bagDetails.put('I', new TileBagDetails(new WildcardableTile('I', 1), 9));
            bagDetails.put('J', new TileBagDetails(new WildcardableTile('J', 8), 1));
            bagDetails.put('K', new TileBagDetails(new WildcardableTile('K', 5), 1));
            bagDetails.put('L', new TileBagDetails(new WildcardableTile('L', 1), 4));
            bagDetails.put('M', new TileBagDetails(new WildcardableTile('M', 3), 2));
            bagDetails.put('N', new TileBagDetails(new WildcardableTile('N', 1), 6));
            bagDetails.put('O', new TileBagDetails(new WildcardableTile('O', 1), 8));
            bagDetails.put('P', new TileBagDetails(new WildcardableTile('P', 3), 2));
            bagDetails.put('Q', new TileBagDetails(new WildcardableTile('Q', 10), 1));
            bagDetails.put('R', new TileBagDetails(new WildcardableTile('R', 1), 6));
            bagDetails.put('S', new TileBagDetails(new WildcardableTile('S', 1), 4));
            bagDetails.put('T', new TileBagDetails(new WildcardableTile('T', 1), 6));
            bagDetails.put('U', new TileBagDetails(new WildcardableTile('U', 1), 4));
            bagDetails.put('V', new TileBagDetails(new WildcardableTile('V', 4), 2));
            bagDetails.put('W', new TileBagDetails(new WildcardableTile('W', 4), 2));
            bagDetails.put('X', new TileBagDetails(new WildcardableTile('X', 8), 1));
            bagDetails.put('Y', new TileBagDetails(new WildcardableTile('Y', 4), 2));
            bagDetails.put('Z', new TileBagDetails(new WildcardableTile('Z', 10), 1));
            bagDetails.put('&', new TileBagDetails(new WildcardableTile('&', 0), 2));
        }
        return bagDetails;
    }
}
