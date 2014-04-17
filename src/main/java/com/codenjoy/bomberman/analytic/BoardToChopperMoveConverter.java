package com.codenjoy.bomberman.analytic;

import com.codenjoy.bomberman.ElementState;
import com.codenjoy.bomberman.GameState;
import com.codenjoy.bomberman.utils.Point;
import org.apache.commons.io.FileUtils;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by szelenin on 4/7/14.
 */

public class BoardToChopperMoveConverter {
    private File outFile;
    private GameState previousState;
    private boolean previousInitialized;
    private boolean nextInitialized;
    //value0 - previousMove, value1 - nextMove
    private List<Move> chopperMoves = new ArrayList<Move>();

    public BoardToChopperMoveConverter(String outFilePath) throws IOException {
        this.outFile = new File(outFilePath);
        FileUtils.writeStringToFile(outFile, "Previous,UpOccupied,RightOccupied,DownOccupied,LeftOccupied,Next\n", false);
    }

    public void processState(String boardString) throws IOException {
        GameState state = new GameState(boardString);
        for (int i = 0; i < state.getChoppers().size(); i++) {

            Move move = getMove(i);

            ElementState currentChopper = state.getChoppers().get(i);
            ElementState prevChopper = findPrevChopper(i);
            if (move.previousInitialized() && !move.nextInitialized()) {
                move.next = calcMove(prevChopper, currentChopper);
                FileUtils.writeStringToFile(outFile, move.previous + ",0,0,0,0," + move.next + "\n", true);
                continue;
            }

            if (!move.previousInitialized() && prevChopper != null) {
                String prevMove = calcMove(prevChopper, currentChopper);
                chopperMoves.add(new Move(prevMove));
                continue;
            }

            if (move.previousInitialized() && move.nextInitialized()) {
                move.previous = move.next;
                move.next = calcMove(prevChopper, currentChopper);
                FileUtils.writeStringToFile(outFile, move.previous + ",0,0,0,0," + move.next + "\n", true);
            }
        }
        previousState = state;
    }

    private ElementState findPrevChopper(int i) {
        if (previousState == null) {
            return null;
        }
        return previousState.getChoppers().get(i);
    }

    private Move getMove(int i) {
        if (chopperMoves.size() - 1 < i) {
            return new Move(null);
        }
        return chopperMoves.get(i);
    }

    private String calcMove(ElementState prevChopper, ElementState currentChopper) {
        Point prevPosition = prevChopper.position;
        Point currentPosition = currentChopper.position;
        return direction(prevPosition, currentPosition);
    }

    private String direction(Point prevPosition, Point currentPosition) {
        int yDiff = prevPosition.getY() - currentPosition.getY();
        int xDiff = prevPosition.getX() - currentPosition.getX();
        if (yDiff == 0 && xDiff == 0) {
            return "S";
        }
        if (yDiff == 0) {
            return xDiff < 0 ? "R" : "L";
        }
        return yDiff < 0 ? "D" : "U";
    }

    private class Move {
        String previous;
        String next;

        public Move(String previous) {
            this.previous = previous;
        }

        public boolean previousInitialized() {
            return previous != null;
        }

        public boolean nextInitialized() {
            return next != null;
        }
    }
}
