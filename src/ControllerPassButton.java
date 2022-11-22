import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControllerPassButton implements ActionListener {

    private GameView view;
    private Game gameModel;
    private BoardViewModel boardModel;

    public ControllerPassButton(GameView view, Game gameModel, BoardViewModel boardModel) {
        this.view = view;
        this.gameModel = gameModel;
        this.boardModel = boardModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.gameModel.pass();
        this.view.getPlacedTiles().removeIf(_all -> true);
        this.boardModel.setBoard(this.gameModel.getBoard());
        this.view.update();
    }
}
