import java.awt.*;
import java.util.*;
import java.util.HashMap;

/**
 * PremiumSquares
 * Class responsible for all premium Squares on the board
 *
 * @author Tao Lufula, 101164153
 */

public class PremiumSquares {
    private static ArrayList<Point> twoXLetterScore;
    private static ArrayList<Point> threeXLetterScore;
    private static ArrayList<Point> twoXWordScore;
    private static ArrayList<Point> threeXWordScore;

    private static final HashMap<Integer, Character> premiumSquares = new HashMap<>();

    public static HashMap<Integer, Character> getPremiumSquares() {
        twoXLetterScore = new ArrayList<>();
        twoXLetterScore.add(new Point(4,1));
        twoXLetterScore.add(new Point(12,1));
        twoXLetterScore.add(new Point(7,3));
        twoXLetterScore.add(new Point(9,3));
        twoXLetterScore.add(new Point(1,4));
        twoXLetterScore.add(new Point(8,4));
        twoXLetterScore.add(new Point(15,4));
        twoXLetterScore.add(new Point(3,7));
        twoXLetterScore.add(new Point(7,7));
        twoXLetterScore.add(new Point(9,7));
        twoXLetterScore.add(new Point(13,7));
        twoXLetterScore.add(new Point(4,8));
        twoXLetterScore.add(new Point(12,8));
        twoXLetterScore.add(new Point(3,9));
        twoXLetterScore.add(new Point(7,9));
        twoXLetterScore.add(new Point(9,9));
        twoXLetterScore.add(new Point(13,9));
        twoXLetterScore.add(new Point(1,12));
        twoXLetterScore.add(new Point(8,12));
        twoXLetterScore.add(new Point(15,12));
        twoXLetterScore.add(new Point(7,13));
        twoXLetterScore.add(new Point(9,13));
        twoXLetterScore.add(new Point(4,15));
        twoXLetterScore.add(new Point(12,15));

        for(Point p1: twoXLetterScore){
            premiumSquares.put(getIndexFromPoint(p1), '$');
        }

        return premiumSquares;

    }

    /**
     * Method to return position index from the board
     * @param p point(x,y)
     * @return int position index
     *
     * @author Tao Lufula, 101164153
     */
    private static int getIndexFromPoint(Point p){
        return (int) (15 * ((p.getY() - 1)) + (p.getX() - 1));
    }

    public static void main(String[] args) {
        getPremiumSquares();
    }
}
