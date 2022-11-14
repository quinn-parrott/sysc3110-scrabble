import java.util.Optional;

/**
 * Exception throw by the game engine when tiles can't be placed on the board.
 *
 * @author Quinn Parrott, 101169535
 */
public class PlacementException extends Exception {

    private final TilePlacement placement;
    private final Optional<Board> previewBoard;

    /**
     * Constructor for the PlacementException class
     * @author Quinn Parrott, 101169535
     */
    public PlacementException(String message, TilePlacement placement){
        this(message, placement, Optional.empty());
    }

    /**
     * Constructor for the PlacementException class
     * @author Quinn Parrott, 101169535
     */
    public PlacementException(String message, TilePlacement placement, Optional<Board> previousBoard) {
        super(message);
        this.placement = placement;
        this.previewBoard = previousBoard;
    }

    /**
     * Getter method for the previewBoard class attribute
     * @return returns an Optional of the previewBoard attribute
     * @author Quinn Parrott, 101169535
     */
    public Optional<Board> getPreviewBoard() {
        return previewBoard;
    }

    /**
     * Getter method for the placement class attribute
     * @return returns an Optional of the placement attribute
     * @author Quinn Parrott, 101169535
     */
    public TilePlacement getPlacement() {
        return placement;
    }
}
