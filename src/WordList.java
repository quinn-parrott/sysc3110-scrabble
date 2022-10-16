import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;

/**
 * @author Tao Lufula, 101164153
 */

public class WordList {
    private HashSet<String> wordlist;

    /**
     * Default constructor for WordList class
     * @throws IOException
     * @author Tao Lufula, 101164153
     */
    public WordList() throws IOException {
        this.wordlist = new HashSet<>();
        this.readWordListFile("wordList.txt");
    }

    /**
     * This method reads words from a text file and add them to the HashSet WordList
     * @param fileName the name of the txt file to be read
     * @throws IOException
     *
     * @author Tao Lufula, 101164153
     */
    public void readWordListFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        try (Scanner scanner =  new Scanner(path)){
            while (scanner.hasNextLine()){
                this.wordlist.add(scanner.nextLine());
            }
        }
    }
}


