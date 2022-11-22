import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class testTilePlacement {

    @Test
    void testFromShorthandSimple() {
        // TODO: Test for invalid cases

        var t1 = TilePlacement.FromShorthand("a1-a4;go*d");
        Assertions.assertEquals("A1-A4;GO_D", t1.orElseThrow().toString());

        var t2 = TilePlacement.FromShorthand("a1:v;g_-d");
        Assertions.assertEquals("A1-A4;G__D", t2.orElseThrow().toString());

        var t3 = TilePlacement.FromShorthand("a1:h;go-d");
        Assertions.assertEquals("A1-D1;GO_D", t3.orElseThrow().toString());

        var t4 = TilePlacement.FromShorthand("a1:h;g");
        Assertions.assertEquals("A1-A1;G", t4.orElseThrow().toString());

        var t5 = TilePlacement.FromShorthand("a1-a6;ho*d-e");
        Assertions.assertEquals("A1-A6;HO_D_E", t5.orElseThrow().toString());

        var t6 = TilePlacement.FromShorthand("a1-a5;*e*Lo");
        Assertions.assertEquals("A2-A5;E_LO", t6.orElseThrow().toString());

        var t7 = TilePlacement.FromShorthand("a1-a4; *rd");
        Assertions.assertEquals("A3-A4;RD", t7.orElseThrow().toString());

        var t8 = TilePlacement.FromShorthand("a1-a4;bi_*");
        Assertions.assertEquals("A1-A2;BI", t8.orElseThrow().toString());
    }

    @Test
    void testFromShorthandRegression1() {
        var t1 = TilePlacement.FromShorthand("F10:h;br_ad");
        Assertions.assertEquals("F10-J10;BR_AD", t1.orElseThrow().toString());

        var t2 = TilePlacement.FromShorthand("F10:h;br_a ");
        Assertions.assertEquals("F10-I10;BR_A", t2.orElseThrow().toString());

        var t3 = TilePlacement.FromShorthand("F10:v;*read");
        Assertions.assertEquals("F11-F14;READ", t3.orElseThrow().toString());

        var t4 = TilePlacement.FromShorthand("F10:v; r*a ");
        Assertions.assertEquals("F11-F13;R_A", t4.orElseThrow().toString());

        var t5 = TilePlacement.FromShorthand("F10:v;B*_a-");
        Assertions.assertEquals("F10-F13;B__A", t5.orElseThrow().toString());

    }

    @Test
    void testMinTileDistance(){
        var p = Position.FromIndex(Board.getCenterTilePos()).orElseThrow();

        var t1 = TilePlacement.FromShorthand("F10:v;bread");
        Assertions.assertEquals(2, (int)(t1.orElseThrow().minTileDistance(p)));

        var t2 = TilePlacement.FromShorthand("H8:h;bread");
        Assertions.assertEquals(0, (int)(t2.orElseThrow().minTileDistance(p)));

        var t3 = TilePlacement.FromShorthand("g7:h;bread");
        Assertions.assertNotEquals(2, (int)(t3.orElseThrow().minTileDistance(p)));
    }

    @Test
    void testFromTiles() {
        var al = new ArrayList<Positioned<Tile>>();
        al.add(new Positioned<>(new Tile('B', 0), Position.FromInts(7,7).orElseThrow()));
        al.add(new Positioned<>(new Tile('R', 0), Position.FromInts(7,8).orElseThrow()));
        al.add(new Positioned<>(new Tile('E', 0), Position.FromInts(7,9).orElseThrow()));
        al.add(new Positioned<>(new Tile('A', 0), Position.FromInts(7,10).orElseThrow()));
        al.add(new Positioned<>(new Tile('D', 0), Position.FromInts(7,11).orElseThrow()));
        var p = TilePlacement.FromTiles(al);
        Assertions.assertFalse(p.isEmpty());
        TilePlacement expected = TilePlacement.FromShorthand("h8:v;bread").get();
        for (int i = 0; i < p.get().getTiles().size(); i++) {
            Assertions.assertEquals(expected.getTiles().get(i), p.get().getTiles().get(i));
        }
    }
}
