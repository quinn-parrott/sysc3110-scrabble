import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardView {
    Game game;
    JPanel view;

    List<JLabel> nameLabels;
    List<JLabel> pointsLabels;

    /**
     * Creates and updates the score of each player as the game goes on
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
            JLabel x = new JLabel(p.getName() + "'s score:  ");
            x.setHorizontalAlignment(SwingConstants.RIGHT);
            JLabel y = new JLabel("  "+ p.getPoints());

            nameLabels.add(x);
            pointsLabels.add(y);
            score.add(x);
            score.add(y);
        }
        grid.setPreferredSize(new Dimension(300,500));

        this.view = grid;
    }

    public void update() {
        var players = game.getPlayers();
        for(int i = 0; i < players.size(); i++){
            JLabel x = nameLabels.get(i);
            JLabel y = pointsLabels.get(i);
            var player = players.get(i);

            x.setText(player.getName() + "'s score:  ");
            y.setText("  "+ player.getPoints());
        }
    }

    public JPanel getView() {
        return view;
    }
}
