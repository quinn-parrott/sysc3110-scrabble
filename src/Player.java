import java.util.ArrayList;

/**
 * @author Tao Lufula, 101164153
 */
public class Player {

    private String name;
    private int points;
    private ArrayList<Tile> tileHand;
    private static final int TILEHANDSIZE = 7;

    /**
     * Default constructor for Player Class
     * @param name the name of the player
     * @author Tao Lufula, 101164153
     */
    public Player(String name) {
        this.name = name;
        this.points = 0;
        this.tileHand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public ArrayList<Tile> getTileHand() {
        return tileHand;
    }

    /**
     * This method adds the given points to the player's points
     * @param points
     * @author Tao Lufula, 101164153
     */
    public void addPoints(int points){
        this.points += points;
    }

    /**
     * This method add a tile to the players tileHand if the player's hand has less than 7 tiles.
     * @param tile
     * @author Tao Lufula, 101164153
     */
    public boolean addTile(Tile tile){
        if(this.tileHand.size() < TILEHANDSIZE){
            this.tileHand.add(tile);
            return true;
        }
        return false;
    }

    /**
     * This method remove a tile from the player's tile hand.
     * @param tile
     * @author Tao Lufula, 101164153
     */
    public void removeTile(Tile tile){
        this.tileHand.remove(tile);
    }
}
