package com.codenjoy.dojo.battlecity.client

import com.codenjoy.dojo.battlecity.model.Elements
import com.codenjoy.dojo.services.Direction
import spock.lang.Specification
import spock.lang.Unroll

import static com.codenjoy.dojo.services.Direction.*


class BoardSuccessorTest extends Specification {

    @Unroll
    def "board successor should generate #description when actor performed #actions"(String initialExpectedBoards, List<Direction> actions, description) {
        def (String currentBoardString, String expectedBoardString) = calculateBoardString(initialExpectedBoards, actions)
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
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼▼╬ ╬☼|☼►╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, STOP, RIGHT]  || 'bullet cannot break solid wall at the end when firing down'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼▼╬ ╬☼|☼▲╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [UP, ACT]  || 'bullet cannot break solid wall when firing up'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼▼╬ ╬☼|☼◄╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [LEFT, ACT]  || 'bullet cannot break solid wall when firing left'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╬ ╬☼|' +
        '☼►   ☼|☼►  •☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, STOP]  || 'bullet 2 cells right from tank'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╬ ╬☼|' +
        '☼►   ☼|☼►   ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, STOP, STOP]  || 'bullet cannot break solid wall when firing right'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╬ ╬☼|' +
        '☼►˄  ☼|☼►Ѡ  ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'bullet should kill other tank'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼▼╬ ╬☼|☼▼╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼˃   ☼|☼Ѡ   ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, STOP]  || 'bullet should kill other tank on distance'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼?╬ ╬☼|☼Ѡ╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼ ► ╬☼|' +
        '☼ ◄  ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [LEFT, UP, ACT, RIGHT]  || 'bullet should kill ai tank'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼˃• ╬☼|☼˃  •☼|' +
        '☼ ◄  ☼|☼ ◄  ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [STOP]  || 'bullet fired by other tank'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼˅╬ ╬☼|☼˅╬ ╬☼|' +
        '☼•   ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼ ◄  ☼|☼•◄  ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'bullet fired by other tank and me intersects'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼˅╬ ╬☼|☼˅╬ ╬☼|' +
        '☼•   ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼  ◄ ☼|☼••◄ ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'bullet fired by other tank and me'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼˅╬ ╬☼|☼˅╬ ╬☼|' +
        '☼•   ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼ ►  ☼|☼•►• ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'bullet flying by other tank and me'
        //todo: ai tank fire
        //bang disappears
    }

    @Unroll
    def "board successor should return #hitElement when bullet hit"(String initialExpectedBoards, List<Direction> actions, hitElement) {
        def (currentBoardString, expectedBoardString, Board currentBoard) = calculateBoardString(initialExpectedBoards, actions)
        expect:
        currentBoard.getHitElement() == hitElement

        where:
        initialExpectedBoards || actions || hitElement
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╬ ╬☼|' +
        '☼►˄  ☼|☼►Ѡ  ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || Elements.OTHER_TANK_UP
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╬ ╬☼|' +
        '☼►«  ☼|☼►Ѡ  ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || Elements.AI_TANK_LEFT
    }

    private List calculateBoardString(String initialExpectedBoards, List<Direction> actions) {
        String initialBoard = ''
        String expectedBoard = ''
        initialExpectedBoards.split('\\|').eachWithIndex { String entry, int i ->
            if (i % 2 == 0) {
                initialBoard += entry
            } else {
                expectedBoard += entry
            }
        }
        def previousState = null
        Board currentState = (Board) new Board().forString(initialBoard)
        for (Direction action : actions) {
            def nextState = currentState.createSuccessor(action)
            previousState = currentState
            currentState = nextState
        }

        def currentBoardString = currentState.toString()
        def expectedBoardString = new Board().forString(expectedBoard).toString()
        [currentBoardString, expectedBoardString, currentState]
    }

}