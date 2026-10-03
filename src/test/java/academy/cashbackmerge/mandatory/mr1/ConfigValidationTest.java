package academy.cashbackmerge.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import academy.cashbackmerge.config.ConfigValidator;
import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.SpawnRule;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: конфиг кампании. */
@DisplayName("MR1. Конфиг кампании")
class ConfigValidationTest {
    private static final ConfigValidator CONFIG_VALIDATOR = new ConfigValidator();

    private static final List<SpawnRule> VALID_RULES =
            List.of(new SpawnRule(1, new BigDecimal("0.85")), new SpawnRule(2, new BigDecimal("0.15")));

    private static final List<SpawnRule> INVALID_RULES =
            List.of(new SpawnRule(1, new BigDecimal("0.85")), new SpawnRule(2, new BigDecimal("0.16")));

    @Test
    @DisplayName("Валидный конфиг принимается")
    void validConfigIsAccepted() {
        GameConfig config = new GameConfig(5, VALID_RULES);
        CONFIG_VALIDATOR.validate(config);
    }

    @Test
    @DisplayName("Сумма вероятностей spawn-правил не равна 1 — конфиг отклоняется")
    void probabilitiesNotSummingToOneAreRejected() {
        GameConfig config = new GameConfig(5, INVALID_RULES);

        assertThrows(IllegalArgumentException.class, () -> CONFIG_VALIDATOR.validate(config));
    }

    @Test
    @DisplayName("movesLimit <= 0 — конфиг отклоняется")
    void nonPositiveMovesLimitIsRejected() {
        GameConfig config = new GameConfig(0, VALID_RULES);

        assertThrows(IllegalArgumentException.class, () -> CONFIG_VALIDATOR.validate(config));
    }

    @Test
    @DisplayName("Пустой список spawn-правил — конфиг отклоняется")
    void emptySpawnRulesAreRejected() {
        List<SpawnRule> rules = List.of();
        GameConfig config = new GameConfig(5, rules);

        assertThrows(IllegalArgumentException.class, () -> CONFIG_VALIDATOR.validate(config));
    }
}
