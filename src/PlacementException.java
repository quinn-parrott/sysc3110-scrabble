import java.util.Optional;

/**
 * @author Quinn Parrott, 101169535
 */
public class PlacementException extends Exception {

    private final TilePlacement placement;
    private final Optional<Board> previewBoard;

/**
 * @author Quinn Parrott, 101169535
 */
    public PlacementException(String message, TilePlacement placement){
        this(message, placement, Optional.empty());
    }

/**
 * @author Quinn Parrott, 101169535
 */
    public PlacementException(String message, TilePlacement placement, Optional<Board> previousBoard) {
        super(message);
        this.placement = placement;
        this.previewBoard = previousBoard;
    }

/**
 * @author Quinn Parrott, 101169535
 */
    public Optional<Board> getPreviewBoard() {
        return previewBoard;
    }

/**
 * @author Quinn Parrott, 101169535
 */
    public TilePlacement getPlacement() {
        return placement;
    }
}
