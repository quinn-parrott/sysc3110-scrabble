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

    /**
     * addTileToBag method adds a given tile back to the tile bag. It is used when exchanging tiles.
     * @param tile
     * @author Tao Lufula, 101164153
     */
    public void addTileToBag(Tile tile){
        this.tilesLeft.add(tile);
    }

    /**
     * This method is used to swap players' tiles with those in the bag
     * @param tile
     * @return new tile if there are more than 7 tiles in the tile bag, Otherwise returns the same tile.(to be improved later)
     *
     * @author Tao Lufula, 101164153
     */
    public Tile exchangeTile(Tile tile){
        if(this.tilesLeft.size() >= 7){
            Tile newTile = this.drawTile();
            this.addTileToBag(tile);
            return newTile;
        }
//      return same tile since there are less than 7 tiles left in the bag
        return tile;
    }
}
