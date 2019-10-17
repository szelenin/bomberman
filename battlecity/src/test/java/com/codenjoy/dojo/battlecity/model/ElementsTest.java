package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(Parameterized.class)
public class ElementsTest {
    @Test
    public void testShoot() {
        assertEquals(Elements.CONSTRUCTION_DESTROYED_DOWN.ch(), Elements.CONSTRUCTION.shoot(Direction.UP));
        assertEquals(Elements.CONSTRUCTION_DESTROYED_UP.ch(), Elements.CONSTRUCTION.shoot(Direction.DOWN));
        assertEquals(Elements.CONSTRUCTION_DESTROYED_LEFT.ch(), Elements.CONSTRUCTION.shoot(Direction.RIGHT));
        assertEquals(Elements.CONSTRUCTION_DESTROYED_RIGHT.ch(), Elements.CONSTRUCTION.shoot(Direction.LEFT));
    }
}