package com.codenjoy.bomberman;

import java.util.List;

/**
* Created by szelenin on 4/1/14.
*/
class NearestChopperToBombHeuristic implements Heuristic {
    @Override
    public int calculate(GameState state) {
        //bomb is set
        List<ElementState> bombs = state.getBombs();
        if (bombs.isEmpty()) {
            int minDist = Integer.MAX_VALUE;
            for (ElementState chopper : state.getChoppers()) {
                minDist = Math.min(dist(state, chopper), minDist);
            }
            return 5 + minDist;
        }
        return bombs.get(0).state.getChar() - '0';
    }

    private int dist(GameState state, ElementState chopper) {
        return Math.abs(chopper.position.getX() - state.getBomber().getX()) + Math.abs(chopper.position.getY() - state.getBomber().getY());
    }
}
