package com.codenjoy.minesweeper;

/**
 * User: oleksandr.baglai
 */
public class Action {
    public static final Action UP = new Action(2, 0, -1, 1);
    public static final Action DOWN = new Action(3, 0, 1, 1);
    public static final Action LEFT = new Action(0, -1, 0, 1);
    public static final Action RIGHT = new Action(1, 1, 0, 1);
    public static final Action ACT = new Action(4, 0, 0, 10);

    final int value;
    protected final int dx;
    protected final int dy;
    public final int cost;

    Action(int value, int dx, int dy, int cost) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
        this.cost = cost;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public int changeX(int x) {
        return x + dx;
    }


    public int changeY(int y) {
        return y + dy;
    }

    public static Action[] values() {
        return new Action[]{UP,DOWN,LEFT,RIGHT,ACT};
    }

    public boolean isComposite() {
        return false;
    }
}
