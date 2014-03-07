package com.codenjoy.bomberman

import com.codenjoy.bomberman.utils.LengthToXY
import spock.lang.Specification
import spock.lang.Unroll

import static com.codenjoy.bomberman.Action.*

class GameStateTest extends Specification {

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

    def "generate successor for illegal actions"() {
        when:
        GameState state = new GameState(BOMBER_LEFT_UP_CORNER)
        state.generateSuccessor(LEFT)

        then:
        thrown(IllegalArgumentException)
    }

    def "generate successor (move)"(String initState, Action action, String expectedPosition) {
        GameState state = new GameState(initState)
        GameState newState = state.generateSuccessor(action)
        expect:
        assert "$newState.bomber" == expectedPosition

        where:
        initState             | action || expectedPosition
        BOMBER_LEFT_UP_CORNER | RIGHT  || '[2,1]'
        BOMBER_LEFT_UP_CORNER | DOWN   || '[1,2]'
        BOMB_BOMBER           | UP     || '[2,1]'
    }


    @Unroll
    def "generate successor (bomb timer)"(List<Action> actions, Element expectedTimer, String bomberAt) {
        def state = new GameState(createBoardWithBomberAt(4, 4, 7), true).generateSuccessor(ACT)
        actions.each { action -> state = state.generateSuccessor(action) }
        expect:
        assert state.at(4, 4) == expectedTimer
        assert "$state.bomber" == bomberAt

        where:
        actions                  || expectedTimer | bomberAt
        [RIGHT]                  || Element.BOMB_TIMER_4 | '[5,4]'
        [LEFT, LEFT]             || Element.BOMB_TIMER_3 | '[2,4]'
        [LEFT, LEFT, LEFT]       || Element.BOMB_TIMER_2 | '[1,4]'
        [LEFT, LEFT, LEFT, DOWN] || Element.BOMB_TIMER_1 | '[1,5]'
    }

    def "generate successor (boom)"() {
        def state = new GameState(createBoardWithBomberAt(5, 5, 9), true).generateSuccessor(ACT)
        4.times { state = state.generateSuccessor(LEFT) }
        when:
        GameState boomState = state.generateSuccessor(DOWN)
        then:
        (-3..3).each { it -> assert boomState.at(5 + it, 5) == Element.BOOM }
        (-3..3).each { it -> assert boomState.at(5, 5 + it) == Element.BOOM }
    }

    def "init from string with BOOM"() {
        when:
        GameState state = new GameState(SURROUNDED_BY_EXPLOSION)
        then:
        assert state.at(3, 2) == Element.BOOM
    }

/*
    def "generate successor (boomed bomber)"() {
        def state = new GameState(createBoardWithBomberAt(5, 5, 9), true).generateSuccessor(ACT)
        state.generateSuccessor(STOP)
        4.times { it ->
            println "state = $state"
            state = state.generateSuccessor(STOP)
        }
        when:
        GameState boomState = state.generateSuccessor(STOP)
        then:
        assert boomState.at(5, 5) == Element.DEAD_BOMBERMAN
        assert boomState.dead
    }
*/


    private static String createBoardWithBomberAt(int x, int y, int width) {
        def point = new LengthToXY(width).getLength(x, y)

        def chars = emptyBoard(width).chars
        chars[point + width + 1] = '☺'
        def board = new String(chars)
        board
    }

    private static def emptyBoard(int size) {
        String result = '☼' * (size + 2)
        size.times { result += '☼' + ' ' * size + '☼' }
        result += '☼' * (size + 2)
        result
    }

    public static final String BOMBER_LEFT_UP_CORNER = """
☼☼☼☼
☼☺ ☼
☼  ☼
☼☼☼☼
"""
    public static final String BOMER_DOWN_RIGHT_CORNER = """
☼☼☼☼
☼  ☼
☼ ☺☼
☼☼☼☼
"""
    public static final String BOMB_BOMBER = """
☼☼☼☼
☼  ☼
☼ ☻☼
☼☼☼☼
"""
    public static final String SURROUNDED_BY_OTHER = """
☼☼☼☼
☼ ♠☼
☼♥☻☼
☼☼☼☼
"""
    public static final String SURROUNDED_BY_WALLS = """
☼☼☼☼
☼ #☼
☼H☻☼
☼☼☼☼
"""
    public static final String SURROUNDED_BY_CHOPPERS = """
☼☼☼☼
☼ x☼
☼&☺☼
☼☼☼☼
"""
    public static final String SURROUNDED_BY_BOMBS = """
☼☼☼☼☼
☼ 1 ☼
☼4☺2☼
☼ 3 ☼
☼☼☼☼☼
"""
    public static final String SURROUNDED_BY_EXPLOSION = """
☼☼☼☼☼
☼ 5 ☼
☼Ѡ☺҉☼
☼ ♣ ☼
☼☼☼☼☼
"""
}