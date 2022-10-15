import java.util.ArrayList;

public class Board {
    // Written by Colin Mandeville, 101140289

    private ArrayList<Tile> board;

    public Board() {
        this.board = new ArrayList<>();
        for (int i = 0; i < 15*15; i++) {
            if (i == 112) {
                this.board.add(new Tile('*',0));
            } else {
                this.board.add(new Tile('_', 0));
            }
        }
    }

    public void setTile(Tile tile, int x, int y) {
        this.board.add(15 * x + y, tile);
    }

    /**
     * Prints the current board state to console
     * @Author Colin Mandeville, 101140289
     */
    public void printBoard() {
        for (int i = 0; i < 15; i++) {
            if (i < 9) {
                System.out.print(" ");
            }
            System.out.print(i + 1 + " ");
            for (int j = 0; j < 15; j++) {
                System.out.print(this.board.get(15 * i + j).getChr() + "  ");
            }
            System.out.println();
        }
        System.out.print("   ");
        for (int i = 0; i < 15; i++) {
            int j = i + 'A';
            char c = (char) j;
            System.out.print(c + "  ");
        }
    }
}
