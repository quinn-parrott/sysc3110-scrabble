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

        this.createBoard();
        this.createTileHand();
        this.createScoreBoard();
        this.createPlayButtons();
        this.getPlayers();

        game = new Game(playersList, wordList);
        game.addGameView(this);
        playerTurnLabel = new JLabel();


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setVisible(true);
    }



    /**
     * To be implented
     */
    private void createPlayButtons() {
    }



    /**
     * To be implented
     */
    private void createScoreBoard() {
        JPanel grid = new JPanel(new GridLayout(1, 1));
        JButton button = new JButton("Score Board will be here!");
        button.setPreferredSize(new Dimension(200, 100));
        grid.add(button);
        pane.add(grid, BorderLayout.EAST);
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
