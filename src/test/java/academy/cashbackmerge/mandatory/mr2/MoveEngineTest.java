package academy.cashbackmerge.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

/** Обязательные тесты: движок ходов — что считается ходом и что после него происходит. */
@DisplayName("MR2. Движок ходов")
class MoveEngineTest {

    private static final GameConfig CONFIG = new GameConfig(3, List.of(new SpawnRule(1, new BigDecimal("1.0"))));

    @Test
    @DisplayName("Невалидный ход не меняет поле, не тратит ход и не создаёт плитку")
    void invalidMoveChangesNothing() {
        Board board = new Board();
        board.setTile(new Position(3, 0), new Tile(1));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.RIGHT);

        assertEquals(1, countTiles(result.gameState().board()));
        assertEquals(0, result.gameState().moves());
        assertEquals(0, result.gameState().score());
        assertEquals(false, result.gameState().gameOver());
    }

    @Test
    @DisplayName("После валидного хода появляется ровно одна новая плитка")
    void validMoveSpawnsExactlyOneTile() {
        Board board = new Board();
        board.setTile(new Position(0, 0), new Tile(1));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.RIGHT);

        assertEquals(2, countTiles(result.gameState().board()));
        assertEquals(1, result.gameState().moves());
    }

    @Test
    @DisplayName("После N валидных ходов игра завершена")
    void gameEndsAfterMovesLimit() {
        Board board = new Board();
        board.setTile(new Position(0, 0), new Tile(1));

        GameState gameState = new GameState(board, 2, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.RIGHT);

        assertEquals(3, result.gameState().moves());
        assertEquals(true, result.gameState().gameOver());
    }

    @Test
    @DisplayName("Достижение плитки 32% не заканчивает игру — доигрываем до лимита ходов")
    void reachingMaxTileDoesNotEndGame() {
        Board board = new Board();

        board.setTile(new Position(0, 0), new Tile(16));
        board.setTile(new Position(1, 0), new Tile(16));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.LEFT);

        assertEquals(32, result.gameState().board().getTile(new Position(0, 0)).cashback());

        assertEquals(false, result.gameState().gameOver());
    }

    @Test
    @DisplayName("Score считается по правилам и растёт только при слияниях")
    void scoreIsCalculatedOnMerges() {
        Board board = new Board();

        board.setTile(new Position(0, 0), new Tile(1));
        board.setTile(new Position(1, 0), new Tile(1));
        board.setTile(new Position(2, 0), new Tile(2));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.LEFT);

        assertEquals(2, result.score());
        assertEquals(2, result.gameState().score());
    }

    private int countTiles(Board board) {
        int count = 0;

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (!board.isEmpty(new Position(x, y))) {
                    count++;
                }
            }
        }

        return count;
    }
}
