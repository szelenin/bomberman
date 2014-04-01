package com.codenjoy.bomberman;

/**
 * Created by szelenin on 3/25/14.
 */
public abstract class Agent {
    private SearchFunction searchFunction;

    protected Agent(SearchFunction searchFunction) {
        this.searchFunction = searchFunction;
    }

    public Action getAction(GameState state) {
        return searchFunction.search(new Problem(state)).get(0);
    }
}
