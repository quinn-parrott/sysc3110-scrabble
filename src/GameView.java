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
     * To be implented
     */
    private void getPlayers() {
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
