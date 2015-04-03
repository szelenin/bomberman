package com.codenjoy.bomberman;

/**
 * User: oleksandr.baglai
 */
public enum Action {
    UP(2, 0, -1), DOWN(3, 0, 1), LEFT(0, -1, 0), RIGHT(1, 1, 0),  // direction of Bomberman
    ACT(4, 0, 0);                                                // stop the Bomberman

    final int value;
    private final int dx;
    private final int dy;

    Action(int value, int dx, int dy) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
    }

    public String toString() {
        return this.name();
    }

    public static Action valueOf(int i) {
        for (Action d : Action.values()) {
            if (d.value == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Action for " + i);
    }

    public int changeX(int x) {
        return x + dx;
    }


    public int changeY(int y) {
        return y + dy;
    }

}
