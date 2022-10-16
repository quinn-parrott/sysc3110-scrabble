import java.util.ArrayList;
import java.util.Random;

/**
 * @author Colin Mandeville, 101140289
 */
public class TileBag {

    private final ArrayList<Tile> tilesLeft;

    /**
     * Constructor for TileBag Class.
     * @Author Colin Mandeville, 101140289
     */
    public TileBag() {
        this.tilesLeft = new ArrayList<>();
        this.resetBag();
    }

    /**
     * resetBag method empties the bag of existing tiles, then adds the starting assortment of tiles to the tilesLeft
     * ArrayList
     * @Author Colin Mandeville, 101140289
     */
    public void resetBag() {
        this.tilesLeft.clear();
        for (TileBagDetails bagDetails : TileBagSingleton.getBagDetails().values()) {
            for (int i = 0; i < bagDetails.numInBag(); i++) {
                this.tilesLeft.add(bagDetails.tile());
            }
        }
    }

    /**
     * drawTile method initializes a Random object, and removes a random tile from the ArrayList
     * @return returns the Tile removed from the tilesLeft ArrayList
     * @Author Colin Mandeville, 101140289
     */
    public Tile drawTile() {
        Random rand = new Random();
        return this.tilesLeft.remove(rand.nextInt(this.tilesLeft.size()));
    }
}
