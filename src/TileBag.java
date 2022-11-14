import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

/**
 * @author Colin Mandeville, 101140289
 */
public class TileBag {

    private final HashMap<Tile, Integer> tilesLeft;
    private static final int MIN_TILES_FOR_EXCHANGE = 7;

    /**
     * Constructor for TileBag Class.
     * @author Colin Mandeville, 101140289
     */
    public TileBag() {
        this.tilesLeft = new HashMap<>();
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
            this.tilesLeft.put(bagDetails.tile(), bagDetails.numInBag());
        }
    }

    /**
     * drawTile method initializes a Random object, and removes a random tile from the ArrayList
     * @return returns the Tile removed from the tilesLeft ArrayList
     * @author Colin Mandeville, 101140289
     */
    public Optional<Tile> drawTile() {
        Random rand = new Random();
        if (this.getNumTilesLeft() > 0) {
            ArrayList<Tile> listTiles = new ArrayList<>(this.tilesLeft.keySet());
            int randInt = rand.nextInt(this.tilesLeft.keySet().size());
            this.tilesLeft.replace(listTiles.get(randInt), this.tilesLeft.get(listTiles.get(randInt)),
                    this.tilesLeft.get(listTiles.get(randInt)) - 1);
            if (this.tilesLeft.get(listTiles.get(randInt)) == 0) {
                this.tilesLeft.remove(listTiles.get(randInt));
            }
            return Optional.of(listTiles.get(randInt));
        }
        return Optional.empty();
    }

    /**
     * addTileToBag method adds a given tile back to the tile bag. It is used when exchanging tiles.
     * @param tile tile being added to the bag
     * @author Tao Lufula, 101164153
     */
    private void addTileToBag(Tile tile){
        Tile t = null;
        for (TileBagDetails tbg : TileBagSingleton.getBagDetails().values()) {
            if(tile.chr() == tbg.tile().chr()) {
                t = tbg.tile();
            }
        }
        if(t != null) {
            this.tilesLeft.put(t, this.tilesLeft.get(t) + 1);
        }
    }

    /**
     * Checks if the tilesLeft attribute contains at least one Tile
     * @return Returns the Boolean of if the tilesLeft attribute contains a value
     */
    public boolean isEmpty() {
        int numTiles = 0;
        for (Tile t : this.tilesLeft.keySet()) {
            numTiles += this.tilesLeft.get(t);
        }
        return numTiles == 0;
    }

    /**
     * Getter method for number of tiles left in bag
     * @return size of tilesLeft Arraylist, i.e. number of tiles left in the bag
     */
    public int getNumTilesLeft() {
        int tilesLeft = 0;
        for (TileBagDetails tbg : TileBagSingleton.getBagDetails().values()) {
            tilesLeft += this.tilesLeft.getOrDefault(tbg.tile(), 0);
        }
        return tilesLeft;
    }
}
