package com.codenjoy.bomberman;

import com.codenjoy.bomberman.utils.BitElements;
import com.codenjoy.bomberman.utils.Board;
import com.codenjoy.bomberman.utils.LengthToXY;
import com.codenjoy.bomberman.utils.Point;

import java.util.*;

import static com.codenjoy.bomberman.Element.*;

public class GameState {
    private List<BomberState> bombs = new ArrayList<BomberState>();
    //    private final Board board;
    private List<BomberState> otherBombers = new ArrayList<BomberState>();
    private List<BomberState> choppers = new ArrayList<BomberState>();
    private List<BomberState> explosion = new ArrayList<BomberState>();

    private BomberState bomber;

    private LengthToXY toXY;
    //0-WALL, 1-DESTROY_WALL, 2-DESTROYED_WALL
    private BitElements[] walls = new BitElements[3];

    public GameState(String board) {
        this(board, false);
    }

    public GameState(String boardString, boolean oneLine) {
        if (!oneLine) {
            boardString = boardString.replaceAll("\n", "");
        }
        List<Character> bomberStates = Arrays.asList(BOMBERMAN.getChar(), BOMB_BOMBERMAN.getChar(), DEAD_BOMBERMAN.getChar());
        List<Character> chopperStates = Arrays.asList(MEAT_CHOPPER.getChar(), DEAD_MEAT_CHOPPER.getChar());
        List<Character> otherBombers = Arrays.asList(OTHER_BOMBERMAN.getChar(), OTHER_BOMB_BOMBERMAN.getChar(), OTHER_DEAD_BOMBERMAN.getChar());
        List<Character> bombs = Arrays.asList(BOMB_TIMER_5.getChar(), BOMB_TIMER_4.getChar(), BOMB_TIMER_3.getChar(), BOMB_TIMER_2.getChar(), BOMB_TIMER_1.getChar(), BOOM.getChar());
        for (int i = 0; i < 3; i++) {
            walls[i] = new BitElements(boardString.length());
        }

        Board board = new Board(boardString, oneLine);
        toXY = new LengthToXY(board.boardSize());
        for (int i = 0; i < boardString.length(); i++) {
            Point currentPosition = toXY.getXY(i);
            char elementChar = boardString.charAt(i);
            int wallPosition = wallPosition(elementChar);
            if (wallPosition >= 0) {
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

            if (bombs.contains(elementChar)) {
                this.bombs.add(new BomberState(currentPosition, Element.valueOf(elementChar)));
            }
        }

    }

    public GameState(GameState gameState) {
        this.otherBombers = new ArrayList<BomberState>();
        for (BomberState otherBomber : gameState.otherBombers) {
            this.otherBombers.add(otherBomber.getCopy());
        }
        this.bomber = gameState.bomber.getCopy();
        this.choppers = new ArrayList<BomberState>();
        for (BomberState chopper : gameState.choppers) {
            choppers.add(chopper.getCopy());
        }
        for (BomberState bomb : gameState.bombs) {
            bombs.add(bomb.getCopy());
        }

        this.walls = gameState.walls.clone();
        for (int i = 0; i < walls.length; i++) {
            walls[i] = gameState.walls[i].getCopy();
        }
        toXY = new LengthToXY((int) Math.sqrt(gameState.walls[0].getSize()));
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
        ArrayList<Action> result = new ArrayList<Action>(Arrays.asList(Action.values()));

        for (Action action : Action.values()) {
            int x = action.changeX(bomber.position.getX());
            int y = action.changeY(bomber.position.getY());
            int bitNo = toXY.getLength(x, y);
            for (int i = 0; i < 2; i++) {
                if (walls[i].getBit(bitNo)) {
                    result.remove(action);
                }
                if (onOtherBomber(x, y)) {
                    result.remove(action);
                }
            }
        }
        return result;
    }

    private boolean onOtherBomber(int x, int y) {
        for (BomberState otherBomber : otherBombers) {
            if (otherBomber.position.getX() == x && otherBomber.position.getY() == y && !otherBomber.isDead()) {
                return true;
            }
        }
        return false;
    }

    public GameState generateSuccessor(Action action) {
        if (!getLegalActions().contains(action)) {
            throw new IllegalArgumentException("Can't do " + action.name());
        }

        GameState newGameState = new GameState(this);
        newGameState.bomber.move(action);
        Iterator<BomberState> iterator = newGameState.bombs.iterator();
        while (iterator.hasNext()) {
            BomberState bomb = iterator.next();

        }
        for (BomberState bomb : newGameState.bombs) {
            bomb.changeState(Element.values()[bomb.state.ordinal() + 1]);
            if (bomb.state == Element.BOOM) {
                for (int i = -3; i <= 3; i++) {
                    int x = bomb.position.getX() + i;
                    Element elementByX = newGameState.at(x, bomb.position.getY());
                    if (elementByX != Element.WALL) {
                        newGameState.explosion.add(new BomberState(new Point(x, bomb.position.getY()), BOOM));
                    }
                    int y = bomb.position.getX() + i;
                    Element elementByY = newGameState.at(bomb.position.getX(), y);
                    if (elementByY != Element.WALL) {
                        newGameState.explosion.add(new BomberState(new Point(bomb.position.getX(), y), BOOM));
                    }
                }
            }
        }
        if (action == Action.ACT) {
            newGameState.bombs.add(new BomberState(bomber.position, BOMB_TIMER_5));
        }
        return newGameState;

    }

    Point getBomber() {
        return bomber.position;
    }

    Element at(int x, int y) {
        for (BomberState chopper : choppers) {
            if (x == chopper.position.getX() && y == chopper.position.getY()) {
                return chopper.state;
            }
        }
        for (BomberState otherBomber : otherBombers) {
            if (x == otherBomber.position.getX() && y == otherBomber.position.getY()) {
                return otherBomber.state;
            }
        }
        for (BomberState bomb : bombs) {
            if (x == bomb.position.getX() && y == bomb.position.getY()) {
                return bomb.state;
            }
        }
        for (BomberState boom : explosion) {
            if (x == boom.position.getX() && y == boom.position.getY()) {
                return boom.state;
            }
        }

        int bitNo = toXY.getLength(x, y);
        for (int i = 0; i < 3; i++) {
            if (walls[i].getBit(bitNo)) {
                return Element.values()[Element.WALL.ordinal() + i];
            }
        }
        if (bomber.position.getX() == x && bomber.position.getY() == y) {
            return bomber.state;
        }
        return Element.SPACE;
    }

    private class BomberState {
        public Point position;
        public Element state;

        private BomberState(Point position, Element state) {
            this.position = position;
            this.state = state;
        }

        public boolean isDead() {
            return state == Element.OTHER_DEAD_BOMBERMAN;
        }

        public void move(Action action) {
            this.position = new Point(action.changeX(position.getX()), action.changeY(position.getY()));
        }

        public BomberState getCopy() {
            return new BomberState(new Point(position), state);
        }

        public void changeState(Element newState) {
            this.state = newState;
        }
    }
}
