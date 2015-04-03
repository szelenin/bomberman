package com.codenjoy.bomberman.analytic

import com.codenjoy.bomberman.Element

import static com.codenjoy.bomberman.TestUtils.setElement

/**
 * Created by szelenin on 4/22/14.
 */
class SWrapper {
    private String board
    SWrapper(String board) {
        this.board = board
    }


    SWrapper _(Element element, int x, int y){
        return new SWrapper(setElement(9, x, y, element.char, board.chars))
    }

    @Override
    String toString() {
        board
    }

}
