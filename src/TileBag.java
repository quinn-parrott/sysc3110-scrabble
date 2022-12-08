import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**
 * The bag of tiles that are currently not being used (not in player hands or on board)
 * @author Colin Mandeville, 101140289
 */
public class TileBag implements Serializable {

    private ArrayList<WildcardableTile> tilesLeft;

    /**
     * Constructor for TileBag Class.
     * @author Colin Mandeville, 101140289
     */
    public TileBag() {
        this.tilesLeft = new ArrayList<>();
        this.resetBag();
    }

    /**
     * resetBag method empties the bag of existing tiles, then adds the starting assortment of tiles to the tilesLeft
     * ArrayList
     * @author Colin Mandeville, 101140289
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
     * @author Colin Mandeville, 101140289
     */
    public Optional<WildcardableTile> drawTile() {
        Random rand = new Random();
        if (this.getNumTilesLeft() > 0) {
            int randInt = rand.nextInt(this.tilesLeft.size());
            return Optional.of(this.tilesLeft.remove(randInt));
        }
        return Optional.empty();
    }

    /**
     * Checks if the tilesLeft attribute contains at least one Tile
     * @return Returns the Boolean of if the tilesLeft attribute contains a value
     */
    public boolean isEmpty() {
        return this.tilesLeft.size() == 0;
    }

    /**
     * Getter method for number of tiles left in bag
     * @return size of tilesLeft Arraylist, i.e. number of tiles left in the bag
     */
    public int getNumTilesLeft() {
        return this.tilesLeft.size();
    }

    public void addTileToBag(WildcardableTile tile) {
        this.tilesLeft.add(tile);
    }

    public String toXML(int numTabs) {
        StringBuilder sb = new StringBuilder();
        StringBuilder tabs = new StringBuilder();
        tabs.append("    ".repeat(numTabs));
        for (WildcardableTile t : tilesLeft) {
            sb.append(tabs).append(t.toXML(numTabs));
        }
        return sb.toString();
    }

}
