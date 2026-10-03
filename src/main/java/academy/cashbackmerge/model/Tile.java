package academy.cashbackmerge.model;

public record Tile(int cashback) {

    public Tile {
        if (!isPowerOfTwo(cashback)) {
            throw new IllegalArgumentException("Неверный кэшбэк");
        }
    }

    private static boolean isPowerOfTwo(int number) {
        if (number <= 0) {
            return false;
        }
        while (number % 2 == 0) {
            number /= 2;
        }
        return number == 1;
    }
}
