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

    private static final HashMap<Integer, Character> premiumSquares = new HashMap<>();

    public static HashMap<Integer, Character> getPremiumSquares() {

        // 2x Letters
        ArrayList<Position> twoXLetterScore;
        twoXLetterScore = new ArrayList<>();
        twoXLetterScore.add(new Position(3,0));
        twoXLetterScore.add(new Position(11,0));
        twoXLetterScore.add(new Position(6,2));
        twoXLetterScore.add(new Position(8,2));
        twoXLetterScore.add(new Position(0,3));
        twoXLetterScore.add(new Position(7,3));
        twoXLetterScore.add(new Position(14,3));
        twoXLetterScore.add(new Position(2,6));
        twoXLetterScore.add(new Position(6,6));
        twoXLetterScore.add(new Position(8,6));
        twoXLetterScore.add(new Position(12,6));
        twoXLetterScore.add(new Position(3,7));
        twoXLetterScore.add(new Position(11,7));
        twoXLetterScore.add(new Position(2,8));
        twoXLetterScore.add(new Position(6,8));
        twoXLetterScore.add(new Position(8,8));
        twoXLetterScore.add(new Position(12,8));
        twoXLetterScore.add(new Position(0,11));
        twoXLetterScore.add(new Position(7,11));
        twoXLetterScore.add(new Position(14,11));
        twoXLetterScore.add(new Position(6,12));
        twoXLetterScore.add(new Position(8,12));
        twoXLetterScore.add(new Position(3,14));
        twoXLetterScore.add(new Position(11,14));

        for(Position p: twoXLetterScore) {
            premiumSquares.put(p.getIndex(), '$');
        }

        // 3x Letters
        ArrayList<Position> threeXLetterScore;
        threeXLetterScore = new ArrayList<>();
        threeXLetterScore.add(new Position(1,5));
        threeXLetterScore.add(new Position(1,9));
        threeXLetterScore.add(new Position(5,1));
        threeXLetterScore.add(new Position(5,5));
        threeXLetterScore.add(new Position(5,9));
        threeXLetterScore.add(new Position(5,13));
        threeXLetterScore.add(new Position(9,1));
        threeXLetterScore.add(new Position(9,5));
        threeXLetterScore.add(new Position(9,9));
        threeXLetterScore.add(new Position(9,13));
        threeXLetterScore.add(new Position(13,5));
        threeXLetterScore.add(new Position(13,9));

        for(Position p: threeXLetterScore){
            premiumSquares.put(p.getIndex(), '%');
        }

        // 2x Words
        ArrayList<Position> twoXWordScore;
        twoXWordScore = new ArrayList<>();
        twoXWordScore.add(new Position(1,1));
        twoXWordScore.add(new Position(2,2));
        twoXWordScore.add(new Position(3,3));
        twoXWordScore.add(new Position(4,4));
        twoXWordScore.add(new Position(7,7));
        twoXWordScore.add(new Position(10,10));
        twoXWordScore.add(new Position(11,11));
        twoXWordScore.add(new Position(12,12));
        twoXWordScore.add(new Position(13,13));
        twoXWordScore.add(new Position(10,4));
        twoXWordScore.add(new Position(11,3));
        twoXWordScore.add(new Position(12,2));
        twoXWordScore.add(new Position(13,1));
        twoXWordScore.add(new Position(4,10));
        twoXWordScore.add(new Position(3,11));
        twoXWordScore.add(new Position(2,12));
        twoXWordScore.add(new Position(1,13));

        for(Position p: twoXWordScore){
            premiumSquares.put(p.getIndex(), '@');
        }

        // 3x Words
        ArrayList<Position> threeXWordScore;
        threeXWordScore = new ArrayList<>();
        threeXWordScore.add(new Position(0,0));
        threeXWordScore.add(new Position(0,7));
        threeXWordScore.add(new Position(0,14));
        threeXWordScore.add(new Position(7,0));
        threeXWordScore.add(new Position(7,14));
        threeXWordScore.add(new Position(14,0));
        threeXWordScore.add(new Position(14,7));
        threeXWordScore.add(new Position(14,14));

        for(Position p: threeXWordScore){
            premiumSquares.put(p.getIndex(), '#');
        }

        return premiumSquares;

    }

}
