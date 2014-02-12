package com.codenjoy.bomberman

import com.codenjoy.bomberman.utils.Board

/**
 * Created by szelenin on 2/11/14.
 */
class GameState {
    private Board board

    GameState(String board) {
        this.board = new Board(board)
        this.board
    }

    public List<Action> getLegalActions() {
        def result = []
        for (action in Action.values()) {
            int x = action.changeX(board.bomberman.x)
            int y = action.changeY(board.bomberman.y)

            Element element = board.getAt(x, y)
            if (element == Element.SPACE || element == Element.BOMBERMAN || element == Element.BOMB_BOMBERMAN) {
                result += action
            }
        }
        return result
    }
}
