package com.codenjoy.bomberman;

import com.codenjoy.bomberman.utils.Board;

/**
 * User: your name
 */
public class YourDirectionSolver implements DirectionSolver {

    @Override
    public String get(Board board) {
        return Action.ACT.toString();
    }
}
