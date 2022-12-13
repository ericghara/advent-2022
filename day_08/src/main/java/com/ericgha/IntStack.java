package com.ericgha;

import java.util.Arrays;

/**
 * This doesn't perform any bounds checking.  Call isFull and/or isEmpty if you need these guarantees.
 * Stack does not resize.
 */
public class IntStack {

    private final int size;
    private final int[] stack;
    private int i;

    public IntStack(int size) {
        this.size = size;
        this.stack = new int[this.size];
        this.i = 0;
    }

    public void push(int val) {
        stack[i++] = val;
    }

    public int pop() {
        return stack[--i];
    }

    public int peek() {
        return stack[i - 1];
    }

    // ie cannot pop from
    public boolean isEmpty() {
        return i == 0;
    }

    // ie cannot push to
    public boolean isFull() {
        return i == size;
    }

    public int size() {
        return i;
    }

    public int provisionedSize() {
        return size;
    }

    public void clear() {
        this.i = 0;
    }

    @Override
    public String toString() {
        int[] rawArray = Arrays.copyOfRange( stack, 0, i );
        return Arrays.toString( rawArray );
    }
}
