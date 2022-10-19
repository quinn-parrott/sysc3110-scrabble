import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class GameTest {

    @Test
    void testGameFirstCentered() throws PlacementException {
        var players = Arrays.stream((new String[]{})).toList();
        var g1 = new Game(players);

        Assertions.assertThrows(PlacementException.class, () -> g1.previewPlacement(TilePlacement.FromShorthand("A1-A5;BEADS").get()));

        g1.previewPlacement(TilePlacement.FromShorthand("H8:v;BEADS").get());
        g1.previewPlacement(TilePlacement.FromShorthand("H6:v;BEADS").get());
        Assertions.assertThrows(PlacementException.class, () -> g1.previewPlacement(TilePlacement.FromShorthand("H9:v;BEADS").get()));

        g1.previewPlacement(TilePlacement.FromShorthand("H8:h;BEADS").get());
        g1.previewPlacement(TilePlacement.FromShorthand("E8:h;BEADS").get());
        Assertions.assertThrows(PlacementException.class, () -> g1.previewPlacement(TilePlacement.FromShorthand("I8:h;BEADS").get()));
    }

    @Test
    void testGameSecondTurnAttached() throws PlacementException {
        var players = Arrays.stream((new String[]{})).toList();
        var g1 = new Game(players);

        g1.place(TilePlacement.FromShorthand("H8:h;BEADS").get());
        g1.getBoard().printBoard();
        Assertions.assertThrows(PlacementException.class, () -> g1.previewPlacement(TilePlacement.FromShorthand("I8:v;B").get()));
        g1.place(TilePlacement.FromShorthand("H9:v;MORE").get());
        g1.getBoard().printBoard();
    }

    @Test
    void testGameSecondTurnDetached() {
    }

    @Test
    void testBoardPrinting() throws PlacementException {
        // This doesn't actually test anything, just here to help with visual debugging
        var board = new Board();
        var placement = TilePlacement.FromShorthand("A1:h;Lunch").get();
        board.placeTiles(placement);

        System.out.println(placement);
        board.printBoard();
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
