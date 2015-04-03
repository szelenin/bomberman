package com.codenjoy.bomberman;

import java.util.List;

/**
 * Created by szelenin on 3/7/14.
 */
public abstract class ElementStateProvider {
    protected ElementStateProvider(GameState gameState) {
    }

    abstract List<ElementState> add(ElementState elementState);
}
