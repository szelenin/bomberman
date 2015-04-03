package com.codenjoy.minesweeper;

import com.codenjoy.minesweeper.Board;
import com.codenjoy.minesweeper.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * User: sanja
 * Date: 25.04.13
 * Time: 13:50
 */
public class BoardTest {

    private Board board;

    @Before
    public void before() {
        board = new Board(
                    /*012345678*/
              /*0*/  "☼☼☼☼☼☼☼☼☼" +
              /*1*/  "☼1234567☼" +
              /*2*/  "☼81     ☼" +
              /*3*/  "☼ 4*****☼" +
              /*4*/  "☼☺  ‼   ☼" +
              /*5*/  "☼** ‼ 5☻☼" +
              /*6*/  "☼****☻**☼" +
              /*7*/  "☼ x * Ѡ ☼" +
              /*8*/  "☼☼☼☼☼☼☼☼☼");
    }


    @Test
    public void shouldWork_getAt() {
        assertEquals(Element.BOMB, board.getAt(7, 5));
        assertEquals(Element.WALL, board.getAt(0, 0));
        assertEquals(Element.BOMBS_1, board.getAt(1, 1));
        assertEquals(Element.DEAD_MINESWEEPER, board.getAt(6, 7));
        assertEquals(Element.HIDDEN, board.getAt(6, 6));
        assertEquals(Element.FLAG, board.getAt(4, 5));
        assertEquals(Element.DESTROYED_BOMB, board.getAt(2, 7));
    }

    @Test
    public void shouldWork_getAllHints() {
        assert '[1,[1,1], 2,[2,1], 3,[3,1], 4,[4,1], 5,[5,1], 6,[6,1], 7,[7,1], 8,[1,2], 1,[2,2], 4,[2,3], 5,[6,5]]' == board.allHints.toString()
    }

/*
    @Test
    public void shouldWork_getBarriers() {
        assertEquals("[[5,2], [1,7], [3,7], [0,0], [1,0], [2,0], [3,0], [4,0], [5,0], [6,0], [7,0], [8,0], [0,1], " +
                "[8,1], [0,2], [8,2], [0,3], [8,3], [0,4], [8,4], [0,5], [8,5], [0,6], [8,6], [0,7], [8,7], [0,8], " +
                "[1,8], [2,8], [3,8], [4,8], [5,8], [6,8], [7,8], [8,8], [1,1], [2,2], [3,3], [4,4], [7,5], [1,2], " +
                "[1,3], [4,5], [7,1], [7,3], [3,1], [5,3]]", board.getBarriers().toString());
    }

    @Test
    public void shouldWork_isBarrierAt() {
        assertEquals(true, board.isBarrierAt(1, 1));
        assertEquals(false, board.isBarrierAt(5, 1));
    }

    @Test
    public void shouldWork_getBlasts() {
        assertEquals("[[5,6], [6,6], [7,6]]", board.getBlasts().toString());
    }

    @Test
    public void shouldWork_getBomberman() {
        assertEquals("[1,4]", board.getBomberman().toString());
        assertEquals("[0,0]", new Board("☺").getBomberman().toString());
        assertEquals("[0,0]", new Board("☻").getBomberman().toString());
        assertEquals("[0,0]", new Board("Ѡ").getBomberman().toString());
    }

    @Test
    public void shouldWork_getBombs() {
        assertEquals("[[1,1], [2,2], [3,3], [4,4], [7,5]]", board.getBombs().toString());
    }

    @Test
    public void shouldWork_getDestroyWalls() {
        assertEquals("[[1,2], [1,3]]", board.getDestroyWalls().toString());
    }

    @Test
    public void shouldWork_getFutureBlasts() {
        assertEquals("[[1,1], [2,1], [1,2], [2,2], [3,2], [2,3], [3,3], [4,3], [3,4], [4,4], [5,4], [4,5], " +
                "[7,5], [6,5], [7,4], [7,6], [7,1], [6,1], [7,2], [7,3], [6,3]]", board.getFutureBlasts().toString());
    }

    @Test
    public void shouldWork_getMeatChoppers() {
        assertEquals("[[5,2], [1,7], [3,7]]", board.getMeatChoppers().toString());
    }

    @Test
    public void shouldWork_countNear() {
        assertEquals(0, board.countNear(0, 0, Element.MEAT_CHOPPER));
        assertEquals(2, board.countNear(2, 7, Element.MEAT_CHOPPER));
        assertEquals(1, board.countNear(4, 7, Element.MEAT_CHOPPER));

        assertEquals(2, board.countNear(1, 1, Element.WALL));
        assertEquals(2, board.countNear(1, 7, Element.WALL));
        assertEquals(2, board.countNear(7, 1, Element.WALL));
        assertEquals(2, board.countNear(7, 7, Element.WALL));
        assertEquals(1, board.countNear(1, 2, Element.WALL));
    }

    @Test
    public void shouldWork_isAt() {
        assertEquals(true, board.isAt(3, 7, Element.MEAT_CHOPPER));
        assertEquals(false, board.isAt(2, 7, Element.MEAT_CHOPPER));

        assertEquals(true, board.isAt(3, 7, Element.BOMB, Element.MEAT_CHOPPER));
        assertEquals(false, board.isAt(2, 7, Element.BOMB, Element.MEAT_CHOPPER));
    }

    @Test
    public void shouldWork_isNear() {
        assertEquals(true, board.isNear(1, 1, Element.WALL));
        assertEquals(false, board.isNear(5, 5, Element.WALL));
    }

    @Test
    public void shouldWork_getWalls() {
        assertEquals("[[0,0], [1,0], [2,0], [3,0], [4,0], [5,0], [6,0], [7,0], [8,0], [0,1], [8,1], [0,2], " +
                "[8,2], [0,3], [8,3], [0,4], [8,4], [0,5], [8,5], [0,6], [8,6], [0,7], [8,7], [0,8], [1,8], " +
                "[2,8], [3,8], [4,8], [5,8], [6,8], [7,8], [8,8]]", board.getWalls().toString());
    }

    @Test
    public void shouldWork_isMyBombermanDead() {
        assertEquals(false, board.isMyBombermanDead());
        assertEquals(true, new Board("Ѡ").isMyBombermanDead());
    }
*/
}
