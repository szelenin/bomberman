package com.codenjoy.bomberman;

/**
 * Created by szelenin on 3/25/14.
 */
public class AstarBombChoppersAgent extends Agent {
    public AstarBombChoppersAgent() {
        super(new AstarSearch(new NearestChopperToBombHeuristic()));
    }

    @Override
    public Action getAction(GameState state) {
        return null;
    }

}
