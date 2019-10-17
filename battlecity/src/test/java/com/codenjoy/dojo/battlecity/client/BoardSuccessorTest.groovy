package com.codenjoy.dojo.battlecity.client

import com.codenjoy.dojo.battlecity.model.Elements
import com.codenjoy.dojo.services.Direction
import spock.lang.Specification
import spock.lang.Unroll

import static com.codenjoy.dojo.services.Direction.*


class BoardSuccessorTest extends Specification {

    @Unroll
    def "board successor should generate #description when actor performed #actions"(String initialExpectedBoards, List<Direction> actions, description) {
        def (String currentBoardString, String expectedBoardString) = calculateBoardString(initialExpectedBoards, actions, 2, 0)
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
        '☼˃• ╬☼|☼˃  ╠☼|' +
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
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼►•  ☼|☼►• •☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'derived bulled fired by me'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼•╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼•  ╬☼|☼   ╬☼|' +
        '☼?►• ☼|☼?►• ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'bullet flying by AI tank and me'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╬ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼ ►? ☼|☼ ►  ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, STOP]  || 'bang disappears'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼►╬ ╬☼|☼►╠ ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'my bullet damage wall from left'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ◄☼|☼ ╣ ►☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, RIGHT]  || 'my bullet damage wall from right'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬  ☼|☼ ╩  ☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼ ▲ ╬☼|' +
        '☼ ▲  ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, UP]  || 'my bullet damage wall from down'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ▼☼|☼ ╣◄ ☼|' +
        '☼    ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╦☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, LEFT, ACT]  || 'my bullet damage 2 walls from up and right'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ˅  ☼|☼ ˅  ☼|' +
        '☼ •  ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼ ╬ ◄☼|☼ ╦ ◄☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [STOP, STOP]  || 'other bullet damages wall'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ˅  ☼|☼ ˅  ☼|' +
        '☼ •  ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼ ╬ ◄☼|☼ ┐ ▼☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, DOWN]  || 'my and other bullet damage wall'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ˅  ☼|☼ ˅  ☼|' +
        '☼ •  ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼ ╬ ◄☼|☼   ◄☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, ACT, STOP]  || 'my and other bullet destroyed wall'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ˅  ☼|☼ ˅  ☼|' +
        '☼ •  ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼ ◄  ☼|☼ Ѡ  ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [STOP]  || 'my tank is hit by other tank'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ˅◄ ☼|☼ Ѡ◄ ☼|' +
        '☼ •  ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼ »  ☼|☼ Ѡ  ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'ai tank is hit by other tank and other tank hit by me'
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ˅  ☼|☼ ˅  ☼|' +
        '☼ •  ☼|☼    ☼|' +
        '☼   ╬☼|☼   ╬☼|' +
        '☼  ◄ ☼|☼ •◄ ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT]  || 'bullets intersections'
    }

    @Unroll
    def "board successor should return #hitElement when bullet hit"(String initialExpectedBoards, List<Direction> actions, hitElement) {
        def (ignore, ignore2, Board currentBoard) = calculateBoardString(initialExpectedBoards, actions, 2, 0)
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
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╬ ╬☼|' +
        '☼ «  ☼|☼    ☼|' +
        '☼►  ╬☼|☼►  ╠☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [ACT, STOP]  || Elements.CONSTRUCTION
        '☼☼☼☼☼☼|☼☼☼☼☼☼|' +
        '☼ ╬ ╬☼|☼ ╩ ╬☼|' +
        '☼ •  ☼|☼    ☼|' +
        '☼►˄ ╬☼|☼►˄ ╠☼|' +
        '☼    ☼|☼    ☼|' +
        '☼☼☼☼☼☼|☼☼☼☼☼☼|'       || [STOP]  || null
    }

    @Unroll
    def "board successor should generate derive #description from previous state"(String previoiusCurrentExpectedBoards, List<Direction> actions, description) {
        def (String currentBoardString, String expectedBoardString) = calculateBoardString(previoiusCurrentExpectedBoards, actions, 3, 1)
        expect:
        currentBoardString == expectedBoardString

        where:
        previoiusCurrentExpectedBoards || actions || description
        '☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|' +
        '☼▼•  ╬☼|☼▼   ╬☼|☼▼   ╬☼|' +
        '☼     ☼|☼     ☼|☼•    ☼|' +
        '☼    ╬☼|☼ •  ╬☼|☼    ╬☼|' +
        '☼     ☼|☼     ☼|☼     ☼|' +
        '☼     ☼|☼     ☼|☼ •   ☼|' +
        '☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|'       || [ACT]  || 'bullet down direction derived from previous'
        '☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|' +
        '☼▼   ╬☼|☼▼   ╬☼|☼Ѡ   ╬☼|' +
        '☼     ☼|☼     ☼|☼     ☼|' +
        '☼    ╬☼|☼•   ╬☼|☼    ╬☼|' +
        '☼     ☼|☼     ☼|☼     ☼|' +
        '☼•    ☼|☼     ☼|☼     ☼|' +
        '☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|'       || [STOP]  || 'bullet up direction derived from previous'
        '☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|' +
        '☼▼   ╬☼|☼▼   ╬☼|☼▼   ╬☼|' +
        '☼     ☼|☼     ☼|☼•    ☼|' +
        '☼•   ╬☼|☼  • ╬☼|☼    ╠☼|' +
        '☼     ☼|☼     ☼|☼     ☼|' +
        '☼     ☼|☼     ☼|☼     ☼|' +
        '☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|'       || [ACT]  || 'bullet right direction derived from previous'
        '☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|' +
        '☼    ╬☼|☼    ╬☼|☼    ╬☼|' +
        '☼     ☼|☼     ☼|☼     ☼|' +
        '☼▼  • ☼|☼▼•   ☼|☼Ѡ    ☼|' +
        '☼     ☼|☼     ☼|☼     ☼|' +
        '☼     ☼|☼     ☼|☼     ☼|' +
        '☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|☼☼☼☼☼☼☼|'       || [ACT]  || 'bullet left direction derived from previous'
    }

    private List calculateBoardString(String initialExpectedBoards, List<Direction> actions, Integer boardsAmount, Integer currentBoardIndex) {
        def boards = splitBoardString(boardsAmount, initialExpectedBoards)
        def previousState = boardsAmount == 3? new Board().forString(boards[0]): null
        Board currentState = (Board) new Board().forString(boards[currentBoardIndex])
        for (Direction action : actions) {
            def nextState = currentState.createSuccessor(action, previousState)
            previousState = currentState
            currentState = nextState
        }

        def currentBoardString = currentState.toString()
        def expectedBoardString = new Board().forString(boards.last()).toString()
        [currentBoardString, expectedBoardString, currentState]
    }

    private def splitBoardString(int boardsAmount, String boardStrings) {
        def result = []
        boardsAmount.downto 1, { result << '' }
        boardStrings.split('\\|').eachWithIndex { String entry, int i ->
            result[i % boardsAmount] += entry
        }
        result
    }

}