import java.util.ArrayList;
import java.util.Random;

public class TileBag {
    // Written by Colin Mandeville, 101140289

    private enum BagLetterDist {
        // TODO There is almost definitely a better way to do this, but this works for v1.0
        A(9, new Tile('A', 1)),
        B(2, new Tile('B', 3)),
        C(2, new Tile('C', 3)),
        D(4, new Tile('D', 2)),
        E(12, new Tile('E', 1)),
        F(2, new Tile('F', 4)),
        G(3, new Tile('G', 2)),
        H(2, new Tile('H', 4)),
        I(9, new Tile('I', 1)),
        J(1, new Tile('J', 8)),
        K(1, new Tile('K', 5)),
        L(4, new Tile('L', 1)),
        M(2, new Tile('M', 3)),
        N(6, new Tile('N', 1)),
        O(8, new Tile('O', 1)),
        P(2, new Tile('P', 3)),
        Q(1, new Tile('Q', 10)),
        R(6, new Tile('R', 1)),
        S(4, new Tile('S', 1)),
        T(6, new Tile('T', 1)),
        U(4, new Tile('U', 1)),
        V(2, new Tile('V', 4)),
        W(2, new Tile('W', 4)),
        X(1, new Tile('X', 8)),
        Y(2, new Tile('Y', 4)),
        Z(1, new Tile('Z', 10));

        private final int numInBag;
        private final Tile tile;

        /**
         * Constructor fpr BagLetterDist enum
         * @param i Number of tile in starting bag
         * @param tile Tile object to go in bag
         * @Author Colin Mandeville, 101140289
         */
        BagLetterDist(int i, Tile tile) {
            this.numInBag = i;
            this.tile = tile;
        }

        public int getNumInBag() {
            return numInBag;
        }

        public Tile getTile() {
            return tile;
        }
    }

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
        for (BagLetterDist letter : BagLetterDist.values()) {
            Tile tile = letter.getTile();
            for (int i = 0; i < letter.getNumInBag(); i++) {
                this.tilesLeft.add(tile);
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
