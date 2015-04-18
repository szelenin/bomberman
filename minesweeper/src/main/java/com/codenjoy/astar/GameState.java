package com.codenjoy.astar;

import com.codenjoy.minesweeper.Action;
import com.codenjoy.minesweeper.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szelenin on 4/14/2015.
 */
public class GameState {
    private Point minesweeper;
    private double[][] temperatures;

    public GameState(Point minesweeper, double[][] temperatures) {

        this.minesweeper = minesweeper;
        this.temperatures = temperatures;
    }

    public List<Action> getLegalActions() {
        ArrayList<Action> actions = new ArrayList<>();

        for (Action action : Action.values()) {
            if (action == Action.ACT) {
                continue;
            }
            int x = action.changeX(minesweeper.getX());
            int y = action.changeY(minesweeper.getY());
            if (temperatures[x][y] < 100) {
                actions.add(action);
            }
        }
        return actions;
    }

    public Point getMinesweeper() {
        return minesweeper;
    }

    public GameState generateSuccessorState(Action action) {
        int newX = action.changeX(minesweeper.getX());
        int newY = action.changeY(minesweeper.getY());

        return new GameState(new Point(newX, newY), temperatures);
    }
}