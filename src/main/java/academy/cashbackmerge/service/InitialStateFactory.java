package academy.cashbackmerge.service;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;
import java.util.Random;

public class InitialStateFactory {

    public GameState create(GameConfig config, long seed) {
        Random random = new Random(seed);
        WeightedTileSelector tileSelector = new WeightedTileSelector(config);

        Board board = new Board();

        Tile tile = tileSelector.select(random);
        Position position = createRandomPosition(random);

        board.setTile(position, tile);

        return new GameState(board, 0, 0, false);
    }

    private Position createRandomPosition(Random random) {
        int x = random.nextInt(Board.BOARD_SIZE);
        int y = random.nextInt(Board.BOARD_SIZE);

        return new Position(x, y);
    }
}
