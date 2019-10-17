package com.codenjoy.dojo.battlecity.client;

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


import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class Board extends AbstractBoard<Elements> {

    private Elements hitElement;
    private Map<Point, BulletInfo> bulletDirections = new HashMap<>();

    private class BulletInfo {
        private final Direction direction;
        private final boolean isMyBullet;

        private BulletInfo(Direction direction, boolean isMyBullet) {
            this.direction = direction;
            this.isMyBullet = isMyBullet;
        }
    }

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    @Override
    protected int inversionY(int y) { // TODO разобраться с этим чудом
        return size - 1 - y;
    }

    public boolean isBarrierAt(int x, int y) {
        if (isOutOfField(x, y)) {
            return true;
        }

        return getBarriers().contains(pt(x, y));
    }

    public List<Point> getBarriers() {
        return get(Elements.BATTLE_WALL,
                   Elements.CONSTRUCTION,
                   Elements.CONSTRUCTION_DESTROYED_DOWN,
                   Elements.CONSTRUCTION_DESTROYED_UP,
                   Elements.CONSTRUCTION_DESTROYED_LEFT,
                   Elements.CONSTRUCTION_DESTROYED_RIGHT,
                   Elements.CONSTRUCTION_DESTROYED_DOWN_TWICE,
                   Elements.CONSTRUCTION_DESTROYED_UP_TWICE,
                   Elements.CONSTRUCTION_DESTROYED_LEFT_TWICE,
                   Elements.CONSTRUCTION_DESTROYED_RIGHT_TWICE,
                   Elements.CONSTRUCTION_DESTROYED_LEFT_RIGHT,
                   Elements.CONSTRUCTION_DESTROYED_UP_DOWN,
                   Elements.CONSTRUCTION_DESTROYED_UP_LEFT,
                   Elements.CONSTRUCTION_DESTROYED_RIGHT_UP,
                   Elements.CONSTRUCTION_DESTROYED_DOWN_LEFT,
                   Elements.CONSTRUCTION_DESTROYED_DOWN_RIGHT);
    }

    public Point getMe() {
        List<Point> points = get(Elements.TANK_UP,
                                 Elements.TANK_DOWN,
                                 Elements.TANK_LEFT,
                                 Elements.TANK_RIGHT);
        if (points.isEmpty()) {
            return null;
        }
        return points.get(0);
    }

    public List<Point> getEnemies() {
        return get(Elements.AI_TANK_UP,
                   Elements.AI_TANK_DOWN,
                   Elements.AI_TANK_LEFT,
                   Elements.AI_TANK_RIGHT,
                   Elements.OTHER_TANK_UP,
                   Elements.OTHER_TANK_DOWN,
                   Elements.OTHER_TANK_LEFT,
                   Elements.OTHER_TANK_RIGHT);
    }

    public List<Point> getBullets() {
        return get(Elements.BULLET);
    }

    public boolean isGameOver() {
        return get(Elements.TANK_UP,
                   Elements.TANK_DOWN,
                   Elements.TANK_LEFT,
                   Elements.TANK_RIGHT).isEmpty();
    }

    public Elements getAt(int x, int y) {
        if (isOutOfField(x, y)) {
            return Elements.BATTLE_WALL;
        }
        return super.getAt(x, y);
    }

    public boolean isBulletAt(int x, int y) {
        return getAt(x, y).equals(Elements.BULLET);
    }

    @Override
    public String toString() {
        return String.format("%s\n" +
                             "My tank at: %s\n" +
                             "Enemies at: %s\n" +
                             "Bullets at: %s\n",
                             boardAsString(),
                             getMe(),
                             getEnemies(),
                             getBullets());
    }

    public List<Direction> getLegalActions() {
        List<Direction> legalActions = new ArrayList<>(values().length);
        for (Direction direction : Direction.values()) {
            Point nextPoint = direction.change(getMe());
            if (isBarrierAt(nextPoint.getX(), nextPoint.getY())) {
                continue;
            }
            legalActions.add(direction);
        }
        return legalActions;
    }

    public Board createSuccessor(Direction direction, Board previousState) {
        Point oldPosition = getMe();
        Point newPosition = direction.change(oldPosition);
        Board successor = getCopy();
        List<Point> bangs = get(Elements.BANG);
        for (Point bang : bangs) {
            successor.set(bang.getX(), bang.getY(), Elements.NONE.ch());
        }
        moveBullets(successor, previousState);
        if (successor.isGameOver()) {
            return successor;
        }
        newPosition = moveTank(direction, oldPosition, newPosition, successor);

        if (direction == ACT) {
            BulletInfo bulletInfo = new BulletInfo(getNewDirectionFromTank(), true);
            Point newBulletPosition = moveBullet(successor, newPosition, bulletInfo, 1);
            if (newBulletPosition != null) {
                successor.bulletDirections.put(newBulletPosition, bulletInfo);
            }
        }
        return successor;
    }

    private Direction getNewDirectionFromTank() {
        Elements me = getAt(getMe());
        switch (me) {
            case TANK_UP:
                return UP;
            case TANK_DOWN:
                return DOWN;
            case TANK_LEFT:
                return LEFT;
            case TANK_RIGHT:
                return RIGHT;
        }
        return null;
    }

    private Point moveTank(Direction direction, Point oldPosition, Point newPosition, Board successor) {
        if (successor.isBarrierAt(newPosition.getX(), newPosition.getY())) {
            newPosition = oldPosition;
        }
        successor.set(oldPosition.getX(), oldPosition.getY(), Elements.NONE.ch());
        Elements oldTank = this.getAt(oldPosition.getX(), oldPosition.getY());
        Elements newTank = getNewTankFromDirection(direction, oldTank);
        successor.set(newPosition.getX(), newPosition.getY(), newTank.ch());
        return newPosition;
    }

    private Board getCopy() {
        Board successor = new Board();
        successor.size = this.size;
        successor.field = Arrays.stream(field).map(
                (a) -> Arrays.stream(a.clone()).map(char[]::clone).toArray(char[][]::new)
        ).toArray(char[][][]::new);
        return successor;
    }

    private void moveBullets(Board successor, Board previousState) {
        for (Point bullet : getBullets()) {
            successor.set(bullet.getX(), bullet.getY(), Elements.NONE.ch());

            BulletInfo bulletInfo = bulletDirections.get(bullet);
            if (bulletInfo == null) {
                Direction derivedDirection = deriveBulletDirection(bullet, previousState);
                if (derivedDirection != null) {
                    bulletInfo = new BulletInfo(derivedDirection, false);
                }
            }
            //todo:Always down?
            bulletInfo = bulletInfo == null? new BulletInfo(DOWN, false) : bulletInfo;
            moveBullet(successor, bullet, bulletInfo, 2);
        }
    }

    private Direction deriveBulletDirection(Point bullet, Board previousState) {
        List<Direction> allDirections = Arrays.asList(UP, RIGHT, DOWN, LEFT);
        for (Direction direction : allDirections) {
            Point checkPoint = direction.change(bullet);
            Elements element = getAt(checkPoint);
            if (element.name().contains("TANK") && element.name().contains(direction.inverted().name())) {
                return direction.inverted();
            }
        }
        if (previousState == null) {
            return null;
        }
        for (Direction direction : allDirections) {
            Point checkPoint = direction.change(bullet);
            checkPoint = direction.change(checkPoint);
            Elements element = previousState.getAt(checkPoint);
            if (element == Elements.BULLET) {
                return direction.inverted();
            }
        }

        return null;
    }

    private Point moveBullet(Board successor, Point bullet, BulletInfo bulletInfo, int speed) {
        bulletDirections.remove(bullet);
        for (int i = 0; i < speed; i++) {
            bullet = bulletInfo.direction.change(bullet);
            if (successor.getAt(bullet) == Elements.BATTLE_WALL) {
                return null;
            }
            if (isOutOfField(bullet.getX(), bullet.getY())) {
                return null;
            }
            Elements bulletHit = getAt(bullet);
            if (bulletHit == Elements.NONE) {
                continue;
            }
            successor.set(bullet.getX(), bullet.getY(), bulletHit.shoot(bulletInfo.direction).ch());
            if (bulletInfo.isMyBullet) {
                successor.hitElement = bulletHit;
            }
            return null;
        }

        successor.set(bullet.getX(), bullet.getY(), Elements.BULLET.ch());
        successor.bulletDirections.put(bullet, bulletInfo);
        return bullet;
    }

    private Elements getNewTankFromDirection(Direction direction, Elements oldTank) {
        switch (direction) {
            case RIGHT:
                return Elements.TANK_RIGHT;
            case LEFT:
                return Elements.TANK_LEFT;
            case UP:
                return Elements.TANK_UP;
            case DOWN:
                return Elements.TANK_DOWN;
        }
        return oldTank;
    }

    public Elements getHitElement() {
        return hitElement;
    }
}
