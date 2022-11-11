import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayerAdderView {

    private final Component parent;

    public PlayerAdderView(Component parent) {
        this.parent = parent;
    }

    /**
     * Method to get the number of players that will be playing the game.
     * Player must be between 2 and 4.
     *
     *  @author Tao Lufula, 101164153
     */
    public java.util.List<Player> getPlayers() {
        while(true) {
            JPanel playerPanel = new JPanel();
            JTextField enterNumOfPlayers = new JTextField("Enter number of players (2 to 4) :  ");
            enterNumOfPlayers.setEditable(false);
            playerPanel.add(enterNumOfPlayers);

            JTextField PlayersNumber = new JTextField(10);
            playerPanel.add(PlayersNumber);
            JOptionPane.showOptionDialog(this.parent, playerPanel, "Players' Setup" + "                    " + "SCRABBLE ", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

            int numberOfPlayers = 0;
            try {
                numberOfPlayers = Integer.parseInt(PlayersNumber.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry! Enter number between 2 to 4");
                continue;
            }

            if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry!  Number of players must be between 2 to 4");
                continue;
            }

            return getPlayersNames(numberOfPlayers).stream().map(Player::new).collect(Collectors.toList());
        }
    }

    /**
     * Method to get all the players' names
     *
     * @param numberOfPlayers specified by the user in  getPlayers
     * @return The collected players
     *  @author Tao Lufula, 101164153
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
