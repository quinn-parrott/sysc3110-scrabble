import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
            var boardTile = model.getBoard().getTile(pos).get();

            var placedTile = model.getPlacedTiles().stream().filter(t -> t.pos().equals(pos)).findFirst();

            JButton button = new JButton(String.format("%c", placedTile.map(TilePositioned::tile).orElse(boardTile).chr()));
            buttons.add(button);
            callbackDispatch.add(CallbackType.None);
            button.addActionListener(source -> {
                var placedTileInner = model.getPlacedTiles().stream().filter(t -> t.pos().equals(pos)).findFirst();
                var boardTileInner = model.getBoard().getTile(pos).get();
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
            var boardTile = model.getBoard().getTile(pos).get();

            var placedTile = model.getPlacedTiles().stream().filter(t -> t.pos().equals(pos)).findFirst();

            JButton button = this.buttons.get(i);
            this.callbackDispatch.set(i, CallbackType.None);
            button.setText(String.format("%c", placedTile.map(TilePositioned::tile).orElse(boardTile).chr()));

            if (placedTile.isPresent()) {
                this.callbackDispatch.set(i, CallbackType.RemoveTile);
            } else {
                this.callbackDispatch.set(i, CallbackType.AddTile);
            }
            button.setEnabled(!boardTile.isFilledWithLetter());

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

    public Optional<TilePlacement> buildPlacement() {
        // Sorting tiles make sure the letters are in order when the word is built
        var placedTiles =
                this.model.getPlacedTiles()
                        .stream()
                        .sorted(
                                Comparator.comparingInt(o -> o.pos().getIndex())
                                )
                        .toList();

        StringBuilder word = new StringBuilder();
        StringBuilder shorthand = new StringBuilder();

        boolean isValid = true;
        Optional<TilePlacement> tp = Optional.empty();
        char direction = ' ';

        if(placedTiles.size() > 1) {
            Position p = placedTiles.get(0).pos();

            for (TilePositioned tile : placedTiles.subList(1, placedTiles.size() - 1)) {
                if (direction == ' ') {
                    if (tile.pos().getX() == p.getX() && tile.pos().getY() != p.getY()) {
                        direction = 'h';
                    } else if (tile.pos().getX() != p.getX() && tile.pos().getY() == p.getY()) {
                        direction = 'v';
                    } else {
                        isValid = false;
                        break;
                    }

                } else {
                    if (direction == 'h') {
                        if (tile.pos().getX() != p.getX() && tile.pos().getY() == p.getY()) {
                            isValid = false;
                            break;
                        }
                    } else {
                        if (tile.pos().getX() == p.getX() && tile.pos().getY() != p.getY()) {
                            isValid = false;
                            break;
                        }
                    }
                }
            }

            shorthand
                    .append(p)
                    .append(':')
                    .append(direction)
                    .append(';');
        } else if (placedTiles.size() == 1) {
            var tile = placedTiles.get(0);
            shorthand
                    .append(tile.pos())
                    .append(":h;")
                    .append(tile.tile().chr());

        } else {
            isValid = false;
        }

        if(isValid) {
            for (TilePositioned tile : placedTiles) {
                word.append(tile.tile().chr());
            }
            shorthand.append(word);
            tp = TilePlacement.FromShorthand(String.valueOf(shorthand));
        }

        return tp;
    }
}
