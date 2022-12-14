package com.ericgha.event;

public class AddX extends Event {

    private static final int DURATION = 2;
    private final int amount;

    public AddX(int start, int amount) {
        super(start, start+DURATION);
        this.amount = amount;
    }

    public int amount() {
        return amount;
    }


}
