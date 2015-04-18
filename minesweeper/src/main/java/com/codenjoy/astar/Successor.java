package com.codenjoy.astar;

import com.codenjoy.minesweeper.Action;

/**
 * Created by szelenin on 4/14/2015.
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

