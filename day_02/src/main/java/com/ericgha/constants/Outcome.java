package com.ericgha.constants;

public enum Outcome {
    LOSS( 0 ), DRAW( 3 ), WIN( 6 );

    public final int points;

    Outcome(int points) {
        this.points = points;
    }
}
