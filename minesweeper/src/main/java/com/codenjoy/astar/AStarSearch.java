package com.codenjoy.astar;

import com.codenjoy.minesweeper.Action;
import org.javatuples.Pair;

import java.util.*;

/**
 * Created by szelenin on 4/14/2015.
 */
public class AStarSearch {
    private final Heuristic heuristic;

    public AStarSearch(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    private Map<GameState, Integer> costs = new HashMap<GameState, Integer>();

    private int priorityFunction(GameState state) {
        return heuristic.calculate(state) + costs.get(state);
    }

    public Pair<List<Action>, Integer> search(Problem problem) {
        PriorityQueue<Node> fringe = new PriorityQueue<Node>(11,
                (Node node1, Node node2) -> priorityFunction(node1.state) - priorityFunction(node2.state));

        Set<GameState> closed = new HashSet<>();

        //State, action, parent
        GameState startState = problem.getStartState();
        costs.put(startState, 0);
        fringe.add(new Node(startState, null, null));

        while (true) {
            if (fringe.isEmpty()) {
                return new Pair<>(Arrays.asList(Action.ACT), 0);
            }
            Node node = fringe.poll();
            if (problem.isGoalState(node.state)) {
                List<Action> path = constructPath(node);
                return new Pair<>(path, costs.get(node.state));
            }
            if (!closed.contains(node.state)) {
                closed.add(node.state);
                List<Successor> successors = problem.getSuccessors(node.state);
                for (Successor successor : successors) {
                    int totalCost = costs.get(node.state) + successor.cost;
                    costs.put(successor.state, totalCost);
                    fringe.add(new Node(successor.state, node, successor.action));
                }
            }
        }

    }

    private List<Action> constructPath(Node node) {
        if (node.previousNode != null) {
            List<Action> prev = constructPath(node.previousNode);
            System.out.print(String.format("%s, %d: ", node.action, costs.get(node.state)));
            prev.add(node.action);
            return prev;
        } else {
            return new LinkedList<Action>();
        }
    }

    private class Node {
        public final GameState state;
        public final Node previousNode;
        public final Action action;

        private Node(GameState state, Node previousNode, Action action) {
            this.state = state;
            this.previousNode = previousNode;
            this.action = action;
        }

    }
}

