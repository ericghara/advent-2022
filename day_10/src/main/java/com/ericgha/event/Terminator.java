package com.ericgha.event;

/**
 * A zero length event to allow for final reading sequence.
 */
public class Terminator extends Event {

    public Terminator(int start) {
        super(start, start);
    }

}
