import java.util.ArrayList;
import java.io.Serializable;

/**
 * Class representing a player's state
 *
 * @author Tao Lufula, 101164153
 */
public class Player implements Serializable {

    private String name;
    private int points;
    private ArrayList<WildcardableTile> tileHand;
    private boolean isAI;
    private static final int TILE_HAND_SIZE = 7;

    private static final WordList WORD_LIST = new WordList();

    public static int getTileHandSize() {
        return TILE_HAND_SIZE;
    }

    /**
     * Constructor for the player class if no boolean for isAI attribute is passed
     * @param name the name of the player
     * @author Tao Lufula, 101164153
     */
    public Player(String name) {
        this(name, false);
    }

    /**
     * Default constructor for Player Class
     * @param name the name of the player
     * @param isAI boolean, true = this is an AI player, false = not an AI player
     * @author Colin Mandeville, 101140289
     */
    public Player(String name, boolean isAI) {
        this.name = name;
        this.points = 0;
        this.isAI = isAI;
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

    /**
     * Getter for the isAI method
     * @return Returns attribute for isAI boolean
     */
    public boolean isAI() {
        return isAI;
    }

    /**
     * Filters down full wordlist to only those which the player has almost all the tiles to play
     * @param numTilesOff The number of tiles that the player's hand can be off by
     * @return Returns an ArrayList of all words the player may be able to play
     */
    public ArrayList<String> getPossibleWords(int numTilesOff) {
        ArrayList<String> possibleWords = new ArrayList<>();
        StringBuilder tileHandSB = new StringBuilder();
        for (WildcardableTile tile : this.getTileHand()) {
            tileHandSB.append(tile.chr());
        }
        for (String word : WORD_LIST.getWordlist()) {
            int missingTileCount = numTilesOff;
            if (word.length() > 2) {
                String letterHandString = tileHandSB.toString();
                boolean valid = true;
                for (char c : word.toUpperCase().toCharArray()) {
                    if (letterHandString.contains(Character.toString(c))) {
                        letterHandString = letterHandString.replaceFirst(Character.toString(c), " ");
                    } else if (missingTileCount > 0) {
                        missingTileCount--;
                    } else {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    possibleWords.add(word);
                }
            }
        }
        return possibleWords;
    }

    public String toXML(int numTabs) {
        StringBuilder sb = new StringBuilder();
        StringBuilder tabs = new StringBuilder();
        tabs.append("    ".repeat(numTabs));
        sb.append(tabs).append("<Player name=\"").append(name).append("\" points=\"").append(points).append("\" isAI=\"").append(isAI).append("\">\n");
        for (WildcardableTile t : tileHand) {
            sb.append(t.toXML(numTabs + 1));
        }
        sb.append(tabs).append("</Player>\n");
        return sb.toString();
    }
}
