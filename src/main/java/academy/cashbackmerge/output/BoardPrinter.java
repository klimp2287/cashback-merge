package academy.cashbackmerge.output;

import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;

public class BoardPrinter {
    private static final int MAX_BOARD_INDEX = 3;

    public String print(Board board) {
        StringBuilder result = new StringBuilder();

        for (int y = 0; y <= MAX_BOARD_INDEX; y++) {
            for (int x = 0; x <= MAX_BOARD_INDEX; x++) {
                Tile tile = board.getTile(new Position(x, y));

                if (tile == null) {
                    result.append(".");
                } else {
                    result.append(tile.cashback());
                }

                if (x < MAX_BOARD_INDEX) {
                    result.append(" ");
                }
            }

            if (y < MAX_BOARD_INDEX) {
                result.append("\n");
            }
        }

        return result.toString();
    }
}
