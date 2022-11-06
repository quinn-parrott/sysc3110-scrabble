import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Game View
 * GUI of the game. This class will handle all the JFrame Components for the scrabble game
 *
 */
public class GameView extends JFrame {
    private final Container pane;
    private final Game game;     // model if we'll be using MVC . Some more changes to be made in the Game class
    private final WordList wordList = new WordList();
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
        game = new Game(playersList, wordList);

        pane.add(this.createBoard(), BorderLayout.WEST);
        pane.add(this.createTileHand() , BorderLayout.SOUTH);
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
        JPanel grid = new JPanel(new GridLayout(1, 1));
        JPanel score = new JPanel(new GridLayout(playersList.size(),playersList.size()));
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


    /**
     * To be implented
     */
    private Component createTileHand() {
        // TODO: Implement this
        JPanel grid = new JPanel(new GridLayout(1, 1));
        JButton button = new JButton("Tile Hand and Play Buttons will be here!.  Jlabel to tell whose turn it is will also be here.");
        button.setPreferredSize(new Dimension(100, 200));
        grid.add(button);
        return grid;
    }



    /**
     * To be implemented just
     */
    private Component createBoard() {
        JPanel grid = new JPanel(new GridLayout(1, 1));
        JButton button = new JButton("Game Board will be here!");
        button.setPreferredSize(new Dimension(1000, 100));
        grid.add(button);
        return button;
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(GameView::new);
    }

}
