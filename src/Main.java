import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class Main {
    public static void main(String[] args) throws PlacementException, IOException {
        var players = Arrays.stream((new String[]{})).toList();
        Game g1 = new Game(players, new WordList());
//        g1.place(TilePlacement.FromShorthand("h8:v;BOARD").get());
        g1.place(TilePlacement.FromShorthand("h8-h12;BOARD").get());
        g1.getBoard().printBoard();
//        g1.place(TilePlacement.FromShorthand("h9:v;MORE").get());
        g1.getBoard().printBoard();

        // Game
        // CommandLineFrontend
        // GuiFrontend <- milestone 2
    }
}