import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class WordListTest {

    @Test
    void isValidWord() {
        WordList wl = new WordList();
        var stream = Optional.ofNullable(this.getClass().getResourceAsStream(WordList.resourcePath));
        Scanner scanner =  new Scanner(stream.orElseThrow());
        String word = scanner.nextLine().strip().toUpperCase();
        Assertions.assertTrue(wl.isValidWord(word));
        word = "woerhfbvarlbjckbwkliwuabjvsbrlawb;jrkwAJVRBQBgrawetahst";
        Assertions.assertFalse(wl.isValidWord(word));
    }
}