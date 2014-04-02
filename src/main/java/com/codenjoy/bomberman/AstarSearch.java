package com.codenjoy.bomberman;

import java.util.*;

/**
 * Created by szelenin on 3/28/14.
 */
public class AstarSearch implements SearchFunction {
    private final Heuristic heuristic;

    public AstarSearch(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    private Map<GameState, Integer> costs = new HashMap<GameState, Integer>();

    private int priorityFunction(GameState state) {
        return heuristic.calculate(state) + costs.get(state);
    }

    public List<Action> search(Problem problem) {
        PriorityQueue<Node> fringe = new PriorityQueue<Node>(11, new Comparator<Node>() {
            @Override
            public int compare(Node node1, Node node2) {
                return priorityFunction(node1.state) - priorityFunction(node2.state);
            }
        });

        Set<GameState> closed = new HashSet<GameState>();

        //State, action, parent
        GameState startState = problem.getStartState();
        costs.put(startState, 0);
        fringe.add(new Node(startState, null, null));

        while (true) {
            if (fringe.isEmpty()) {
                return Arrays.asList(Action.ACT);
            }
            Node node = fringe.poll();
            if (problem.isGoalState(node.state)) {
                return constructPath(node);
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
