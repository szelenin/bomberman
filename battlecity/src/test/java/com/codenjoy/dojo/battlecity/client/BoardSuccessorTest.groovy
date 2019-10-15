package com.codenjoy.dojo.battlecity.client

import com.codenjoy.dojo.services.Direction
import spock.lang.Specification
import spock.lang.Unroll

import static com.codenjoy.dojo.services.Direction.*


class BoardSuccessorTest extends Specification {
    @Unroll
    def "board successor should generate #description when actor performed #actions"(String initialExpectedBoards, List<Direction> actions, description) {
        String initialBoard = ''
        String expectedBoard = ''
        initialExpectedBoards.split('\\|').eachWithIndex { String entry, int i ->
            if (i % 2 == 0) {
                initialBoard += entry
            } else {
                expectedBoard+=entry
            }
        }
        def previousState = null
        Board currentState = (Board) new Board().forString(initialBoard)
        for (Direction action : actions) {
            def nextState = currentState.createSuccessor(action, previousState)
            previousState = currentState
            currentState = nextState
        }

        def currentBoardString = currentState.toString()
        def expectedBoardString = new Board().forString(expectedBoard).toString()
        expect:
        currentBoardString == expectedBoardString

        where:
        initialExpectedBoards || actions || description
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼▼╬ ╬☼|☼▼╬ ╬☼|' +
        '☼    ☼|☼•   ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'bullet down next to tank'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼▼╬ ╬☼|☼▼╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼•   ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, STOP]  || 'bullet 2 cells down to tank'
    }

}