import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class BoardTest {

    @Test
    void testSetTilePositionArgument() {
        var board = new Board();
        var tile = new Tile('A', 0);
        var pos = Position.FromStrings("a", "1").get();
        board.setTile(tile, pos);
        Assertions.assertEquals(tile, board.getTile(pos).orElseThrow());
    }

    @Test
    void testSetTileXYArguments() {
        var board = new Board();
        var tile = new Tile('A', 0);
        int x = Position.FromStrings("a", "1").orElseThrow().getX();
        int y = Position.FromStrings("a", "1").orElseThrow().getY();
        board.setTile(tile, x, y);
        Assertions.assertEquals(tile, board.getTile(x, y).orElseThrow());
    }

    @Test
    void testSetTileIndexArgument() {
        var board = new Board();
        var tile = new Tile('A', 0);
        int x = Position.FromStrings("a", "1").orElseThrow().getX();
        int y = Position.FromStrings("a", "1").orElseThrow().getY();
        board.setTile(tile, x + y * Board.getROW_NUMBER());
        Assertions.assertEquals(tile, board.getTile(x, y).orElseThrow());
    }

    @Test
    void testGetTiles() throws PlacementException {
        var board = new Board();
        var tiles = new ArrayList<Tile>();
        String word1 = "Lunch";
        var placement = TilePlacement.FromShorthand("A2:h;" + word1).orElseThrow();
        board.placeTiles(placement);
        for (char c : word1.toUpperCase().toCharArray()) {
            tiles.add(new Tile(c, 0));
        }
        String word2 = "Outer";
        placement = TilePlacement.FromShorthand("B1:v;" + word2).orElseThrow();
        board.placeTiles(placement);
        for (char c : word2.toUpperCase().toCharArray()) {
            if(c != 'U') {
                tiles.add(new Tile(c, 0));
            }
        }
        for (TilePositioned t : board.getTiles()) {
            Assertions.assertTrue(tiles.contains(t.tile()));
            tiles.remove(t.tile());
        }
        Assertions.assertTrue(tiles.isEmpty());
    }

    @Test
    void testCollectCharSequences() throws PlacementException {
        var board = new Board();
        var words = new ArrayList<String>();
        String word1 = "Lunch";
        var placement = TilePlacement.FromShorthand("A2:h;" + word1).orElseThrow();
        board.placeTiles(placement);
        words.add(word1.toUpperCase());
        String word2 = "Outer";
        placement = TilePlacement.FromShorthand("B1:v;" + word2).orElseThrow();
        board.placeTiles(placement);
        words.add(word2.toUpperCase());
        String word3 = "Law";
        placement = TilePlacement.FromShorthand("A2:v;" + word3).orElseThrow();
        board.placeTiles(placement);
        words.add(word3.toUpperCase());
        words.add("AT");
        words.add("WE");
        for (String word : board.collectCharSequences()) {
            Assertions.assertTrue(words.contains(word));
            words.remove(word);
        }
        Assertions.assertTrue(words.isEmpty());
    }

    @Test
    void testClone() throws PlacementException {
        var board = new Board();
        var placement = TilePlacement.FromShorthand("A2:h;Lunch").orElseThrow();
        board.placeTiles(placement);
        placement = TilePlacement.FromShorthand("B1:v;OUTER").orElseThrow();
        board.placeTiles(placement);
        var clone = board.clone();
        for (int i = 0; i < Board.getROW_NUMBER(); i++) {
            for (int j = 0; j < Board.getCOLUMN_NUMBER(); j++) {
                Assertions.assertEquals(board.getTile(i, j), clone.getTile(i, j));
            }
        }
    }

    @Test
    void testBoardPrinting() throws PlacementException {
        var board = new Board();
        var placement = TilePlacement.FromShorthand("A2:h;Lunch").orElseThrow();
        board.placeTiles(placement);
        board.placeTiles(TilePlacement.FromShorthand("B1:v;OUCHRQ").orElseThrow());

        System.out.println(placement);
        board.printBoard();

        for (var w : board.collectCharSequences()) {
            if (w.length() > 1) {
                System.out.println(w);
            }
        }
    }

    @Test
    void testPlaceTilesWithValidOverlap() throws PlacementException {
        var board = new Board();
        var words = new ArrayList<String>();
        String word1 = "Lunch";
        var placement = TilePlacement.FromShorthand("A2:h;" + word1).orElseThrow();
        board.placeTiles(placement);
        words.add(word1.toUpperCase());
        String word2 = "Outer";
        placement = TilePlacement.FromShorthand("B1:v;" + word2).orElseThrow();
        TilePlacement finalPlacement = placement;
        board.placeTiles(finalPlacement);
    }

    @Test
    void testPlaceTilesWithInvalidOverlap() throws PlacementException {
        var board = new Board();
        String word1 = "Lunch";
        var placement = TilePlacement.FromShorthand("A2:h;" + word1).orElseThrow();
        board.placeTiles(placement);
        String word2 = "Inner";
        placement = TilePlacement.FromShorthand("B1:v;" + word2).orElseThrow();
        TilePlacement finalPlacement = placement;
        Assertions.assertThrows(PlacementException.class, () -> board.placeTiles(finalPlacement));
    }
}