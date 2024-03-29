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

        p1.addTile(new WildcardableTile('B', 0));
        p1.addTile(new WildcardableTile('R', 0));
        p1.addTile(new WildcardableTile('E', 0));
        p1.addTile(new WildcardableTile('A', 0));
        p1.addTile(new WildcardableTile('D', 0));
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

        p1.addTile(new WildcardableTile('B', 0));
        p1.addTile(new WildcardableTile('R', 0));
        p1.addTile(new WildcardableTile('E', 0));
        p1.addTile(new WildcardableTile('A', 0));
        p1.addTile(new WildcardableTile('D', 0));
        p2.addTile(new WildcardableTile('R', 0));
        p2.addTile(new WildcardableTile('O', 0));
        p2.addTile(new WildcardableTile('K', 0));
        p2.addTile(new WildcardableTile('E', 0));
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

        p1.addTile(new WildcardableTile('B', 0));
        p1.addTile(new WildcardableTile('R', 0));
        p1.addTile(new WildcardableTile('E', 0));
        p1.addTile(new WildcardableTile('A', 0));
        p1.addTile(new WildcardableTile('D', 0));
        p2.addTile(new WildcardableTile('B', 0));
        p2.addTile(new WildcardableTile('R', 0));
        p2.addTile(new WildcardableTile('O', 0));
        p2.addTile(new WildcardableTile('K', 0));
        p2.addTile(new WildcardableTile('E', 0));
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

        p1.addTile(new WildcardableTile('B', 0));
        p1.addTile(new WildcardableTile('R', 0));
        p1.addTile(new WildcardableTile('E', 0));
        p1.addTile(new WildcardableTile('A', 0));
        p1.addTile(new WildcardableTile('D', 0));
        p2.addTile(new WildcardableTile('B', 0));
        p2.addTile(new WildcardableTile('Z', 0));
        p2.addTile(new WildcardableTile('Q', 0));
        p2.addTile(new WildcardableTile('X', 0));
        p2.addTile(new WildcardableTile('J', 0));
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

        p1.addTile(new WildcardableTile('B', 0));
        p1.addTile(new WildcardableTile('R', 0));
        p1.addTile(new WildcardableTile('E', 0));
        p1.addTile(new WildcardableTile('A', 0));
        p1.addTile(new WildcardableTile('D', 0));
        p2.addTile(new WildcardableTile('S', 0));
        p2.addTile(new WildcardableTile('P', 0));
        p2.addTile(new WildcardableTile('A', 0));
        p2.addTile(new WildcardableTile('R', 0));
        p2.addTile(new WildcardableTile('K', 0));
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

        p1.addTile(new WildcardableTile('B', 3));
        p1.addTile(new WildcardableTile('R', 1));
        p1.addTile(new WildcardableTile('E', 1));
        p1.addTile(new WildcardableTile('A', 1));
        p1.addTile(new WildcardableTile('D', 2));
        g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow());
        Assertions.assertEquals(20, g1.getPlayer().getPoints());
    }

    @Test
    void testSecondWordPointsGivenCorrectly() throws PlacementException {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        players.add(p1);
        var g1 = new Game(players, new WordList());

        p1.addTile(new WildcardableTile('B', 3));
        p1.addTile(new WildcardableTile('R', 1));
        p1.addTile(new WildcardableTile('E', 1));
        p1.addTile(new WildcardableTile('A', 1));
        p1.addTile(new WildcardableTile('D', 2));
        g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow());

        p1.addTile(new WildcardableTile('R', 1));
        p1.addTile(new WildcardableTile('O', 1));
        p1.addTile(new WildcardableTile('K', 5));
        p1.addTile(new WildcardableTile('E', 1));
        g1.place(TilePlacement.FromShorthand("H9:v;ROKE").orElseThrow());
        Assertions.assertEquals(32, g1.getPlayer().getPoints());
    }

    @Test
    void test3xLetterPointsGivenCorrectly() throws PlacementException {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        players.add(p1);
        var g1 = new Game(players, new WordList());

        p1.addTile(new WildcardableTile('B', 3));
        p1.addTile(new WildcardableTile('R', 1));
        p1.addTile(new WildcardableTile('E', 1));
        p1.addTile(new WildcardableTile('A', 1));
        p1.addTile(new WildcardableTile('D', 2));
        g1.place(TilePlacement.FromShorthand("H8:h;BREAD").orElseThrow());

        p1.addTile(new WildcardableTile('R', 1));
        p1.addTile(new WildcardableTile('O', 1));
        p1.addTile(new WildcardableTile('K', 5));
        p1.addTile(new WildcardableTile('E', 1));
        g1.place(TilePlacement.FromShorthand("H9:v;ROKE").orElseThrow());

        p1.addTile(new WildcardableTile('T', 1));
        p1.addTile(new WildcardableTile('R', 1));
        p1.addTile(new WildcardableTile('S', 1));
        p1.addTile(new WildcardableTile('E', 1));
        g1.place(TilePlacement.FromShorthand("J6:v;TR_ES").orElseThrow());
        Assertions.assertEquals(41, g1.getPlayer().getPoints());
    }

    @Test
    void test3xWordPointsGivenCorrectly() throws PlacementException {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        players.add(p1);
        var g1 = new Game(players, new WordList());

        p1.addTile(new WildcardableTile('B', 3));
        p1.addTile(new WildcardableTile('R', 1));
        p1.addTile(new WildcardableTile('E', 1));
        p1.addTile(new WildcardableTile('E', 1));
        p1.addTile(new WildcardableTile('D', 2));
        g1.place(TilePlacement.FromShorthand("H8:h;BREED").orElseThrow());

        p1.addTile(new WildcardableTile('R', 1));
        p1.addTile(new WildcardableTile('O', 1));
        p1.addTile(new WildcardableTile('K', 5));
        p1.addTile(new WildcardableTile('E', 1));
        g1.place(TilePlacement.FromShorthand("H9:v;ROKE").orElseThrow());

        p1.addTile(new WildcardableTile('T', 1));
        p1.addTile(new WildcardableTile('R', 1));
        p1.addTile(new WildcardableTile('S', 1));
        p1.addTile(new WildcardableTile('E', 1));
        g1.place(TilePlacement.FromShorthand("J6:v;TR_ES").orElseThrow());

        p1.addTile(new WildcardableTile('I', 1));
        p1.addTile(new WildcardableTile('N', 1));
        p1.addTile(new WildcardableTile('G', 2));
        g1.place(TilePlacement.FromShorthand("M8:h;ING").orElseThrow());
        Assertions.assertEquals(77, g1.getPlayer().getPoints());
    }

    @Test
    void testAIFirstMove() {
        Player ai = new Player("AI", true);
        Player human = new Player("Colin");
        ArrayList<Player> al = new ArrayList<>();
        al.add(ai);
        al.add(human);
        Game g = new Game(al, new WordList());
        (new AiPlayer(g)).AiTurn();
        Assertions.assertNotEquals('*', g.getBoard().getTile(Position.FromIndex(Board.getCenterTilePos()).get()).orElseThrow().chr());
        Assertions.assertNotEquals(0, ai.getPoints());
    }

    @Test
    void testAITurnOnlyUsableByAI() {
        Player ai = new Player("AI", true);
        Player human = new Player("Colin");
        ArrayList<Player> al = new ArrayList<>();
        al.add(ai);
        al.add(human);
        Game g = new Game(al, new WordList());
        Assertions.assertEquals(ai, g.getPlayer());
        (new AiPlayer(g)).AiTurn();
        Assertions.assertEquals(human, g.getPlayer());
        (new AiPlayer(g)).AiTurn();
        Assertions.assertEquals(human, g.getPlayer());
    }

}
