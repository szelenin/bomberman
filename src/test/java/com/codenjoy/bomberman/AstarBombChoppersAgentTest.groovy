package com.codenjoy.bomberman

import spock.lang.Specification

import static com.codenjoy.bomberman.TestUtils.*

/**
 * Created by szelenin on 3/25/14.
 */
class AstarBombChoppersAgentTest extends Specification {
    def "boom chopper"() {
        String board = setElement(9, 4, 4, Element.MEAT_CHOPPER.char, createBoardWithBomberAt(5, 5, 9).chars)
        def currentState = new GameState(board, true)
        def agent = new AstarBombChoppersAgent()
        boolean deadMeatChopper = false
        when:
        5.each {
            Action action = agent.getAction(currentState)
            currentState = currentState.generateSuccessor(action)
            if (currentState.at(4, 4) == Element.DEAD_MEAT_CHOPPER) {
                deadMeatChopper = true
            }
        }

        then:
        assert deadMeatChopper
    }
}
