package academy.cashbackmerge.service;

import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Move;
import academy.cashbackmerge.model.Position;
import academy.cashbackmerge.model.Tile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardMover {

    private static final int MAX_CASHBACK = 32;

    private int moveScore;

    public int move(Board board, Move move) {
        moveScore = 0;

        switch (move) {
            case LEFT -> moveLeft(board);
            case RIGHT -> moveRight(board);
            case UP -> moveUp(board);
            case DOWN -> moveDown(board);
        }

        return moveScore;
    }

    private void moveLeft(Board board) {
        for (int y = 0; y < board.getHeight(); y++) {
            List<Tile> line = new ArrayList<>();

            for (int x = 0; x < board.getWidth(); x++) {
                line.add(board.getTile(new Position(x, y)));
            }

            List<Tile> movedLine = moveLine(line);

            for (int x = 0; x < board.getWidth(); x++) {
                board.setTile(new Position(x, y), movedLine.get(x));
            }
        }
    }

    private void moveRight(Board board) {
        for (int y = 0; y < board.getHeight(); y++) {
            List<Tile> line = new ArrayList<>();

            for (int x = 0; x < board.getWidth(); x++) {
                line.add(board.getTile(new Position(x, y)));
            }

            List<Tile> movedLine = moveLineRight(line);

            for (int x = 0; x < board.getWidth(); x++) {
                board.setTile(new Position(x, y), movedLine.get(x));
            }
        }
    }

    private void moveUp(Board board) {
        for (int x = 0; x < board.getWidth(); x++) {
            List<Tile> line = new ArrayList<>();

            for (int y = 0; y < board.getHeight(); y++) {
                line.add(board.getTile(new Position(x, y)));
            }

            List<Tile> movedLine = moveLine(line);

            for (int y = 0; y < board.getHeight(); y++) {
                board.setTile(new Position(x, y), movedLine.get(y));
            }
        }
    }

    private void moveDown(Board board) {
        for (int x = 0; x < board.getWidth(); x++) {
            List<Tile> line = new ArrayList<>();

            for (int y = 0; y < board.getHeight(); y++) {
                line.add(board.getTile(new Position(x, y)));
            }

            List<Tile> movedLine = moveLineRight(line);

            for (int y = 0; y < board.getHeight(); y++) {
                board.setTile(new Position(x, y), movedLine.get(y));
            }
        }
    }

    private List<Tile> moveLine(List<Tile> line) {
        List<Tile> notEmptyTiles = new ArrayList<>();

        for (Tile tile : line) {
            if (tile != null) {
                notEmptyTiles.add(tile);
            }
        }

        List<Tile> result = new ArrayList<>();

        int i = 0;

        while (i < notEmptyTiles.size()) {
            Tile currentTile = notEmptyTiles.get(i);

            if (i + 1 < notEmptyTiles.size()
                    && currentTile.cashback() == notEmptyTiles.get(i + 1).cashback()
                    && currentTile.cashback() < MAX_CASHBACK) {

                int cashback = currentTile.cashback() * 2;

                result.add(new Tile(cashback));
                moveScore += cashback;

                i += 2;
            } else {
                result.add(currentTile);
                i++;
            }
        }

        while (result.size() < line.size()) {
            result.add(null);
        }

        return result;
    }

    private List<Tile> moveLineRight(List<Tile> line) {
        List<Tile> reversedLine = new ArrayList<>(line);

        Collections.reverse(reversedLine);

        List<Tile> movedLine = moveLine(reversedLine);

        Collections.reverse(movedLine);

        return movedLine;
    }
}
