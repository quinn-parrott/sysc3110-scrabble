import java.util.*;

public class AIPlayer extends Player {

    private final WordList wordList;

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
    private ArrayList<String> getPossibleWords(int numTilesOff) {
        ArrayList<String> possibleWords = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (Tile tile : this.getTileHand()) {
            sb.append(tile.chr());
        }
        String letterHandString;
        boolean valid;
        for (String word : this.wordList.getWordlist()) {
            if (word.length() > 2) {
                letterHandString = sb.toString();
                valid = true;
                for (char c : word.toUpperCase().toCharArray()) {
                    if (letterHandString.contains(Character.toString(c))) {
                        letterHandString = letterHandString.replace(c, ' ');
                    } else if (numTilesOff > 0) {
                        numTilesOff--;
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

    /**
     * Checks for the first valid horizontal placement of a string on a Board object
     * @param game Game object, the current model of the game
     * @param word String representing the word to be played
     * @return Returns the starting index at which to place tiles
     */
    private Optional<TilePlacement> checkHorizontal(Game game, String word) {
        ArrayList<TilePositioned> tiles = new ArrayList<>();
        HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
        for (int i = 0; i < Board.getROW_NUMBER() * Board.getCOLUMN_NUMBER() - word.length(); i++) {
            if (i % Board.getROW_NUMBER() > Board.getROW_NUMBER() - word.length()) {
                i += word.length();
                continue;
            }
            tiles.clear();
            for (int j = 0; j < word.length(); j++) {
                char c = word.toCharArray()[j];
                Optional<Position> cPos = Position.FromIndex(j + i);
                cPos.ifPresent(position -> tiles.add(new TilePositioned(tbs.get(c).tile(), position)));
            }
            Optional<TilePlacement> tp = TilePlacement.FromTiles(tiles);
            if (tp.isPresent()) {
                try {
                    game.previewPlacement(tp.get());
                    System.out.println(word);
                    return tp;
                } catch (PlacementException ignored) {}
            }
        }
        return Optional.empty();
    }

    /**
     * Checks for the first valid vertical placement of a string on a Board object
     * @param game Game object, the current model of the game
     * @param word String representing the word to be played
     * @return Returns the starting index at which to place tiles
     */
    private Optional<TilePlacement> checkVertical(Game game, String word) {
        ArrayList<TilePositioned> tiles = new ArrayList<>();
        HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
        for (int i = 0; i < Board.getCOLUMN_NUMBER() * Board.getROW_NUMBER(); i++) {
            tiles.clear();
            for (int j = 0; j < word.length(); j++) {
                char c = word.toCharArray()[j];
                Optional<Position> cPos = Position.FromIndex(i + (j * Board.getROW_NUMBER()));
                cPos.ifPresent(position -> tiles.add(new TilePositioned(tbs.get(c).tile(), position)));
            }
            Optional<TilePlacement> tp = TilePlacement.FromTiles(tiles);
            if (tp.isPresent()) {
                try {
                    game.previewPlacement(tp.get());
                    return tp;
                } catch (PlacementException ignored) {}
            }
        }
        return Optional.empty();
    }

    /**
     * Determines if there is a placement for a passed word on the current board state of the game object
     * @param game Game object the AI player is playing in
     * @param word A word the AI player is trying to play
     * @return Returns an optional of a TilePlacement object, which is empty if no placement of the given word is
     * possible
     */
    private Optional<TilePlacement> boardPlacement(Game game, String word) {
        if (game.getBoard().getTile(Position.FromIndex(Board.getCenterTilePos()).get()).get().chr() == '*') {
            ArrayList<TilePositioned> tiles = new ArrayList<>();
            ArrayList<String> words = this.getPossibleWords(0);
            words.sort(new PointComparator());
            String first = words.get(0);
            HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
            for (int i = 0; i < first.length(); i++) {
                char c = first.toUpperCase().toCharArray()[i];
                tiles.add(new TilePositioned(tbs.get(c).tile(), Position.FromIndex(Board.getCenterTilePos() + i).get()));
            }
            return TilePlacement.FromTiles(tiles);
        }
        Random r = new Random();
        if (r.nextBoolean()) {
            Optional<TilePlacement> tp = checkHorizontal(game, word);
            if (tp.isEmpty()) {
                tp = checkVertical(game, word);
            }
            return tp;
        } else {
            Optional<TilePlacement> tp = checkVertical(game, word);
            if (tp.isEmpty()) {
                tp = checkHorizontal(game, word);
            }
            return tp;
        }
    }

    /**
     * Function call to make a move for the AI player, either playing their highest scoring word, or passing if no word
     * is possible to play
     * @param game The Game object within which the AI Player is playing
     */
    public void AITurn(Game game) {
        ArrayList<String> possibleWords = getPossibleWords(1);
        boolean hasPlayed = false;
        possibleWords.sort(new PointComparator());
        for (String word : possibleWords) {
            Optional<TilePlacement> tp = this.boardPlacement(game, word);
            if (tp.isPresent()) {
                try {
                    game.place(tp.get());
                    hasPlayed = true;
                    break;
                } catch (PlacementException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (!hasPlayed) {
            for (Tile t : this.getTileHand()) {
                System.out.println(t.chr());
            }
            game.pass();
        }
    }

    public static class PointComparator implements java.util.Comparator<String> {
        public PointComparator() {
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
