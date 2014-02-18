package com.codenjoy.bomberman;

import com.codenjoy.bomberman.utils.BitElements;
import com.codenjoy.bomberman.utils.Board;
import com.codenjoy.bomberman.utils.LengthToXY;
import com.codenjoy.bomberman.utils.Point;
import it.unimi.dsi.fastutil.chars.Char2LongOpenHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codenjoy.bomberman.Element.*;

/**
 * Created by szelenin on 2/18/14.
 */
public class GameState {
    private final Board board;
    private final List<BomberState> otherBombers = new ArrayList<BomberState>();
    private List<BomberState> choppers = new ArrayList<BomberState>();
    private BomberState bomber;

    private LengthToXY toXY;
    //0-WALL, 1-DESTROY_WALL, 2-DESTROYED_WALL
    private BitElements[] walls = new BitElements[3];

    private List<Element> canMoveElements = Arrays.asList(SPACE, BOMBERMAN, BOMB_BOMBERMAN, DESTROYED_WALL, DEAD_MEAT_CHOPPER,
            BOMB_TIMER_1, BOMB_TIMER_2, BOMB_TIMER_3, BOMB_TIMER_4, BOMB_TIMER_5,
            DEAD_BOMBERMAN, BOOM, OTHER_DEAD_BOMBERMAN);

    public GameState(String board) {
        this(board, false);
    }

    public GameState(String board, boolean oneLine) {
        List<Character> bomberStates = Arrays.asList(BOMBERMAN.getChar(), BOMB_BOMBERMAN.getChar(), DEAD_BOMBERMAN.getChar());
        List<Character> chopperStates = Arrays.asList(MEAT_CHOPPER.getChar(), DEAD_MEAT_CHOPPER.getChar());
        List<Character> otherBombers = Arrays.asList(OTHER_BOMBERMAN.getChar(), OTHER_BOMB_BOMBERMAN.getChar(), OTHER_DEAD_BOMBERMAN.getChar());
        for (int i = 0; i < 3; i++) {
            walls[i] = new BitElements(board.length());
        }

        this.board = new Board(board, oneLine);
        toXY = new LengthToXY(this.board.boardSize());
        for (int i = 0; i < board.length(); i++) {
            Point currentPosition = toXY.getXY(i);
            char elementChar = board.charAt(i);
            int wallPosition = wallPosition(elementChar);
            if (wallPosition > 0) {
                walls[wallPosition].setBit(i);
                continue;
            }

            if (bomberStates.contains(elementChar)) {
                this.bomber = new BomberState(currentPosition, Element.valueOf(elementChar));
                continue;
            }

            if (chopperStates.contains(elementChar)) {
                this.choppers.add(new BomberState(currentPosition, Element.valueOf(elementChar)));
                continue;
            }

            if (otherBombers.contains(elementChar)) {
                this.otherBombers.add(new BomberState(currentPosition, Element.valueOf(elementChar)));
                continue;
            }

        }

    }


    private int wallPosition(char elementChar) {
        if (elementChar == Element.WALL.getChar()) {
            return 0;
        }
        if (elementChar == Element.DESTROY_WALL.getChar()) {
            return 1;
        }
        if (elementChar == Element.DESTROYED_WALL.getChar()) {
            return 2;
        }
        return -1;
    }

    public List<Action> getLegalActions() {
        List<Action> result = new ArrayList<Action>();
        for (Action action : Action.values()) {
            int x = action.changeX(board.getBomberman().getX());
            int y = action.changeY(board.getBomberman().getY());

            Element element = board.getAt(x, y);

            if (canMoveElements.contains(element)) {
                result.add(action);
            }
        }
        return result;
    }

    public GameState generateSuccessor(Action action) {
        if (!getLegalActions().contains(action)) {
            throw new IllegalArgumentException("Can't do $action!");
        }
        char[] boardChars = board.board.toCharArray();

        int bomberCharAt = toXY.getLength(board.getBomberman());
        int newBomberCharAt = toXY.getLength(action.changeX(board.getBomberman().getX()), action.changeY(board.getBomberman().getY()));

        char currentBomberChar = boardChars[bomberCharAt];
        boardChars[bomberCharAt] = currentBomberChar == BOMB_BOMBERMAN.getChar() ? BOMB_TIMER_5.getChar() : SPACE.getChar();
        boardChars[newBomberCharAt] = bomberManChar(action);

        for (Point bomb : board.getBombs()) {
            int bombPosition = toXY.getLength(bomb);
            char boardChar = boardChars[bombPosition];
            boardChars[bombPosition] = (char) (boardChar - 1);
            if (boardChars[bombPosition] == '0') {
                for (int i = -3; i <= 3;i++) {
                    boardChars[toXY.getLength(bomb.getX() + i, bomb.getY())] = BOOM.getChar();
                    boardChars[toXY.getLength(bomb.getX(), bomb.getY() + i)] = BOOM.getChar();
                }
            }
        }

        return new GameState(new String(boardChars), true);

    }

    private char bomberManChar(Action action) {
        char bomberChar = BOMBERMAN.getChar();
        if (action == Action.ACT) {
            bomberChar = BOMB_BOMBERMAN.getChar();
        }
        return bomberChar;
    }

    Point getBomber() {
        return board.getBomberman();
    }

    Element at(int x, int y) {
        return board.getAt(x, y);
    }

    @Override
    public String toString() {
        return board.toString();
    }

    private class BomberState {
        private final Point position;
        private final Element state;

        private BomberState(Point position, Element state) {
            this.position = position;
            this.state = state;
        }
    }
}
