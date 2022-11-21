import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class testGame {

    @Test
    void testFirstWordCrossesCenterSquare() throws PlacementException {
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
        g1.place(TilePlacement.FromShorthand("H8:v;BREAD").orElseThrow());
    }

    @Test
    void testSecondWordAttachedToExistingWord() throws PlacementException {
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
        g1.place(TilePlacement.FromShorthand("H8:h;Bread").orElseThrow());
        g1.place(TilePlacement.FromShorthand("H9:v;roke").orElseThrow());
    }

    @Test
    void testSecondTurnDetachedFromExistingWord() throws PlacementException {
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
        g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow());
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
    void testInvalidWordPlayed() throws PlacementException {
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
        g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow());
        Assertions.assertThrows(PlacementException.class,
                () -> g1.place(TilePlacement.FromShorthand("H8:h;Bzqxj").orElseThrow()));
    }

    @Test
    void testInvalidWordOverlap() throws PlacementException {
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
        g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow());
        Assertions.assertThrows(PlacementException.class,
                () -> g1.place(TilePlacement.FromShorthand("A1:h;SPARK").orElseThrow()));
    }

    @Test
    void testPointsGivenCorrectly() throws PlacementException {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        players.add(p1);
        var g1 = new Game(players, new WordList());

        p1.addTile(new Tile('B', 3));
        p1.addTile(new Tile('R', 1));
        p1.addTile(new Tile('E', 1));
        p1.addTile(new Tile('A', 1));
        p1.addTile(new Tile('D', 2));
        g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow());
        Assertions.assertEquals(10, g1.getPlayer().getPoints());
    }

    @Test
    void testSecondWordPointsGivenCorrectly() throws PlacementException {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        players.add(p1);
        var g1 = new Game(players, new WordList());

        p1.addTile(new Tile('B', 3));
        p1.addTile(new Tile('R', 1));
        p1.addTile(new Tile('E', 1));
        p1.addTile(new Tile('A', 1));
        p1.addTile(new Tile('D', 2));
        g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow());

        p1.addTile(new Tile('R', 1));
        p1.addTile(new Tile('O', 1));
        p1.addTile(new Tile('K', 5));
        p1.addTile(new Tile('E', 1));
        g1.place(TilePlacement.FromShorthand("H9:v;ROKE").orElseThrow());
        Assertions.assertEquals(22, g1.getPlayer().getPoints());
    }
}
