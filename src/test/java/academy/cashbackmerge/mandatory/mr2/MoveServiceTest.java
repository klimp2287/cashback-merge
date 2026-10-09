package academy.cashbackmerge.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.junit.jupiter.api.Test;

class MoveServiceTest {

    private static final GameConfig CONFIG = new GameConfig(10, List.of(new SpawnRule(1, new BigDecimal("1.0"))));

    @Test
    void moveLeftMergesEqualTiles() {
        Board board = new Board();

        board.setTile(new Position(0, 0), new Tile(1));
        board.setTile(new Position(2, 0), new Tile(1));
        board.setTile(new Position(3, 0), new Tile(2));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.LEFT);

        Board resultBoard = result.gameState().board();

        assertEquals(2, resultBoard.getTile(new Position(0, 0)).cashback());
        assertEquals(2, resultBoard.getTile(new Position(1, 0)).cashback());
        assertNull(resultBoard.getTile(new Position(2, 0)));
        assertNull(resultBoard.getTile(new Position(3, 0)));
    }

    @Test
    void moveRightMergesEqualTiles() {
        Board board = new Board();

        board.setTile(new Position(0, 0), new Tile(1));
        board.setTile(new Position(2, 0), new Tile(1));
        board.setTile(new Position(3, 0), new Tile(2));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.RIGHT);

        Board resultBoard = result.gameState().board();

        assertEquals(2, resultBoard.getTile(new Position(2, 0)).cashback());
        assertEquals(2, resultBoard.getTile(new Position(3, 0)).cashback());
        assertNull(resultBoard.getTile(new Position(0, 0)));
        assertNull(resultBoard.getTile(new Position(1, 0)));
    }

    @Test
    void moveUpMergesEqualTiles() {
        Board board = new Board();

        board.setTile(new Position(0, 0), new Tile(1));
        board.setTile(new Position(0, 2), new Tile(1));
        board.setTile(new Position(0, 3), new Tile(2));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.UP);

        Board resultBoard = result.gameState().board();

        assertEquals(2, resultBoard.getTile(new Position(0, 0)).cashback());
        assertEquals(2, resultBoard.getTile(new Position(0, 1)).cashback());
        assertNull(resultBoard.getTile(new Position(0, 2)));
        assertNull(resultBoard.getTile(new Position(0, 3)));
    }

    @Test
    void moveDownMergesEqualTiles() {
        Board board = new Board();

        board.setTile(new Position(0, 0), new Tile(1));
        board.setTile(new Position(0, 2), new Tile(1));
        board.setTile(new Position(0, 3), new Tile(2));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.DOWN);

        Board resultBoard = result.gameState().board();

        assertEquals(2, resultBoard.getTile(new Position(0, 2)).cashback());
        assertEquals(2, resultBoard.getTile(new Position(0, 3)).cashback());
        assertNull(resultBoard.getTile(new Position(0, 0)));
        assertNull(resultBoard.getTile(new Position(0, 1)));
    }

    @Test
    void moveIsChangedWhenTilesMove() {
        Board board = new Board();

        board.setTile(new Position(0, 0), new Tile(1));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.RIGHT);

        assertTrue(result.boardChanged());
    }

    @Test
    void moveIsNotChangedWhenTilesCannotMove() {
        Board board = new Board();

        board.setTile(new Position(3, 0), new Tile(1));

        GameState gameState = new GameState(board, 0, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.RIGHT);

        assertFalse(result.boardChanged());
    }

    @Test
    void validMoveIncreasesMoves() {
        Board board = new Board();

        board.setTile(new Position(0, 0), new Tile(1));

        GameState gameState = new GameState(board, 3, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.RIGHT);

        assertEquals(4, result.gameState().moves());
    }

    @Test
    void invalidMoveDoesNotIncreaseMoves() {
        Board board = new Board();

        board.setTile(new Position(3, 0), new Tile(1));

        GameState gameState = new GameState(board, 3, 0, false);

        MoveService moveService = new MoveService(CONFIG, 42);

        MoveResult result = moveService.applyMove(gameState, Move.RIGHT);

        assertEquals(3, result.gameState().moves());
    }
}
