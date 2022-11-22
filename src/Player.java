import java.util.ArrayList;

/**
 * Class representing a player's state
 *
 * @author Tao Lufula, 101164153
 */
public class Player {

    private String name;
    private int points;
    private ArrayList<WildcardableTile> tileHand;
    private static final int TILE_HAND_SIZE = 7;

    public static int getTileHandSize() {
        return TILE_HAND_SIZE;
    }

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

    /**
     * Getter method for the Player name attribute
     * @return Returns Player name attribute
     * @author Tao Lufula, 101164153
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for the Player points attribute
     * @return Returns Player points attribute
     * @author Tao Lufula, 101164153
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter method for the Player tileHand attribute
     * @return Returns Player tileHand attribute
     * @author Tao Lufula, 101164153
     */
    public ArrayList<WildcardableTile> getTileHand() {
        return tileHand;
    }

    /**
     * This method adds the given points to the player's points
     * @param points points to be added to the PLayer instance's score
     * @author Tao Lufula, 101164153
     */
    public void addPoints(int points){
        this.points += points;
    }

    /**
     * This method add a tile to the players tileHand.
     * @param tile tile to be added to the Player instance's tileHand
     * @author Tao Lufula, 101164153
     */
    public void addTile(WildcardableTile tile){
        this.tileHand.add(tile);
    }

    /**
     * This method remove a tile from the player's tile hand.
     * @param tile tile to be removed from the Player instance's tileHand
     * @author Tao Lufula, 101164153
     */
    public void removeTile(WildcardableTile tile){
        this.tileHand.remove(tile);
    }
}
