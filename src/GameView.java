import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
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
    private final List<Player> playersList = new ArrayList<>();
    private final JLabel playerTurnLabel;
    private final JButton playButton;
    private final JButton passTurn;




    /**
     * Default Constructor that will initialize the GUI and set up all the JFrame components
     *
     * @throws IOException //This will be removed once word list is fixed.
     *
     * @author Tao Lufula, 101164153, .....
     */
    public GameView() throws IOException {
        super("SCRABBLE");

        pane = this.getContentPane();
        pane.setLayout(new BorderLayout());

        playerTurnLabel = new JLabel();
        playButton = new JButton();
        passTurn = new JButton();



        // A layout manager for these components is still pending

        this.getPlayers();
        this.createBoard();
        this.createTileHand();
        this.createScoreBoard();
        this.createPlayButtons();

        game = new Game(playersList, wordList);
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
    private void createScoreBoard() {
        JPanel grid = new JPanel(new GridLayout(1, 1));
        pane.add(grid, BorderLayout.EAST);
        JPanel score = new JPanel(new GridLayout(playersList.size(),playersList.size()));
        score.setBackground(Color.green);
        grid.add(score);
        for(Player p : playersList){
            JLabel x = new JLabel(p.getName() + "'s score:  ");
            JLabel y = new JLabel("  "+ p.getPoints());
            score.add(x);
            score.add(y);
        } //USE THIS AS UPDATE METHOD. (CREATE/UPDATE SCORE BOARD)
    }




    /**
     * To be implented
     */
    private void createTileHand() {
        JPanel grid = new JPanel(new GridLayout(1, 1));
        JButton button = new JButton("Tile Hand and Play Buttons will be here!.  Jlabel to tell whose turn it is will also be here.");
        button.setPreferredSize(new Dimension(100, 200));
        grid.add(button);
        pane.add(grid, BorderLayout.SOUTH);

    }



    /**
     * To be implented just
     */
    private void createBoard() {
        JPanel grid = new JPanel(new GridLayout(1, 1));
        JButton button = new JButton("Game Board will be here!");
        button.setPreferredSize(new Dimension(1000, 100));
        grid.add(button);
        pane.add(grid, BorderLayout.WEST);
    }

    /**
     * Method to get the number of players that will be playing the game.
     * Player must be between 2 and 4.
     *
     *  @author Tao Lufula, 101164153
     */
    private void getPlayers() {
        boolean gotPlayers = false;

        while(!gotPlayers) {

            int numberOfPlayers = 0;

            JPanel playerPanel = new JPanel();
            JTextField enterNumOfPlayers = new JTextField("Enter number of players (2 to 4) :  ");
            enterNumOfPlayers.setEditable(false);
            playerPanel.add(enterNumOfPlayers);

            JTextField PlayersNumber = new JTextField(10);
            playerPanel.add(PlayersNumber);
            JOptionPane.showOptionDialog(this, playerPanel, "Players' Setup" + "                    " + "SCRABBLE ", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            try {
                numberOfPlayers = Integer.parseInt(PlayersNumber.getText());
            }catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid entry! Enter number between 2 to 4");
                continue;
            }

            if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                JOptionPane.showMessageDialog(this, "Invalid entry!  Number of players must be between 2 to 4");
            } else {
                getPlayersNames(numberOfPlayers);
                gotPlayers = true;
            }
        }

    }

    /**
     * Method to get all the players' names
     * @param numberOfPlayers specified by the user in  getPlayers
     *
     *  @author Tao Lufula, 101164153
     */
    private void getPlayersNames(int numberOfPlayers){

        for (int i = 0; i < numberOfPlayers; i++) {

            Boolean validName = false;

            while (!validName) {

                JPanel addNamePanel = new JPanel();
                JTextField enterName = new JTextField("Enter name of player " + (i + 1));
                enterName.setEditable(false);
                addNamePanel.add(enterName);

                JTextField getName = new JTextField(10);
                addNamePanel.add(getName);
                JOptionPane.showOptionDialog(this, addNamePanel, "Players Names", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                if (getName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Invalid entry! Name cannot be empty");
                }
                else {
                    playersList.add(new Player(getName.getText()));
                    validName = true;
                }

            }
        }
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            try {
                new GameView();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
