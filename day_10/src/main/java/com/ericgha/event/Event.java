package com.ericgha.event;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class Event {

    int start;
    int stop;
    Integer registerVal;

    /**
     *
     * @param start inclusive
     * @param stop exclusive
     */
    public Event(int start, int stop) {
        this.start = start;
        this.stop = stop;
        this.registerVal = null;
    }

    public boolean hasCompleted() {
        return Objects.nonNull(registerVal);
    }

    public void setRegisterVal(int val) {
        if (Objects.nonNull(registerVal) ) {
            throw new IllegalArgumentException("registerVal has already been set! Event cannot occur twice.");
        }
        this.registerVal = val;
    }

    /**
     *
     * @return Register val during event or null if event hasn't occurred.
     */
    @Nullable
    public Integer registerVal() {
        return this.registerVal;
    }

    public int start() {
        return start;
    }

    public int end() {
        return stop;
    }

}
