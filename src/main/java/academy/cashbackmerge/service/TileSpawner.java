package academy.cashbackmerge.service;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileSpawner {

    private final Random random;
    private final WeightedTileSelector tileSelector;

    public TileSpawner(GameConfig gameConfig, long seed) {
        this.random = new Random(seed);
        this.tileSelector = new WeightedTileSelector(gameConfig);
    }

    public void spawn(Board board) {
        Position position = createRandomPosition(board);
        Tile tile = tileSelector.select(random);

        board.setTile(position, tile);
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
