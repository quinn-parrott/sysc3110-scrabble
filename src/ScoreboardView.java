import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI component that displays player scores
 *
 * @author Quinn, 101169535
 */
public class ScoreboardView {
    private Game game;
    private JPanel view;

    private List<JLabel> nameLabels;
    private List<JLabel> pointsLabels;

    /**
     * Creates and updates the score of each player as the game goes on
     *
     * @author Jawad Nasrallah, 101201038
     */
    public ScoreboardView(Game game) {
        this.game = game;
        this.nameLabels = new ArrayList<>();
        this.pointsLabels = new ArrayList<>();

        List<Player> playersList = game.getPlayers();
        JPanel grid = new JPanel(new GridLayout(1, 1));
        JPanel score = new JPanel(new GridLayout(playersList.size(), playersList.size()));
        score.setBackground(Color.green);
        grid.add(score);
        for(Player p : playersList){
            JLabel x = new JLabel();
            x.setHorizontalAlignment(SwingConstants.RIGHT);
            JLabel y = new JLabel();

            nameLabels.add(x);
            pointsLabels.add(y);
            score.add(x);
            score.add(y);
        }
        grid.setPreferredSize(new Dimension(300,460));
        update();

        this.view = grid;
    }

    /**
     * Update the current view
     */
    public void update() {
        var players = game.getPlayers();
        var currentPlayer = game.getPlayer();
        for(int i = 0; i < players.size(); i++){
            JLabel x = nameLabels.get(i);
            JLabel y = pointsLabels.get(i);
            var player = players.get(i);
            if (player == currentPlayer) {
                x.setBorder(new LineBorder(Color.red));
            } else {
                x.setBorder(new LineBorder(Color.green));
            }

            x.setText(player.getName() + "'s score:  ");
            y.setText("  "+ player.getPoints());
        }
    }

    public JPanel getView() {
        return view;
    }
}
