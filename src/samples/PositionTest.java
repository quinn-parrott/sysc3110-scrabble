import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    void testPositionMinLetter() {
        System.out.println("Testing Minimum Letter Input");
        Assertions.assertEquals(0, Position.FromStrings("a", "1").orElseThrow().getX(),
                "Minimum Letter Input Not Handled Properly");
        System.out.println("Minimum Letter Input Handled Successfully");
    }

    @Test
    void testPositionMinNumber() {
        System.out.println("Testing Minimum Number Input");
        Assertions.assertEquals(0, Position.FromStrings("a", "1").orElseThrow().getY(),
                "Minimum Number Input Not Handled Properly");
        System.out.println("Minimum Number Input Handled Successfully");
    }

    @Test
    void testPositionMaxLetter() {
        System.out.println("Testing Maximum Letter Input");
        Assertions.assertEquals(14, Position.FromStrings("O", "15").orElseThrow().getX(),
                "Maximum Letter Input Not Handled Properly");
        System.out.println("Maximum Letter Input Handled Successfully");
    }

    @Test
    void testPositionMaxNumber() {
        System.out.println("Testing Maximum Number Input");
        Assertions.assertEquals(14, Position.FromStrings("O", "15").orElseThrow().getY(),
                "Maximum Number Input Not Handled Properly");
        System.out.println("Maximum Number Input Handled Successfully");
    }

    @Test
    void testPositionInvalidLetter() {
        System.out.println("Testing Invalid Letter Input");
        Assertions.assertTrue(Position.FromStrings("P", "1").isEmpty(), "Invalid Letter Input Not Handled Properly");
        System.out.println("Invalid Letter Input Handled Successfully");
    }

    @Test
    void testPositionInvalidNumber() {
        System.out.println("Testing Invalid Number Input");
        Assertions.assertTrue(Position.FromStrings("D", "16").isEmpty(), "Invalid Number Input Not Handled Properly");
        System.out.println("Invalid Number Input Handled Successfully");
    }

    @Test
    void testPositionNonLetterX() {
        System.out.println("Testing Non-Letter Input");
        Assertions.assertTrue(Position.FromStrings("9", "E").isEmpty(), "Non-Letter Input Not Handled Properly");
        System.out.println("Non-Letter Input Handled Successfully");
    }

    @Test
    void testPositionNonNumberY() {
        System.out.println("Testing Non-Number Input");
        Assertions.assertTrue(Position.FromStrings("9", "E").isEmpty(), "Non-Number Input Not Handled Properly");
        System.out.println("Non-Number Input Handled Successfully");
    }

    @Test
    void testPositionFromString() {
        var pos1 = Position.FromString("A1").orElseThrow();
        Assertions.assertEquals(0, pos1.getX());
        Assertions.assertEquals(0, pos1.getY());

        var pos2 = Position.FromString("c10").orElseThrow();
        Assertions.assertEquals(2, pos2.getX());
        Assertions.assertEquals(9, pos2.getY());
    }

    @Test
    void testPositionFromStringInvalid() {
        Assertions.assertTrue(Position.FromString("").isEmpty());
        Assertions.assertTrue(Position.FromString("1c").isEmpty());
        Assertions.assertTrue(Position.FromString("1").isEmpty());
        Assertions.assertTrue(Position.FromString("c").isEmpty());
    }

    @Test
    void testPositionInterpolateAbsolute() {
        Assertions.assertArrayEquals(
                Position.Interpolate(
                        Position.FromString("C4").orElseThrow(),
                        Position.FromString("C7").orElseThrow()
                ).orElseThrow().toArray(),
                new Position[]{
                        Position.FromString("C4").orElseThrow(),
                        Position.FromString("C5").orElseThrow(),
                        Position.FromString("C6").orElseThrow(),
                        Position.FromString("C7").orElseThrow(),
                }
        );

        Assertions.assertArrayEquals(
                Position.Interpolate(
                        Position.FromString("A7").orElseThrow(),
                        Position.FromString("A4").orElseThrow()
                ).orElseThrow().toArray(),
                new Position[]{
                        Position.FromString("a4").orElseThrow(),
                        Position.FromString("a5").orElseThrow(),
                        Position.FromString("a6").orElseThrow(),
                        Position.FromString("a7").orElseThrow(),
                }
        );

        Assertions.assertArrayEquals(
                Position.Interpolate(
                        Position.FromString("E4").orElseThrow(),
                        Position.FromString("K4").orElseThrow()
                ).orElseThrow().toArray(),
                new Position[]{
                        Position.FromString("E4").orElseThrow(),
                        Position.FromString("F4").orElseThrow(),
                        Position.FromString("G4").orElseThrow(),
                        Position.FromString("H4").orElseThrow(),
                        Position.FromString("I4").orElseThrow(),
                        Position.FromString("J4").orElseThrow(),
                        Position.FromString("K4").orElseThrow()
                }
        );

        Assertions.assertArrayEquals(
                Position.Interpolate(
                        Position.FromString("C1").orElseThrow(),
                        Position.FromString("B1").orElseThrow()
                ).orElseThrow().toArray(),
                new Position[]{
                        Position.FromString("B1").orElseThrow(),
                        Position.FromString("C1").orElseThrow()
                }
        );
    }

    @Test
    void testPositionIndex() {
        for (var i = 0; i < Board.getCOLUMN_NUMBER() * Board.getROW_NUMBER(); i++) {
            Assertions.assertEquals(i, Position.FromIndex(i).orElseThrow().getIndex());
        }
    }

    @Test
    void testPositionDistance() {
        var pos1 = Position.FromString("C3");
        var pos2 = Position.FromString("C4");
        Assertions.assertTrue(Math.abs(Position.Distance(pos1.orElseThrow(), pos2.orElseThrow()) - 1) < 0.01);
        pos1 = Position.FromString("A7");
        pos2 = Position.FromString("C5");
        Assertions.assertTrue(Math.abs(Position.Distance(pos1.orElseThrow(), pos2.orElseThrow()) - 2.828427) < 0.01);
    }

    @Test
    void testEquals() {
        var pos1 = Position.FromString("C5").orElseThrow();
        var pos2 = Position.FromString("C6").orElseThrow();
        var pos3 = Position.FromString("C6").orElseThrow();
        var pos4 = Position.FromString("D5").orElseThrow();
        Assertions.assertNotEquals(pos1, pos2);
        Assertions.assertEquals(pos2, pos3);
        Assertions.assertNotEquals(pos3, pos4);
        Assertions.assertNotEquals(pos1, pos4);
    }

    @Test
    void testToString() {
        var pos = Position.FromInts(6, 11).orElseThrow();
        Assertions.assertEquals("G12", pos.toString());
    }
}
