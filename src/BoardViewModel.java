import java.util.List;

public record BoardViewModel(Board board, List<TilePositioned> placedTiles) {
}
