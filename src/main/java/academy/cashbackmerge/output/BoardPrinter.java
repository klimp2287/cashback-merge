package academy.cashbackmerge.output;

import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;

public class BoardPrinter {

    public String print(Board board) {
        StringBuilder result = new StringBuilder();

        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                Tile tile = board.getTile(new Position(x, y));

                if (tile == null) {
                    result.append(".");
                } else {
                    result.append(tile.cashback());
                }

                if (x < board.getWidth() - 1) {
                    result.append(" ");
                }
            }

            if (y < board.getHeight() - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }
}
