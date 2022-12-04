import java.util.*;
import java.util.stream.Stream;

public class AiPlayer {
    Game game;

    public AiPlayer(Game game) {
        this.game = game;
    }

    /**
     * Checks for the first valid horizontal placement of a string on a Board object
     * @param word String representing the word to be played
     * @return Returns the starting index at which to place tiles
     * @author Colin Mandeville, 101140289
     */
    private Optional<TilePlacement> checkHorizontal(String word) {
        HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
        for (int i = 0; i < Board.getROW_NUMBER() * Board.getCOLUMN_NUMBER() - word.length(); i++) {
            if (i % Board.getROW_NUMBER() > Board.getROW_NUMBER() - word.length()) {
                i += word.length();
                continue;
            }
            ArrayList<Positioned<Tile>> tiles = new ArrayList<>();
            for (int j = 0; j < word.length(); j++) {
                char c = word.toCharArray()[j];
                Optional<Position> cPos = Position.FromIndex(j + i);
                Tile tile;
                if (tbs.get(c).tile().isWildcard()) {
                    tile = new Tile ('E', 0);
                } else {
                    tile = new Tile (tbs.get(c).tile().chr(), tbs.get(c).tile().pointValue());
                }
                cPos.ifPresent(position -> tiles.add(new Positioned<>(tile, position)));
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
     * Checks for the first valid vertical placement of a string on a Board object
     * @param word String representing the word to be played
     * @return Returns the starting index at which to place tiles
     * @author Colin Mandeville, 101140289
     */
    private Optional<TilePlacement> checkVertical(String word) {
        ArrayList<Positioned<Tile>> tiles = new ArrayList<>();
        HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
        for (int i = 0; i < Board.getCOLUMN_NUMBER() * Board.getROW_NUMBER(); i++) {
            tiles.clear();
            for (int j = 0; j < word.length(); j++) {
                char c = word.toCharArray()[j];
                Optional<Position> cPos = Position.FromInts(i, j);
                Tile tile;
                if (tbs.get(c).tile().isWildcard()) {
                    tile = new Tile ('E', 0);
                } else {
                    tile = new Tile (tbs.get(c).tile().chr(), tbs.get(c).tile().pointValue());
                }
                cPos.ifPresent(position -> tiles.add(new Positioned<>(tile, position)));
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
     * @param word A word the AI player is trying to play
     * @return Returns an optional of a TilePlacement object, which is empty if no placement of the given word is
     * possible
     * @author Colin Mandeville, 101140289
     */
    private Optional<TilePlacement> boardPlacement(String word) {

        if (game.getBoard().collectCharSequences().size() == 0) {
            ArrayList<Positioned<Tile>> tiles = new ArrayList<>();
            ArrayList<String> words = game.getPlayer().getPossibleWords(0);
            words.sort(new PointComparator());
            String first = "";
            if (words.size() > 0) {
                first = words.get(0);
            }
            HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();
            for (int i = 0; i < first.length(); i++) {
                char c = first.toUpperCase().toCharArray()[i];
                Tile tile;
                if (tbs.get(c).tile().isWildcard()) {
                    tile = new Tile ('E', 0);
                } else {
                    tile = new Tile (tbs.get(c).tile().chr(), tbs.get(c).tile().pointValue());
                }
                tiles.add(new Positioned<>(tile, Position.FromInts(Math.floorDiv(Board.getROW_NUMBER(), 2), Math.floorDiv(Board.getCOLUMN_NUMBER(), 2) + i).get()));
            }
            return TilePlacement.FromTiles(tiles);
        }

        Random r = new Random();
        if (r.nextBoolean()) {
            Optional<TilePlacement> tp = checkHorizontal(word);
            if (tp.isEmpty()) {
                tp = checkVertical(word);
            }
            return tp;
        } else {
            Optional<TilePlacement> tp = checkVertical(word);
            if (tp.isEmpty()) {
                tp = checkHorizontal(word);
            }
            return tp;
        }
    }

    /**
     * Function call to make a move for the AI player, either playing their highest scoring word, or passing if no word
     * is possible to play
     * @author Colin Mandeville, 101140289
     * @author Quinn Parrott 101169535
     */
    public Stream<TilePlacement> nextWord() {
        ArrayList<String> possibleWords = game.getPlayer().getPossibleWords(1);
        possibleWords.sort(new PointComparator());

        return possibleWords
                .stream()
                .map(this::boardPlacement) // Find the first that passes boardPlacement
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    /**
     * Function call to make a move for the AI player, either playing their highest scoring word, or passing if no word
     * is possible to play
     * @author Colin Mandeville, 101140289
     */
    public void AiTurn() {
        var result = nextWord();
        for (Iterator<TilePlacement> it = result.iterator(); it.hasNext(); ) {
            TilePlacement tp = it.next();
            try {
                game.place(tp);
                game.discardLastTurnFromUndo(); // Make it so this turn cannot be undone
                return;
            } catch (PlacementException ignored) {}
        }
        game.pass();
        game.discardLastTurnFromUndo(); // Make it so this turn cannot be undone
    }

    private static class PointComparator implements java.util.Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            HashMap<Character, TileBagDetails> tbs = TileBagSingleton.getBagDetails();

            int score1 = 0;
            for (int i = 0; i < o1.length(); i++) {
                score1 += tbs.get(o1.toUpperCase().toCharArray()[i]).tile().pointValue();
            }

            int score2 = 0;
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
