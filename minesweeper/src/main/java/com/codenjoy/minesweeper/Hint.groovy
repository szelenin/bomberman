package com.codenjoy.minesweeper;

/**
 * Created by szelenin on 4/3/2015.
 */
public class Hint {
    private int hint;
    private Point point;

    public Hint(int hint, Point point) {
        this.hint = hint;
        this.point = point;
    }

    @Override
    public String toString() {
        "$hint,[$point.x,$point.y]"
    }
}
