package com.codenjoy.bomberman;

import org.javatuples.Pair;

import java.util.List;

/**
 * Created by szelenin on 4/3/14.
 */
public class Utils {
    public static final int CHOPPER_NEAR_REVENUE = 20;
    public static final int DEAD_CHOPPER_REVENUE = -200;
    public static final int DEAD_BOMBER_REVENUE = 1000;
    public static final int STAY_ON_BOMB_REVENUE = 400;
    public static final int ACT_NEAR_CHOPPER_REVENUE = -50;

    static int distToclosestChopper(GameState state) {
        return minDistToElement(state, state.getChoppers()).getValue1();
    }

    public static Pair<ElementState, Integer> minDistToElement(GameState state, List<ElementState> elements) {
        if (elements.isEmpty()) {
            return null;
        }
        int minDist = Integer.MAX_VALUE;
        ElementState result = null;
        for (ElementState element : elements) {
            int dist = dist(state, element);
            if (dist < minDist) {
                minDist = dist;
                result = element;
            }
        }
        return new Pair<ElementState, Integer>(result, minDist);
    }

    static int countChoppersWithin(GameState state, int radius) {
        return countElementsWithin(state, radius, state.getChoppers());
    }

    static int countBombsWithin(GameState state, int radius) {
        return countElementsWithin(state, radius, state.getBombs());
    }

    static Pair<ElementState, Integer> nearestBomb(GameState state) {
        return minDistToElement(state, state.getBombs());
    }

    private static int countElementsWithin(GameState state, int radius, List<ElementState> elements) {
        int result = 0;
        for (ElementState element : elements) {
            if (dist(state, element) <= radius) {
                result++;
            }
        }
        return result;
    }

    private static int dist(GameState state, ElementState chopper) {
        return Math.abs(chopper.position.getX() - state.getBomber().getX()) + Math.abs(chopper.position.getY() - state.getBomber().getY());
    }

    public static Pair<ElementState, Integer> nearestChopper(GameState state) {
        return minDistToElement(state, state.getChoppers());
    }
}
