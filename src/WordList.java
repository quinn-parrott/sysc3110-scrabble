import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;

/**
 * Checking for valid scrabble words
 *
 * @author Tao Lufula, 101164153
 */

public class WordList {
    public static final String resourcePath = "wordList.resources";

    private final HashSet<String> wordlist;

    /**
     * Default constructor for WordList class
     * @author Tao Lufula, 101164153
     */
    public WordList() {
        this.wordlist = new HashSet<>();
        this.readWordListFile(resourcePath);
    }

    /**
     * This method reads words from a text file and add them to the HashSet WordList
     * @param resourcePath the name of the txt file to be read
     *
     * @author Tao Lufula, 101164153
     */
    public void readWordListFile(String resourcePath) {
        var stream = Optional.ofNullable(this.getClass().getResourceAsStream(resourcePath));
        if (stream.isEmpty()) {
            throw new RuntimeException(String.format("Resource '%s' doesn't exist", resourcePath));
        }

        try (Scanner scanner =  new Scanner(stream.get())){
            while (scanner.hasNextLine()){
                this.wordlist.add(scanner.nextLine().strip().toUpperCase());
            }
        }
    }

    /**
     * Method isValidWord checks whether the given word exists in the set of accepted words
     * @param word word to be checked
     * @return true if word exists in the set, false otherwise
     * @author Tao Lufula, 101164153
     */
    public boolean isValidWord(String word){
        return this.wordlist.contains(word.toUpperCase());
    }

    /**
     * Getter method for the list of valid words, used by AI players when determining a move
     * @return wordlist attribute of the Wordlist object
     */
    public HashSet<String> getWordlist() {
        return new HashSet<>(wordlist);
    }
}


