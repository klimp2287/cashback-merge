package academy.cashbackmerge.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.Move;
import academy.cashbackmerge.model.MoveResult;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;
import academy.cashbackmerge.service.MoveService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Обязательные тесты: правила слияния плиток. */
@DisplayName("MR2. Слияние плиток")
class MergeRulesTest {
    private static final GameConfig CONFIG = new GameConfig(3, List.of(new SpawnRule(1, new BigDecimal("1.0"))));

    @ParameterizedTest
    @CsvSource({
        "'1,1,.,.', '2,.,.,.'",
        "'1,1,1,.', '2,1,.,.'",
        "'1,1,1,1', '2,2,.,.'",
        "'2,2,4,4', '4,8,.,.'",
    })
    @DisplayName("Сдвиг влево: {0} -> {1}")
    void rowShiftsLeftAndMerges(String before, String expected) {
        Board board = new Board();

        String[] values = before.split(",");

        for (int x = 0; x < values.length; x++) {
            if (!values[x].equals(".")) {
                board.setTile(new Position(x, 0), new Tile(Integer.parseInt(values[x])));
            }
        }

        GameState gameState = new GameState(board, 0, 0, false);

        GameConfig config = new GameConfig(100, List.of(new SpawnRule(1, new BigDecimal("1.0"))));

        MoveService moveService = new MoveService(config, 42);

        MoveResult result = moveService.applyMove(gameState, Move.LEFT);

        assertRowEquals(result.gameState().board(), expected);
    }

    private void assertRowEquals(Board board, String expected) {
        String[] values = expected.split(",");

        for (int x = 0; x < values.length; x++) {
            Tile tile = board.getTile(new Position(x, 0));

            if (values[x].equals(".")) {
                assertNull(tile);
            } else {
                assertEquals(Integer.parseInt(values[x]), tile.cashback());
            }
        }
    }

    @Test
    @DisplayName("Одна плитка не сливается дважды за ход: [1,1,2,.] -> [2,2,.,.]")
    void tileDoesNotMergeTwiceInOneMove() {
        Board board = new Board();

        board.setTile(new Position(0, 0), new Tile(1));
        board.setTile(new Position(1, 0), new Tile(1));
        board.setTile(new Position(2, 0), new Tile(2));

        GameState gameState = new GameState(board, 0, 0, false);

        GameConfig config = new GameConfig(100, List.of(new SpawnRule(1, new BigDecimal("1.0"))));

        MoveService moveService = new MoveService(config, 42);

        MoveResult result = moveService.applyMove(gameState, Move.LEFT);

        assertEquals(2, result.gameState().board().getTile(new Position(0, 0)).cashback());

        assertEquals(2, result.gameState().board().getTile(new Position(1, 0)).cashback());

        assertEquals(2, result.score());
    }

    @Test
    @DisplayName("Слияние работает во всех четырёх направлениях")
    void mergeWorksInAllDirections() {
        // LEFT
        Board leftBoard = new Board();
        leftBoard.setTile(new Position(0, 0), new Tile(1));
        leftBoard.setTile(new Position(1, 0), new Tile(1));

        GameState leftState = new GameState(leftBoard, 0, 0, false);
        MoveResult leftResult = new MoveService(CONFIG, 42).applyMove(leftState, Move.LEFT);

        assertEquals(
                2, leftResult.gameState().board().getTile(new Position(0, 0)).cashback());

        // RIGHT
        Board rightBoard = new Board();
        rightBoard.setTile(new Position(2, 0), new Tile(1));
        rightBoard.setTile(new Position(3, 0), new Tile(1));

        GameState rightState = new GameState(rightBoard, 0, 0, false);
        MoveResult rightResult = new MoveService(CONFIG, 42).applyMove(rightState, Move.RIGHT);

        assertEquals(
                2, rightResult.gameState().board().getTile(new Position(3, 0)).cashback());

        // UP
        Board upBoard = new Board();
        upBoard.setTile(new Position(0, 0), new Tile(1));
        upBoard.setTile(new Position(0, 1), new Tile(1));

        GameState upState = new GameState(upBoard, 0, 0, false);
        MoveResult upResult = new MoveService(CONFIG, 42).applyMove(upState, Move.UP);

        assertEquals(2, upResult.gameState().board().getTile(new Position(0, 0)).cashback());

        // DOWN
        Board downBoard = new Board();
        downBoard.setTile(new Position(0, 2), new Tile(1));
        downBoard.setTile(new Position(0, 3), new Tile(1));

        GameState downState = new GameState(downBoard, 0, 0, false);
        MoveResult downResult = new MoveService(CONFIG, 42).applyMove(downState, Move.DOWN);

        assertEquals(
                2, downResult.gameState().board().getTile(new Position(0, 3)).cashback());
    }
}
