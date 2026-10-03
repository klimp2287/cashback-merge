package academy.cashbackmerge.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;
import academy.cashbackmerge.service.InitialStateFactory;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: воспроизводимость по seed. */
@DisplayName("MR1. Воспроизводимость по seed")
class SeedTest {
    private static final List<SpawnRule> VALID_RULES =
            List.of(new SpawnRule(1, new BigDecimal("0.85")), new SpawnRule(2, new BigDecimal("0.15")));

    @Test
    @DisplayName("Одинаковый конфиг и seed дают одинаковое начальное поле")
    void sameConfigAndSeedProduceSameInitialBoard() {
        GameConfig gameConfig = new GameConfig(5, VALID_RULES);

        InitialStateFactory factory = new InitialStateFactory();

        GameState firstGameState = factory.create(gameConfig, 42);
        GameState secondGameState = factory.create(gameConfig, 42);

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                Position position = new Position(x, y);

                Tile firstTile = firstGameState.board().getTile(position);
                Tile secondTile = secondGameState.board().getTile(position);

                if (firstTile == null || secondTile == null) {
                    assertEquals(firstTile, secondTile);
                } else {
                    assertEquals(firstTile.cashback(), secondTile.cashback());
                }
            }
        }
    }

    @Test
    @Disabled("MR3: реализуй тест и удали эту строку")
    @DisplayName("Одинаковый конфиг, seed и стратегия дают одинаковый отчёт симуляции")
    void sameInputsProduceSameSimulationReport() {
        fail("Тест не реализован");
    }
}
