package com.codenjoy.astar;

import com.codenjoy.minesweeper.Action;
import com.codenjoy.minesweeper.BoardTemperatures;
import com.codenjoy.minesweeper.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szelenin on 4/20/2015.
 */
public class SetFlagProblem implements Problem {
    private GameState startState;
    private BoardTemperatures temperatures;
    private Point flag;

    public SetFlagProblem(GameState startState, BoardTemperatures temperatures, Point flag) {
        this.startState = startState;
        this.temperatures = temperatures;
        this.flag = flag;
    }

    @Override
    public GameState getStartState() {
        return startState;
    }

    @Override
    public boolean isGoalState(GameState state) {
        return state.getFlag()!=null && state.getFlag().equals(flag);
    }

    @Override
    public List<Successor> getSuccessors(GameState state) {
        ArrayList<Successor> successors = new ArrayList<>();
        List<Action> legalActions = state.getLegalActions();
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
