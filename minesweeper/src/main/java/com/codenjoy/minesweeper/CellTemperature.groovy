package com.codenjoy.minesweeper

/**
 * Created by szelenin on 4/14/2015.
 */
class CellTemperature {
    Point coordinates
    double temperature

    CellTemperature(Point coordinates, double temperature) {
        this.coordinates = coordinates
        this.temperature = temperature
    }

    Point getCoordinates() {
        return coordinates
    }

    double getTemperature() {
        return temperature
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        CellTemperature rhs = (CellTemperature) obj;
        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.coordinates, rhs.coordinates)
                .append(this.temperature, rhs.temperature)
                .isEquals();
    }
}
