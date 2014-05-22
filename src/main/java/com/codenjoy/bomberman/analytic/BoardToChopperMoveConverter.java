package com.codenjoy.bomberman.analytic;

import com.codenjoy.bomberman.Action;
import com.codenjoy.bomberman.Element;
import com.codenjoy.bomberman.ElementState;
import com.codenjoy.bomberman.GameState;
import com.codenjoy.bomberman.utils.Point;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by szelenin on 4/7/14.
 */

public class BoardToChopperMoveConverter {
    private File outFile;
    private GameState previousState;
    private List<Move> chopperMoves = new ArrayList<Move>();
    private Map<Element, Character> elementMap = new HashMap<Element, Character>();

    public BoardToChopperMoveConverter(String outFilePath) throws IOException {
        this.outFile = new File(outFilePath);
        elementMap.put(Element.WALL, 'W');
        elementMap.put(Element.DESTROY_WALL, 'W');
        elementMap.put(Element.SPACE, '0');
        elementMap.put(Element.MEAT_CHOPPER, 'C');
        FileUtils.writeStringToFile(outFile, "Previous,Up,Right,Down,Left,Next\n", false);
    }

    public void processState(String boardString) throws IOException {
        GameState state = new GameState(boardString);

        List<Move> moves = getChopperMoves(state);
        for (Move move : moves) {
            if (!move.previousInitialized() || !move.nextInitialized()) {
                continue;
            }
            Move prevMove = findPrevMove(move.chopper);
            FileUtils.writeStringToFile(outFile, move.previous + ',' + occupied(prevMove.chopper, previousState) + ',' + move.next + "\n", true);
        }
        chopperMoves = moves;
        previousState = state;
    }

    private List<Move> getChopperMoves(GameState state) {
        List<ElementState> choppers = state.getChoppers();
        List<Move> result = new ArrayList<Move>();
        for (ElementState chopper : choppers) {
            if (previousState == null) {
                result.add(new Move(null, chopper));
                continue;
            }
            Move prevMove = findPrevMove(chopper);
            if (!prevMove.previousInitialized()) {
                result.add(new Move(calcMove(prevMove.chopper, chopper), chopper));
                continue;
            }
            if (prevMove.previousInitialized() && !prevMove.nextInitialized()) {
                result.add(new Move(prevMove.previous, calcMove(prevMove.chopper, chopper), chopper));
                continue;
            }
            result.add(new Move(prevMove.next, calcMove(prevMove.chopper, chopper), chopper));
        }
        return result;
    }

    private Move findPrevMove(ElementState chopper) {
        ArrayList<Move> potentialMoves = new ArrayList<Move>();
        for (Move move : chopperMoves) {
            ElementState prevChopper = move.chopper;
            if (prevChopper.equals(chopper)) {
                return move;
            }
            List<Action> legalActions = previousState.getLegalActions(prevChopper);
            for (Action action : legalActions) {
                int x = action.changeX(prevChopper.position.getX());
                int y = action.changeY(prevChopper.position.getY());
                if (chopper.position.getX() == x && chopper.position.getY() == y) {
                    potentialMoves.add(move);
                }
            }
        }
        assert potentialMoves.size() == 1;
        return potentialMoves.get(0);
    }


    private String occupied(ElementState chopper, GameState state) {
        Element up = state.at(chopper.position.getX(), chopper.position.getY() - 1);
        Element right = state.at(chopper.position.getX() + 1, chopper.position.getY());
        Element down = state.at(chopper.position.getX(), chopper.position.getY() + 1);
        Element left = state.at(chopper.position.getX() - 1, chopper.position.getY());
        StringBuilder sb = new StringBuilder();
        sb.append(elementMap.get(up)).append(",")
                .append(elementMap.get(right)).append(",")
                .append(elementMap.get(down)).append(",")
                .append(elementMap.get(left));
        return sb.toString();
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
            this(previous, null, chopper);
        }

        public Move(String previous, String next, ElementState chopper) {
            this.previous = previous;
            this.next = next;
            this.chopper = chopper;
        }

        public boolean previousInitialized() {
            return previous != null;
        }

        public boolean nextInitialized() {
            return next != null;
        }
    }

    private interface ListElementAdaptor<T> {
        ElementState getElement(T collectionElement);
    }

    private class MoveAdaptor implements ListElementAdaptor<Move> {

        @Override
        public ElementState getElement(Move move) {
            return move.chopper;
        }
    }

    private class ElementAdaptor implements ListElementAdaptor<ElementState> {

        @Override
        public ElementState getElement(ElementState element) {
            return element;
        }
    }
}
