package academy.cashbackmerge.service;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.GameResult;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.MoveResult;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;

public class GameSimulator {

    private final GameConfig gameConfig;

    public GameSimulator(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public int simulateGame(long seed) {
        InitialStateFactory initialStateFactory = new InitialStateFactory();
        GameState state = initialStateFactory.create(gameConfig, seed);

        MoveService moveService = new MoveService(gameConfig, seed + 1);
        RandomPlayerStrategy strategy = new RandomPlayerStrategy(seed + 2);

        while (!state.gameOver() && moveService.hasAvailableMove(state)) {
            MoveResult result = moveService.applyMove(state, strategy.nextMove());
            state = result.gameState();
        }

        RewardService rewardService = new RewardService(gameConfig);
        return rewardService.calculateReward(state);
    }

    public GameResult simulateGameResult(long seed) {
        InitialStateFactory initialStateFactory = new InitialStateFactory();
        GameState state = initialStateFactory.create(gameConfig, seed);

        MoveService moveService = new MoveService(gameConfig, seed + 1);
        RandomPlayerStrategy strategy = new RandomPlayerStrategy(seed + 2);

        while (!state.gameOver() && moveService.hasAvailableMove(state)) {
            MoveResult result = moveService.applyMove(state, strategy.nextMove());
            state = result.gameState();
        }

        RewardService rewardService = new RewardService(gameConfig);
        int reward = rewardService.calculateReward(state);
        int maxCashback = findMaxCashback(state.board());

        return new GameResult(maxCashback, reward, state.moves(), state.score());
    }

    private int findMaxCashback(Board board) {
        int maxCashback = 0;

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Tile tile = board.getTile(new Position(x, y));

                if (tile != null && tile.cashback() > maxCashback) {
                    maxCashback = tile.cashback();
                }
            }
        }

        return maxCashback;
    }
}
