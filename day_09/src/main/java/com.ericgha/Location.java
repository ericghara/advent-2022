package com.ericgha;

public record Location(int x, int y) {

    public int distanceSq(Location other) {
        int deltaX = Math.abs( this.x() - other.x() );
        int deltaY = Math.abs( this.y() - other.y() );
        return deltaX * deltaX + deltaY * deltaY;
    }

}
