import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TilePlacementTest {

    @Test
    void testFromShorthandSimple() {
        // TODO: Test for invalid cases

        var t1 = TilePlacement.FromShorthand("a1-a4;go*d");
        Assertions.assertEquals("A1-A4;GO_D", t1.get().toString());

        var t2 = TilePlacement.FromShorthand("a1:v;g_-d");
        Assertions.assertEquals("A1-A4;G__D", t2.get().toString());

        var t3 = TilePlacement.FromShorthand("a1:h;go-d");
        Assertions.assertEquals("A1-D1;GO_D", t3.get().toString());

        var t4 = TilePlacement.FromShorthand("a1:h;g");
        Assertions.assertEquals("A1-A1;G", t4.get().toString());
    }

}
