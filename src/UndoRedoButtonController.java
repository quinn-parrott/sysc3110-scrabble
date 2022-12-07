import javax.swing.*;

public class UndoRedoButtonController{

    private GameView gameView;
    private Game game;
    private BoardViewModel boardViewModel;

    public UndoRedoButtonController(GameView gameView, Game game, BoardViewModel boardViewModel) {
        this.gameView = gameView;
        this.game = game;
        this.boardViewModel = boardViewModel;
    }

    public void undo(JButton source) {
        if (this.game.undo()) {
            this.boardViewModel.setBoard(this.game.getBoard());
            this.boardViewModel.getPlacedTiles().removeIf(_ignore -> true);
            gameView.update();
        }
    }

    public void redo(JButton source) {
        if (this.game.redo()) {
            this.boardViewModel.setBoard(this.game.getBoard());
            this.boardViewModel.getPlacedTiles().removeIf(_ignore -> true);
            gameView.update();
        }
    }
}
