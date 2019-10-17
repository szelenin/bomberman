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
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum Elements implements CharElements {

    NONE(' '),
    BATTLE_WALL('☼'),
    BANG('Ѡ'),


    CONSTRUCTION_DESTROYED_DOWN('╩', 2),
    CONSTRUCTION_DESTROYED_UP('╦', 2),
    CONSTRUCTION_DESTROYED_LEFT('╠', 2),
    CONSTRUCTION_DESTROYED_RIGHT('╣', 2),

    CONSTRUCTION('╬', 3, CONSTRUCTION_DESTROYED_DOWN, CONSTRUCTION_DESTROYED_UP, CONSTRUCTION_DESTROYED_LEFT, CONSTRUCTION_DESTROYED_RIGHT),

    CONSTRUCTION_DESTROYED_DOWN_TWICE('╨', 1),
    CONSTRUCTION_DESTROYED_UP_TWICE('╥', 1),
    CONSTRUCTION_DESTROYED_LEFT_TWICE('╞', 1),
    CONSTRUCTION_DESTROYED_RIGHT_TWICE('╡', 1),

    CONSTRUCTION_DESTROYED_LEFT_RIGHT('│', 1),
    CONSTRUCTION_DESTROYED_UP_DOWN('─', 1),

    CONSTRUCTION_DESTROYED_UP_LEFT('┌', 1),
    CONSTRUCTION_DESTROYED_RIGHT_UP('┐', 1),
    CONSTRUCTION_DESTROYED_DOWN_LEFT('└', 1),
    CONSTRUCTION_DESTROYED_DOWN_RIGHT('┘', 1),

    CONSTRUCTION_DESTROYED(' ', 0),

    BULLET('•'),

    TANK_UP('▲'),
    TANK_RIGHT('►'),
    TANK_DOWN('▼'),
    TANK_LEFT('◄'),

    OTHER_TANK_UP('˄'),
    OTHER_TANK_RIGHT('˃'),
    OTHER_TANK_DOWN('˅'),
    OTHER_TANK_LEFT('˂'),

    AI_TANK_UP('?'),
    AI_TANK_RIGHT('»'),
    AI_TANK_DOWN('¿'),
    AI_TANK_LEFT('«'),
    ;

    public final char ch;
    private final Elements[] hitOptions;
    int power;

    private static List<Elements> result = null;

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

    Elements(char ch, int power, Elements ... hitOptions) {
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

    public char shoot(Direction fromDirection) {
        Collection<Elements> hitElements = constructionHits.get(this);
        return hitElements.stream().filter((e) -> e.name().contains(fromDirection.inverted().name())).findFirst().orElse(NONE).ch();
    }
}
