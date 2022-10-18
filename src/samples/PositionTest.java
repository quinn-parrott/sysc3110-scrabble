import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
        Assertions.assertEquals(true, Position.FromStrings("P", "1").isEmpty(),
                "Invalid Letter Input Not Handled Properly");
        System.out.println("Invalid Letter Input Handled Successfully");
    }

    @Test
    void testPositionInvalidNumber() {
        System.out.println("Testing Invalid Number Input");
        Assertions.assertEquals(true, Position.FromStrings("D", "16").isEmpty(),
                "Invalid Number Input Not Handled Properly");
        System.out.println("Invalid Number Input Handled Successfully");
    }

    @Test
    void testPositionNonLetterX() {
        System.out.println("Testing Non-Letter Input");
        Assertions.assertEquals(true, Position.FromStrings("9", "E").isEmpty(),
                "Non-Letter Input Not Handled Properly");
        System.out.println("Non-Letter Input Handled Successfully");
    }

    @Test
    void testPositionNonNumberY() {
        System.out.println("Testing Non-Number Input");
        Assertions.assertEquals(true, Position.FromStrings("9", "E").isEmpty(),
                "Non-Number Input Not Handled Properly");
        System.out.println("Non-Number Input Handled Successfully");
    }

    @Test
    void testPositionFromString() {
        var pos1 = Position.FromString("A1").get();
        Assertions.assertEquals(0, pos1.getX());
        Assertions.assertEquals(0, pos1.getY());

        var pos2 = Position.FromString("c10").get();
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
                        Position.FromString("C4").get(),
                        Position.FromString("C7").get()
                ).get().toArray(),
                new Position[]{
                        Position.FromString("C4").get(),
                        Position.FromString("C5").get(),
                        Position.FromString("C6").get(),
                        Position.FromString("C7").get(),
                }
        );

        Assertions.assertArrayEquals(
                Position.Interpolate(
                        Position.FromString("A7").get(),
                        Position.FromString("A4").get()
                ).get().toArray(),
                new Position[]{
                        Position.FromString("a4").get(),
                        Position.FromString("a5").get(),
                        Position.FromString("a6").get(),
                        Position.FromString("a7").get(),
                }
        );

        Assertions.assertArrayEquals(
                Position.Interpolate(
                        Position.FromString("E4").get(),
                        Position.FromString("K4").get()
                ).get().toArray(),
                new Position[]{
                        Position.FromString("E4").get(),
                        Position.FromString("F4").get(),
                        Position.FromString("G4").get(),
                        Position.FromString("H4").get(),
                        Position.FromString("I4").get(),
                        Position.FromString("J4").get(),
                        Position.FromString("K4").get()
                }
        );

        Assertions.assertArrayEquals(
                Position.Interpolate(
                        Position.FromString("C1").get(),
                        Position.FromString("B1").get()
                ).get().toArray(),
                new Position[]{
                        Position.FromString("B1").get(),
                        Position.FromString("C1").get()
                }
        );
    }
}
