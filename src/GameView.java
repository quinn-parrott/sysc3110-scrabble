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
    }



    /**
     * To be implented
     */
    private void createTileHand() {
    }



    /**
     * To be implented
     */
    private void createBoard() {

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
