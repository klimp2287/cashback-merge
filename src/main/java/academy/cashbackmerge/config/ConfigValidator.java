package academy.cashbackmerge.config;

import java.math.BigDecimal;
import java.util.List;

public class ConfigValidator {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal ONE = BigDecimal.ONE;

    public void validate(GameConfig config) {
        ensureNotNullConfig(config);
        ensureNotNullSpawnRules(config.spawnRules());

        ensurePositiveMovesLimit(config.movesLimit());
        ensureNotEmptySpawnRules(config.spawnRules());
        ensureValidProbability(config.spawnRules());
        ensureSumProbabilityIsOne(config.spawnRules());
        ensureValidCashback(config.spawnRules());

        ensureValidRewardTable(config.rewardTable());
        ensureValidMaxRewardCashback(config);
        ensureValidMaxRewardAllowedRate(config);
    }

    private void ensurePositiveMovesLimit(int movesLimit) {
        if (movesLimit <= 0) {
            throw new IllegalArgumentException("Число ходов не положительное");
        }
    }

    private void ensureNotEmptySpawnRules(List<SpawnRule> rules) {
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("Пустой список правил");
        }
    }

    private void ensureValidProbability(List<SpawnRule> rules) {
        rules.forEach(rule -> {
            if (rule.probability().compareTo(ZERO) < 0 || rule.probability().compareTo(ONE) > 0) {
                throw new IllegalArgumentException("Вероятность должна быть от 0 до 1");
            }
        });
    }

    private void ensureSumProbabilityIsOne(List<SpawnRule> rules) {
        BigDecimal sum = rules.stream().map(SpawnRule::probability).reduce(ZERO, BigDecimal::add);

        if (sum.compareTo(ONE) != 0) {
            throw new IllegalArgumentException("Сумма не равна 1");
        }
    }

    private void ensureValidCashback(List<SpawnRule> rules) {
        rules.forEach(rule -> {
            int cashback = rule.cashback();
            if (cashback <= 0) {
                throw new IllegalArgumentException("Неправильный кэшбэк");
            }
            while (cashback % 2 == 0) {
                cashback /= 2;
            }
            if (cashback != 1) {
                throw new IllegalArgumentException("Неправильный кэшбэк");
            }
        });
    }

    private void ensureNotNullConfig(GameConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Пустой конфиг");
        }
    }

    private void ensureNotNullSpawnRules(List<SpawnRule> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("Пустой конфиг");
        }
    }

    private void ensureValidRewardTable(List<RewardRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return;
        }

        for (RewardRule rule : rules) {
            if (rule.cashback() <= 0 || rule.reward() < 0) {
                throw new IllegalArgumentException("Некорректное правило награды");
            }

            ensurePowerOfTwo(rule.cashback());
        }
    }

    private void ensureValidMaxRewardCashback(GameConfig config) {
        if (config.maxRewardCashback() == 0) {
            return;
        }

        if (config.maxRewardCashback() < 0) {
            throw new IllegalArgumentException("Некорректный максимальный кэшбэк");
        }

        ensurePowerOfTwo(config.maxRewardCashback());
    }

    private void ensureValidMaxRewardAllowedRate(GameConfig config) {
        BigDecimal rate = config.maxRewardAllowedRate();

        if (rate == null) {
            return;
        }

        if (rate.compareTo(ZERO) < 0 || rate.compareTo(ONE) > 0) {
            throw new IllegalArgumentException("Допустимый процент наград должен быть от 0 до 1");
        }
    }

    private void ensurePowerOfTwo(int value) {
        if (value <= 0 || (value & (value - 1)) != 0) {
            throw new IllegalArgumentException("Кэшбэк должен быть положительной степенью двойки");
        }
    }
}
