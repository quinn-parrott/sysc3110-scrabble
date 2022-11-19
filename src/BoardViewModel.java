import java.util.List;

/**
 * Class store state related to BoardViewModel
 *
 * @author Quinn Parrott, 101169535
 */
public class BoardViewModel {
    private Board board;
    private List<Positioned<Tile>> placedTiles;

    public BoardViewModel(
        Board board,
        List<Positioned<Tile>> placedTiles
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

    public List<Positioned<Tile>> getPlacedTiles() {
        return placedTiles;
    }
}
