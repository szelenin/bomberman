package com.codenjoy.bomberman

import com.codenjoy.bomberman.utils.Board
import com.codenjoy.bomberman.utils.LengthToXY
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
    private LengthToXY toXY

    GameState(String board, boolean oneLine = false) {
        this.board = new Board(board, oneLine)
        toXY = new LengthToXY(this.board.boardSize())
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
        char[] boardChars = board.board.toCharArray()

        int bomberCharAt = toXY.getLength(board.bomberman)
        int newBomberCharAt = toXY.getLength(action.changeX(board.bomberman.x), action.changeY(board.bomberman.y))

        char currentBomberChar = boardChars[bomberCharAt]
        boardChars[bomberCharAt] = currentBomberChar == BOMB_BOMBERMAN.char ? BOMB_TIMER_5.char : SPACE.char
        boardChars[newBomberCharAt] = bomberManChar(action)

        for (bomb in board.bombs) {
            int bombPosition = toXY.getLength(bomb)
            boardChars[bombPosition] = ((boardChars[bombPosition] as int) - 1) as char
            if (boardChars[bombPosition] == '0') {
                (-3..3).each {it->
                    boardChars[toXY.getLength(bomb.x + it, bomb.y)] = BOOM.char
                    boardChars[toXY.getLength(bomb.x, bomb.y + it)] = BOOM.char
                }
            }
        }

        def state = new GameState(new String(boardChars), true)
        state
    }

    private char bomberManChar(Action action) {
        def bomberChar = BOMBERMAN.char
        if (action == Action.ACT) {
            bomberChar = BOMB_BOMBERMAN.char
        }
        bomberChar
    }

    def Point getBomber() {
        board.bomberman
    }

    def at(int x, int y) {
        board.getAt(x, y)
    }

    @Override
    String toString() {
        return board.toString()
    }
}
