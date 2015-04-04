package com.codenjoy.minesweeper

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by szelenin on 4/4/2015.
 */
class BoardTemperaturesTest extends Specification {

    @Unroll
    def "tempearture when #coordinates"(String board, def coordinates, expectedTemperature) {
        def temperatures = new BoardTemperatures(new Board(board))

        expect:
        coordinates.each {
            assert temperatures.temperatureAt(it[0], it[1]) as double == expectedTemperature
        }

        where:
        board        || coordinates                      || expectedTemperature
        '☼☼☼☼'       || [[0, 0], [1, 0], [0, 1], [1, 1]] || 100
        "" +
                "☼☼☼" +
                "☼*☼" +
                "☼☼☼" || [[1, 1]] || 100 - 1
        "" +
                "☼☼☼" +
                "☼1☼" +
                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼" +
                "☼2☼" +
                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼" +
                "☼3☼" +
                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼" +
                "☼4☼" +
                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼" +
                "☼5☼" +
                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼" +
                "☼6☼" +
                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼" +
                "☼7☼" +
                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼" +
                "☼8☼" +
                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼" +
                "☼ ☼" +
                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼☼☼" +
                "☼***☼" +
                "☼*1*☼" +
                "☼***☼" +
                "☼☼☼☼☼" || [[1, 1],[2,1],[3,1],[1,2],[3,2],[1,3],[2,3],[3,3]] || 1/8
    }
}
