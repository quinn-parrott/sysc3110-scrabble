import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Game View
 * GUI of the game. This class will handle all the JFrame Components for the scrabble game
 *
 */
public class GameView extends JFrame implements IBoardTileAdder, IBoardTileRemover{
    private static final Color colorUnselected = new Color(240, 240, 240);
    private static final Color colorSelected = new Color(200, 200, 200);

    private final Container pane;
    private final Game game;     // model if we'll be using MVC . Some more changes to be made in the Game class
    private final JLabel playerTurnLabel;
    private final JButton playButton;
    private final JButton passTurn;
    private List<TilePositioned> placedTiles;
    private Component boardComponent;
    private BoardView boardView;
    private BoardViewModel boardViewModel;
    private TileTrayModel tileTrayModel;
    private TileTrayView tileTrayView;
    private ScoreboardView scoreboard;


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
        playButton = new JButton();
        passTurn = new JButton();

        // A layout manager for these components is still pending

        List<Player> playersList = (new PlayerAdderView(this)).getPlayers();

        game = new Game(playersList, new WordList());


        this.placedTiles = new ArrayList<>();
        this.boardComponent = this.createBoard(game.getBoard(), this.placedTiles);
        this.boardComponent.setPreferredSize(new Dimension(500, 600));

        JPanel boardAndTileHandPanel = new JPanel();
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
        scoreBoardAndButtonPanel.add(this.createPlayButtons(), BorderLayout.SOUTH);

        pane.add(boardAndTileHandPanel, BorderLayout.WEST);
        pane.add(scoreBoardAndButtonPanel, BorderLayout.EAST);

        game.addGameView(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400, 1000);
        this.setVisible(true);
    }


    /**
     * Method to create the play and pass turn buttons
     *
     * @author Tao Lufula, 101164153
     */
    private Component createPlayButtons() {
        //Reset or play word button. This button validates the words being placed on the board or will also clear the players letters placed on the board
        this.switchPlayButtonText("PLAY");
        playButton.setPreferredSize(new Dimension(150,50));

        //Pass players turn when they press this button
        passTurn.setText("PASS");
        passTurn.setPreferredSize(new Dimension(150,50));

        playButton.addActionListener((e) -> {
            if (this.boardViewModel.getPlacedTiles().size() != 0) {
                Optional<TilePlacement> tp = TilePlacement.FromTiles(this.boardViewModel.getPlacedTiles());
                if (tp.isPresent()) {
                    try {
                        this.game.place(tp.get());
                        this.placedTiles.removeIf(_all -> true);
                        boardViewModel.setBoard(this.game.getBoard());
                    } catch (PlacementException pe) {
                        JOptionPane.showMessageDialog(this, String.format("Bad placement: %s", pe.getMessage()));
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid tile");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No Word Played");
            }
            this.update();
        });

        passTurn.addActionListener((e) -> {
            this.game.pass();
            this.placedTiles.removeIf(_all -> true);
            boardViewModel.setBoard(this.game.getBoard());
            this.update();
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BorderLayout());

        buttonsPanel.add(playButton, BorderLayout.EAST);
        buttonsPanel.add(passTurn, BorderLayout.WEST);

        playerTurnLabel.setPreferredSize(new Dimension(150,150));
        buttonsPanel.add(playerTurnLabel, BorderLayout.NORTH);

        return buttonsPanel;
    }

    public void update() {
        this.scoreboard.update();
        var model = this.createTileHand(this.game.getPlayer());
        this.tileTrayModel.setSelected(model.getSelected());
        this.tileTrayModel.setEntries(model.getEntries());
        this.tileTrayView.update();
        this.boardView.update();
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


    public void handleBoardTileRemover(TilePositioned tile) {
        for (var i = 0; i < this.placedTiles.size(); i++) {
            var temp = this.placedTiles.get(i);
            if (temp.pos() == tile.pos()) {
                this.placedTiles.remove(i);
                break;
            }
        }
        this.boardView.update();
    }

    public void handleBoardTileAdder(Position pos) {
        if (tileTrayModel.getSelected().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tile selected to place");
            return;
        }

        var tile_i = tileTrayModel.getSelected().get();
        this.placedTiles.add(new TilePositioned(tileTrayModel.getEntries().get(tile_i).tile(), pos));
        this.boardView.update();
        // TODO: Remove from hand
    }

    private Component createBoard(Board board, List<TilePositioned> placedTiles) {
        this.boardViewModel = new BoardViewModel(this.game.getBoard(), this.placedTiles);
        var boardView = new BoardView(this.boardViewModel);
        boardView.addBoardTileAdder(this);
        boardView.addBoardTileRemover(this);
        this.boardView = boardView;
        return boardView;
    }

}
