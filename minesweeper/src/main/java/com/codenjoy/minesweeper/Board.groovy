package com.codenjoy.minesweeper;

import java.util.LinkedList;
import java.util.List;
import static com.codenjoy.minesweeper.Element.*;

/**
 * User: oleksandr.baglai
 */
public class Board {
    public String board;
    private LengthToXY xyl;
    private int size;

    public Board(String boardString) {
        this(boardString, false);
    }

    public Board(String boardString, boolean oneLine) {
        board = boardString;
        if (!oneLine) {
            board = boardString.replaceAll("\n", "");
        }
        size = boardSize();
        xyl = new LengthToXY(size);
    }

    public boolean isAt(int x, int y, Element element) {
        if (Point.pt(x, y).isBad(size)) {
            return false;
        }
        return getAt(x, y).equals(element);
    }

    public Element getAt(int x, int y) {
        return Element.valueOf(board.charAt(xyl.getLength(x, y)));
    }

    public int boardSize() {
        return (int) Math.sqrt(board.length());
    }

    private String boardAsString() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i <= size - 1; i++) {
            result.append(board.substring(i * size, (i + 1) * size));
            result.append("\n");
        }
        return result.toString();
    }

    public List<Point> getBarriers() {
        return removeDuplicates(getWalls());
    }

    private List<Point> removeDuplicates(List<Point> all) {
        List<Point> result = new LinkedList<Point>();
        for (Point point : all) {
            if (!result.contains(point)) {
                result.add(point);
            }
        }
        return result;
    }

    private List<Point> findAll(Element... element) {
        List<Point> result = new LinkedList<Point>();
        for (int i = 0; i < size*size; i++) {
            Point pt = xyl.getXY(i);
            if (isAt(pt.getX(), pt.getY(), element)) {
                result.add(pt);
            }
        }
        return result;
    }

    public List<Point> getWalls() {
        return findAll(WALL);
    }


    public boolean isAt(int x, int y, Element... elements) {
        for (Element c : elements) {
            if (isAt(x, y, c)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNear(int x, int y, Element element) {
        if (Point.pt(x, y).isBad(size)) {
            return false;
        }
        return isAt(x + 1, y, element) || isAt(x - 1, y, element) || isAt(x, y + 1, element) || isAt(x, y - 1, element);
    }

    public boolean isBarrierAt(int x, int y) {
        return getBarriers().contains(Point.pt(x, y));
    }


    def getAllHints() {
        findAll(BOMBS_1,BOMBS_2,BOMBS_3,BOMBS_4,BOMBS_5,BOMBS_6,BOMBS_7,BOMBS_8).collect {new Hint(Character.getNumericValue(getAt(it.x, it.y).char), it)}
    }
}