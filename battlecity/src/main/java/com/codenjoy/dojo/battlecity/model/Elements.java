package com.codenjoy.dojo.battlecity.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.printer.CharElements;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public enum Elements implements CharElements {

    NONE(' '),
    BATTLE_WALL('☼'),
    BANG('Ѡ'),


    CONSTRUCTION_DESTROYED_UP_DOWN('─', 1),
    CONSTRUCTION_DESTROYED_UP_LEFT('┌', 1),
    CONSTRUCTION_DESTROYED_UP_TWICE('╥', 1),
    CONSTRUCTION_DESTROYED_RIGHT_UP('┐', 1),

    CONSTRUCTION_DESTROYED_DOWN_TWICE('╨', 1),
    CONSTRUCTION_DESTROYED_DOWN_LEFT('└', 1),
    CONSTRUCTION_DESTROYED_DOWN_RIGHT('┘', 1),

    CONSTRUCTION_DESTROYED_LEFT_TWICE('╞', 1),
    CONSTRUCTION_DESTROYED_LEFT_RIGHT('│', 1),

    CONSTRUCTION_DESTROYED_RIGHT_TWICE('╡', 1),

    CONSTRUCTION_DESTROYED_DOWN('╩', 2, CONSTRUCTION_DESTROYED_DOWN_TWICE, CONSTRUCTION_DESTROYED_UP_DOWN,
                                CONSTRUCTION_DESTROYED_DOWN_LEFT, CONSTRUCTION_DESTROYED_DOWN_RIGHT),
    CONSTRUCTION_DESTROYED_UP('╦', 2, CONSTRUCTION_DESTROYED_UP_DOWN, CONSTRUCTION_DESTROYED_UP_LEFT,
                              CONSTRUCTION_DESTROYED_UP_TWICE, CONSTRUCTION_DESTROYED_RIGHT_UP),
    CONSTRUCTION_DESTROYED_LEFT('╠', 2, CONSTRUCTION_DESTROYED_DOWN_LEFT, CONSTRUCTION_DESTROYED_UP_LEFT,
                                CONSTRUCTION_DESTROYED_LEFT_TWICE, CONSTRUCTION_DESTROYED_LEFT_RIGHT),
    CONSTRUCTION_DESTROYED_RIGHT('╣', 2, CONSTRUCTION_DESTROYED_DOWN_RIGHT, CONSTRUCTION_DESTROYED_RIGHT_UP,
                                 CONSTRUCTION_DESTROYED_LEFT_RIGHT, CONSTRUCTION_DESTROYED_RIGHT_TWICE),

    CONSTRUCTION('╬', 3, CONSTRUCTION_DESTROYED_DOWN, CONSTRUCTION_DESTROYED_UP, CONSTRUCTION_DESTROYED_LEFT,
                 CONSTRUCTION_DESTROYED_RIGHT),

    CONSTRUCTION_DESTROYED(' ', 0),

    BULLET('•'),

    TANK_UP('▲', -1, BANG),
    TANK_RIGHT('►', -1, BANG),
    TANK_DOWN('▼', -1, BANG),
    TANK_LEFT('◄', -1, BANG),

    OTHER_TANK_UP('˄', -1, BANG),
    OTHER_TANK_RIGHT('˃', -1, BANG),
    OTHER_TANK_DOWN('˅', -1, BANG),
    OTHER_TANK_LEFT('˂', -1, BANG),

    AI_TANK_UP('?', -1, BANG),
    AI_TANK_RIGHT('»', -1, BANG),
    AI_TANK_DOWN('¿', -1, BANG),
    AI_TANK_LEFT('«', -1, BANG),
    ;

    public final char ch;
    private final Elements[] hitOptions;
    int power;

    private static List<Elements> result = null;
    private static Pattern directionsPattern = Pattern.compile("(DOWN)|(UP)|(LEFT)|(RIGHT)");

    public static Collection<Elements> getConstructions() {
        if (result == null) {
            result = Arrays.stream(values())
                    .filter(e -> e.name().startsWith(CONSTRUCTION.name()))
                    .collect(toList());
        }
        return result;
    }

    private static Multimap<Elements, Elements> constructionHits = ImmutableMultimap.<Elements, Elements>builder()
            .put(CONSTRUCTION, CONSTRUCTION_DESTROYED_DOWN)
            .put(CONSTRUCTION, CONSTRUCTION_DESTROYED_RIGHT)
            .put(CONSTRUCTION, CONSTRUCTION_DESTROYED_UP)
            .put(CONSTRUCTION, CONSTRUCTION_DESTROYED_LEFT)
            .build();


    @Override
    public char ch() {
        return ch;
    }

    Elements(char ch) {
        this(ch, -1);

    }

    Elements(char ch, int power, Elements... hitOptions) {
        this.ch = ch;
        this.power = power;
        this.hitOptions = hitOptions;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

    public Elements shoot(Direction fromDirection) {
        if (this == BULLET) {
            return BULLET;
        }
        if (hitOptions.length == 1) {
            return hitOptions[0];
        }
        return Arrays.stream(hitOptions).filter((e) -> contains(e, fromDirection)).findFirst().orElse(NONE);
    }

    private boolean contains(Elements potentialAfterHitElement, Direction fromDirection) {
        String hitSide = fromDirection.inverted().name();
        String potentialAfterHitElementName = potentialAfterHitElement.name();
        if (power == 2) {
            Matcher matcher = directionsPattern.matcher(name());
            if (matcher.find()) {
                String alreadyHitSide = matcher.group();
                hitSide = hitSide.equals(alreadyHitSide) ? "TWICE" : hitSide;
                potentialAfterHitElementName = potentialAfterHitElement.name().replace(alreadyHitSide, "");
            }
        }
        return potentialAfterHitElementName.contains(hitSide);
    }
}
