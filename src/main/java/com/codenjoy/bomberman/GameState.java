package com.codenjoy.bomberman;

import com.codenjoy.bomberman.utils.BitElements;
import com.codenjoy.bomberman.utils.Board;
import com.codenjoy.bomberman.utils.LengthToXY;
import com.codenjoy.bomberman.utils.Point;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

import static com.codenjoy.bomberman.Element.*;

public class GameState {
    private List<ElementState> bombs = new ArrayList<ElementState>();
    private List<ElementState> otherBombers = new ArrayList<ElementState>();
    private List<ElementState> choppers = new ArrayList<ElementState>();
    private List<ElementState> explosion = new ArrayList<ElementState>();

    private ElementState bomber;

    private LengthToXY toXY;
    //0-WALL, 1-DESTROY_WALL, 2-DESTROYED_WALL
    private BitElements[] walls = new BitElements[3];
    public static final List<Character> BOMB_TIMER_SYMBOLS = Arrays.asList(BOMB_TIMER_5.getChar(), BOMB_TIMER_4.getChar(), BOMB_TIMER_3.getChar(), BOMB_TIMER_2.getChar(), BOMB_TIMER_1.getChar());
    public static final List<Character> OTHER_BOMBERS_SYMBOLS = Arrays.asList(OTHER_BOMBERMAN.getChar(), OTHER_BOMB_BOMBERMAN.getChar(), OTHER_DEAD_BOMBERMAN.getChar());
    public static final List<Character> CHOPPER_SYMBOLS = Arrays.asList(MEAT_CHOPPER.getChar(), DEAD_MEAT_CHOPPER.getChar());
    public static final List<Character> BOMBER_SYMBOLS = Arrays.asList(BOMBERMAN.getChar(), BOMB_BOMBERMAN.getChar(), DEAD_BOMBERMAN.getChar());
    private static Map<Character, Elements> elementStates = new HashMap<Character, Elements>();

    static {
        for (Character symbol : BOMB_TIMER_SYMBOLS) {
            elementStates.put(symbol, new BombTimers());
        }
        for (Character symbol : OTHER_BOMBERS_SYMBOLS) {
            elementStates.put(symbol, new OtherBombers());
        }
        for (Character symbol : CHOPPER_SYMBOLS) {
            elementStates.put(symbol, new Choppers());
        }
        for (Character symbol : BOMBER_SYMBOLS) {
            elementStates.put(symbol, new Bomber());
        }
        elementStates.put(BOOM.getChar(), new Explosions());
    }

    public GameState(String board) {
        this(board, false);
    }

    public GameState(String boardString, boolean oneLine) {
        if (!oneLine) {
            boardString = boardString.replaceAll("\n", "");
        }

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

            Elements elements = elementStates.get(elementChar);
            if (elements == null) {
                continue;
            }
            elements.add(new ElementState(currentPosition, Element.valueOf(elementChar)), this);
        }
    }

    public GameState(GameState gameState) {
        this.otherBombers = new ArrayList<ElementState>();
        for (ElementState otherBomber : gameState.otherBombers) {
            this.otherBombers.add(otherBomber.getCopy());
        }
        this.bomber = gameState.bomber.getCopy();
        this.choppers = new ArrayList<ElementState>();
        for (ElementState chopper : gameState.choppers) {
            choppers.add(chopper.getCopy());
        }
        for (ElementState bomb : gameState.bombs) {
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
        for (ElementState otherBomber : otherBombers) {
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

        tickAllBombs(newGameState);
        killBomberWhenOnChopper(newGameState);

        if (action == Action.ACT) {
            newGameState.bombs.add(new ElementState(bomber.position, BOMB_TIMER_5));
        }
        return newGameState;

    }

    private void killBomberWhenOnChopper(GameState newGameState) {
        for (ElementState chopper : newGameState.choppers) {
            if (chopper.position.equals(newGameState.bomber.position)) {
                newGameState.bomber.changeState(DEAD_BOMBERMAN);
            }
        }
    }

    private void tickAllBombs(GameState newGameState) {
        Iterator<ElementState> iterator = newGameState.bombs.iterator();
        while (iterator.hasNext()) {
            ElementState bomb = iterator.next();
            bomb.changeState(Element.values()[bomb.state.ordinal() + 1]);
            if (bomb.state == Element.BOOM) {
                explodeBomb(newGameState, bomb);
                iterator.remove();
            }
        }
    }

    private void explodeBomb(GameState newGameState, ElementState bomb) {
        for (int i = -3; i <= 3; i++) {
            int x = bomb.position.getX() + i;
            int y = bomb.position.getX() + i;
            addExplosionIfNoWall(newGameState, x, bomb.position.getY());
            addExplosionIfNoWall(newGameState, bomb.position.getX(), y);
        }
    }

    private void addExplosionIfNoWall(GameState newGameState, int x, int y) {
        Element element = newGameState.at(x, y);
        if (element != Element.WALL) {
            newGameState.explosion.add(new ElementState(new Point(x, y), BOOM));
        }
        Point bomberPosition = newGameState.bomber.position;
        if (bomberPosition.getX() == x && bomberPosition.getY() == y) {
            newGameState.bomber.changeState(Element.DEAD_BOMBERMAN);
        }
        for (ElementState chopper : newGameState.choppers) {
            if (chopper.position.getX() == x && chopper.position.getY() == y) {
                chopper.changeState(Element.DEAD_MEAT_CHOPPER);
            }
        }
    }

    Point getBomber() {
        return bomber.position;
    }

    Element at(int x, int y) {
        if (bomber.position.getX() == x && bomber.position.getY() == y) {
            return bomber.state;
        }
        for (ElementState chopper : choppers) {
            if (x == chopper.position.getX() && y == chopper.position.getY()) {
                return chopper.state;
            }
        }
        for (ElementState otherBomber : otherBombers) {
            if (x == otherBomber.position.getX() && y == otherBomber.position.getY()) {
                return otherBomber.state;
            }
        }
        for (ElementState bomb : bombs) {
            if (x == bomb.position.getX() && y == bomb.position.getY()) {
                return bomb.state;
            }
        }
        for (ElementState boom : explosion) {
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
        return Element.SPACE;
    }

    public boolean isDead() {
        return bomber.isDead();
    }

    public List<ElementState> getChoppers() {
        return choppers;
    }

    public List<ElementState> getBombs() {
        return bombs;
    }

    private interface Elements {
        void add(ElementState elementState, GameState gameState);
    }


    private static class BombTimers implements Elements {
        @Override
        public void add(ElementState elementState, GameState gameState) {
            gameState.bombs.add(elementState);
        }
    }

    private static class OtherBombers implements Elements {
        @Override
        public void add(ElementState elementState, GameState gameState) {
            gameState.otherBombers.add(elementState);
        }
    }

    private static class Choppers implements Elements {
        @Override
        public void add(ElementState elementState, GameState gameState) {
            gameState.choppers.add(elementState);
        }
    }

    private static class Bomber implements Elements {
        @Override
        public void add(ElementState elementState, GameState gameState) {
            gameState.bomber = elementState;
        }
    }

    private static class Explosions implements Elements {
        @Override
        public void add(ElementState elementState, GameState gameState) {
            gameState.explosion.add(elementState);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < toXY.boardSize; y++) {
            for (int x = 0; x < toXY.boardSize; x++) {
                sb.append(at(x, y).getChar());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GameState)) {
            return false;
        }
        GameState other = (GameState) obj;
        return new EqualsBuilder()
                .append(bomber, other.bomber)
                .append(bombs, other.bombs)
                .append(otherBombers, other.otherBombers)
                .append(choppers, other.choppers)
                .append(explosion, other.explosion)
                .append(walls, other.walls)
                .isEquals();
    }

    @Override
    public int hashCode() {
/*
        System.out.println("bomber.hashCode() = " + bomber.hashCode());
        System.out.println("bombs.hashCode() = " + bombs.hashCode());
        System.out.println("otherBombers.hashCode() = " + otherBombers.hashCode());
        System.out.println("choppers.hashCode() = " + choppers.hashCode());
        System.out.println("explosion.hashCode() = " + explosion.hashCode());
        System.out.println("walls.hashCode() = " + new HashCodeBuilder().append(walls).toHashCode());
        System.out.println("------------------------------------");
*/
        return new HashCodeBuilder()
                .append(bomber).append(bombs).append(otherBombers).append(choppers).append(explosion).append(walls).toHashCode();
    }

}
