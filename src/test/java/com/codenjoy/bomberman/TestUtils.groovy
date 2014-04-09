package com.codenjoy.bomberman

/**
 * Created by szelenin on 3/25/14.
 */
class TestUtils {

    static String createBoardWithBomberAt(int x, int y, int width) {
        createBoardWithElementAt(x, y, width, Element.BOMBERMAN)
    }

    static String createBoardWithElementAt(int x, int y, int width, Element element) {
        def chars = emptyBoard(width).chars

        setElement(width, x, y, element.char, chars)
    }

    static String board(int x, int y, int width, Element element) {
        createBoardWithElementAt(x, y, width, element)
    }

    static String setElement(int width, int x, int y, char elementChar, char[] chars) {
        def point = y * (width + 2) + x

        chars[point] = elementChar
        def board = new String(chars)
        board
    }

    private static def emptyBoard(int size) {
        String result = '☼' * (size + 2)
        size.times { result += '☼' + ' ' * size + '☼' }
        result += '☼' * (size + 2)
        result
    }

}
