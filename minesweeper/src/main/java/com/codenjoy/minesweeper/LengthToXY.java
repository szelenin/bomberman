package com.codenjoy.minesweeper;

/**
 * User: oleksandr.baglai
 */
public class LengthToXY {
    public final int boardSize;

    public LengthToXY(int boardSize) {
        this.boardSize = boardSize;
    }

    public Point getXY(int length) {
        if (length == -1) {
            return null;
        }
        return new Point(length % boardSize, length / boardSize);
    }

    public int getLength(int x, int y) {
        return (y)*boardSize + x;
    }

    public int getLength(Point point) {
        return (point.getY())*boardSize + point.getX();
    }
}