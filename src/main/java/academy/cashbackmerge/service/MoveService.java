package academy.cashbackmerge.service;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.Move;
import academy.cashbackmerge.model.MoveResult;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;

public class MoveService {

    private final GameConfig gameConfig;
    private final TileSpawner tileSpawner;
    private final BoardMover boardMover;

    public MoveService(GameConfig gameConfig, long seed) {
        this.gameConfig = gameConfig;
        this.tileSpawner = new TileSpawner(gameConfig, seed);
        this.boardMover = new BoardMover();
    }

    public MoveResult applyMove(GameState gameState, Move move) {
        Board board = gameState.board().copy();

        int moveScore = boardMover.move(board, move);

        boolean boardChanged = !isBoardsEqual(gameState.board(), board);

        if (boardChanged) {
            tileSpawner.spawn(board);
        }

        int moves = gameState.moves() + (boardChanged ? 1 : 0);
        int score = gameState.score() + moveScore;
        boolean gameOver = moves >= gameConfig.movesLimit();

        GameState resultState = new GameState(board, moves, score, gameOver);

        return new MoveResult(resultState, boardChanged, moveScore);
    }

    private boolean isBoardsEqual(Board first, Board second) {
        for (int x = 0; x < first.getWidth(); x++) {
            for (int y = 0; y < first.getHeight(); y++) {
                Tile firstTile = first.getTile(new Position(x, y));
                Tile secondTile = second.getTile(new Position(x, y));

                if (firstTile == null && secondTile == null) {
                    continue;
                }

                if (firstTile == null || secondTile == null) {
                    return false;
                }

                if (firstTile.cashback() != secondTile.cashback()) {
                    return false;
                }
            }
        }

        return true;
    }
}
