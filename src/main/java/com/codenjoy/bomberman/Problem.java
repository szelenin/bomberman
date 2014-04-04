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
            totalCost += divideOnSquareDistanceTo(nearestBomb, Utils.STAY_ON_BOMB_REVENUE) * ('6' - nearestBomb.getValue0().state.getChar());
        }

        //penalty for setting bomb
        Pair<ElementState, Integer> nearestChopper = Utils.nearestChopper(newState);
        if (nearestChopper != null) {
            totalCost += (double) nearestChopper.getValue1() / (newState.getBoardSize() * 2)  * Utils.CHOPPER_NEAR_REVENUE;
        }

        //penalty to being close to chopper
//        totalCost += divideOnSquareDistanceTo(nearestChopper, Utils.CHOPPER_NEAR_REVENUE);
        return totalCost;
    }

    private double divideOnSquareDistanceTo(Pair<ElementState, Integer> nearestBomb, double penalty) {
        return penalty / Math.pow(nearestBomb.getValue1() + 1, 2);
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
