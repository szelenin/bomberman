package com.codenjoy.astar;

import java.util.List;

/**
 * Created by szelenin on 4/20/2015.
 */
public interface Problem {
    GameState getStartState();

    boolean isGoalState(GameState state);

    List<Successor> getSuccessors(GameState state);
}
