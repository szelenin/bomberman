package com.codenjoy.minesweeper;

/**
 * Created by szelenin on 4/21/2015.
 */
public class FlagAction extends Action {
    public FlagAction(Action direction) {
        super(direction.value, direction.dx, direction.dy, direction.cost + ACT.cost);
    }

    @Override
    public boolean isComposite() {
        return true;
    }
}
