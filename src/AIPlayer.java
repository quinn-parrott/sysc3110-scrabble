import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class AIPlayer extends Player {

    private WordList wordList;

    /**
     * Default constructor for AIPlayer Class.
     * @param name the name of the player
     * @author Colin Mandeville, 101140289
     */
    public AIPlayer(String name) {
        super(name);
        this.wordList = new WordList();
    }

    /**
     * Filters down full wordlist to only those which the player has almost all the tiles to play, missing at most 1 tile
     * @return Returns an ArrayList of all words the player may be able to play
     */
    private ArrayList<String> getPossibleWords() {
        ArrayList<String> possibleWords = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for (Tile tile : this.getTileHand()) {
            sb.append(tile.chr());
        }
        String letterHandString;
        boolean oneOff;
        boolean valid;
        for (String word : this.wordList.getWordlist()) {
            if (word.length() > 2) {
                letterHandString = sb.toString();
                oneOff = true;
                valid = true;
                for (char c : word.toUpperCase().toCharArray()) {
                    if (letterHandString.contains(c + "")) {
                        letterHandString = letterHandString.replace(c, ' ');
                    } else if (oneOff) {
                        oneOff = false;
                    } else {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    possibleWords.add(word);
                }
            }
        }
        return possibleWords;
    }

    private boolean checkHorizontal(Board board, String word) {
        return true;
    }

    private boolean checkVertical(Board board, String word) {
        return true;
    }

    private Optional<TilePlacement> boardPlacement(Board board, String word) {
        ArrayList<TilePositioned> currBoard = board.getTiles();
        for (int i = 0; i < word.length(); i++) {
            char c = word.toUpperCase().toCharArray()[i];
            for (int j = 0; j < currBoard.size(); j++) {
                TilePositioned tp = currBoard.get(j);
                if (tp.tile().chr() == c) {
                    int hIndex = j - (word.length());
                    int vIndex = j - (word.length() * 15);
                    if (hIndex < 0 || vIndex < 0) {continue;}
                    boolean hValid = true;
                    boolean vValid = true;
                    for (int k = 0; k < word.length(); k++) {
                        if (currBoard.get(hIndex + k).tile().isFilledWithLetter() && currBoard.get(hIndex + k).tile().chr() != word.charAt(k)) {
                            hValid = false;
                            break;
                        }
                    }
                    if (hValid) {
                        return TilePlacement.FromShorthand(Position.FromIndex(hIndex) + ":h;" + word);
                    }
                    for (int k = 0; k < word.length(); k++) {
                        if (currBoard.get(vIndex + k * 15).tile().isFilledWithLetter() && currBoard.get(vIndex + k).tile().chr() != word.charAt(k)) {
                            vValid = false;
                            break;
                        }
                    }
                    if (vValid) {
                        break;
                    }
                }
            }
        }
    }

    public Optional<TilePlacement> AITurn(Board board) {
        ArrayList<String> possibleWords = getPossibleWords();
        possibleWords.sort(new LengthComparator());
        for (String word : possibleWords) {
            Optional<TilePlacement> tp = this.boardPlacement(board, word);
            if (tp.isPresent()) {
                return tp;
            }
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        AIPlayer p = new AIPlayer("AI");
        p.addTile(new Tile('E', 0));
        p.addTile(new Tile('T', 0));
        p.addTile(new Tile('Z', 0));
        p.addTile(new Tile('E', 0));
        p.addTile(new Tile('Y', 0));
        p.addTile(new Tile('F', 0));
        p.addTile(new Tile('L', 0));
        for (Object validWord : p.AITurn().orElseThrow()) {
            System.out.println(validWord);
        }
    }

    public class LengthComparator implements java.util.Comparator<String> {
        public LengthComparator() {
            super();
        }

        @Override
        public int compare(String o1, String o2) {
            HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
            int score1 = 0;
            int score2 = 0;
            for (int i = 0; i < o1.length(); i++) {
                score1 += tbs.get(o1.toUpperCase().toCharArray()[i]).tile().pointValue();
            }
            for (int i = 0; i < o2.length(); i++) {
                score2 += tbs.get(o2.toUpperCase().toCharArray()[i]).tile().pointValue();
            }
            if (score1 > score2) {
                return -1;
            } else if (score2 > score1) {
                return 1;
            }
            return 0;
        }
    }
}
