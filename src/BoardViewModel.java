import java.util.List;

/**
 * Class store state related to BoardViewModel
 *
 * @author Quinn Parrott, 101169535
 */
public class BoardViewModel {
    private Board board;
    private List<TilePositioned> placedTiles;

    public BoardViewModel(
        Board board,
        List<TilePositioned> placedTiles
    ) {
        this.board = board;
        this.placedTiles = placedTiles;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<TilePositioned> getPlacedTiles() {
        return placedTiles;
    }
}
