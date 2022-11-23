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
        twoXLetterScore.add(Position.FromInts(3,0).get());
        twoXLetterScore.add(Position.FromInts(11,0).get());
        twoXLetterScore.add(Position.FromInts(6,2).get());
        twoXLetterScore.add(Position.FromInts(8,2).get());
        twoXLetterScore.add(Position.FromInts(0,3).get());
        twoXLetterScore.add(Position.FromInts(7,3).get());
        twoXLetterScore.add(Position.FromInts(14,3).get());
        twoXLetterScore.add(Position.FromInts(2,6).get());
        twoXLetterScore.add(Position.FromInts(6,6).get());
        twoXLetterScore.add(Position.FromInts(8,6).get());
        twoXLetterScore.add(Position.FromInts(12,6).get());
        twoXLetterScore.add(Position.FromInts(3,7).get());
        twoXLetterScore.add(Position.FromInts(11,7).get());
        twoXLetterScore.add(Position.FromInts(2,8).get());
        twoXLetterScore.add(Position.FromInts(6,8).get());
        twoXLetterScore.add(Position.FromInts(8,8).get());
        twoXLetterScore.add(Position.FromInts(12,8).get());
        twoXLetterScore.add(Position.FromInts(0,11).get());
        twoXLetterScore.add(Position.FromInts(7,11).get());
        twoXLetterScore.add(Position.FromInts(14,11).get());
        twoXLetterScore.add(Position.FromInts(6,12).get());
        twoXLetterScore.add(Position.FromInts(8,12).get());
        twoXLetterScore.add(Position.FromInts(3,14).get());
        twoXLetterScore.add(Position.FromInts(11,14).get());

        for(Position p: twoXLetterScore) {
            premiumSquares.put(p.getIndex(), '$');
        }

        // 3x Letters
        ArrayList<Position> threeXLetterScore;
        threeXLetterScore = new ArrayList<>();
        threeXLetterScore.add(Position.FromInts(1,5).get());
        threeXLetterScore.add(Position.FromInts(1,9).get());
        threeXLetterScore.add(Position.FromInts(5,1).get());
        threeXLetterScore.add(Position.FromInts(5,5).get());
        threeXLetterScore.add(Position.FromInts(5,9).get());
        threeXLetterScore.add(Position.FromInts(5,13).get());
        threeXLetterScore.add(Position.FromInts(9,1).get());
        threeXLetterScore.add(Position.FromInts(9,5).get());
        threeXLetterScore.add(Position.FromInts(9,9).get());
        threeXLetterScore.add(Position.FromInts(9,13).get());
        threeXLetterScore.add(Position.FromInts(13,5).get());
        threeXLetterScore.add(Position.FromInts(13,9).get());

        for(Position p: threeXLetterScore){
            premiumSquares.put(p.getIndex(), '%');
        }

        // 2x Words
        ArrayList<Position> twoXWordScore;
        twoXWordScore = new ArrayList<>();
        twoXWordScore.add(Position.FromInts(1,1).get());
        twoXWordScore.add(Position.FromInts(2,2).get());
        twoXWordScore.add(Position.FromInts(3,3).get());
        twoXWordScore.add(Position.FromInts(4,4).get());
        twoXWordScore.add(Position.FromInts(7,7).get());
        twoXWordScore.add(Position.FromInts(10,10).get());
        twoXWordScore.add(Position.FromInts(11,11).get());
        twoXWordScore.add(Position.FromInts(12,12).get());
        twoXWordScore.add(Position.FromInts(13,13).get());
        twoXWordScore.add(Position.FromInts(10,4).get());
        twoXWordScore.add(Position.FromInts(11,3).get());
        twoXWordScore.add(Position.FromInts(12,2).get());
        twoXWordScore.add(Position.FromInts(13,1).get());
        twoXWordScore.add(Position.FromInts(4,10).get());
        twoXWordScore.add(Position.FromInts(3,11).get());
        twoXWordScore.add(Position.FromInts(2,12).get());
        twoXWordScore.add(Position.FromInts(1,13).get());

        for(Position p: twoXWordScore){
            premiumSquares.put(p.getIndex(), '@');
        }

        // 3x Words
        ArrayList<Position> threeXWordScore;
        threeXWordScore = new ArrayList<>();
        threeXWordScore.add(Position.FromInts(0,0).get());
        threeXWordScore.add(Position.FromInts(0,7).get());
        threeXWordScore.add(Position.FromInts(0,14).get());
        threeXWordScore.add(Position.FromInts(7,0).get());
        threeXWordScore.add(Position.FromInts(7,14).get());
        threeXWordScore.add(Position.FromInts(14,0).get());
        threeXWordScore.add(Position.FromInts(14,7).get());
        threeXWordScore.add(Position.FromInts(14,14).get());

        for(Position p: threeXWordScore){
            premiumSquares.put(p.getIndex(), '#');
        }

        return premiumSquares;

    }

}
