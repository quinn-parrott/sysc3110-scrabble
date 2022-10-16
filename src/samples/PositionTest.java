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
}
