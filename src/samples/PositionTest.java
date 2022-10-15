import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    void testPositionMinLetter() {
        System.out.println("Testing Minimum Letter Input");
        Position p = new Position("a", "1");
        Assertions.assertEquals(p.getX(), 1, "Minimum Letter Input Not Handled Properly");
        System.out.println("Minimum Letter Input Handled Successfully");
    }
    @Test
    void testPositionMinNumber() {
        System.out.println("Testing Minimum Number Input");
        Position p = new Position("a", "1");
        Assertions.assertEquals(p.getY(), 1, "Minimum Number Input Not Handled Properly");
        System.out.println("Minimum Number Input Handled Successfully");
    }
    @Test
    void testPositionMaxLetter() {
        System.out.println("Testing Maximum Letter Input");
        Position p = new Position("O", "15");
        Assertions.assertEquals(p.getX(), 15, "Maximum Letter Input Not Handled Properly");
        System.out.println("Maximum Letter Input Handled Successfully");
    }
    @Test
    void testPositionMaxNumber() {
        System.out.println("Testing Maximum Number Input");
        Position p = new Position("O", "15");
        Assertions.assertEquals(p.getY(), 15, "Maximum Number Input Not Handled Properly");
        System.out.println("Maximum Number Input Handled Successfully");
    }
    @Test
    void testPositionInvalidLetter() {
        System.out.println("Testing Invalid Letter Input");
        Position p = new Position("P", "1");
        Assertions.assertEquals(p.getX(), 0, "Invalid Letter Input Not Handled Properly");
        System.out.println("Invalid Letter Input Handled Successfully");
    }
    @Test
    void testPositionInvalidNumber() {
        System.out.println("Testing Invalid Number Input");
        Position p = new Position("D", "16");
        Assertions.assertEquals(p.getY(), 0, "Invalid Number Input Not Handled Properly");
        System.out.println("Invalid Number Input Handled Successfully");
    }
    @Test
    void testPositionNonLetterX() {
        System.out.println("Testing Non-Letter Input");
        Position p = new Position("9", "E");
        Assertions.assertEquals(p.getX(), 0, "Non-Letter Input Not Handled Properly");
        System.out.println("Non-Letter Input Handled Successfully");
    }
    @Test
    void testPositionNonNumberY() {
        System.out.println("Testing Non-Number Input");
        Position p = new Position("9", "E");
        Assertions.assertEquals(p.getY(), 0, "Non-Number Input Not Handled Properly");
        System.out.println("Non-Number Input Handled Successfully");
    }
}
