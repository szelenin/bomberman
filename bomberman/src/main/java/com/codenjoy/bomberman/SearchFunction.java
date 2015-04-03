package com.codenjoy.bomberman;

import java.util.List;

/**
 * Created by szelenin on 4/1/14.
 */
public interface SearchFunction {
    List<Action> search(Problem problem);
}
