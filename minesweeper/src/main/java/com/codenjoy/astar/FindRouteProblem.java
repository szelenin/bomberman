package com.codenjoy.astar;

import com.codenjoy.minesweeper.Action;
import com.codenjoy.minesweeper.BoardTemperatures;
import com.codenjoy.minesweeper.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szelenin on 4/14/2015.
 */
public class FindRouteProblem implements Problem {
    private GameState startState;
    protected Point target;
    private BoardTemperatures temperatures;

    public FindRouteProblem(GameState startState, Point target, BoardTemperatures temperatures) {
        this.startState = startState;
        this.target = target;
        this.temperatures = temperatures;
    }

    @Override
    public GameState getStartState() {
        return startState;
    }

    @Override
    public boolean isGoalState(GameState state) {
        return state.getMinesweeper().equals(target);
    }

    @Override
    public List<Successor> getSuccessors(GameState state) {
        List<Action> legalActions = state.getLegalActions();
        return getSuccessors(state, legalActions);
    }

    protected ArrayList<Successor> getSuccessors(GameState state, List<Action> legalActions) {
        ArrayList<Successor> successors = new ArrayList<>();
        for (Action action : legalActions) {
            GameState successorState = state.generateSuccessorState(action);
            Point minesweeper = successorState.getMinesweeper();
            successors.add(new Successor(successorState, action,
                    (int) (action.cost + 100 * temperatures.temperatureAt(minesweeper.getX(),
                            minesweeper.getY()))));

        }
        return successors;
    }
}
