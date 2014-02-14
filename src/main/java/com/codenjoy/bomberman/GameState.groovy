package com.codenjoy.bomberman

import com.codenjoy.bomberman.utils.Board
import com.codenjoy.bomberman.utils.Point

import static com.codenjoy.bomberman.Element.*

/**
 * Created by szelenin on 2/11/14.
 */
class GameState {
    private Board board
    private canMoveElements = [SPACE, BOMBERMAN, BOMB_BOMBERMAN, DESTROYED_WALL, DEAD_MEAT_CHOPPER,
            BOMB_TIMER_1, BOMB_TIMER_2, BOMB_TIMER_3, BOMB_TIMER_4, BOMB_TIMER_5,
            DEAD_BOMBERMAN, BOOM, OTHER_DEAD_BOMBERMAN]

    GameState(String board, boolean oneLine = false) {
        this.board = new Board(board, oneLine)
        this.board
    }

    public List<Action> getLegalActions() {
        def result = []
        for (action in Action.values()) {
            int x = action.changeX(board.bomberman.x)
            int y = action.changeY(board.bomberman.y)

            Element element = board.getAt(x, y)

            if (element in canMoveElements) {
                result += action
            }
        }
        return result
    }

    def generateSuccessor(Action action) {
        if (!legalActions.contains(action)) {
            throw new IllegalArgumentException("Can't do $action!")
        }

        int bomberCharAt = board.bomberman.x + board.boardSize() * board.bomberman.y
        int newCharAt = action.changeX(board.bomberman.x) + board.boardSize() * action.changeY(board.bomberman.y)
        char[] chars = board.board.toCharArray()
        chars[bomberCharAt] = Element.SPACE.char
        chars[newCharAt] = Element.BOMBERMAN.char

        def state = new GameState(new String(chars), true)
        state
    }

    def Point getBomber() {
        board.bomberman
    }
}
