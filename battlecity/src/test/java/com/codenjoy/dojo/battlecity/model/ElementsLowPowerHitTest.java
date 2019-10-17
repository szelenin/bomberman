package com.codenjoy.dojo.battlecity.model;


import com.codenjoy.dojo.services.Direction;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.battlecity.model.Elements.BANG;
import static com.codenjoy.dojo.battlecity.model.Elements.NONE;
import static com.codenjoy.dojo.services.Direction.*;
import static org.junit.Assert.assertEquals;

public class ElementsLowPowerHitTest {

    private final List<Direction> directions;

    public ElementsLowPowerHitTest() {
        directions = Arrays.asList(UP, DOWN, LEFT, RIGHT);
    }

    @Test
    public void shouldReturnBangWhenTankHitFromAllDirections() {
        verifyElementsAllBecomeAfterHit((e) -> e.name().startsWith("TANK"), BANG);
    }

    @Test
    public void shouldReturnBangWhenAiTankHitFromAllDirections() {
        verifyElementsAllBecomeAfterHit((e) -> e.name().startsWith("AI_TANK"), BANG);
    }

    @Test
    public void shouldReturnBangWhenOtherTankHitFromAllDirections() {
        verifyElementsAllBecomeAfterHit((e) -> e.name().startsWith("OTHER_TANK"), BANG);
    }

    @Test
    public void shouldReturnNoneWhenLowPowerConstructionHitFromAllDirections() {
        verifyElementsAllBecomeAfterHit((e) -> e.name().startsWith("CONSTRUCTION") && e.power == 1, NONE);
    }

    private void verifyElementsAllBecomeAfterHit(Predicate<Elements> elementsPredicate, Elements expectedAfterShoot) {
        List<Elements> tanks = filterElements(elementsPredicate);
        for (Direction direction : directions) {
            for (Elements tank : tanks) {
                assertEquals(expectedAfterShoot, tank.shoot(direction));
            }
        }
    }

    private List<Elements> filterElements(Predicate<Elements> predicate) {
        return Arrays.stream(Elements.values()).filter(predicate).collect(
                Collectors.toList());
    }

}