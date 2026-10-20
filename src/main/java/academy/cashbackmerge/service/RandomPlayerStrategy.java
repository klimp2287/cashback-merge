package academy.cashbackmerge.service;

import academy.cashbackmerge.model.Move;
import java.util.Random;

public class RandomPlayerStrategy {

    private final Random random;

    public RandomPlayerStrategy(long seed) {
        this.random = new Random(seed);
    }

    public Move nextMove() {
        Move[] moves = Move.values();
        return moves[random.nextInt(moves.length)];
    }
}
