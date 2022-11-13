import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals("A1-A5;_E_LO", t6.orElseThrow().toString());

        var t7 = TilePlacement.FromShorthand("a1-a4; *rd");
        Assertions.assertEquals("A1-A4;__RD", t7.orElseThrow().toString());

        var t8 = TilePlacement.FromShorthand("a1-a4;bi_*");
        Assertions.assertEquals("A1-A4;BI__", t8.orElseThrow().toString());

        var t9 = TilePlacement.FromShorthand("a1-a3;b  ");
        Assertions.assertNotEquals("A1-A1;B", t9.orElseThrow().toString());

        var t10 = TilePlacement.FromShorthand("a1-a4;b r-");
        Assertions.assertNotEquals("A1-A3;BR_", t10.orElseThrow().toString());

        var t11 = TilePlacement.FromShorthand("a1-a3;b  ");
        Assertions.assertNotEquals("A1-A1;B", t11.orElseThrow().toString());

        var t12 = TilePlacement.FromShorthand("a1-a5; bird");
        Assertions.assertNotEquals("A1-A4;BIRD", t12.orElseThrow().toString());

    }

    @Test
    void testFromShorthandRegression1() {
        var t1 = TilePlacement.FromShorthand("F10:h;br_ad");
        Assertions.assertEquals("F10-J10;BR_AD", t1.orElseThrow().toString());

        var t2 = TilePlacement.FromShorthand("F10:h;br_a ");
        Assertions.assertEquals("F10-J10;BR_A_", t2.orElseThrow().toString());

        var t3 = TilePlacement.FromShorthand("F10:v;*read");
        Assertions.assertEquals("F10-F14;_READ", t3.orElseThrow().toString());

        var t4 = TilePlacement.FromShorthand("F10:v; r*a ");
        Assertions.assertEquals("F10-F14;_R_A_", t4.orElseThrow().toString());

        var t5 = TilePlacement.FromShorthand("F10:v;B*_a-");
        Assertions.assertEquals("F10-F14;B__A_", t5.orElseThrow().toString());

        var t6 = TilePlacement.FromShorthand("F10:h;br_  ");
        Assertions.assertNotEquals("F10-J12;BR_", t6.orElseThrow().toString());

        var t7 = TilePlacement.FromShorthand("F10:v;bread ");
        Assertions.assertNotEquals("F10-F14;BREAD", t7.orElseThrow().toString());

        var t8 = TilePlacement.FromShorthand("F10:v; bread");
        Assertions.assertNotEquals("F10-F14;BREAD", t8.orElseThrow().toString());

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
}
