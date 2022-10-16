import java.util.ArrayList;

/**
 * @author Colin Mandeville, 101140289
 */
public class Board {
    private static final int COLUMN_NUMBER = 15;
    private static final int ROW_NUMBER = 15;

    private final ArrayList<Tile> board;

    /**
     * Board Constructor, defines ArrayList to contain Board and sets each square to an arbitrary _ on each tile,
     * with * marking the center square
     * @author Colin Mandeville, 101140289
     */
    public Board() {
        this.board = new ArrayList<>();
        for (int i = 0; i < COLUMN_NUMBER*ROW_NUMBER; i++) {
            if (i == Math.floorDiv(COLUMN_NUMBER * ROW_NUMBER, 2)) {
                this.board.add(new Tile('*', 0));
            } else {
                this.board.add(new Tile('_', 0));
            }
        }
    }

    public void setTile(Tile tile, int x, int y) {
        this.board.add(ROW_NUMBER * x + y, tile);
    }

    public static int getCOLUMN_NUMBER() {
        return COLUMN_NUMBER;
    }

    public static int getROW_NUMBER() {
        return ROW_NUMBER;
    }

    /**
     * Prints the current board state to console
     * @author Colin Mandeville, 101140289
     */
    public void printBoard() {
        // Print each row of the board and the Vertical Legend
        for (int i = 0; i < ROW_NUMBER; i++) {
            System.out.printf("%2s ", i + 1);
            // Print each tile in row i
            for (int j = 0; j < COLUMN_NUMBER; j++) {
                System.out.print(this.board.get(15 * i + j).chr() + "  ");
            }
            System.out.println();
        }
        // Print the Horizontal Legend
        System.out.print("   ");
        for (int i = 0; i < COLUMN_NUMBER; i++) {
            int j = i + 'A';
            char c = (char) j;
            System.out.print(c + "  ");
        }
    }
}
