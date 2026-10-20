package academy.cashbackmerge.service;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.RewardRule;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;

public class RewardService {

    private final GameConfig gameConfig;

    public RewardService(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public int calculateReward(GameState state) {
        int maxCashback = findMaxCashback(state.board());

        return gameConfig.rewardTable().stream()
                .filter(rule -> rule.cashback() == maxCashback)
                .mapToInt(RewardRule::reward)
                .findFirst()
                .orElse(0);
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
