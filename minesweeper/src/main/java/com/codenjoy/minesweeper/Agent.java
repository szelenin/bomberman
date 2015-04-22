package com.codenjoy.minesweeper;

import com.codenjoy.astar.AStarSearch;
import com.codenjoy.astar.GameState;
import com.codenjoy.astar.Heuristic;
import com.codenjoy.astar.FindRouteProblem;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by szelenin on 4/13/2015.
 */
public class Agent {

    Action getAction(BoardTemperatures temperatures, Board board) {
        PriorityQueue<CellTemperature> lowestTemps = new PriorityQueue<>((CellTemperature c1, CellTemperature c2) -> (int) (c1.getTemperature() * 100 - c2.getTemperature() * 100));
        PriorityQueue<CellTemperature> highestTemps = new PriorityQueue<>((CellTemperature c1, CellTemperature c2) -> (int) (c2.getTemperature() * 100 - c1.getTemperature() * 100));

        temperatures.traverseTemperatures((int x, int y, double temperature) -> {
            CellTemperature cellTemperature = new CellTemperature(new Point(x, y), temperature);
            if (temperature > 0 && new Point(x, y) != board.getMinesweeper()) {
                lowestTemps.add(cellTemperature);
            }
            highestTemps.add(cellTemperature);
        });

        CellTemperature first = lowestTemps.peek();

        List<CellTemperature> filteredLowestTemps = lowestTemps.stream()
                .filter((CellTemperature it) -> (
                        Math.abs(it.getTemperature() - first.getTemperature()) < 0.000001))
                .collect(Collectors.toList());


        PriorityQueue<Pair<List<Action>, Integer>> paths =
                new PriorityQueue<>((path1, path2)->(path1.getValue1() - path2.getValue1()));

        filteredLowestTemps.forEach((it)->paths.add(findPathTo(it, board, temperatures)));
        return paths.peek().getValue0().get(0);
    }

    private static int manhattanDist(Point p1, Point p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    Pair<List<Action>, Integer> findPathTo(CellTemperature temperature, Board board, BoardTemperatures temperatures) {
        return new AStarSearch((state)->(manhattanDist(state.getMinesweeper(), temperature.getCoordinates())))
                .search(new FindRouteProblem(new GameState(board.getMinesweeper(), temperatures.getTemperatures()),
                        temperature.getCoordinates(), temperatures));

    }
}
