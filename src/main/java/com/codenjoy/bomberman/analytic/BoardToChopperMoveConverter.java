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
        ArrayList<Move> chopperMovesCopy = new ArrayList<Move>(chopperMoves);
        for (int i = 0; i < state.getChoppers().size(); i++) {
            ElementState currentChopper = state.getChoppers().get(i);
            Move move = getChopperMove(currentChopper, chopperMovesCopy);
            ElementState prevChopper = move.chopper;
            if (prevChopper == null) {
                continue;
            }

            if (move.previousInitialized() && !move.nextInitialized()) {
                move.next = calcMove(prevChopper, currentChopper);
                move.chopper = currentChopper;
                FileUtils.writeStringToFile(outFile, move.previous + ',' + occupied(prevChopper, previousState) + ',' + move.next + "\n", true);
                continue;
            }

            if (!move.previousInitialized()) {
                String prevMove = calcMove(prevChopper, currentChopper);
                chopperMoves.add(new Move(prevMove, currentChopper));
                continue;
            }

            if (move.previousInitialized() && move.nextInitialized()) {
                move.previous = move.next;
                move.next = calcMove(prevChopper, currentChopper);
                move.chopper = currentChopper;
                FileUtils.writeStringToFile(outFile, move.previous + ',' + occupied(prevChopper, previousState) + ',' + move.next + "\n", true);
            }
        }
        previousState = state;
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

    private <T> T findPrevChopper(ElementState currentChopper, List<T> elements, ListElementAdaptor<T> adaptor) {
        ArrayList<T> potentialChoppers = new ArrayList<T>();
        for (T element : elements) {
            ElementState prevChopper = adaptor.getElement(element);
            List<Action> legalActions = previousState.getLegalActions(prevChopper);
            for (Action action : legalActions) {
                int x = action.changeX(prevChopper.position.getX());
                int y = action.changeY(prevChopper.position.getY());
                if (currentChopper.position.getX() == x && currentChopper.position.getY() == y) {
                    potentialChoppers.add(element);
                }
            }
/*
            if (manhattanDist(currentChopper, prevChopper) <= 1) {
                potentialChoppers.add(element);
            }
*/
        }
        return potentialChoppers.get(0);
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
            ElementState prevChopper = findPrevChopper(currentChopper, previousState.getChoppers(), new ElementAdaptor());
            return new Move(null, prevChopper);
        }
        return findPrevChopper(currentChopper, chopperMoves, new MoveAdaptor());
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
