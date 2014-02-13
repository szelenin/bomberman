package com.codenjoy.bomberman

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
        board                       || expectedActions
        BOMBER_LEFT_UP_CORNER       || [RIGHT, DOWN, STOP, ACT]
        BOMER_DOWN_RIGHT_CORNER     || [UP, LEFT, STOP, ACT]
        BOMB_BOMBER                 || [UP, LEFT, STOP, ACT]
        SURROUNDED_BY_OTHER             || [STOP, ACT]
        SURROUNDED_BY_WALLS             || [LEFT, STOP, ACT]
        SURROUNDED_BY_CHOPPERS          || [UP, STOP, ACT]
        SURROUNDED_BY_BOMBS          || [LEFT, RIGHT, UP, DOWN, STOP, ACT]
        SURROUNDED_BY_EXPLOSION          || [LEFT, RIGHT, UP, DOWN, STOP, ACT]
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