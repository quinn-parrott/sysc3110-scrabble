import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class ControllerPlayButton implements ActionListener {

    private final GameView view;
    private final Game gameModel;
    private final BoardViewModel boardModel;

    public ControllerPlayButton(GameView view, Game gameModel, BoardViewModel boardModel) {
        this.view = view;
        this.gameModel = gameModel;
        this.boardModel = boardModel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.boardModel.getPlacedTiles().size() != 0) {
            Optional<TilePlacement> tp = TilePlacement.FromTiles(this.boardModel.getPlacedTiles());
            if (tp.isPresent()) {
                try {
                    this.gameModel.place(tp.get());
                    this.view.getPlacedTiles().removeIf(_all -> true);
                    this.boardModel.setBoard(this.gameModel.getBoard());
                } catch (PlacementException pe) {
                    JOptionPane.showMessageDialog(this.view, String.format("Bad placement: %s", pe.getMessage()));
                }
            } else {
                JOptionPane.showMessageDialog(this.view, "Invalid tile");
            }
        } else {
            JOptionPane.showMessageDialog(this.view, "No Word Played");
        }
        this.view.update();
    }
}
