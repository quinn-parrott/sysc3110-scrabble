import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Game View
 * GUI of the game. This class will handle all the JFrame Components for the scrabble game
 *
 */
public class GameView extends JFrame {
    private static final Color colorUnselected = new Color(240, 240, 240);
    private static final Color colorSelected = new Color(200, 200, 200);

    private final Container pane;
    private final Game game;     // model if we'll be using MVC . Some more changes to be made in the Game class
    private final JLabel playerTurnLabel;
    private final JButton playButton;
    private final JButton passTurn;




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

        TileBag gameBag = new TileBag();
        int PLAYER_HAND_SIZE = 7;
        for (Player player : playersList) {
            for (int i = 0; i < PLAYER_HAND_SIZE; i++) {
                player.getTileHand().add(gameBag.drawTile());
            }
        }

        game = new Game(playersList, new WordList());


        pane.add(this.createBoard(game.getBoard()), BorderLayout.WEST);
        pane.add(this.createTileHand(playersList.get(0)) , BorderLayout.SOUTH);
        pane.add(this.createScoreBoard(), BorderLayout.EAST);
        this.createPlayButtons();

        game.addGameView(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1130, 1130);
        this.setVisible(true);
    }


    /**
     * Method to create the play and pass turn buttons
     *
     * @author Tao Lufula, 101164153
     */
    private void createPlayButtons() {
        //Reset or play word button. This button validates the words being placed on the board or will also clear the players letters placed on the board
        this.switchPlayButtonText("PLAY");
        playButton.setSize(50,50);
        playButton.setEnabled(false);

        //Pass players turn when they press this button
        passTurn.setText("PLAY");
        passTurn.setSize(50,50);
        passTurn.setEnabled(true);

        //will add action listeners for this buttons

        pane.add(playButton);
        pane.add(passTurn);

    }

    /**
     * Changes the play button text depending on the game circumstance
     * @param text
     *
     * @author Tao Lufula, 101164153
     */
    public void switchPlayButtonText(String text){
        // PLAY
        // RESET
        playButton.setText(text);
    }



    /**
     * Creates and updates the score of each player as the game goes on
     * @author Jawad Nasrallah, 101201038
     *
     */
    private Component createScoreBoard() {
        List<Player> playersList = game.getPlayers();
        JPanel grid = new JPanel(new GridLayout(1, 1));
        JPanel score = new JPanel(new GridLayout(playersList.size(), playersList.size()));
        score.setBackground(Color.green);
        grid.add(score);
        for(Player p : playersList){
            JLabel x = new JLabel(p.getName() + "'s score:  ");
            JLabel y = new JLabel("  "+ p.getPoints());
            score.add(x);
            score.add(y);
        } //USE THIS AS UPDATE METHOD. (CREATE/UPDATE SCORE BOARD)
        return grid;
    }


    private Component createTileHand(Player player) {
        var tiles = player.getTileHand();
        JPanel gridPanel = new JPanel(new GridLayout(1, tiles.size()));
        gridPanel.setPreferredSize(new Dimension(100, 200));

        var buttons = new ArrayList<JButton>();

        for (Tile tile : tiles) {
            JButton button = new JButton(String.format("%c tile", tile.chr()));
            button.setBackground(colorUnselected);
            button.addActionListener(event -> {
                var but = (JButton) event.getSource();

                var isSelected = but.getBackground() == colorSelected;

                // Reset all the buttons
                for (JButton b : buttons) {
                    b.setBackground(colorUnselected);
                }

                but.setBackground(
                        isSelected ? colorUnselected : colorSelected
                );
            });
            buttons.add(button);

            gridPanel.add(button);
        }

        return gridPanel;
    }



    private Component createBoard(Board board) {
        JPanel grid = new JPanel(new GridLayout(Board.getROW_NUMBER(), Board.getCOLUMN_NUMBER()));
        grid.setPreferredSize(new Dimension(1000, 100));

        for (int i =0; i< (Board.getCOLUMN_NUMBER() * Board.getROW_NUMBER()); i++) {
            var pos = Position.FromIndex(i).get();
            var tile = board.getTile(pos).get();
            JButton button = new JButton(String.format("%c", tile.chr()));

            var isTilePlaceable = false;
            if (!tile.isFilledWithLetter()) { // Can't place on filled tile
                if (Board.getCenterTilePos() == pos.getIndex()) {
                    // Center tile is a special case since it's possible to place
                    // the tile here when there are no other tiles.
                    isTilePlaceable = true;
                } else {
                    // Only make the tile placeable if there is a tile adjacent that has a letter
                    for (var adjacentPos : pos.adjacentPositions()) {
                        var adjacentTile = board.getTile(adjacentPos).get();
                        if (adjacentTile.isFilledWithLetter()) {
                            isTilePlaceable = true;
                            break;
                        }
                    }
                }
            }
            button.setEnabled(isTilePlaceable);

            button.setBackground(colorUnselected);
            grid.add(button);
        }

        return grid;
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(GameView::new);
    }

}
