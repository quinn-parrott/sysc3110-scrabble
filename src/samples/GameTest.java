import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class GameTest {

    @Test
    void testGameFirstCentered() throws PlacementException, IOException {
        Player[] playerArray = new Player[2];
        playerArray[0] = new Player("P1");
        playerArray[1] = new Player("P2");
        var players = Arrays.stream(playerArray).toList();
        // TODO: Use custom wordlist here so test doesn't fail if word not present
        var g1 = new Game(players, new WordList());

        Assertions.assertThrows(PlacementException.class, () -> g1.previewPlacement(TilePlacement.FromShorthand("A1-A5;BEADS").get()));

        g1.previewPlacement(TilePlacement.FromShorthand("H8:v;Bread").get());
        var p = TilePlacement.FromShorthand("F10:h;br_ad").get();
        g1.previewPlacement(p);
        g1.printBoardState();
    }

    @Test
    void testGameSecondTurnAttached() throws PlacementException, IOException {
        Player[] playerArray = new Player[2];
        playerArray[0] = new Player("P1");
        playerArray[1] = new Player("P2");
        var players = Arrays.stream(playerArray).toList();
        var g1 = new Game(players, new WordList());

        g1.place(TilePlacement.FromShorthand("H8:h;BREAD").get());
        g1.printBoardState();
        Assertions.assertThrows(PlacementException.class, () -> g1.previewPlacement(TilePlacement.FromShorthand("I8:v;B").get()));
        g1.place(TilePlacement.FromShorthand("H9:v;Roke").get());
        g1.printBoardState();
    }

    @Test
    void testGameSecondTurnDetached() {
    }

    @Test
    void testBoardPrinting() throws PlacementException {
        // This doesn't actually test anything, just here to help with visual debugging
        var board = new Board();
        var placement = TilePlacement.FromShorthand("A2:h;Lunch").get();
        board.placeTiles(placement);
        board.placeTiles(TilePlacement.FromShorthand("B1:v;O_CHRQ").get());

        System.out.println(placement);
        board.printBoard();

        for (var w : board.collectCharSequences()) {
            if (w.length() > 1) {
                System.out.println(w);
            }
        }
    }

    @Test
    void testGameDetached() {
    }

    @Test
    void testGameInvalidWordSecond() {
    }

    @Test
    void testGameInvalidWordCross() {
    }

    @Test
    void testGameInvalidTilePlacement() {
        // TODO: Should be handled by `TilePlacement` instead
    }

}
