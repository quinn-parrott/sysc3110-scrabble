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

        // 2x Letters
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

        for(Point p: twoXLetterScore) {
            premiumSquares.put(getIndexFromPoint(p), '$');
        }

        // 3x Letters
        threeXLetterScore = new ArrayList<>();
        threeXLetterScore.add(new Point(2,6));
        threeXLetterScore.add(new Point(2,10));
        threeXLetterScore.add(new Point(6,2));
        threeXLetterScore.add(new Point(6,6));
        threeXLetterScore.add(new Point(6,10));
        threeXLetterScore.add(new Point(6,14));
        threeXLetterScore.add(new Point(10,2));
        threeXLetterScore.add(new Point(10,6));
        threeXLetterScore.add(new Point(10,10));
        threeXLetterScore.add(new Point(10,14));
        threeXLetterScore.add(new Point(14,6));
        threeXLetterScore.add(new Point(14,10));

        for(Point p: threeXLetterScore){
            premiumSquares.put(getIndexFromPoint(p), '%');
        }

        // 2x Words
        twoXWordScore = new ArrayList<>();
        twoXWordScore.add(new Point(2,2));
        twoXWordScore.add(new Point(3,3));
        twoXWordScore.add(new Point(4,4));
        twoXWordScore.add(new Point(5,5));
        twoXWordScore.add(new Point(8,8));
        twoXWordScore.add(new Point(11,11));
        twoXWordScore.add(new Point(12,12));
        twoXWordScore.add(new Point(13,13));
        twoXWordScore.add(new Point(14,14));
        twoXWordScore.add(new Point(11,5));
        twoXWordScore.add(new Point(12,4));
        twoXWordScore.add(new Point(13,3));
        twoXWordScore.add(new Point(14,2));
        twoXWordScore.add(new Point(5,11));
        twoXWordScore.add(new Point(4,12));
        twoXWordScore.add(new Point(3,13));
        twoXWordScore.add(new Point(2,14));

        for(Point p: twoXWordScore){
            premiumSquares.put(getIndexFromPoint(p), '@');
        }

        // 3x Words
        threeXWordScore = new ArrayList<>();
        threeXWordScore.add(new Point(1,1));
        threeXWordScore.add(new Point(1,8));
        threeXWordScore.add(new Point(1,15));
        threeXWordScore.add(new Point(8,1));
        threeXWordScore.add(new Point(8,15));
        threeXWordScore.add(new Point(15,1));
        threeXWordScore.add(new Point(15,8));
        threeXWordScore.add(new Point(15,15));

        for(Point p: threeXWordScore){
            premiumSquares.put(getIndexFromPoint(p), '#');
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
        return (int) (Board.getROW_NUMBER() * ((p.getY() - 1)) + (p.getX() - 1));
    }

}
