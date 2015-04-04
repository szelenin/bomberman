package com.codenjoy.minesweeper

/**
 * Created by szelenin on 4/4/2015.
 */
class BoardTemperatures {
    public static final int WALL_TEMPERATURE = 100
    public static final int HIDDEN_TEMP = 1
    public static final int SPACE_TEMP = 2
    private Board board
    private double[][] temperatures;

    BoardTemperatures(Board board) {
        this.board = board
        initTemperatures()
    }

    def fillStaticElements =  { element->
        if (element == Element.WALL) {
            temperatures[x][y] = WALL_TEMPERATURE
        }
        if (element == Element.HIDDEN) {
            temperatures[x][y] = WALL_TEMPERATURE - HIDDEN_TEMP
        }
        if ([Element.SPACE, Element.BOMBS_1, Element.BOMBS_2, Element.BOMBS_3, Element.BOMBS_4, Element.BOMBS_5, Element.BOMBS_6, Element.BOMBS_7, Element.BOMBS_8].contains(element)) {
            temperatures[x][y] = WALL_TEMPERATURE - SPACE_TEMP
        }
    }

    def fillHints = { element, int x, int y ->
        if (element != Element.BOMBS_1) {
            return
        }
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                if (dx == dy && dy == 0) {
                    continue;
                }
                temperatures[x + dx][y + dy] = 1d / 8
            }
        }
    }

    def initTemperatures() {
        temperatures = new double[board.boardSize()][board.boardSize()]
        traverseBoard(fillStaticElements)
        traverseBoard(fillHints)
    }

    def traverseBoard(Closure closure) {
        for (int x = 0; x < board.boardSize(); x++) {
            for (int y = 0; y < board.boardSize(); y++) {
                closure.call([board.getAt(x, y), x, y])
            }
        }
    }

    double temperatureAt(int x, int y) {
        temperatures[x][y]
    }
}
