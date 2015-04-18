package com.codenjoy.astar;

import com.codenjoy.minesweeper.Action;
import com.codenjoy.minesweeper.BoardTemperatures;
import com.codenjoy.minesweeper.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szelenin on 4/14/2015.
 */
public class Problem {
    private GameState startState;
    private Point target;
    private BoardTemperatures temperatures;

    public Problem(GameState startState, Point target, BoardTemperatures temperatures) {
        this.startState = startState;
        this.target = target;
        this.temperatures = temperatures;
    }

    public GameState getStartState() {
        return startState;
    }

    public boolean isGoalState(GameState state) {
        return state.getMinesweeper().equals(target);
    }

    public List<Successor> getSuccessors(GameState state) {
        ArrayList<Successor> successors = new ArrayList<>();
        List<Action> legalActions = state.getLegalActions();
        for (Action action : legalActions) {
            GameState successorState = state.generateSuccessorState(action);
            successors.add(new Successor(successorState, action,
                    (int) (1 + 100 * temperatures.temperatureAt(successorState.getMinesweeper().getX(), successorState.getMinesweeper().getY()))));

        }
        return successors;
    }
}
