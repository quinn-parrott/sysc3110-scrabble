import java.util.Arrays;
import java.util.Collection;

public class Main {
    public static void main(String[] args) throws PlacementException {
        var players = Arrays.stream((new String[]{})).toList();
        Game g1 = new Game(players);
        TilePlacement tiles = TilePlacement.FromShorthand("h8:v;BOARD").get();
        g1.place(tiles);
//        g1.place(TilePlacement.FromShorthand("h8-h12;BOARD").get());
        g1.getBoard().printBoard();
//        g1.place(TilePlacement.FromShorthand("i5:v;MORE").get());
        g1.getBoard().printBoard();

        // Game
        // CommandLineFrontend
        // GuiFrontend <- milestone 2
    }
}