import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Game View
 * GUI of the game. This class will handle all the JFrame Components for the scrabble game
 *
 */
public class GameView extends JFrame implements IBoardTileAdder, IBoardTileRemover {
    private static final Color colorUnselected = new Color(240, 240, 240);
    private static final Color colorSelected = new Color(200, 200, 200);

    private final Container pane;
    private Game game;     // model if we'll be using MVC . Some more changes to be made in the Game class
    private final JLabel playerTurnLabel;
    private JButton playButton;
    private JButton passTurn;
    private final JButton undoButton;
    private final JButton redoButton;
    private final JButton saveBoard;
    private final JButton loadBoard;
    private JPanel boardAndTileHandPanel;
    private List<Positioned<WildcardableStoreTile>> placedTiles;
    private Component boardComponent;
    private BoardView boardView;
    private BoardViewModel boardViewModel;
    private TileTrayModel tileTrayModel;
    private TileTrayView tileTrayView;
    private ScoreboardView scoreboard;
    private boolean isCustomBoard;
    private HashMap<String, HashSet<Integer>> customPremiumPositions;


    /**
     * Default Constructor that will initialize the GUI and set up all the JFrame components
     *
     * @author Tao Lufula, 101164153, .....
     */
    public GameView() {
        super("SCRABBLE");

        pane = this.getContentPane();
        pane.setLayout(new BorderLayout());

        playerTurnLabel = new JLabel();
        undoButton = new JButton("Undo");
        undoButton.setEnabled(false);
        redoButton = new JButton("Redo");
        redoButton.setEnabled(false);
        saveBoard = new JButton();
        loadBoard = new JButton();
        isCustomBoard = false;
        customPremiumPositions = new HashMap<>();

        Optional<List<Player>> playersList = (new PlayerAdderView(this)).getPlayers();
        if (playersList.isEmpty()) {
            System.exit(0);
        }

        selectBoardType();

        if (isCustomBoard) {
            customBoardSetUp();
            game = new Game(playersList.get(), new WordList(), customPremiumPositions);
        }else{
            game = new Game(playersList.get(), new WordList());
        }

        this.placedTiles = new ArrayList<>();
        this.boardComponent = this.createBoard();
        this.boardComponent.setPreferredSize(new Dimension(500, 600));

        boardAndTileHandPanel = new JPanel();
        boardAndTileHandPanel.setLayout(new BorderLayout());

        JPanel tileHandPanel = new JPanel();
        this.tileTrayModel = this.createTileHand(this.game.getPlayer());
        this.tileTrayView = new TileTrayView(this.tileTrayModel);
        tileHandPanel.add(this.tileTrayView, new GridBagConstraints());
        tileHandPanel.setPreferredSize(new Dimension(1100,50));

        boardAndTileHandPanel.add(this.boardComponent, BorderLayout.NORTH);
        boardAndTileHandPanel.add(tileHandPanel, BorderLayout.SOUTH);

        JPanel scoreBoardAndButtonPanel = new JPanel();
        scoreBoardAndButtonPanel.setLayout(new BorderLayout());
        this.scoreboard = new ScoreboardView(game);
        scoreBoardAndButtonPanel.add(this.scoreboard.getView(), BorderLayout.NORTH);
        scoreBoardAndButtonPanel.add(this.createLegend(), BorderLayout.CENTER);
        scoreBoardAndButtonPanel.add(this.createPlayButtons(), BorderLayout.SOUTH);

        pane.add(boardAndTileHandPanel, BorderLayout.WEST);
        pane.add(scoreBoardAndButtonPanel, BorderLayout.EAST);

        game.addGameView(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1600, 1000);
        this.setVisible(true);
    }

    /**
     * Method to create Legend for special squares
     *
     * @author Tao Lufula, 101164153
     */
    private Component createLegend() {
        JPanel legendPanel = new JPanel();
        String text = "\n           LEGEND" + "\n\n  $ : Double letter score  " + "\n  % : Triple letter score  " + "\n  @ : Double word score  " + "\n  # : Triple word score";
        JTextArea legendText = new JTextArea(text);
        legendText.setEditable(false);
        legendPanel.add(legendText);
        return legendPanel;
    }


    /**
     * Method to create the play and pass turn buttons
     *
     * @author Tao Lufula, 101164153
     */
    private Component createPlayButtons() {
        playButton = new JButton();
        passTurn = new JButton();
        //Reset or play word button. This button validates the words being placed on the board or will also clear the players letters placed on the board
        this.switchPlayButtonText("PLAY");
        playButton.setPreferredSize(new Dimension(140,50));

        //Pass players turn when they press this button
        passTurn.setText("PASS");
        passTurn.setPreferredSize(new Dimension(140,50));

        saveBoard.setText("SAVE BOARD");
        saveBoard.setPreferredSize(new Dimension(140,50));

        loadBoard.setText("LOAD BOARD");
        loadBoard.setPreferredSize(new Dimension(140,50));

        playButton.addActionListener(new PlayButtonController(this, this.game, this.boardViewModel));
        passTurn.addActionListener(new PassButtonController(this, this.game, this.boardViewModel));

        var undoRedoController = new UndoRedoButtonController(this, this.game, this.boardViewModel);
        undoButton.addActionListener(source -> undoRedoController.undo(undoButton));
        redoButton.addActionListener(source -> undoRedoController.redo(redoButton));
        saveBoard.addActionListener(e -> {
            String filename = JOptionPane.showInputDialog("Enter a file name to save to");
            try {
                this.game.saveGame(filename);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        loadBoard.addActionListener(e -> {
            String filename = JOptionPane.showInputDialog("Enter a file name to load from");
            try {
                game.loadGame(filename);
                this.boardViewModel.getPlacedTiles().removeIf(_ignore -> true);
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2));
        buttonsPanel.add(undoButton);
        buttonsPanel.add(redoButton);
        buttonsPanel.add(playButton);
        buttonsPanel.add(passTurn);
        buttonsPanel.add(saveBoard);
        buttonsPanel.add(loadBoard);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BorderLayout());
        playerTurnLabel.setPreferredSize(new Dimension(150,50));
        controlsPanel.add(playerTurnLabel, BorderLayout.NORTH);
        controlsPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return buttonsPanel;
    }

    public void update() {
        this.scoreboard.update();
        var model = this.createTileHand(this.game.getPlayer());
        this.tileTrayModel.setSelected(model.getSelected());
        this.tileTrayModel.setEntries(model.getEntries());
        this.tileTrayView.update();
        this.boardViewModel.setBoard(game.getBoard());
        this.boardView.update(isCustomBoard, customPremiumPositions);
        this.undoButton.setEnabled(game.canUndo());
        this.redoButton.setEnabled(game.canRedo());
    }

    /**
     * Changes the play button text depending on the game circumstance
     * @param text
     * @author Tao Lufula, 101164153
     */
    public void switchPlayButtonText(String text){
        // PLAY
        // RESET
        playButton.setText(text);
    }


    private TileTrayModel createTileHand(Player player) {
        var tiles = player.getTileHand();
        var model = new TileTrayModel(
                tiles
                        .stream()
                        .map(t -> new TileTrayModel.TileTrayEntry(TileTrayModel.TileStatus.Unplayed, t))
                        .toList(),
                Optional.empty()
        );
        return model;
    }


    public void handleBoardTileRemover(Positioned<WildcardableStoreTile> tile) {
        for (var i = 0; i < this.placedTiles.size(); i++) {
            var temp = this.placedTiles.get(i);
            if (temp.pos().equals(tile.pos())) {
                this.placedTiles.remove(i);

                // Make the tile selectable in the tile tray again
                int j = 0;
                for (var entry : tileTrayModel.getEntries()) {
                    if (entry.status().equals(TileTrayModel.TileStatus.Played)) {
                        if (temp.value().isWildcard()) {
                            if ( entry.tile().isWildcard() ) {
                                tileTrayModel.setEntry(j, new TileTrayModel.TileTrayEntry(TileTrayModel.TileStatus.Unplayed, entry.tile()));
                                tileTrayModel.setSelected(Optional.of(j));

                                tileTrayView.update();
                                break;
                            }
                        } else {
                            if (entry.tile().chr() == temp.value().chr()) {
                                tileTrayModel.setEntry(j, new TileTrayModel.TileTrayEntry(TileTrayModel.TileStatus.Unplayed, entry.tile()));
                                tileTrayModel.setSelected(Optional.of(j));

                                tileTrayView.update();
                                break;
                            }
                        }
                    }

                    j++;
                }

                this.boardView.update(isCustomBoard, customPremiumPositions);
                break;
            }
        }
    }

    public void handleBoardTileAdder(Position pos) {
        if (tileTrayModel.getSelected().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tile selected to place");
            return;
        }

        int tile_i = tileTrayModel.getSelected().get();
        var entry = tileTrayModel.getEntries().get(tile_i);
        tileTrayModel.setEntry(tile_i, new TileTrayModel.TileTrayEntry(TileTrayModel.TileStatus.Played, entry.tile()));

        Optional<Integer> selected = Optional.empty();
        var entries = tileTrayModel.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            var ent = entries.get(i);
            if (ent.status().equals(TileTrayModel.TileStatus.Unplayed)) {
                selected = Optional.of(i);
                break;
            }
        }

        tileTrayModel.setSelected(selected);
        tileTrayView.update();

        var tile = entry.tile();

        char letter = tile.chr();
        if (tile.isWildcard()) {
            String letters = "";
            while (letters.length() != 1) {
                letters = Optional.ofNullable(JOptionPane.showInputDialog(this, "What letter?")).orElse("").toUpperCase();
            }
            letter = letters.toCharArray()[0];
        }

        this.placedTiles.add(new Positioned<>(new WildcardableStoreTile(letter, tile.isWildcard(), tile.pointValue()), pos));
        this.boardView.update(isCustomBoard, customPremiumPositions);
    }

    private Component createBoard() {
        this.boardViewModel = new BoardViewModel(this.game.getBoard(), this.placedTiles);
        var boardView = new BoardView(this.boardViewModel, isCustomBoard, customPremiumPositions);
        boardView.addBoardTileAdder(this);
        boardView.addBoardTileRemover(this);
        this.boardView = boardView;
        return boardView;
    }


    /**
     * Method to get all the premium squares and positions from the user(Standard, Custom)
     *
     * @author Tao Lufula, 101164153
     * @author Jawad Nasrallah, 101201038
     */
    private void customBoardSetUp() {
        HashSet<Integer> twoXLetterScoreSet = new HashSet<>();
        HashSet<Integer> threeXLetterScoreSet = new HashSet<>();
        HashSet<Integer> twoXWordScoreSet = new HashSet<>();
        HashSet<Integer> threeXWordScoreSet = new HashSet<>();

        var isValidInputs = false;
        while (!isValidInputs) {
            var twoXLetterScoreSquares = String.valueOf(
                    JOptionPane.showInputDialog(
                            this,
                            "Enter the positions for 2 X LetterScore Squares on the Board " + "\n\n(0 - 224) separated by commas eg: 0,49,156,200,..." + "\n\nHINT: 0 - top left square, 224 - bottom right square. \n\nEach square can only be assigned once!",
                            "Custom Board Setup" + "                     " + "SCRABBLE",
                            JOptionPane.PLAIN_MESSAGE
                    )
            );
            isValidInputs = validateInputs(twoXLetterScoreSquares);
            if(isValidInputs) {
                for (String s : twoXLetterScoreSquares.split(",")) {
                    twoXLetterScoreSet.add(Integer.parseInt(s));
                }
                customPremiumPositions.put("twoXLetterScore", twoXLetterScoreSet);
            }
        }

        isValidInputs = false;
        while (!isValidInputs) {
            var threeXLetterScoreSquares = String.valueOf(
                    JOptionPane.showInputDialog(
                            this,
                            "Enter the positions for 3 X LetterScore Squares on the Board " + "\n\n(0 - 224) separated by commas eg: 0,49,156,200,..." + "\n\nHINT: 0 - top left square, 224 - bottom right square. \n\nEach square can only be assigned once!",
                            "Custom Board Setup" + "                     " + "SCRABBLE",
                            JOptionPane.PLAIN_MESSAGE
                    )
            );
            isValidInputs = validateInputs(threeXLetterScoreSquares);
            if(isValidInputs) {
                for (String s : threeXLetterScoreSquares.split(",")) {
                    if (!twoXLetterScoreSet.contains(Integer.parseInt(s))) {
                        threeXLetterScoreSet.add(Integer.parseInt(s));
                    }
                }
                customPremiumPositions.put("threeXLetterScore", threeXLetterScoreSet);
            }
        }

        isValidInputs = false;
        while (!isValidInputs) {
            var twoWordLetterScoreSquares = String.valueOf(
                    JOptionPane.showInputDialog(
                            this,
                            "Enter the positions for 2 X WordScore Squares on the Board " + "\n\n(0 - 224) separated by commas eg: 0,49,156,200,..." + "\n\nHINT: 0 - top left square, 224 - bottom right square. \n\nEach square can only be assigned once!",
                            "Custom Board Setup" + "                     " + "SCRABBLE",
                            JOptionPane.PLAIN_MESSAGE
                    )
            );
            isValidInputs = validateInputs(twoWordLetterScoreSquares);
            if(isValidInputs) {
                for (String s : twoWordLetterScoreSquares.split(",")) {
                    if (!twoXLetterScoreSet.contains(Integer.parseInt(s)) && !threeXLetterScoreSet.contains(Integer.parseInt(s))) {
                        twoXWordScoreSet.add(Integer.parseInt(s));
                    }
                }
                customPremiumPositions.put("twoXWordScore", twoXWordScoreSet);
            }
        }

        isValidInputs = false;
        while (!isValidInputs) {
            var threeWordLetterScoreSquares = String.valueOf(
                    JOptionPane.showInputDialog(
                            this,
                            "Enter the positions for 3 X WordScore Squares on the Board " + "\n\n(0 - 224) separated by commas eg: 0,49,156,200,..." + "\n\nHINT: 0 - top left square, 224 - bottom right square. \n\nEach square can only be assigned once!",
                            "Custom Board Setup" + "                     " + "SCRABBLE",
                            JOptionPane.PLAIN_MESSAGE
                    )
            );
            isValidInputs = validateInputs(threeWordLetterScoreSquares);
            if(isValidInputs) {
                for (String s : threeWordLetterScoreSquares.split(",")) {
                    if (!twoXLetterScoreSet.contains(Integer.parseInt(s)) && !threeXLetterScoreSet.contains(Integer.parseInt(s)) && !twoXWordScoreSet.contains(Integer.parseInt(s))) {
                        threeXWordScoreSet.add(Integer.parseInt(s));
                    }
                }
                customPremiumPositions.put("threeXWordScore", threeXWordScoreSet);
            }
        }
    }


    /**
     *
     * @param wordAndLetterScoreSquares
     * @return true if userInputs are valid and false if not
     * @author Jawad Nasrallah, 101201038
     */
    private boolean validateInputs(String wordAndLetterScoreSquares) {
        var isValidInputs = false;
        if (wordAndLetterScoreSquares.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid entry! Field cannot be empty");
        }
        else {
            try {
                int x = 0;
                for (String s : wordAndLetterScoreSquares.split(",")) {
                    x = Integer.parseInt(s);
                    if (x < 0 || x > 224) {
                        JOptionPane.showMessageDialog(this, "Invalid entry! Each entry should be between 0 and 224");
                        return false;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid entry! Enter only integer values between 0 and 224 separated by commas");
                return false;
            }
            isValidInputs = true;
        }
        return isValidInputs;
    }

    /**
     * Method to prompt users to select BoardType (Standard, Custom)
     *
     * @author Tao Lufula, 101164153
     * @author Jawad Nasrallah, 101201038
     */
    private void selectBoardType() {
        String[] boardOptions = {"Standard  Board", "Custom  Board"};

        int x = JOptionPane.showOptionDialog(null, "Select the type of board you want to play!" + "\n\nHint: With Custom Board, you will be able to specify the amount and location of Premium Squares on the board." + "\n ",
                "Board Type Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, boardOptions,
                boardOptions[0]);
        if (x == 1) {
            isCustomBoard = true;
        }
    }
}
