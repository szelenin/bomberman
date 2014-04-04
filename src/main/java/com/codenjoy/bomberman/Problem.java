package com.codenjoy.bomberman;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szelenin on 3/28/14.
 */
public class Problem {
    private GameState startState;

    public Problem(GameState startState) {
        this.startState = startState;
    }

    public List<Successor> getSuccessors(GameState state) {
        if (state.isDead()) {
            return new ArrayList<Successor>();
        }
        ArrayList<Successor> successors = new ArrayList<Successor>();
        List<Action> actions = state.getLegalActions();
        for (Action action : actions) {
            GameState newState = state.generateSuccessor(action);
            successors.add(new Successor(newState, action, cost(action, newState)));
        }
        return successors;
    }

    private int cost(Action action, GameState newState) {
        int totalCost = 0;
        // cost of move
        if (action != Action.STOP) {
            totalCost++;
        }
        // dead bomber
        if (newState.isDead()) {
            totalCost += Utils.DEAD_BOMBER_REVENUE;
        }
        Pair<ElementState, Integer> nearestBomb = Utils.nearestBomb(newState);
        boolean bombIsSet = nearestBomb != null;
        //if bomb is set then penalty for being close to bomb
        if (bombIsSet) {
            totalCost += (double) Utils.STAY_ON_BOMB_REVENUE / Math.pow(nearestBomb.getValue1() + 1, 2) * ('6' - nearestBomb.getValue0().state.getChar());
        }

        //penalty for setting bomb
        Pair<ElementState, Integer> nearestChopper = Utils.nearestChopper(newState);
        if (nearestChopper != null) {
            totalCost += (double) nearestChopper.getValue1() / (newState.getBoardSize() * 2)  * Utils.CHOPPER_NEAR_REVENUE;
        }
/*
        // dead chopper
        List<ElementState> choppers = newState.getChoppers();
        for (ElementState chopper : choppers) {
            if (chopper.isDead()) {
                totalCost += Utils.DEAD_CHOPPER_REVENUE;
            }
        }
*/

        return totalCost;
    }

    public boolean isGoalState(GameState state) {
        List<ElementState> choppers = state.getChoppers();
        for (ElementState chopper : choppers) {
            if (chopper.isDead() && !state.isDead()) {
                return true;
            }
        }
        return false;
    }

    public GameState getStartState() {
        return startState;
    }
}
