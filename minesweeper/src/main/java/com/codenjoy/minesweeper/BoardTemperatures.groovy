package com.codenjoy.minesweeper

/**
 * Created by szelenin on 4/4/2015.
 */
class BoardTemperatures {
    public static final int WALL_TEMPERATURE = 100
    public static final int HIDDEN_TEMP = 1
    public static final int SPACE_TEMP = 2
    public static
    final ArrayList<Element> HINTS = [Element.BOMBS_1, Element.BOMBS_2, Element.BOMBS_3, Element.BOMBS_4, Element.BOMBS_5, Element.BOMBS_6, Element.BOMBS_7, Element.BOMBS_8]
    private Board board
    private double[][] temperatures;

    BoardTemperatures(Board board) {
        this.board = board
        initTemperatures()
    }

    def fillStaticElementsForZeroTemps = { skipElement ->
        { element, int x, int y ->
            if (temperatures[x][y] != 0) {
                return
            }

            if (element == Element.WALL && skipElement != Element.WALL) {
                temperatures[x][y] = WALL_TEMPERATURE
            }
            if (element == Element.HIDDEN && skipElement != Element.HIDDEN) {
                temperatures[x][y] = WALL_TEMPERATURE - HIDDEN_TEMP
            }
            if (isSpace(element) && skipElement != Element.SPACE) {
                temperatures[x][y] = WALL_TEMPERATURE - SPACE_TEMP
            }
        }
    }

    private boolean isSpace(element) {
        ([Element.SPACE] + HINTS).contains(element)
    }

    boolean isHint(Element element) {
        HINTS.contains(element)
    }

    def fillHints = { element, int x, int y ->
        if (!isHint(element)) {
            return
        }
        int hiddenCells = traverseNearby(x, y) { int itX, int itY -> board.getAt(itX, itY) == Element.HIDDEN ? 1 : 0 }
        def hintNo = Character.getNumericValue(element.char)
        traverseNearby(x, y) { int itX, int itY ->
            temperatures[itX][itY] += (hintNo as double) / hiddenCells
        }
    }


    def initTemperatures() {
        temperatures = new double[board.boardSize()][board.boardSize()]
        def aCall = fillStaticElementsForZeroTemps Element.HIDDEN
        traverseBoard(aCall)
        traverseBoard(fillHints)
        def secondTraverse = fillStaticElementsForZeroTemps null
        traverseBoard(secondTraverse)
    }

    def traverseBoard(Closure closure) {
        for (int x = 0; x < board.boardSize(); x++) {
            for (int y = 0; y < board.boardSize(); y++) {
                closure.call(board.getAt(x, y), x, y)
            }
        }
    }

    def traverseNearby(x, y, Closure closure) {
        int sum = 0;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                if (dx == dy && dy == 0) {
                    continue;
                }
                sum += closure.call(x + dx, y + dy)
            }
        }
        sum
    }

    double temperatureAt(int x, int y) {
        temperatures[x][y]
    }

    void traverseTemperatures(Closure closure) {
        traverseBoard { ignore, int x, int y ->
            closure.call(x, y, temperatures[x][y])
        }
    }
}
