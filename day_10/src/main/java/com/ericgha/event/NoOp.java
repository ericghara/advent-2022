package com.ericgha.event;

public class NoOp extends Event {

    private static final int DURATION = 1;

    public NoOp(int start) {
        super( start, start + DURATION );
    }

}
