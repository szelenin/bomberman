package com.codenjoy.astar;

import com.codenjoy.minesweeper.Action;
import com.codenjoy.minesweeper.Point;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by szelenin on 4/18/2015.
 */
public class GameStateTest {
    @Test
    public void shouldGetEmptyLegalActions() {
        GameState gameState = new GameState(new Point(1, 1), new double[][]{
                {100.0, 100.0, 100.0},
                {100.0, 0.0, 100.0},
                {100.0, 100.0, 100.0},
        });
        assertTrue(gameState.getLegalActions().isEmpty());
    }

    @Test
    public void test() {
        GameState gameState = new GameState(new Point(1, 1), new double[][]{
                {100.0, 100.0, 100.0, 100.0},
                {100.0, 0.0, 0.0, 100.0},
                {100.0, 0.0, 0.0, 100.0},
                {100.0, 100.0, 100.0, 100.0}
        });
        assertTrue(gameState.getLegalActions().contains(Action.RIGHT));
        assertTrue(gameState.getLegalActions().contains(Action.DOWN));
        assertEquals(2, gameState.getLegalActions().size());
    }

    @Test
    public void shouldGenerateSuccessorX() {
        GameState gameState = new GameState(new Point(1, 1), new double[][]{
                {100.0, 100.0, 100.0, 100.0},
                {100.0, 0.0, 0.0, 100.0},
                {100.0, 0.0, 0.0, 100.0},
                {100.0, 100.0, 100.0, 100.0}
        });

        GameState successorState = gameState.generateSuccessorState(Action.RIGHT);
        assertEquals(new Point(2,1), successorState.getMinesweeper());
    }

    @Test
    public void shouldGenerateSuccessorY() {
        GameState gameState = new GameState(new Point(1, 1), new double[][]{
                {100.0, 100.0, 100.0, 100.0},
                {100.0, 0.0, 0.0, 100.0},
                {100.0, 0.0, 0.0, 100.0},
                {100.0, 100.0, 100.0, 100.0}
        });

        GameState successorState = gameState.generateSuccessorState(Action.DOWN);
        assertEquals(new Point(1,2), successorState.getMinesweeper());
    }
}
