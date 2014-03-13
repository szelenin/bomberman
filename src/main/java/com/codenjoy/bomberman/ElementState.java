package com.codenjoy.bomberman;

import com.codenjoy.bomberman.utils.Point;

/**
 * Created by szelenin on 3/7/14.
 */
class ElementState {
    public Point position;
    public Element state;

    ElementState(Point position, Element state) {
        this.position = position;
        this.state = state;
    }

    public boolean isDead() {
        return state == Element.OTHER_DEAD_BOMBERMAN || state == Element.DEAD_BOMBERMAN;
    }

    public void move(Action action) {
        this.position = new Point(action.changeX(position.getX()), action.changeY(position.getY()));
    }

    public ElementState getCopy() {
        return new ElementState(new Point(position), state);
    }

    public void changeState(Element newState) {
        this.state = newState;
    }
}
