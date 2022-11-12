import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
            var boardTile = model.board().getTile(pos).get();

            var placedTile = model.placedTiles().stream().filter(t -> t.pos().equals(pos)).findFirst();

            JButton button = new JButton(String.format("%c", placedTile.map(TilePositioned::tile).orElse(boardTile).chr()));
            buttons.add(button);
            callbackDispatch.add(CallbackType.None);
            button.addActionListener(source -> {
                var placedTileInner = model.placedTiles().stream().filter(t -> t.pos().equals(pos)).findFirst();
                var boardTileInner = model.board().getTile(pos).get();
                var tileInner = placedTileInner.map(TilePositioned::tile).orElse(boardTileInner);
                callbackDispatch(new TilePositioned(tileInner, pos));
            });
            this.add(button);
        }

        update();
    }

    public void update() {
        for (int i = 0; i < (Board.getCOLUMN_NUMBER() * Board.getROW_NUMBER()); i++) {
            var pos = Position.FromIndex(i).get();
            var boardTile = model.board().getTile(pos).get();

            var placedTile = model.placedTiles().stream().filter(t -> t.pos().equals(pos)).findFirst();

            JButton button = this.buttons.get(i);
            this.callbackDispatch.set(i, CallbackType.None);
            button.setText(String.format("%c", placedTile.map(TilePositioned::tile).orElse(boardTile).chr()));

            var isTilePlaceable = false;
            if (placedTile.isPresent()) {
                isTilePlaceable = true;
                this.callbackDispatch.set(i, CallbackType.RemoveTile);
            } else {
                if (!boardTile.isFilledWithLetter()) { // Can't place on filled tile
                    if (Board.getCenterTilePos() == pos.getIndex()) {
                        // Center tile is a special case since it's possible to place
                        // the tile here when there are no other tiles.
                        isTilePlaceable = true;
                    } else {
                        // Only make the tile placeable if there is a tile adjacent that has a letter
                        for (var adjacentPos : pos.adjacentPositions()) {
                            var adjacentTile = model.board().getTile(adjacentPos).get();
                            if (adjacentTile.isFilledWithLetter()) {
                                isTilePlaceable = true;
                                break;
                            }
                        }
                    }
                }
                this.callbackDispatch.set(i, CallbackType.AddTile);
            }
            button.setEnabled(isTilePlaceable);

            button.setBackground(colorUnselected);
        }
    }

    private void callbackDispatch(TilePositioned tile) {
        switch (this.callbackDispatch.get(tile.pos().getIndex())){
            case AddTile -> boardViewAddTile(tile.pos());
            case RemoveTile -> boardViewRemoveTile(tile);
        }
    }

    public void addBoardTileRemover(IBoardTileRemover remover) {
        this.boardTileRemover.add(remover);
    }
    private void boardViewRemoveTile(TilePositioned tile) {
        for (var remover: this.boardTileRemover) {
            remover.handleBoardTileRemover(tile);
        }
    }

    public void addBoardTileAdder(IBoardTileAdder adder) {
        this.boardTileAdder.add(adder);
    }

    private void boardViewAddTile(Position pos) {
        for (var adder: this.boardTileAdder) {
            adder.handleBoardTileAdder(pos);
        }
    }
}
