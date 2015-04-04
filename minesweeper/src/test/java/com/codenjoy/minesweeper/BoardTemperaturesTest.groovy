package com.codenjoy.minesweeper

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by szelenin on 4/4/2015.
 */
class BoardTemperaturesTest extends Specification {
    def "tempearture when no hints"() {
        when:
        def temperatures = new BoardTemperatures(new Board("" +
                "☼☼☼☼" +
                "☼**☼" +
                "☼**☼" +
                "☼☼☼☼"))
        then:
        temperatures.temperatureAt(0, 0) == Double.MAX_VALUE
        temperatures.temperatureAt(1, 1) == Double.MAX_VALUE - 1
    }

    @Unroll
    def "tempearture when #coordinates"(String board, def coordinates, expectedTemperature) {
        def temperatures = new BoardTemperatures(new Board(board))

        expect:
        coordinates.each {
            assert temperatures.temperatureAt(it[0], it[1]) as double == expectedTemperature
        }

        where:
        board        || coordinates                      || expectedTemperature
//        '☼☼☼☼'       || [[0, 0], [1, 0], [0, 1], [1, 1]] || 100
//        "" +
//                "☼☼☼" +
//                "☼*☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 1
//        "" +
//                "☼☼☼" +
//                "☼1☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 2
//        "" +
//                "☼☼☼" +
//                "☼2☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 2
//        "" +
//                "☼☼☼" +
//                "☼3☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 2
//        "" +
//                "☼☼☼" +
//                "☼4☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 2
//        "" +
//                "☼☼☼" +
//                "☼5☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 2
//        "" +
//                "☼☼☼" +
//                "☼6☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 2
//        "" +
//                "☼☼☼" +
//                "☼7☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 2
//        "" +
//                "☼☼☼" +
//                "☼8☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 2
//        "" +
//                "☼☼☼" +
//                "☼ ☼" +
//                "☼☼☼" || [[1, 1]] || 100 - 2
        "" +
                "☼☼☼☼☼" +
                "☼***☼" +
                "☼*1*☼" +
                "☼***☼" +
                "☼☼☼☼☼" || [[1, 1],[2,1],[3,1],[1,2],[3,2],[1,3],[2,3],[3,3]] || 1/8
    }
/*
    @Unroll
    def "bomberman legal actions #expectedActions"(String board, List<Action> expectedActions) {
        GameState state = new GameState(board)
        expect:
        (state.legalActions - expectedActions).empty
        (expectedActions - state.legalActions).empty

        where:
        board                   || expectedActions
        BOMBER_LEFT_UP_CORNER   || [RIGHT, DOWN, STOP, ACT]
        BOMER_DOWN_RIGHT_CORNER || [UP, LEFT, STOP, ACT]
        BOMB_BOMBER             || [UP, LEFT, STOP, ACT]
        SURROUNDED_BY_OTHER     || [STOP, ACT]
        SURROUNDED_BY_WALLS     || [LEFT, STOP, ACT]
        SURROUNDED_BY_CHOPPERS  || [UP, LEFT, STOP, ACT]
        SURROUNDED_BY_BOMBS     || [LEFT, RIGHT, UP, DOWN, STOP, ACT]
        SURROUNDED_BY_EXPLOSION || [LEFT, RIGHT, UP, DOWN, STOP, ACT]
    }
*/
}
