package academy.cashbackmerge.service;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.Tile;
import java.util.Random;

public class WeightedTileSelector {

    private final GameConfig gameConfig;

    public WeightedTileSelector(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public Tile select(Random random) {
        double value = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (SpawnRule rule : gameConfig.spawnRules()) {
            cumulativeProbability += rule.probability().doubleValue();

            if (value < cumulativeProbability) {
                return new Tile(rule.cashback());
            }
        }

        SpawnRule lastRule = gameConfig.spawnRules().get(gameConfig.spawnRules().size() - 1);

        return new Tile(lastRule.cashback());
    }
}
