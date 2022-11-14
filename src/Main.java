import java.io.IOException;

/**
 * Entrypoint
 */
public class Main {
    public static void main(String[] args) throws PlacementException, IOException {
//        CommandLineFrontend cli = new CommandLineFrontend();
//        cli.play();
        new GameView();
    }
}