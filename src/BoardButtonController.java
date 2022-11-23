import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardButtonController implements ActionListener {

    private BoardView view;
    private BoardViewModel model;

    public BoardButtonController(BoardView view, BoardViewModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent source) {
        Position pos = Position.FromIndex(Integer.parseInt(source.getActionCommand())).get();
        var placedTileInner = model.getPlacedTiles().stream().filter(t -> t.pos().equals(pos)).findFirst();
        switch (view.getCallbackDispatch().get(pos.getIndex())){
            case AddTile -> boardViewAddTile(pos);
            case RemoveTile -> boardViewRemoveTile(new Positioned<>(placedTileInner.get().value(), pos));
        }
    }

    private void boardViewRemoveTile(Positioned<WildcardableStoreTile> tile) {
        for (var remover: view.getBoardTileRemover()) {
            remover.handleBoardTileRemover(tile);
        }
    }

    private void boardViewAddTile(Position pos) {
        for (var adder: view.getBoardTileAdder()) {
            adder.handleBoardTileAdder(pos);
        }
    }
}
