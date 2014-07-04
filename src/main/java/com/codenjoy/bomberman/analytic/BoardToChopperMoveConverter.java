package com.codenjoy.bomberman.analytic;

import com.codenjoy.bomberman.Action;
import com.codenjoy.bomberman.Element;
import com.codenjoy.bomberman.ElementState;
import com.codenjoy.bomberman.GameState;
import com.codenjoy.bomberman.utils.Point;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by szelenin on 4/7/14.
 */

public class BoardToChopperMoveConverter {
    private File outFile;
    private GameState previousState;
    private List<Move> chopperMoves = new ArrayList<Move>();
    private Map<Element, String> elementMap = new HashMap<Element, String>();

    private static final Logger logger = LogManager.getLogger(BoardToChopperMoveConverter.class);

    public BoardToChopperMoveConverter(String outFilePath) throws IOException {
        this.outFile = new File(outFilePath);
        elementMap.put(Element.WALL, "W");
        elementMap.put(Element.DESTROY_WALL, "W");
        elementMap.put(Element.SPACE, "0");
        elementMap.put(Element.MEAT_CHOPPER, "C");
        elementMap.put(Element.DEAD_MEAT_CHOPPER, "C");
        elementMap.put(Element.OTHER_BOMBERMAN, "B");
        elementMap.put(Element.BOMBERMAN, "B");
        elementMap.put(Element.BOMB_TIMER_1, "1");
        elementMap.put(Element.BOMB_TIMER_2, "2");
        elementMap.put(Element.BOMB_TIMER_3, "3");
        elementMap.put(Element.BOMB_TIMER_4, "4");
        elementMap.put(Element.BOOM, "BOOM");
        FileUtils.writeStringToFile(outFile, "Previous,Up,Right,Down,Left,Next\n", false);
    }

    public void processState(String boardString) throws IOException {
        GameState state = new GameState(boardString);
        logger.trace("Starting board {}", state);
        List<Move> moves = getChopperMoves(state);
        for (Move move : moves) {
            if (!move.previousInitialized() || !move.nextInitialized()) {
                continue;
            }
            FileUtils.writeStringToFile(outFile, move.previous + ',' + occupied(move.prevMove.chopper, previousState) + ',' + move.next + "\n", true);
        }
        chopperMoves = removeDead(moves);
        previousState = state;
    }

    private List<Move> removeDead(List<Move> moves) {
        Iterator<Move> iterator = moves.iterator();
        while (iterator.hasNext()) {
            Move next = iterator.next();
            if (next.prevMove == null) {
                continue;
            }
            if (next.prevMove.chopper.isDead()) {
                iterator.remove();
            }
        }
        return moves;
    }

    private List<VariableValue> findPreviousPotentialMoves(GameState state) {
        List<VariableValue> result = new ArrayList<VariableValue>();
        List<ElementState> choppers = state.getChoppers();
        for (ElementState chopper : choppers) {
            result.add(new VariableValue(chopper, findPotentialMoves(chopper)));
        }
        for (Move prevMove : chopperMoves) {
            if (prevMove.chopper.isDead()) {
                result.add(new VariableValue(prevMove.chopper, Arrays.asList(prevMove)));
            }
        }
        return result;
    }

    private List<Move> getChopperMoves(GameState state) {
        List<Move> result = new ArrayList<Move>();
        List<VariableValue> previousMoves = resolveConstraints(findPreviousPotentialMoves(state));
        logger.trace("PreviousMoves: {}", previousMoves);
        for (VariableValue variableValue : previousMoves) {
            if (previousState == null) {
                result.add(new Move(null, null, variableValue.chopper, null));
                continue;
            }

            if (variableValue.previousMoves.isEmpty()) {
                logger.warn("Potential frame drop. Variable {}. Board: {}", variableValue, state);
                result.add(new Move(null, null, variableValue.chopper, null));
                continue;
            }

            Move prevMove = variableValue.previousMoves.get(0);
            if (variableValue.previousMoves.size() > 1) {
                logger.error("Multiple values for var {}. Board: {}", variableValue, state);
            }
            if (!prevMove.previousInitialized()) {
                Move currentMove = new Move(calcMove(prevMove.chopper, variableValue.chopper), null, variableValue.chopper, prevMove);
                result.add(currentMove);
                continue;
            }
            if (prevMove.previousInitialized() && !prevMove.nextInitialized()) {
                result.add(new Move(prevMove.previous, calcMove(prevMove.chopper, variableValue.chopper), variableValue.chopper, prevMove));
                continue;
            }
            result.add(new Move(prevMove.next, calcMove(prevMove.chopper, variableValue.chopper), variableValue.chopper, prevMove));
        }
        return result;
    }

    private List<VariableValue> resolveConstraints(List<VariableValue> previousPotentialMoves) {
        List<VariableValue> resolved = new ArrayList<VariableValue>();
        List<VariableValue> unresolved = new ArrayList<VariableValue>();
        for (VariableValue potentialMove : previousPotentialMoves) {
            if (potentialMove.previousMoves.size() <= 1) {
                resolved.add(potentialMove);
            } else {
                unresolved.add(potentialMove);
            }
        }
        for (VariableValue unresolvedVar : unresolved) {
            Iterator<Move> iterator = unresolvedVar.previousMoves.iterator();
            while (iterator.hasNext()) {
                Move move = iterator.next();
                for (VariableValue resolvedVar : resolved) {
                    if (resolvedVar.previousMoves.isEmpty()) {
                        logger.warn("No previous move for choper {}", resolvedVar.chopper);
//                        continue;
                    }
                    if (move.chopper.position.equals(resolvedVar.previousMoves.get(0).chopper.position)) {
                        iterator.remove();
                    }
                }
            }
            if (unresolvedVar.previousMoves.size() == 1) {
                resolved.add(unresolvedVar);
            }
        }
        return resolved;
    }

    private List<Move> findPotentialMoves(ElementState chopper) {
        ArrayList<Move> potentialMoves = new ArrayList<Move>();
        for (Move move : chopperMoves) {
            ElementState prevChopper = move.chopper;
            if (prevChopper.equals(chopper)) {
                logger.trace("Var: {}, Domain: [{}]", chopper, move);
                return Collections.singletonList(move);
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
        logger.trace("Var: {}, Domain: {}", chopper, potentialMoves);
        return potentialMoves;
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
        int yDiff = prevPosition.getY() - currentPosition.getY();
        int xDiff = prevPosition.getX() - currentPosition.getX();
        if (yDiff == 0 && xDiff == 0) {
            return currentChopper.isDead() ? "DEATH" : "S";
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
        public Move prevMove;

        public Move(String previous, String next, ElementState chopper, Move prevMove) {
            this.previous = previous;
            this.next = next;
            this.chopper = chopper;
            this.prevMove = prevMove;
        }


        public boolean previousInitialized() {
            return previous != null;
        }

        public boolean nextInitialized() {
            return next != null;
        }

        @Override
        public String toString() {
            return (prevMove == null ? "null" : prevMove.chopper) + "->" + chopper;
        }
    }

    class VariableValue {
        public ElementState chopper;
        public List<Move> previousMoves;

        private VariableValue(ElementState chopper, List<Move> previousMoves) {
            this.chopper = chopper;
            this.previousMoves = previousMoves;
        }

        @Override
        public String toString() {
            return String.format("Var: %s, Domain: %s", chopper, previousMoves);
        }
    }
}
