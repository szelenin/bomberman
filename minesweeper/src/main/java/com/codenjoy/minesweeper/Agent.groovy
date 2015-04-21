package com.codenjoy.minesweeper

import com.codenjoy.astar.AStarSearch
import com.codenjoy.astar.GameState
import com.codenjoy.astar.Heuristic
import com.codenjoy.astar.FindRouteProblem
import org.javatuples.Pair

/**
 * Created by szelenin on 4/13/2015.
 */
class Agent {

    Action getAction(BoardTemperatures temperatures, Board board) {

        PriorityQueue sortedByTemp = new PriorityQueue<CellTemperature>(new Comparator<CellTemperature>() {
            @Override
            int compare(CellTemperature cell1, CellTemperature cell2) {
                return cell1.temperature * 100 - cell2.temperature * 100
            }
        })
        temperatures.traverseTemperatures { int x, int y, double temp ->
            if (temp > 0 && new Point(x,y) != board.minesweeper) {
                sortedByTemp.add(new CellTemperature(coordinates: new Point(x, y), temperature: temp))
            }
        }
        def first = sortedByTemp.first()
        def lowestTempCell = sortedByTemp.findAll { Math.abs(it.temperature - first.temperature) < 0.000001 }

        List<Pair<List<Action>, Integer>> paths = []
        lowestTempCell.each {
            paths.add(findPathTo(it, board, temperatures))

        }
        Pair<List<Action>, Integer> lowestCostPath = paths[0]
        paths.each { if (lowestCostPath.getValue1() > it.getValue1()){lowestCostPath = it} }
        return lowestCostPath.getValue0()[0]
    }

    Pair<List<Action>, Integer> findPathTo(CellTemperature temperature, Board board, BoardTemperatures temperatures) {
        new AStarSearch(new Heuristic() {
            @Override
            int calculate(GameState state) {
                return manhattanDist(state)
            }

            private int manhattanDist(GameState state) {
                Math.abs(state.minesweeper.x - temperature.coordinates.x) + Math.abs(state.minesweeper.y - temperature.coordinates.y)
            }
        }).search(new FindRouteProblem(new GameState(board.minesweeper, temperatures.temperatures), temperature.coordinates, temperatures))
    }
}
