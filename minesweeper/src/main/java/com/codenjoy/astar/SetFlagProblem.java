package com.codenjoy.astar;

import com.codenjoy.minesweeper.Action;
import com.codenjoy.minesweeper.BoardTemperatures;
import com.codenjoy.minesweeper.FlagAction;
import com.codenjoy.minesweeper.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szelenin on 4/20/2015.
 */
public class SetFlagProblem extends FindRouteProblem {

    public SetFlagProblem(GameState startState, BoardTemperatures temperatures, Point target) {
        super(startState, target, temperatures);
    }

    @Override
    public boolean isGoalState(GameState state) {
        return state.getFlag()!=null && state.getFlag().equals(target);
    }

    @Override
    public List<Successor> getSuccessors(GameState state) {
        ArrayList<Action> actions = new ArrayList<>();
        actions.addAll(state.getLegalActions());
        for (Action action : state.getLegalActions()) {
            actions.add(new FlagAction(action));
        }
        return super.getSuccessors(state, actions);
    }
}
