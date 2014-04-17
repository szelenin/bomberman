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
        ArrayList<Move> chopperMovesCopy = new ArrayList<Move>(chopperMoves);
        for (int i = 0; i < state.getChoppers().size(); i++) {
            ElementState currentChopper = state.getChoppers().get(i);
            Move move = getChopperMove(currentChopper, chopperMovesCopy);
            ElementState prevChopper = move.chopper;

            if (move.previousInitialized() && !move.nextInitialized()) {
                move.next = calcMove(prevChopper, currentChopper);
                move.chopper = currentChopper;
                FileUtils.writeStringToFile(outFile, move.previous + ",0,0,0,0," + move.next + "\n", true);
                continue;
            }

            if (!move.previousInitialized() && prevChopper != null) {
                String prevMove = calcMove(prevChopper, currentChopper);
                chopperMoves.add(new Move(prevMove, currentChopper));
                continue;
            }

            if (move.previousInitialized() && move.nextInitialized()) {
                move.previous = move.next;
                move.next = calcMove(prevChopper, currentChopper);
                move.chopper = currentChopper;
                FileUtils.writeStringToFile(outFile, move.previous + ",0,0,0,0," + move.next + "\n", true);
            }
        }
        previousState = state;
    }

    private ElementState findPrevChopper(ElementState currentChopper) {
        if (previousState == null) {
            return null;
        }
        ArrayList<ElementState> potentialChoppers = new ArrayList<ElementState>();
        for (ElementState chopper : previousState.getChoppers()) {
            if (manhattanDist(currentChopper, chopper) <= 1) {
                potentialChoppers.add(chopper);
            }
        }

        ElementState result = potentialChoppers.get(0);
        return result;
    }

    private int manhattanDist(ElementState currentChopper, ElementState prevChopper) {
        return Math.abs(currentChopper.position.getX() - prevChopper.position.getX()) +
                Math.abs(currentChopper.position.getY() - prevChopper.position.getY());
    }

    private Move getChopperMove(ElementState currentChopper, List<Move> chopperMoves) {
        if (chopperMoves.isEmpty() && previousState == null) {
            return new Move(null, null);
        }
        if (chopperMoves.isEmpty() && previousState != null) {
            ElementState prevChopper = findPrevChopper(currentChopper);
            return new Move(null, prevChopper);
        }

        ArrayList<Move> potentialMoves = new ArrayList<Move>();
        for (Move move : chopperMoves) {
            if (manhattanDist(currentChopper, move.chopper) <= 1) {
                potentialMoves.add(move);
            }
        }
        return potentialMoves.get(0);
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
        public ElementState chopper;

        public Move(String previous, ElementState chopper) {
            this.previous = previous;
            this.chopper = chopper;
        }

        public boolean previousInitialized() {
            return previous != null;
        }

        public boolean nextInitialized() {
            return next != null;
        }
    }
}
