package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.codenjoy.dojo.battlecity.model.Elements.*;
import static com.codenjoy.dojo.services.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(Parameterized.class)
public class ElementsConstructionHitTest {
    private final Elements hitElement;
    private final Elements expectedAfterHit;
    private final Direction shootDirection;

    @Parameterized.Parameters(name = "{index}: Element {0} becomes {1} after shoot {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {CONSTRUCTION, CONSTRUCTION_DESTROYED_DOWN, UP},
                {CONSTRUCTION, CONSTRUCTION_DESTROYED_UP, DOWN},
                {CONSTRUCTION, CONSTRUCTION_DESTROYED_LEFT, RIGHT},
                {CONSTRUCTION, CONSTRUCTION_DESTROYED_RIGHT, LEFT},

                {CONSTRUCTION_DESTROYED_DOWN, CONSTRUCTION_DESTROYED_DOWN_TWICE, UP},
                {CONSTRUCTION_DESTROYED_DOWN, CONSTRUCTION_DESTROYED_UP_DOWN, DOWN},
                {CONSTRUCTION_DESTROYED_DOWN, CONSTRUCTION_DESTROYED_DOWN_LEFT, RIGHT},
                {CONSTRUCTION_DESTROYED_DOWN, CONSTRUCTION_DESTROYED_DOWN_RIGHT, LEFT},

                {CONSTRUCTION_DESTROYED_UP, CONSTRUCTION_DESTROYED_UP_DOWN, UP},
                {CONSTRUCTION_DESTROYED_UP, CONSTRUCTION_DESTROYED_UP_TWICE, DOWN},
                {CONSTRUCTION_DESTROYED_UP, CONSTRUCTION_DESTROYED_UP_LEFT, RIGHT},
                {CONSTRUCTION_DESTROYED_UP, CONSTRUCTION_DESTROYED_RIGHT_UP, LEFT},

                {CONSTRUCTION_DESTROYED_LEFT, CONSTRUCTION_DESTROYED_DOWN_LEFT, UP},
                {CONSTRUCTION_DESTROYED_LEFT, CONSTRUCTION_DESTROYED_UP_LEFT, DOWN},
                {CONSTRUCTION_DESTROYED_LEFT, CONSTRUCTION_DESTROYED_LEFT_TWICE, RIGHT},
                {CONSTRUCTION_DESTROYED_LEFT, CONSTRUCTION_DESTROYED_LEFT_RIGHT, LEFT},

                {CONSTRUCTION_DESTROYED_RIGHT, CONSTRUCTION_DESTROYED_DOWN_RIGHT, UP},
                {CONSTRUCTION_DESTROYED_RIGHT, CONSTRUCTION_DESTROYED_RIGHT_UP, DOWN},
                {CONSTRUCTION_DESTROYED_RIGHT, CONSTRUCTION_DESTROYED_LEFT_RIGHT, RIGHT},
                {CONSTRUCTION_DESTROYED_RIGHT, CONSTRUCTION_DESTROYED_RIGHT_TWICE, LEFT},
        });
    }

    public ElementsConstructionHitTest(Elements hitElement, Elements expectedAfterHit, Direction shootDirection) {
        this.hitElement = hitElement;
        this.expectedAfterHit = expectedAfterHit;
        this.shootDirection = shootDirection;
    }

    @Test
    public void testShoot() {
        assertEquals(expectedAfterHit, hitElement.shoot(shootDirection));
    }
}