import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that displays a Board
 *
 * @author Quinn Parrott, 101169535
 */
public class BoardView extends JPanel {
    private static final Color colorUnselected = new Color(240, 240, 240);
    private static final Color colorSelected = new Color(200, 200, 200);

    private List<JButton> buttons;

    static enum CallbackType {
        None,
        AddTile,
        RemoveTile,
    }
    private List<CallbackType> callbackDispatch;
    private BoardViewModel model;

    private List<IBoardTileAdder> boardTileAdder;
    private List<IBoardTileRemover> boardTileRemover;

    public BoardView(BoardViewModel model) {
        // TODO: Make backed by a model/store state in another object
        super(new GridLayout(Board.getROW_NUMBER(), Board.getCOLUMN_NUMBER()));
        this.model = model;
        this.buttons = new ArrayList<>();
        this.callbackDispatch = new ArrayList<>();
        this.boardTileAdder = new ArrayList<>();
        this.boardTileRemover = new ArrayList<>();
        this.setPreferredSize(new Dimension(1000, 100));

        for (int i = 0; i < (Board.getCOLUMN_NUMBER() * Board.getROW_NUMBER()); i++) {
            var pos = Position.FromIndex(i).get();

            JButton button = new JButton();
            buttons.add(button);
            callbackDispatch.add(CallbackType.None);
            button.setActionCommand(String.valueOf(i));
            button.addActionListener(new BoardButtonController(this, this.model));
            this.add(button);
        }

        update();
    }

    public void update() {
        for (int i = 0; i < (Board.getCOLUMN_NUMBER() * Board.getROW_NUMBER()); i++) {
            var pos = Position.FromIndex(i).get();

            var placedTile = model.getPlacedTiles().stream().filter(t -> t.pos().equals(pos)).findFirst();
            var boardTile = model.getBoard().getTile(pos);

            JButton button = this.buttons.get(i);
            this.callbackDispatch.set(i, CallbackType.None);
            char letter = placedTile.map(tilePositioned -> tilePositioned.value().chr()).orElse(boardTile.map(Tile::chr).orElse(pos.getBackgroundChar()));
            button.setText(String.format("%c", letter));

            if (placedTile.isPresent()) {
                this.callbackDispatch.set(i, CallbackType.RemoveTile);
            } else {
                this.callbackDispatch.set(i, CallbackType.AddTile);
            }
            button.setEnabled(model.getBoard().getTile(pos).isEmpty());

            button.setBackground(colorUnselected);
        }
    }

    public void addBoardTileRemover(IBoardTileRemover remover) {
        this.boardTileRemover.add(remover);
    }

    public void addBoardTileAdder(IBoardTileAdder adder) {
        this.boardTileAdder.add(adder);
    }

    public List<CallbackType> getCallbackDispatch() {
        return callbackDispatch;
    }

    public List<IBoardTileAdder> getBoardTileAdder() {
        return boardTileAdder;
    }

    public List<IBoardTileRemover> getBoardTileRemover() {
        return boardTileRemover;
    }
}
