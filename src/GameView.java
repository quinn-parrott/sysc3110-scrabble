import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Game View
 * GUI of the game. This class will handle all the JFrame Components for the scrabble game
 *
 */
public class GameView extends JFrame {
    private final Container pane;
    private final List<Player> playersList = new ArrayList<>();


    public GameView(){
        super("SCRABBLE");

        pane = this.getContentPane();
        pane.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);



        this.setVisible(true);
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            new GameView();
        });
    }
}

