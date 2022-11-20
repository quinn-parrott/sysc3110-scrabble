import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * GUI view that implements player name collection
 *
 * @author Quinn, 101169535
 */
public class PlayerAdderView {

    private final Component parent;

    public PlayerAdderView(Component parent) {
        this.parent = parent;
    }

    /**
     * Method to get the number of players and AIplayers that will be playing the game.
     * Number of players + AIs must be between 2 and 4.
     *
     *  @author Tao Lufula, 101164153
     *  @author Colin Mandeville, 101140289
     */
    public java.util.List<Player> getPlayers() {
        while(true) {
            JPanel playerPanel = new JPanel();
            JTextField enterNumOfPlayers = new JTextField("Enter number of players (1-4) :  ");
            enterNumOfPlayers.setEditable(false);
            playerPanel.add(enterNumOfPlayers);

            JTextField PlayersNumber = new JTextField(10);
            playerPanel.add(PlayersNumber);
            JOptionPane.showOptionDialog(this.parent, playerPanel, "Players' Setup" + "                    " + "SCRABBLE ", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

            int numberOfPlayers;
            try {
                numberOfPlayers = Integer.parseInt(PlayersNumber.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry! Enter number between 1 and 4");
                continue;
            }

            if (numberOfPlayers < 1 || numberOfPlayers > 4) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry!  Number of players must be between 1 and 4");
                continue;
            }

            playerPanel = new JPanel();
            enterNumOfPlayers = new JTextField("Enter number of AIs :  ");
            enterNumOfPlayers.setEditable(false);
            playerPanel.add(enterNumOfPlayers);

            PlayersNumber = new JTextField(10);
            playerPanel.add(PlayersNumber);
            JOptionPane.showOptionDialog(this.parent, playerPanel, "Players' Setup" + "                    " + "SCRABBLE ", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

            int numberOfAIs;
            try {
                numberOfAIs = Integer.parseInt(PlayersNumber.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry! Enter a number");
                continue;
            }

            if (numberOfAIs + numberOfPlayers < 2 || numberOfAIs + numberOfPlayers > 4) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry!  Number of players + AIs must be between 2 and 4");
                continue;
            }

            ArrayList<Player> players = new ArrayList<>();

            for (String name : getPlayersNames(numberOfPlayers)) {
                players.add(new Player(name));
            }

            for (int i = 0; i < numberOfAIs; i++) {
                players.add(new AIPlayer("AI" + (i+1)));
            }

            return players;
        }
    }

    /**
     * Method to get all the players' names
     *
     * @param numberOfPlayers specified by the user in  getPlayers
     * @return The collected players
     * @author Tao Lufula, 101164153
     */
    private ArrayList<String> getPlayersNames(int numberOfPlayers){

        var playersList = new ArrayList<String>(numberOfPlayers);

        for (int i = 0; i < numberOfPlayers; i++) {

            boolean validName = false;

            while (!validName) {

                JPanel addNamePanel = new JPanel();
                JTextField enterName = new JTextField("Enter name of player " + (i + 1));
                enterName.setEditable(false);
                addNamePanel.add(enterName);

                JTextField getName = new JTextField(10);
                addNamePanel.add(getName);
                JOptionPane.showOptionDialog(this.parent, addNamePanel, "Players Names", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                if (getName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this.parent, "Invalid entry! Name cannot be empty");
                }
                else {
                    playersList.add(getName.getText());
                    validName = true;
                }

            }
        }

        return playersList;
    }

}
