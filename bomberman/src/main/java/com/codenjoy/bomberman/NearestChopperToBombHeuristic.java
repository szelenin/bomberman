package com.codenjoy.bomberman;

import org.javatuples.Pair;

/**
 * Created by szelenin on 4/1/14.
 */
public class NearestChopperToBombHeuristic implements Heuristic {
    @Override
    public int calculate(GameState state) {
        Pair<ElementState,Integer> nearestBomb = Utils.nearestBomb(state);
        if (nearestBomb != null) {
            return -(Utils.minDistToElement(state, state.getBombs()).getValue1() + 1);
        }
        return Utils.distToclosestChopper(state);
    }

}
