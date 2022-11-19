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

    private Optional<Integer> checkHorizontal(Board board, String word) {
        ArrayList<TilePositioned> currBoard = board.getTiles();
        for (int j = 0; j < currBoard.size(); j++) {
            if (j % Board.getROW_NUMBER() > Board.getROW_NUMBER() - word.length()) {
                j += word.length();
                continue;
            }
            Optional<TilePlacement> tp = TilePlacement.FromShorthand(Position.FromIndex(j).get() + ";h:" + word);
            if (tp.isPresent()) {
                return Optional.of(j);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> checkVertical(Board board, String word) {
        ArrayList<TilePositioned> currBoard = board.getTiles();
        for (int j = 0; j < currBoard.size() - (Board.getCOLUMN_NUMBER() * word.length()); j++) {
            Optional<TilePlacement> tp = TilePlacement.FromShorthand(Position.FromIndex(j).get() + ";v:" + word);
            if (tp.isPresent()) {
                return Optional.of(j);
            }
        }
        return Optional.empty();
    }

    private Optional<TilePlacement> boardPlacement(Board board, String word) {
        int multiplier = 1;
        for (int i = 0; i < word.length(); i++) {
            Optional<Integer> pos = checkHorizontal(board, word);
            if (pos.isEmpty()) {
                pos = checkVertical(board, word);
                multiplier = 15;
            }
            if (pos.isPresent()) {
                ArrayList<TilePositioned> tiles = new ArrayList<>();
                HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
                for (int j = 0; j < word.length(); j++) {
                    char c = word.toCharArray()[j];
                    System.out.println(c);
                    Optional<Position> cPos = Position.FromIndex(pos.get() + j * multiplier);
                    cPos.ifPresent(position -> tiles.add(new TilePositioned(tbs.get(c).tile(), position)));
                }
                return TilePlacement.FromTiles(tiles);
            }
        }
        return Optional.empty();
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

    public static void main(String[] args) throws PlacementException {
        AIPlayer p = new AIPlayer("AI");
        p.addTile(new Tile('E', 0));
        p.addTile(new Tile('T', 0));
        p.addTile(new Tile('Z', 0));
        p.addTile(new Tile('E', 0));
        p.addTile(new Tile('Y', 0));
        p.addTile(new Tile('F', 0));
        p.addTile(new Tile('L', 0));
        Board board =  new Board();
        Optional<TilePlacement> tp = TilePlacement.FromShorthand("h8:h;place");
        if (tp.isPresent()) {
            board.placeTiles(tp.get());
        }
        tp = p.AITurn(board);
        board.placeTiles(tp.orElseThrow());
        board.printBoard();
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
