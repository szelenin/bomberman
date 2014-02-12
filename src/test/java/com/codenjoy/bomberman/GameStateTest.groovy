package com.codenjoy.bomberman

import spock.lang.Specification
import spock.lang.Unroll

import static com.codenjoy.bomberman.Action.*

class GameStateTest extends Specification {

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
    @Unroll
    def "bomberman legal actions #expectedActions"(String board, List<Action> expectedActions) {
        GameState state = new GameState(board)
        expect:
        (state.legalActions - expectedActions).empty
        (expectedActions - state.legalActions).empty

        where:
        board                       || expectedActions
        BOMBER_LEFT_UP_CORNER       || [RIGHT, DOWN, STOP, ACT]
        BOMER_DOWN_RIGHT_CORNER     || [UP, LEFT, STOP, ACT]
        BOMB_BOMBER                 || [UP, LEFT, STOP, ACT]
    }
}