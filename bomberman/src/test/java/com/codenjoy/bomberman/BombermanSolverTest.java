package com.codenjoy.bomberman;

import com.codenjoy.bomberman.Action;
import com.codenjoy.bomberman.YourDirectionSolver;
import com.codenjoy.bomberman.utils.Board;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BombermanSolverTest {

    private final YourDirectionSolver solver = new YourDirectionSolver();

    private void assertB(String board, Action action) {
        Board board1 = new Board(board);
        board1.getBomberman();
        assertEquals(action.toString(), solver.get(board1));
    }

    @Test
    public void should_when() {
        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼☺        # # ☼" +
                "☼ ☼ ☼ ☼#☼ ☼ ☼ ☼" +
                "☼##           ☼" +
                "☼ ☼ ☼#☼ ☼ ☼ ☼ ☼" +
                "☼   #    # #  ☼" +
                "☼ ☼ ☼ ☼#☼ ☼ ☼ ☼" +
                "☼             ☼" +
                "☼#☼ ☼ ☼#☼ ☼ ☼#☼" +
                "☼  #  #       ☼" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼#☼" +
                "☼ ##      #   ☼" +
                "☼ ☼ ☼ ☼ ☼ ☼ ☼#☼" +
                "☼ #   #  &    ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼",
                Action.ACT);
    }

}
