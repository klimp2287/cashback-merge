package academy.cashbackmerge.service;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;
import java.util.Random;

public class InitialStateFactory {
    public GameState create(GameConfig config, long seed) {
        Random random = new Random(seed);
        Board board = new Board();

        Tile tile = createRandomTile(random, config);
        Position position = createRandomPosition(random);
        board.setTile(position, tile);

        return new GameState(board, 0, 0, false);
    }

    private Tile createRandomTile(Random random, GameConfig config) {
        int randomIndex = random.nextInt(config.spawnRules().size());
        SpawnRule spawnRule = config.spawnRules().get(randomIndex);
        return new Tile(spawnRule.cashback());
    }

    private Position createRandomPosition(Random random) {
        int x = random.nextInt(Board.BOARD_SIZE);
        int y = random.nextInt(Board.BOARD_SIZE);
        return new Position(x, y);
    }
}
