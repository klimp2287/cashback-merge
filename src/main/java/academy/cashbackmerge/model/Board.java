package academy.cashbackmerge.model;

public class Board {

    public static final int BOARD_SIZE = 4;

    private final Tile[][] tiles = new Tile[BOARD_SIZE][BOARD_SIZE];

    public Tile getTile(Position position) {
        checkPosition(position);
        return tiles[position.x()][position.y()];
    }

    public void setTile(Position position, Tile tile) {
        checkPosition(position);
        tiles[position.x()][position.y()] = tile;
    }

    private void checkPosition(Position position) {
        if (position.x() < 0 || position.x() >= BOARD_SIZE || position.y() < 0 || position.y() >= BOARD_SIZE) {
            throw new IllegalArgumentException("Position is out of board");
        }
    }

    public boolean isEmpty(Position position) {
        return getTile(position) == null;
    }

    public int getWidth() {
        return BOARD_SIZE;
    }

    public int getHeight() {
        return BOARD_SIZE;
    }
}
