import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

public class PlayButtonController implements ActionListener {

    private final GameView view;
    private final Game gameModel;
    private final BoardViewModel boardModel;

    public PlayButtonController(GameView view, Game gameModel, BoardViewModel boardModel) {
        this.view = view;
        this.gameModel = gameModel;
        this.boardModel = boardModel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.boardModel.getPlacedTiles().size() != 0) {
            Optional<TilePlacement> tp = TilePlacement.FromTiles(
                    new ArrayList<>(this.boardModel.getPlacedTiles()
                            .stream()
                            .map(t -> new Positioned<>(new Tile(t.value().chr(), t.value().pointValue()), t.pos()))
                            .toList())
            );

            if (tp.isPresent()) {
                try {
                    this.gameModel.place(tp.get());
                    this.gameModel.runAi();
                    this.gameModel.commit();
                    this.boardModel.getPlacedTiles().removeIf(_all -> true);
                    this.boardModel.setBoard(this.gameModel.getBoard());
                    this.view.update();
                } catch (PlacementException pe) {
                    JOptionPane.showMessageDialog(this.view, String.format("Bad placement: %s", pe.getMessage()));
                }
            } else {
                JOptionPane.showMessageDialog(this.view, "Invalid tile");
            }
        } else {
            JOptionPane.showMessageDialog(this.view, "No Word Played");
        }
    }
}
