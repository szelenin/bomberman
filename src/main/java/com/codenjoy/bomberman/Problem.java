package com.codenjoy.bomberman;

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
        if (action != Action.STOP) {
            totalCost++;
        }
        if (newState.isDead()) {
            totalCost += 1000;
        }

        List<ElementState> choppers = newState.getChoppers();
        for (ElementState chopper : choppers) {
            if (chopper.isDead()) {
                totalCost -= 500;
            }
        }

        return totalCost;
    }

    public boolean isGoalState(GameState state) {
        List<ElementState> choppers = state.getChoppers();
        for (ElementState chopper : choppers) {
            if (chopper.isDead()) {
                return true;
            }
        }
        return false;
    }

    public GameState getStartState() {
        return startState;
    }
}
