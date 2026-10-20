package academy.cashbackmerge.service;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.model.GameResult;
import academy.cashbackmerge.model.SimulationReport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

public class SimulationService {

    private static final int RATE_SCALE = 4;

    private final GameConfig gameConfig;

    public SimulationService(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public SimulationReport simulate(int totalGames, long seed) {
        if (totalGames <= 0) {
            throw new IllegalArgumentException("Количество партий должно быть положительным");
        }

        GameSimulator simulator = new GameSimulator(gameConfig);

        Map<Integer, Integer> cashbackDistribution = new TreeMap<>();
        Map<Integer, Integer> rewardDistribution = new TreeMap<>();

        int maxRewardGames = 0;
        long totalScore = 0;
        int maxScore = 0;

        for (int i = 0; i < totalGames; i++) {
            GameResult result = simulator.simulateGameResult(seed + i);

            cashbackDistribution.merge(result.maxCashback(), 1, Integer::sum);
            rewardDistribution.merge(result.reward(), 1, Integer::sum);

            totalScore += result.score();
            maxScore = Math.max(maxScore, result.score());

            if (result.maxCashback() >= gameConfig.maxRewardCashback()) {
                maxRewardGames++;
            }
        }

        BigDecimal maxRewardRate = BigDecimal.valueOf(maxRewardGames)
                .divide(BigDecimal.valueOf(totalGames), RATE_SCALE, RoundingMode.HALF_UP);

        BigDecimal allowedMaxRewardGames = gameConfig.maxRewardAllowedRate().multiply(BigDecimal.valueOf(totalGames));

        boolean constraintSatisfied = BigDecimal.valueOf(maxRewardGames).compareTo(allowedMaxRewardGames) <= 0;

        double averageScore = (double) totalScore / totalGames;

        return new SimulationReport(
                totalGames,
                gameConfig.movesLimit(),
                seed,
                "random",
                cashbackDistribution,
                rewardDistribution,
                maxRewardGames,
                maxRewardRate,
                constraintSatisfied,
                averageScore,
                maxScore);
    }
}
