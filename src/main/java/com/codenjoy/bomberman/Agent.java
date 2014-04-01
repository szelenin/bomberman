package com.codenjoy.bomberman;

import java.util.List;

/**
 * Created by szelenin on 3/25/14.
 */
public abstract class Agent {
    private SearchFunction searchFunction;

    protected Agent(SearchFunction searchFunction) {
        this.searchFunction = searchFunction;
    }

    public Action getAction(GameState state) {
        List<Action> result = searchFunction.search(new Problem(state));
        return result.get(0);
    }
}
