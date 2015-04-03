package com.codenjoy.bomberman;

/**
 * Created by szelenin on 4/1/14.
 */
public class Successor {
    public final GameState state;
    public final Action action;
    public final int cost;

    public Successor(GameState state, Action action, int cost) {
        this.state = state;
        this.action = action;
        this.cost = cost;
    }
}
