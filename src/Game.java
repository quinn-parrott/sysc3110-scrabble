import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.*;

/**
 * Core game engine. This implements all game state.
 *
 * @author Quinn Parrott, 101169535
 */
public class Game {
    public record GameUpdateState(Board board, ArrayList<String> newWords, HashMap<String, ArrayList<Integer>> playedWords) {}

    private Game game = this;
    private ArrayList<GameView> views;
    private WordList wordList;


    private static class GameMutableState implements Serializable {
        public ArrayList<Player> players;
        public ArrayList<TilePlacement> turns;
        public TileBag gameBag;
        public HashMap<Integer, Character> gamePremiumSquares;

        public GameMutableState(
            ArrayList<Player> players,
            ArrayList<TilePlacement> turns,
            TileBag gameBag,
            HashMap<Integer, Character> gamePremiumSquares
        ) {
            this.players = players;
            this.turns = turns;
            this.gameBag = gameBag;
            this.gamePremiumSquares = gamePremiumSquares;
        }

        public GameMutableState clone() {
            // Beware ye who seek to enter below:
            // beyond lay cloning evils so vile that the faint of heart may perish.
            try {
                // The only reliable way to make a _true_ deep copy in java is to serialize
                // then deserialize the objects.
                var stream = new ByteArrayOutputStream();
                var output = new ObjectOutputStream(stream);
                output.writeObject(this);

                var input = new ObjectInputStream(new ByteArrayInputStream(stream.toByteArray()));
                return (GameMutableState) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Converts the instance into XML data
         * @return Returns a String representing the XML data of the GameMutableState instance
         */
        private String toXML(int numTabs) {
            StringBuilder sb = new StringBuilder();
            StringBuilder tabs = new StringBuilder();
            tabs.append("    ".repeat(numTabs));
            for (Player p : this.players) {
                sb.append(p.toXML(numTabs + 1));
            }
            for (TilePlacement tp : this.turns) {
                sb.append(tp.toXML(numTabs + 1));
            }
            sb.append(tabs).append("    ").append("<TileBag>\n").append(this.gameBag.toXML(numTabs + 1));
            sb.append(tabs).append("    ").append("</TileBag>\n");
            sb.append(tabs).append("    ").append("<PremiumSquares>\n");
            for (int key : this.gamePremiumSquares.keySet()) {
                sb.append(tabs).append("        ").append("<Premium index=\"").append(key).append("\" char=\"");
                sb.append(this.gamePremiumSquares.get(key)).append("\"/>\n");
            }
            sb.append(tabs).append("    ").append("</PremiumSquares>\n");
            return sb.toString();
        }
    }

    private Transactionable<GameMutableState> state;

    /**
     * Constructor for the Game class
     * @param players players playing the game
     * @param wordList wordList of legal words for the game
     * @author Quinn Parrott, 101169535
     */
    public Game(List<Player> players, WordList wordList) {
        this.wordList = wordList;
        this.views = new ArrayList<>();

        var playersTemp = new ArrayList<>(players);
        var tileBag = new TileBag();

        int PLAYER_HAND_SIZE = 7;
        for (Player player : playersTemp) {
            int currentPlayerHandSize= player.getTileHand().size();
            int numTilesToAdd = PLAYER_HAND_SIZE - currentPlayerHandSize;
            for (int i = 0; i < numTilesToAdd; i++) {
                player.getTileHand().add(tileBag.drawTile().get());
            }
        }

        this.state = new Transactionable<>(new GameMutableState(
            playersTemp,
            new ArrayList<>(),
            tileBag,
            PremiumSquares.getPremiumSquares()
        ));
    }

    /**
     * Setting the view for model
     *
     * @param gv GameView
     */
    public void addGameView(GameView gv){
        views.add(gv);
    }

    /**
     * Verifies if the TilePlacement object can be placed on the board
     * @param placement placement which will be applied to the current board state
     * @return Returns a Board object with the TilePlacement parameter placed on it
     * @author Quinn Parrott, 101169535, and Colin Mandeville, 101140289
     */
    public GameUpdateState previewPlacement(TilePlacement placement) throws PlacementException {
        var board = getBoard();
        var playedWords = board.collectCharSequences();

        if (playedWords.size() == 0) {
            // First turn
            var p = Position.FromIndex(Board.getCenterTilePos()).orElseThrow();
            var distanceFromCenter = (int) placement.minTileDistance(p);
            if (distanceFromCenter > 0) {
                throw new PlacementException("At least one tile must intercept with the center on the first turn",
                        placement, Optional.of(board));
            }
        } else {
            enum MinResult {
                TooFar,
                Adjacent,
            }

            var minResult = MinResult.TooFar;

            var tiles = board.getTiles();

            boolean firstOverlap = true;
            for (var tile : tiles) {
                int distance = (int) Math.round(placement.minTileDistance(tile.pos()));

                if (distance == 0) {
                    if (board.getTile(tile.pos()).isPresent() || !firstOverlap) {
                        throw new PlacementException(String.format("Can't place tile at %s since there is already a " +
                                "tile there", tile.pos()), placement, Optional.of(board));
                    } else {
                        firstOverlap = false;
                    }
                }

                if (distance == 1) {
                    minResult = MinResult.Adjacent;
                }
            }

            if (minResult != MinResult.Adjacent) {
                throw new PlacementException("New tiles aren't adjacent to existing tiles",
                        placement, Optional.of(board));
            }
        }

        // TODO: Probably don't need to clone this?
        var nextBoard = board.clone();

        nextBoard.placeTiles(placement);

        // Check that the TilePlacement does not contain any spaces
        {
            var tiles = placement.getTiles();
            var firstTile = tiles.get(0);
            var lastTile = tiles.get(tiles.size() - 1);
            for (var pos : Position.Interpolate(firstTile.pos(), lastTile.pos()).get()) {
                if (nextBoard.getTile(pos).isEmpty()) {
                    throw new PlacementException("There can't be spaces in the placed word",
                            placement, Optional.of(board));
                }
            }
        }

        var nextWords = nextBoard.collectCharSequences();
        ArrayList<String> newWords = new ArrayList<>();

        for (String word : nextWords.keySet()) {
            if (!playedWords.containsKey(word)) {
                newWords.add(word);
            }
            if (word.length() > 1 && !this.wordList.isValidWord(word)) {
                throw new PlacementException(String.format("'%s' is not a valid word", word),
                        placement, Optional.of(board));
            }
        }

        if (!this.playerHasNeededTiles(placement.getTiles())) {
            throw new PlacementException("You do not have all needed tiles",
                    placement, Optional.of(board));
        }

        return new GameUpdateState(nextBoard, newWords, nextWords);
    }

    /**
     * Places a TilePlacement object on the board
     * @param placement placement object to be applied to the current board
     * @author Quinn Parrott, 101169535, and Colin Mandeville, 101140289
     */
    public void place(TilePlacement placement) throws PlacementException {
        var update = this.previewPlacement(placement);

        StringBuilder tilesUsed = new StringBuilder();

        for (var tile : placement.getTiles()) {
            tilesUsed.append(tile.value().chr());
        }

        this.removeTilesFromHand(tilesUsed.toString());

        HashMap<Character, TileBagDetails> tileDetails = TileBagSingleton.getBagDetails();
        int score = 0;

        for (String word : update.newWords()) {
            int tileScore = 0;
            int wordMultiplier = 1;
            int wordscore = 0;
            for (int i = 0; i < word.length(); i++) {
                if (this.state.state(GameMutableState::clone).gamePremiumSquares.containsKey(update.playedWords().get(word).get(i))) {
                    var chr = this.state.state(GameMutableState::clone).gamePremiumSquares.get(update.playedWords().get(word).get(i));
                    switch (chr){

                        case '$':
                            tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue()) * 2; break;
                        case '%':
                            tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue()) * 3; break;
                        case '@':
                            wordMultiplier *= 2;
                            tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue()); break;
                        case '#':
                            wordMultiplier *= 3;
                            tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue()); break;

                    }

                    this.state.state(GameMutableState::clone).gamePremiumSquares.remove(update.playedWords().get(word).get(i));

                }else{
                    tileScore += (tileDetails.get(word.charAt(i)).tile().pointValue());
                }
            }
            wordscore = tileScore * wordMultiplier;
            score += wordscore;

        }

        this.getPlayer().addPoints(score);
        int currentPlayerHandSize= this.getPlayer().getTileHand().size();
        int PLAYER_HAND_SIZE = 7;
        int numTilesToAdd = PLAYER_HAND_SIZE - currentPlayerHandSize;
        for (int i = 0; i < numTilesToAdd; i++) {
            Optional<WildcardableTile> t = this.state.state(GameMutableState::clone).gameBag.drawTile();
            if (t.isPresent()) {
                if (t.get().isWildcard() && this.getPlayer().isAI()) {
                    t = Optional.of(new WildcardableTile('E', 0));
                }
                t.ifPresent(tile -> this.getPlayer().getTileHand().add(tile));
            }
        }

        this.state.state(GameMutableState::clone).turns.add(placement);
        this.update();
    }

    /**
     * Allows a player to pass a turn without playing a word
     * @author Colin Mandeville, 101140289
     */
    public void pass() {
        this.state.state(GameMutableState::clone).turns.add(new TilePlacement(new ArrayList<>()));
        this.update();
    }

    public void commit() {
        this.state.commitWorking(GameMutableState::clone); // Commit the state change
    }

    public void runAi() {
        while (this.getPlayer().isAI()) {
            (new AiPlayer(this)).AiTurn();
        }
    }

    private void update() {
        for (GameView view : this.views) {
            view.update();
        }
    }

    /**
     * Checks if the active Player has the required tiles to make their move.
     * @param tiles tiles being used to create the word
     * @return Returns a boolean representing if the player has all required tiles to make their word
     * @author Colin Mandeville, 101140289
     */
    private boolean playerHasNeededTiles(List<Positioned<Tile>> tiles) {
        Player activePlayer = this.getPlayer();

        var letters = new ArrayList<>();

        int wildcards = 0;
        for (WildcardableTile tile : activePlayer.getTileHand()) {
            if (tile.isWildcard()) {
                wildcards++;
            } else {
                Character chr = tile.chr();
                letters.add(chr);
            }
        }

        for (var tile : tiles) {
            Character chr = tile.value().chr();
            if (!letters.remove(chr)) {
                wildcards--;
                if (wildcards < 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Removes all used tiles from the players hand of tiles
     * @param letters The string of letters being used by the player
     * @author Colin Mandeville, 101140289
     */
    private void removeTilesFromHand(String letters) {
        Player activePlayer = this.getPlayer();

        for (char c : letters.toCharArray()) {

            boolean found = false;
            for (WildcardableTile tile: activePlayer.getTileHand()) {
                if (!tile.isWildcard() && tile.chr() == c) {
                    activePlayer.getTileHand().remove(tile);
                    found = true;
                    break;
                }
            }

            if (!found) {
                for (WildcardableTile tile: activePlayer.getTileHand()) {
                    if (tile.isWildcard()) {
                        activePlayer.getTileHand().remove(tile);
                        break;
                    }
                }
            }
        }
    }

    public int getNumTurns() {
        return this.state.state(GameMutableState::clone).turns.size();
    }

    public Player getPlayer() {
        return getPlayers().get(getNumTurns() % getPlayers().size());
    }

    public List<Player> getPlayers() {
        return this.state.state(GameMutableState::clone).players;
    }

    /**
     * Method to save current board state into a file
     * @param filename String representing the name of the file being saved
     */
    public void saveGame(String filename) throws IOException {
        File file = new File(filename);
        FileOutputStream f = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            f = new FileOutputStream(file.getName());
        } catch (Exception ignored) {}
        try {
            assert f != null;
            f.write(this.toXML().getBytes());
            f.close();
        } catch (Exception ignored) {}
    }

    public String toXML() {
        int numTabs = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<Game>\n");
        for (GameMutableState gm : this.state.internalState) {
            sb.append("    ").append("<Transaction>\n");
            sb.append(gm.toXML(numTabs + 1));
            sb.append("    ").append("</Transaction>\n");
        }
        sb.append("</Game>");
        return sb.toString();
    }

    public void loadGame(String filename, GameView view) throws ParserConfigurationException, SAXException, IOException {
        File f = new File(filename);
        if (f.exists()) {
            SAXParser s = SAXParserFactory.newInstance().newSAXParser();
            DefaultHandler dh = new DefaultHandler() {
                private Stack<GameMutableState> transactions;
                private ArrayList<Player> p;
                private TileBag tb;
                private ArrayList<TilePlacement> gameTurns;
                private ArrayList<Positioned<WildcardableTile>> turn;
                private HashMap<Integer, Character> premiumSquares;
                private accessLimit access = accessLimit.NONE;
                enum accessLimit {
                    NONE,
                    GAME,
                    TRANSACTION,
                    PLAYER,
                    BAG,
                    TURN,
                    PREMIUM
                }

                @Override
                public void startElement(String url, String localName, String qName, Attributes a) {
                    switch (qName) {
                        case "Game" -> {
                            access = accessLimit.GAME;
                            transactions = new Stack<>();
                        }
                        case "Transaction" -> {
                            if (access == accessLimit.GAME) {
                                access = accessLimit.TRANSACTION;
                                p = new ArrayList<>();
                                gameTurns = new ArrayList<>();
                                tb = new TileBag();
                                premiumSquares = new HashMap<>();
                            }
                        }
                        case "Player" -> {
                            if (access == accessLimit.TRANSACTION) {
                                access = accessLimit.PLAYER;
                                p.add(new Player(a.getValue(a.getIndex("name")), a.getValue(a.getIndex("isAI")).equalsIgnoreCase("true")));
                                p.get(p.size() - 1).addPoints(Integer.parseInt(a.getValue(a.getIndex("points"))));
                            }
                        }
                        case "TileBag" -> {
                            if (access == accessLimit.TRANSACTION) {
                                access = accessLimit.BAG;
                                while(!tb.isEmpty()) {
                                    tb.drawTile();
                                }
                            }
                        }
                        case "WildcardableTile" -> {
                            if (access == accessLimit.PLAYER || access == accessLimit.BAG) {
                                WildcardableTile t = new WildcardableTile(a.getValue(a.getIndex("chr")).charAt(0), Integer.parseInt(a.getValue(a.getIndex("pointValue"))));
                                if (access == accessLimit.PLAYER && p.get(p.size() - 1).getTileHand().size() < Player.getTileHandSize()) {
                                    p.get(p.size() - 1).addTile(t);
                                } else {
                                    tb.addTileToBag(t);
                                }
                            }
                        }
                        case "TilePlacement" -> {
                            if (access == accessLimit.TRANSACTION) {
                                access = accessLimit.TURN;
                                turn = new ArrayList<>();
                            }
                        }
                        case "Positioned" -> {
                            if (access == accessLimit.TURN) {
                                String value = a.getValue(a.getIndex("value"));
                                char tileChar = value.split("chr=")[1].charAt(0);
                                int pointValue = Integer.parseInt(value.split("pointValue=")[1].charAt(1) == ']' ?
                                        String.valueOf(value.split("pointValue=")[1].charAt(0)) :
                                        value.split("pointValue=")[1].substring(0, 1));
                                if (tileChar >= 65 && tileChar <= 90 && pointValue >= 0 && pointValue <= 10) {
                                    WildcardableTile tile = new WildcardableTile(tileChar, pointValue);
                                    turn.add(new Positioned<>(tile,
                                            Position.FromInts(Integer.parseInt(a.getValue(a.getIndex("x"))),
                                                    Integer.parseInt(a.getValue(a.getIndex("y")))).get()));
                                }
                            }
                        }
                        case "PremiumSquares" -> {
                            if (access == accessLimit.TRANSACTION) {
                                access = accessLimit.PREMIUM;
                            }
                        }
                        case "Premium" -> {
                            if (access == accessLimit.PREMIUM) {
                                int i = Integer.parseInt(a.getValue(a.getIndex("index")));
                                char c = a.getValue(a.getIndex("char")).charAt(0);
                                boolean validC = false;
                                for (char x : PremiumSquares.reservedSymbols) {
                                    if (c == x) {
                                        validC = true;
                                        break;
                                    }
                                }
                                if (i >= 0 && i < Board.getROW_NUMBER() * Board.getCOLUMN_NUMBER() && validC) {
                                    premiumSquares.put(i, c);
                                }
                            }
                        }
                        default -> {}
                    }
                }

                @Override
                public void endElement(String url, String localName, String qName) {
                    switch (qName) {
                        case "Game" -> {
                            access = accessLimit.NONE;
                            while (Game.this.state.internalState.size() > 0) {
                                Game.this.state.internalState.pop();
                            }
                            for (GameMutableState gm : transactions) {
                                Game.this.state.internalState.push(gm);
                            }
                            Game.this.state.head = Optional.empty();
                            Game.this.state.i = Game.this.state.internalState.size() - 1;
                            Game.this.update();
                        }
                        case "Transaction" -> {
                            if (access == accessLimit.TRANSACTION) {
                                access = accessLimit.GAME;
                                transactions.push(new GameMutableState(p, gameTurns, tb, premiumSquares));
                                p = null;
                                gameTurns = null;
                                tb = null;
                                premiumSquares = null;
                            }
                        }
                        case "Player" -> {
                            if (access == accessLimit.PLAYER) {
                                access = accessLimit.TRANSACTION;
                            }
                        }
                        case "TileBag" -> {
                            if (access == accessLimit.BAG) {
                                access = accessLimit.TRANSACTION;
                            }
                        }
                        case "TilePlacement" -> {
                            if (access == accessLimit.TURN) {
                                access = accessLimit.TRANSACTION;
                                ArrayList<Positioned<Tile>> regularTiles = new ArrayList<>();
                                for (Positioned<WildcardableTile> tile : turn) {
                                    regularTiles.add(new Positioned<>(new Tile(tile.value().chr(), tile.value().pointValue()), tile.pos()));
                                }
                                Optional<TilePlacement> placement = TilePlacement.FromTiles(regularTiles);
                                if (placement.isPresent()) {
                                    gameTurns.add(placement.get());
                                } else {
                                    gameTurns.add(new TilePlacement(new ArrayList<>()));
                                }
                            }
                        }
                        case "PremiumSquares" -> {
                            if (access == accessLimit.PREMIUM) {
                                access = accessLimit.TRANSACTION;
                            }
                        }
                    }
                }
            };
            s.parse(f, dh);
        }
    }

    public Board getBoard() {
        var board = new Board();
        for (var placement : this.state.state(GameMutableState::clone).turns) {
            try {
                board.placeTiles(placement);
            } catch (PlacementException e) {
                throw new RuntimeException("Invalid turns prevented board reconstruction. This should never happen", e);
            }
        }
        return board;
    }

    public boolean canUndo() {
        return this.state.canUndo();
    }

    public boolean canRedo() {
        return this.state.canRedo();
    }

    public boolean undo() {
        var result = this.state.undo();
        this.update();
        return result;
    }

    public boolean redo() {
        var result = this.state.redo();
        this.update();
        return result;
    }
}
