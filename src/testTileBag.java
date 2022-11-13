import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class testTileBag {

    @Test
    void testTileBagInit() {
        TileBag tb = new TileBag();
        Assertions.assertEquals(98, tb.getNumTilesLeft());
    }

    @Test
    void testDrawTileResetTileBag() {
        TileBag tb = new TileBag();
        for (int i = 0; i < 98; i++) {
            tb.drawTile();
        }
        Assertions.assertEquals(0, tb.getNumTilesLeft());
        Assertions.assertEquals(Optional.empty(), tb.drawTile());
        tb.resetBag();
        Assertions.assertEquals(98, tb.getNumTilesLeft());
    }

    @Test
    void testExchangeTile() {
        TileBag tb = new TileBag();
        Tile tile = tb.drawTile().orElseThrow();
        Assertions.assertEquals(97, tb.getNumTilesLeft());
        Tile newTile = tb.exchangeTile(tile);
        Assertions.assertEquals(97, tb.getNumTilesLeft());
        Assertions.assertNotSame(newTile, tile);
    }

    @Test
    void testIsEmpty() {
        TileBag tb = new TileBag();
        Assertions.assertFalse(tb.isEmpty());
        for (int i = 0; i < 98; i++) {
            tb.drawTile();
        }
        Assertions.assertTrue(tb.isEmpty());
    }

    @Test
    void testGetNumTilesLeft() {
        TileBag tb = new TileBag();
        int fullBag = tb.getNumTilesLeft();
        Assertions.assertEquals(98, fullBag);
        for (int i = 0; i < fullBag; i++) {
            tb.drawTile();
        }
        Assertions.assertEquals(0, tb.getNumTilesLeft());
    }
}