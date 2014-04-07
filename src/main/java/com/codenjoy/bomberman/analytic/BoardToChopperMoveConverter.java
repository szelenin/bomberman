package com.codenjoy.bomberman.analytic;

import com.codenjoy.bomberman.ElementState;
import com.codenjoy.bomberman.GameState;
import com.codenjoy.bomberman.utils.Point;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by szelenin on 4/7/14.
 */

public class BoardToChopperMoveConverter {
    private File outFile;
    private GameState previousState;

    public BoardToChopperMoveConverter(String outFilePath) {
        this.outFile = new File(outFilePath);
    }

    public void processState(String boardString) throws IOException {
        GameState state = new GameState(boardString);
        String previousMove = "NA";
        if (previousState != null) {
            previousMove = calcPreviousMove(state);
        }
        FileUtils.writeStringToFile(outFile, previousMove +
                ",0,0,0,0,NA\n", true);
        previousState = state;
    }

    private String calcPreviousMove(GameState currentState) {
        Point prevPosition = previousState.getChoppers().get(0).position;
        Point currentPosition = currentState.getChoppers().get(0).position;
        return direction(prevPosition, currentPosition);
    }

    private String direction(Point prevPosition, Point currentPosition) {
        int yDiff = prevPosition.getY() - currentPosition.getY();

        return yDiff < 0 ? "D" : "U";
    }
}
