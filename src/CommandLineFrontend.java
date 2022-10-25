import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineFrontend{
    private final List<Player> playersList = new ArrayList<>();
    private final Scanner sc;
    private int playerTurn = 0;
    private int numSequentialPasses = 0;
    private Game gameEngine;

    /**
     * Default constructor for WordList class
     * @author Jawad Nasrallah, Tao Lufula
     */
    public CommandLineFrontend(){
        sc = new Scanner(System.in);
    }

    public Player getPlayer(){
        return this.playersList.get(playerTurn % this.playersList.size());
    }

    /**
     * Runs the Scrabble game through a command line user interface by scanning user entry.
     * @author Colin Mandeville, Jawad Nasrallah
     */
    public void play(){
        boolean validCommand = false;
        TileBag gameBag = new TileBag();

        //Give User the option to begin the game or leave
        System.out.println("Welcome to Scrabble" + "\nPress x to begin game or any other key to exit: ");
        String beginGame = sc.nextLine();

        if(beginGame.equals("x")){

            //store usernames in playersList
            while(!validCommand){
                System.out.println("Enter your name: ");
                String name = sc.nextLine();

                playersList.add(new Player(name));

                System.out.println("Would you like to add another player? Yes or No");
                String addPlayer = sc.nextLine();
                if (!addPlayer.equalsIgnoreCase("yes")){
                    validCommand = true;
                }
            }
        } else {
            System.exit(0); //exit if user enters a character that is not "x"
        }

        try {
            gameEngine = new Game(this.playersList, new WordList());
        } catch (IOException io) {
            System.exit(1);
        }

        int PLAYER_HAND_SIZE = 7;
        for (Player player : this.playersList) {
            for (int i = 0; i < PLAYER_HAND_SIZE; i++) {
                player.getTileHand().add(gameBag.drawTile());
            }
        }

        //inform user that it is their turn. Create a TileBag called "gameBag"
        while(true) {
            if (this.numSequentialPasses >= this.playersList.size()) {
                ArrayList<Player> winners = new ArrayList<>();
                winners.add(new Player(""));
                winners.get(0).addPoints(-1);
                for (Player player : this.playersList) {
                    if (player.getPoints() > winners.get(0).getPoints()) {
                        winners.clear();
                        winners.add(player);
                    } else if (player.getPoints() == winners.get(0).getPoints()) {
                        winners.add(player);
                    }
                }
                if (winners.get(0).getName().equals("") && winners.get(0).getPoints() == -1) {
                    System.out.println("No one wins, Sorry");
                }
                for (Player winner : winners) {
                    System.out.println(winner.getName() + " wins!");
                }
                System.exit(0);
            }

            System.out.println(this.getPlayer().getName() + ", it is your turn");
            while (this.getPlayer().getTileHand().size() < PLAYER_HAND_SIZE) {
                this.getPlayer().getTileHand().add(gameBag.drawTile());
            }

            //Iterate through gameBag, and print all tiles in the bag.
            System.out.println(this.getPlayer().getName() + ": This is your hand of tiles (Letter : Point Value)");
            for (int i = 0; i < PLAYER_HAND_SIZE; i++) {
                System.out.print(this.getPlayer().getTileHand().get(i).chr() + ":" +
                        this.getPlayer().getTileHand().get(i).pointValue() + " ");
            }
            System.out.println();

            gameEngine.printBoardState();

            boolean turnInProcess = true;

            while (turnInProcess) {
                System.out.println("Would you like to pass? (Yes or No)");
                String input = sc.next();

                if (input.equalsIgnoreCase("yes")) {
                    gameEngine.pass();
                    this.playerTurn++;
                    turnInProcess = false;
                    this.numSequentialPasses += 1;
                } else {
                    System.out.println("What word would you like to play?");
                    sc.nextLine();
                    String word = sc.nextLine();
                    System.out.println();

                    System.out.println("""
                            On which square would you like to start your word, and in which direction do you want to go? (h = horizontal, v = vertical)
                            Input is accepted in this format: tile:direction,\s
                            i.e. h8:v = Start at h8 and go vertically""");
                    String tilePlacement = sc.nextLine();
                    System.out.println();
                    try {
                        TilePlacement tp = TilePlacement.FromShorthand(tilePlacement + ";" + word).orElseThrow();
                        gameEngine.place(tp);
                        this.playerTurn++;
                        if (gameBag.isEmpty()) {
                            numSequentialPasses++;
                        }
                        turnInProcess = false;
                    } catch (Exception e) {
                        System.out.println("That is not a valid move: " + e);
                    }
                }
            }
        }
    }
}
