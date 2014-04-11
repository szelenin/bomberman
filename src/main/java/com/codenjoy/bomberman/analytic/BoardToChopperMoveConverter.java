package com.codenjoy.bomberman.analytic;

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
    private String previousMove;
    private String nextMove;

    public BoardToChopperMoveConverter(String outFilePath) throws IOException {
        this.outFile = new File(outFilePath);
        FileUtils.writeStringToFile(outFile, "Previous,UpOccupied,RightOccupied,DownOccupied,LeftOccupied,Next\n", false);
    }

    public void processState(String boardString) throws IOException {
        GameState state = new GameState(boardString);
        if (previousMove != null && nextMove == null) {
            nextMove = calcMove(state);
        }
        if (previousMove == null && previousState != null) {
            previousMove = calcMove(state);
        }

        if (this.previousMove != null && this.nextMove != null) {
            FileUtils.writeStringToFile(outFile, previousMove + ",0,0,0,0," + nextMove + "\n", true);
            previousMove = nextMove;
            nextMove = calcMove(state);
        }
        previousState = state;
    }

    private String calcMove(GameState currentState) {
        Point prevPosition = previousState.getChoppers().get(0).position;
        Point currentPosition = currentState.getChoppers().get(0).position;
        return direction(prevPosition, currentPosition);
    }

    private String direction(Point prevPosition, Point currentPosition) {
        int yDiff = prevPosition.getY() - currentPosition.getY();

        return yDiff < 0 ? "D" : "U";
    }
}
