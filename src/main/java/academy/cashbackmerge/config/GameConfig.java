package academy.cashbackmerge.config;

import java.math.BigDecimal;
import java.util.List;

public record GameConfig(
        int movesLimit,
        List<SpawnRule> spawnRules,
        List<RewardRule> rewardTable,
        int maxRewardCashback,
        BigDecimal maxRewardAllowedRate) {

    public GameConfig(int movesLimit, List<SpawnRule> spawnRules) {
        this(movesLimit, spawnRules, List.of(), 0, BigDecimal.ZERO);
    }
}
