import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Quinn Parrott, 101169535
 */
public class Game {
    private final Map<String, Player> players;
    private final List<TilePlacement> turns;
    private Board board; // TODO: Can be removed if reconstructed each round (superfluous)?

    /**
     * @author Quinn Parrott, 101169535
     */
    public Game(Collection<String> playerNames) {
        this.players = playerNames.stream().collect(Collectors.toMap(playerName -> playerName, Player::new));
        this.turns = new ArrayList<>();
        this.board = new Board();
    }

    /**
     * @author Quinn Parrott, 101169535
     */
    public Board previewPlacement(TilePlacement placement) throws PlacementException {
        var nextBoard = this.board.clone();

        if (turns.size() == 0) {
            // First turn
            var p = Position.FromIndex(Board.getCenterTilePos()).get();
            var distanceFromCenter = (int) placement.minTileDistance(p);
            if (distanceFromCenter > 0) {
                throw new PlacementException("At least one tile must intercept with the center on the first turn", placement, Optional.of(this.board));
            }
        } else {
            enum MinResult {
                TooFar,
                Adjacent,
            }

            var minResult = MinResult.TooFar;

            for (var tile : board.getTiles()) {
                int distance = (int) Math.round(placement.minTileDistance(tile.pos()));

                if (distance == 0) {
                    throw new PlacementException(String.format("Can't place tile at %s since there is already a tile there", tile.pos()), placement, Optional.of(this.board));
                }

                if (distance == 1) {
                    minResult = MinResult.Adjacent;
                }
            }

            if (minResult != MinResult.Adjacent) {
                throw new PlacementException("New tiles aren't adjacent to existing tiles", placement, Optional.of(board));
            }
        }

        // TODO: Do word checking
        // - [ ] Check main word
        // - [x] Attached in straight line (tileplacement handles)
        // - [x] Is attached
        // - [ ] Not invalid word

        nextBoard.placeTiles(placement);

        return nextBoard;
    }

    /**
     * @author Quinn Parrott, 101169535
     */
    public void place(TilePlacement placement) throws PlacementException {
        this.board = this.previewPlacement(placement);
        this.turns.add(placement);
    }

    /**
     * @author Quinn Parrott, 101169535
     */
    public Board getBoard() {
        return board;
    }
}
