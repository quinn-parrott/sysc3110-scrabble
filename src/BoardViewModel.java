import java.util.List;

public class BoardViewModel {
    Board board;
    List<TilePositioned> placedTiles;

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
