import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    void testPositionMinLetter() {
        System.out.println("Testing Minimum Letter Input");
        Position p = new Position("a", "1");
        Assertions.assertEquals(0, p.getX(), "Minimum Letter Input Not Handled Properly");
        System.out.println("Minimum Letter Input Handled Successfully");
    }
    @Test
    void testPositionMinNumber() {
        System.out.println("Testing Minimum Number Input");
        Position p = new Position("a", "1");
        Assertions.assertEquals(0, p.getY(), "Minimum Number Input Not Handled Properly");
        System.out.println("Minimum Number Input Handled Successfully");
    }
    @Test
    void testPositionMaxLetter() {
        System.out.println("Testing Maximum Letter Input");
        Position p = new Position("O", "15");
        Assertions.assertEquals(14, p.getX(), "Maximum Letter Input Not Handled Properly");
        System.out.println("Maximum Letter Input Handled Successfully");
    }
    @Test
    void testPositionMaxNumber() {
        System.out.println("Testing Maximum Number Input");
        Position p = new Position("O", "15");
        Assertions.assertEquals(14, p.getY(), "Maximum Number Input Not Handled Properly");
        System.out.println("Maximum Number Input Handled Successfully");
    }
    @Test
    void testPositionInvalidLetter() {
        System.out.println("Testing Invalid Letter Input");
        Position p = new Position("P", "1");
        Assertions.assertEquals(-1, p.getX(),"Invalid Letter Input Not Handled Properly");
        System.out.println("Invalid Letter Input Handled Successfully");
    }
    @Test
    void testPositionInvalidNumber() {
        System.out.println("Testing Invalid Number Input");
        Position p = new Position("D", "16");
        Assertions.assertEquals(-1, p.getY(), "Invalid Number Input Not Handled Properly");
        System.out.println("Invalid Number Input Handled Successfully");
    }
    @Test
    void testPositionNonLetterX() {
        System.out.println("Testing Non-Letter Input");
        Position p = new Position("9", "E");
        Assertions.assertEquals(-1, p.getX(), "Non-Letter Input Not Handled Properly");
        System.out.println("Non-Letter Input Handled Successfully");
    }
    @Test
    void testPositionNonNumberY() {
        System.out.println("Testing Non-Number Input");
        Position p = new Position("9", "E");
        Assertions.assertEquals(-1, p.getY(), "Non-Number Input Not Handled Properly");
        System.out.println("Non-Number Input Handled Successfully");
    }
}
