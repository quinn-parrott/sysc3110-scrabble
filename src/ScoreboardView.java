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
    private JPanel score;

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
        score = new JPanel(new GridLayout(0, 2));
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
        this.resizeScoreboard(game.getPlayers().size());
        var players = game.getPlayers();
        var currentPlayer = game.getPlayer();
        for(int i = 0; i < players.size(); i++){
            JLabel x = nameLabels.get(i);
            JLabel y = pointsLabels.get(i);
            var player = players.get(i);

            x.setBorder(new LineBorder(player == currentPlayer ? Color.red : Color.green));
            x.setText(String.format("%s's score:  ", player.getName()));
            y.setText(String.format("  %s", player.getPoints()));
        }
    }

    private void resizeScoreboard(int numPlayers) {
        int i = 0;
        int delta = numPlayers - nameLabels.size();
        if (delta < 0) {
            for (; i < numPlayers; i++) {
                JLabel x = new JLabel();
                x.setHorizontalAlignment(SwingConstants.RIGHT);
                score.remove(nameLabels.set(i, x));
                score.add(x, i);
                JLabel y = new JLabel();
                score.remove(pointsLabels.set(i, y));
                score.add(y);
            }
            for (; i < nameLabels.size(); i++) {
                score.remove(nameLabels.remove(i));
                score.remove(pointsLabels.remove(i));
            }
        } else if (delta > 0) {
            for (; i < nameLabels.size(); i++) {
                JLabel x = new JLabel();
                x.setHorizontalAlignment(SwingConstants.RIGHT);
                score.remove(nameLabels.set(i, x));
                score.add(x);
                JLabel y = new JLabel();
                score.remove(pointsLabels.set(i, y));
                score.add(y);
            }
            for (; i < numPlayers; i++) {
                JLabel x = new JLabel();
                x.setHorizontalAlignment(SwingConstants.RIGHT);
                nameLabels.add(x);
                score.add(x);
                x = new JLabel();
                pointsLabels.add(x);
                score.add(x);
            }
        }
    }

    public JPanel getView() {
        return view;
    }
}
