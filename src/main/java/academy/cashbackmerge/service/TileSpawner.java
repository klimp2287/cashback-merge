package academy.cashbackmerge.service;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileSpawner {

    private final GameConfig gameConfig;
    private final Random random;

    public TileSpawner(GameConfig gameConfig, long seed) {
        this.gameConfig = gameConfig;
        this.random = new Random(seed);
    }

    public void spawn(Board board) {
        Position position = createRandomPosition(board);
        Tile tile = createRandomTile();

        board.setTile(position, tile);
    }

    private Tile createRandomTile() {
        int randomIndex = random.nextInt(gameConfig.spawnRules().size());
        SpawnRule spawnRule = gameConfig.spawnRules().get(randomIndex);

        return new Tile(spawnRule.cashback());
    }

    private Position createRandomPosition(Board board) {
        List<Position> freePositions = new ArrayList<>();

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Position position = new Position(x, y);

                if (board.isEmpty(position)) {
                    freePositions.add(position);
                }
            }
        }

        int randomIndex = random.nextInt(freePositions.size());

        return freePositions.get(randomIndex);
    }
}
