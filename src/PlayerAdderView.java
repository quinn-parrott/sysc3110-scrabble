import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;


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
    public Optional<java.util.List<Player>> getPlayers() {
        while(true) {
            var playerNumber = Optional.ofNullable(
                    JOptionPane.showInputDialog(
                            this.parent,
                            "Enter number of players (1-4):",
                            "Players' Setup" + "                    " + "SCRABBLE",
                            JOptionPane.PLAIN_MESSAGE
                    )
            );
            if (playerNumber.isEmpty()) {
                return Optional.empty();
            }

            int numberOfPlayers;
            try {
                numberOfPlayers = Integer.parseInt(playerNumber.get());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry! Enter number between 1 and 4");
                continue;
            }

            if (numberOfPlayers < 1 || numberOfPlayers > 4) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry!  Number of players must be between 1 and 4");
                continue;
            }

            var aiNum = Optional.ofNullable(
                    JOptionPane.showInputDialog(
                            this.parent,
                            "Enter number of AIs:",
                            "Players' Setup" + "                    " + "SCRABBLE",
                            JOptionPane.PLAIN_MESSAGE
                    )
            );
            if (aiNum.isEmpty()) {
                return Optional.empty();
            }

            int numberOfAIs;
            try {
                numberOfAIs = Integer.parseInt(aiNum.get());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry! Enter a number");
                continue;
            }

            if (numberOfAIs + numberOfPlayers < 2 || numberOfAIs + numberOfPlayers > 4) {
                JOptionPane.showMessageDialog(this.parent, "Invalid entry!  Number of players + AIs must be between 2 and 4");
                continue;
            }

            var playersOpt = getPlayersNames(numberOfPlayers);
            if (playersOpt.isEmpty()) {
                return Optional.empty();
            }

            ArrayList<Player> players = new ArrayList<>();
            for (String name : playersOpt.get()) {
                players.add(new Player(name));
            }

            for (int i = 0; i < numberOfAIs; i++) {
                players.add(new Player("AI" + (i+1), true));
            }

            return Optional.of(players);
        }
    }

    /**
     * Method to get all the players' names
     *
     * @param numberOfPlayers specified by the user in  getPlayers
     * @return The collected players
     * @author Tao Lufula, 101164153
     */
    private Optional<ArrayList<String>> getPlayersNames(int numberOfPlayers){

        var playersList = new ArrayList<String>(numberOfPlayers);

        for (int i = 0; i < numberOfPlayers; i++) {

            boolean validName = false;

            while (!validName) {
                var playerName = Optional.ofNullable(
                        JOptionPane.showInputDialog(
                                this.parent,
                                "Enter name of player " + (i + 1),
                                "Players' Setup" + "                    " + "SCRABBLE",
                                JOptionPane.PLAIN_MESSAGE
                        )
                );

                if (playerName.isEmpty()) {
                    return Optional.empty();
                }

                if (playerName.get().isEmpty()) {
                    JOptionPane.showMessageDialog(this.parent, "Invalid entry! Name cannot be empty");
                }
                else {
                    playersList.add(playerName.get());
                    validName = true;
                }

            }
        }

        return Optional.of(playersList);
    }

}
