package academy.cashbackmerge.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.GameConfigReader;
import java.math.BigDecimal;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MR1. Конфиг кампании")
class GameConfigReaderTest {

    @Test
    @DisplayName("Корректный конфиг читается из JSON")
    void readsConfigFromJson() {
        GameConfigReader reader = new GameConfigReader();

        Path path = Path.of("src/test/resources/config/game-config.json");

        GameConfig config = reader.read(path);

        assertEquals(5, config.movesLimit());
        assertEquals(2, config.spawnRules().size());

        assertEquals(1, config.spawnRules().get(0).cashback());
        assertEquals(new BigDecimal("0.85"), config.spawnRules().get(0).probability());

        assertEquals(2, config.spawnRules().get(1).cashback());
        assertEquals(new BigDecimal("0.15"), config.spawnRules().get(1).probability());
    }
}
