package academy.cashbackmerge.config;

import java.util.List;

public record GameConfig(int movesLimit, List<SpawnRule> spawnRules) {}
