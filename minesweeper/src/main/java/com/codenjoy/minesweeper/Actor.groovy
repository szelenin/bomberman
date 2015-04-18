package com.codenjoy.minesweeper

import com.codenjoy.astar.AstarSearch
import com.codenjoy.astar.GameState
import com.codenjoy.astar.Heuristic

/**
 * Created by szelenin on 4/13/2015.
 */
class Actor {
    Action getAction(BoardTemperatures temperatures){
        def sortedByTemp = new TreeSet<CellTemperature>(new Comparator<CellTemperature>() {
            @Override
            int compare(CellTemperature cell1, CellTemperature cell2) {
                return cell1.temperature * 100 - cell2.temperature * 100
            }
        })
        temperatures.traverseTemperatures {int x, int y, double  temp ->
            sortedByTemp.add(new CellTemperature(coordinates: new Point(x,y), temperature: temp))
        }
        def first = sortedByTemp.first()
        def lowestTempCell = sortedByTemp.findAll { Math.abs(it.temperature - first.temperature) < 0.000001 }

        def paths = []
        lowestTempCell.each {paths+=findPathTo(it)}
    }

    def findPathTo(CellTemperature temperature) {
        new AstarSearch(new Heuristic() {
            @Override
            int calculate(GameState state) {
                return distTo(temperature.coordinates)
            }
        }).search()
    }
}
