package academy.cashbackmerge.mandatory.mr3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.RewardRule;
import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.SimulationReport;
import academy.cashbackmerge.model.Tile;
import academy.cashbackmerge.service.RewardService;
import academy.cashbackmerge.service.SimulationService;
import academy.cashbackmerge.service.WeightedTileSelector;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: награды, симуляция и отчёт. */
@DisplayName("MR3. Награды и симуляция")
class RewardAndSimulationTest {

    @Test
    @DisplayName("Максимальная плитка 16% даёт соответствующую награду")
    void rewardMatchesHighestTile() {
        GameConfig config = new GameConfig(
                100,
                List.of(new SpawnRule(1, new BigDecimal("1.0"))),
                List.of(
                        new RewardRule(2, 0),
                        new RewardRule(4, 50),
                        new RewardRule(8, 150),
                        new RewardRule(16, 500),
                        new RewardRule(32, 2000)),
                32,
                new BigDecimal("0.20"));

        Board board = new Board();
        board.setTile(new Position(0, 0), new Tile(4));
        board.setTile(new Position(1, 0), new Tile(8));
        board.setTile(new Position(2, 0), new Tile(16));

        GameState state = new GameState(board, 10, 0, false);

        RewardService rewardService = new RewardService(config);

        assertEquals(500, rewardService.calculateReward(state));
    }

    @Test
    @DisplayName("Weighted random учитывает вероятности из конфига")
    void spawnRespectsConfiguredProbabilities() {
        GameConfig config = new GameConfig(
                100,
                List.of(new SpawnRule(1, new BigDecimal("0.85")), new SpawnRule(2, new BigDecimal("0.15"))),
                List.of(
                        new RewardRule(2, 0),
                        new RewardRule(4, 50),
                        new RewardRule(8, 150),
                        new RewardRule(16, 500),
                        new RewardRule(32, 2000)),
                32,
                new BigDecimal("0.20"));

        WeightedTileSelector selector = new WeightedTileSelector(config);
        Random random = new Random(42);

        int total = 10_000;
        int countOfOnes = 0;

        for (int i = 0; i < total; i++) {
            if (selector.select(random).cashback() == 1) {
                countOfOnes++;
            }
        }

        double actualRate = (double) countOfOnes / total;

        assertEquals(0.85, actualRate, 0.02);
    }

    @Test
    @DisplayName("Отчёт содержит распределение максимальных плиток и наград")
    void reportContainsDistributions() {
        GameConfig config = new GameConfig(
                100,
                List.of(new SpawnRule(1, new BigDecimal("0.85")), new SpawnRule(2, new BigDecimal("0.15"))),
                List.of(
                        new RewardRule(2, 0),
                        new RewardRule(4, 50),
                        new RewardRule(8, 150),
                        new RewardRule(16, 500),
                        new RewardRule(32, 2000)),
                32,
                new BigDecimal("0.20"));

        int totalGames = 100;
        SimulationService simulationService = new SimulationService(config);

        SimulationReport report = simulationService.simulate(totalGames, 42);

        assertEquals(totalGames, report.totalGames());

        int cashbackGames = report.maxCashbackDistribution().values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        int rewardGames = report.rewardDistribution().values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        assertEquals(totalGames, cashbackGames);
        assertEquals(totalGames, rewardGames);
    }

    @Test
    @DisplayName("Отчёт содержит долю игроков с максимальной наградой")
    void reportContainsMaxRewardRate() {
        GameConfig config = new GameConfig(
                100,
                List.of(new SpawnRule(1, new BigDecimal("0.85")), new SpawnRule(2, new BigDecimal("0.15"))),
                List.of(
                        new RewardRule(2, 0),
                        new RewardRule(4, 50),
                        new RewardRule(8, 150),
                        new RewardRule(16, 500),
                        new RewardRule(32, 2000)),
                32,
                new BigDecimal("0.20"));

        int totalGames = 100;
        SimulationService simulationService = new SimulationService(config);

        SimulationReport report = simulationService.simulate(totalGames, 42);

        BigDecimal expectedRate = BigDecimal.valueOf(report.maxRewardGames())
                .divide(BigDecimal.valueOf(totalGames), 4, RoundingMode.HALF_UP);

        assertEquals(totalGames, report.totalGames());
        assertEquals(expectedRate, report.maxRewardRate());
    }

    @Test
    @DisplayName("Статус OK, если actualMaxRewardRate <= maxRewardAllowedRate, иначе FAILED")
    void reportStatusReflectsBusinessConstraint() {
        GameConfig config = new GameConfig(
                100,
                List.of(new SpawnRule(1, new BigDecimal("0.85")), new SpawnRule(2, new BigDecimal("0.15"))),
                List.of(
                        new RewardRule(2, 0),
                        new RewardRule(4, 50),
                        new RewardRule(8, 150),
                        new RewardRule(16, 500),
                        new RewardRule(32, 2000)),
                32,
                new BigDecimal("0.20"));

        SimulationService simulationService = new SimulationService(config);

        SimulationReport report = simulationService.simulate(100, 42);

        boolean expectedStatus = report.maxRewardRate().compareTo(config.maxRewardAllowedRate()) <= 0;

        assertEquals(expectedStatus, report.businessConstraintSatisfied());
    }
}
