package academy.cashbackmerge.model;

import java.math.BigDecimal;
import java.util.Map;

public record SimulationReport(
        int totalGames,
        int movesLimit,
        long seed,
        String strategy,
        Map<Integer, Integer> maxCashbackDistribution,
        Map<Integer, Integer> rewardDistribution,
        int maxRewardGames,
        BigDecimal maxRewardRate,
        boolean businessConstraintSatisfied,
        double averageScore,
        int maxScore) {}
