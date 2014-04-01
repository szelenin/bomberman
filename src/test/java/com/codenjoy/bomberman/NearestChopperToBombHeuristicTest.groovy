package com.codenjoy.bomberman

import spock.lang.Specification

/**
 * Created by szelenin on 4/1/14.
 */
class NearestChopperToBombHeuristicTest extends Specification {
    def "bomb is set"() {
        when:
        def board = TestUtils.createBoardWithBomberAt(5, 5, 9)
        board = TestUtils.setElement(9, 5, 5 + 1, Element.BOMB_TIMER_5.char, board.chars)

        then:
        assert 5 == new NearestChopperToBombHeuristic().calculate(new GameState(board, true))
    }

    def "bomb is not set"() {
        when:
        def board = TestUtils.createBoardWithBomberAt(5, 5, 9)
        board = TestUtils.setElement(9, 5 + 1, 5 + 1, Element.MEAT_CHOPPER.char, board.chars)

        then:
        assert 5 + 2 == new NearestChopperToBombHeuristic().calculate(new GameState(board, true))
    }

    def "nearest chopper"() {
        when:
        def board = TestUtils.createBoardWithBomberAt(5, 5, 9)
        board = TestUtils.setElement(9, 5 - 2, 5 - 2, Element.MEAT_CHOPPER.char, board.chars)
        board = TestUtils.setElement(9, 5 + 1, 5, Element.MEAT_CHOPPER.char, board.chars)

        then:
        assert 5 + 1 == new NearestChopperToBombHeuristic().calculate(new GameState(board, true))
    }

}
