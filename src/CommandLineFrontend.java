import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CommandLineFrontend{
    private List<Player> playersList = new ArrayList<>();
    private Scanner reader;
    private String playerWord = "";
    int playerTurn = 0;

    private Board board;

    /**
     * Default constructor for WordList class
     * @author Jawad Nasrallah, Tao Lufula
     */
    public CommandLineFrontend(){
        board = new Board(); //initialize board
        reader = new Scanner(System.in);

    }

    public Player getPlayer(){
        return this.playersList.get(playerTurn);
    }

    public void play() throws PlacementException {
            boolean validCommand = false;
            boolean isRunning = true;

            //Give User the option to begin the game or leave
            System.out.println("Welcome to Scrabble" + "\n Press x to begin game or any other key to exit: ");
            Scanner beginGame = new Scanner(System.in);

            if(beginGame.equals("x")){

                //store usernames in playersList
                while(!validCommand){
                    System.out.println("Enter your name: ");
                    Scanner playerInput = new Scanner(System.in);

                    String name = playerInput.nextLine();
                    playersList.add(new Player(name));

                    System.out.println("Would you like to add another player? Yes or No");
                    Scanner addPlayer = new Scanner(System.in);
                    if (playersList.size() == 2 || !"Yes".equals(addPlayer) ){
                        validCommand = true;
                    }

               else {
                   System.exit(0); //exit if user enters a character that is not "x"
                    }
            }
                }

            //inform user that it is their turn. Create a TileBag called "gameBag"
            while(isRunning){
                System.out.println(this.getPlayer() + ", it is your turn");
                TileBag gameBag = new TileBag();

                //Scanner tileInput = new Scanner(System.in);

                //Iterate through gameBag, and print all tiles in the bag.
                System.out.println(this.getPlayer() + "This is your hand of tiles");
                for(int i=0; i<7; i++){
                    Tile gameTile = gameBag.drawTile();
                    this.getPlayer().addTile(gameTile);
                    System.out.print(gameTile);
                }

                //print board using method
                //ask user to put word on board using method with position
                //pass position in parameters
                //check if letters are in bag and whether word is valid

                board.printBoard();

                System.out.println("where would you like to place the tiles:");

                String line = reader.next();

                for(char x: line.toCharArray()) {
                    if (gameBag.drawTile().equals(x)) {
                        System.out.println("You do not have letter "+x +" in your hand");
                        return;
                    }
                }

                board.placeTiles(new TilePlacement(new ArrayList<>()));


                }
            }
}



