package com.ericgha.constants;

public enum Play {

    ROCK( 1 ), PAPER( 2 ), SCISSORS( 3 );

    public final int points;

    Play(int points) {
        this.points = points;
    }
}
