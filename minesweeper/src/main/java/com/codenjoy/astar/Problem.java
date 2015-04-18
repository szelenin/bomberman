package com.codenjoy.astar;

import java.util.List;

/**
 * Created by szelenin on 4/14/2015.
 */
public class Problem {
    private GameState startState;

    public GameState getStartState() {
        return startState;
    }

    public boolean isGoalState(GameState state) {
        return false;
    }

    public List<Successor> getSuccessors(GameState state) {
        return null;
    }
}
