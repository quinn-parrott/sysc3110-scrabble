import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameTest {

    @Test
    void testGameFirstCentered() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        players.add(p1);
        players.add(new Player("P2"));
        var g1 = new Game(players, new WordList());

        Assertions.assertThrows(PlacementException.class,
                () -> g1.previewPlacement(TilePlacement.FromShorthand("A1-A5;BEADS").orElseThrow()));

        p1.addTile(new Tile('B', 0));
        p1.addTile(new Tile('R', 0));
        p1.addTile(new Tile('E', 0));
        p1.addTile(new Tile('A', 0));
        p1.addTile(new Tile('D', 0));
        Assertions.assertDoesNotThrow(() -> g1.place(TilePlacement.FromShorthand("H8:v;Bread").orElseThrow()));
    }

    @Test
    void testGameSecondTurnAttached() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        players.add(p1);
        players.add(p2);
        var g1 = new Game(players, new WordList());

        p1.addTile(new Tile('B', 0));
        p1.addTile(new Tile('R', 0));
        p1.addTile(new Tile('E', 0));
        p1.addTile(new Tile('A', 0));
        p1.addTile(new Tile('D', 0));
        p2.addTile(new Tile('R', 0));
        p2.addTile(new Tile('O', 0));
        p2.addTile(new Tile('K', 0));
        p2.addTile(new Tile('E', 0));
        Assertions.assertDoesNotThrow(() -> g1.place(TilePlacement.FromShorthand("H8:h;Bread").orElseThrow()));
        Assertions.assertDoesNotThrow(() -> g1.place(TilePlacement.FromShorthand("H8:v;Broke").orElseThrow()));
    }

    @Test
    void testGameSecondTurnDetached() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        players.add(p1);
        players.add(p2);
        var g1 = new Game(players, new WordList());

        p1.addTile(new Tile('B', 0));
        p1.addTile(new Tile('R', 0));
        p1.addTile(new Tile('E', 0));
        p1.addTile(new Tile('A', 0));
        p1.addTile(new Tile('D', 0));
        p2.addTile(new Tile('B', 0));
        p2.addTile(new Tile('R', 0));
        p2.addTile(new Tile('O', 0));
        p2.addTile(new Tile('K', 0));
        p2.addTile(new Tile('E', 0));
        Assertions.assertDoesNotThrow(() -> g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow()));
        Assertions.assertThrows(PlacementException.class,
                () -> g1.place(TilePlacement.FromShorthand("A1:h;B").orElseThrow()));
    }

    @Test
    void testNoWordsPlayed() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("P1"));
        players.add(new Player("P2"));
        var g1 = new Game(players, new WordList());
        g1.pass();
        Assertions.assertEquals(1, g1.getNumTurns());
        g1.pass();
        Assertions.assertEquals(2, g1.getNumTurns());
    }

    @Test
    void testGameInvalidWordSecond() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        players.add(p1);
        players.add(p2);
        var g1 = new Game(players, new WordList());

        p1.addTile(new Tile('B', 0));
        p1.addTile(new Tile('R', 0));
        p1.addTile(new Tile('E', 0));
        p1.addTile(new Tile('A', 0));
        p1.addTile(new Tile('D', 0));
        p2.addTile(new Tile('B', 0));
        p2.addTile(new Tile('Z', 0));
        p2.addTile(new Tile('Q', 0));
        p2.addTile(new Tile('X', 0));
        p2.addTile(new Tile('J', 0));
        Assertions.assertDoesNotThrow(() -> g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow()));
        Assertions.assertThrows(PlacementException.class,
                () -> g1.place(TilePlacement.FromShorthand("H8:h;Bzqxj").orElseThrow()));
    }

    @Test
    void testGameInvalidWordCross() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        players.add(p1);
        players.add(p2);
        var g1 = new Game(players, new WordList());

        p1.addTile(new Tile('B', 0));
        p1.addTile(new Tile('R', 0));
        p1.addTile(new Tile('E', 0));
        p1.addTile(new Tile('A', 0));
        p1.addTile(new Tile('D', 0));
        p2.addTile(new Tile('S', 0));
        p2.addTile(new Tile('P', 0));
        p2.addTile(new Tile('A', 0));
        p2.addTile(new Tile('R', 0));
        p2.addTile(new Tile('K', 0));
        Assertions.assertDoesNotThrow(() -> g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow()));
        Assertions.assertThrows(PlacementException.class,
                () -> g1.place(TilePlacement.FromShorthand("A1:h;SPARK").orElseThrow()));
    }

    @Test
    void testPointsGivenCorrectly() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        players.add(p1);
        var g1 = new Game(players, new WordList());

        p1.addTile(new Tile('B', 3));
        p1.addTile(new Tile('R', 1));
        p1.addTile(new Tile('E', 1));
        p1.addTile(new Tile('A', 1));
        p1.addTile(new Tile('D', 2));
        Assertions.assertDoesNotThrow(() -> g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow()));
        Assertions.assertEquals(8, g1.getPlayer().getPoints());
    }

    @Test
    void testSecondWordPointsGivenCorrectly() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        players.add(p1);
        var g1 = new Game(players, new WordList());

        p1.addTile(new Tile('B', 3));
        p1.addTile(new Tile('R', 1));
        p1.addTile(new Tile('E', 1));
        p1.addTile(new Tile('A', 1));
        p1.addTile(new Tile('D', 2));
        Assertions.assertDoesNotThrow(() -> g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow()));

        p1.addTile(new Tile('R', 1));
        p1.addTile(new Tile('O', 1));
        p1.addTile(new Tile('K', 5));
        p1.addTile(new Tile('E', 1));
        Assertions.assertDoesNotThrow(() -> g1.place(TilePlacement.FromShorthand("H8:v;BROKE").orElseThrow()));
        Assertions.assertEquals(19, g1.getPlayer().getPoints());
    }
}
