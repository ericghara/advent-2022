package com.ericgha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class IntStackTest {

    IntStack intStack;

    @BeforeEach
    public void before() {
        intStack = new IntStack( 3 );
    }

    @Test
    void pushTests() {
        // quick and very dirty...
        assertEquals( intStack.size(), 0, "initial size" );
        assertFalse( intStack.isFull(), "initial capacity, not full" );
        assertTrue( intStack.isEmpty(), "initial capacity, is empty" );
        intStack.push(1);
        assertEquals(intStack.peek(), 1, "peek should be 1");
        assertEquals( intStack.size(), 1, "size should be 1" );
        assertFalse( intStack.isEmpty(), "size 1, not empty" );
        assertFalse( intStack.isFull(), "size 1, not full");
        intStack.push( 2 );
        assertEquals(intStack.peek(), 2, "peek should be 2");
        assertEquals( intStack.size(), 2, "size should be 2" );
        assertFalse( intStack.isEmpty(), "size 2, not empty" );
        assertFalse( intStack.isFull(), "size 2, not full");
        intStack.push( 3 );
        assertEquals(intStack.peek(), 3, "peek 3");
        assertEquals( intStack.size(), 3, "size 3" );
        assertFalse( intStack.isEmpty(), "size 3, not empty" );
        assertTrue( intStack.isFull(), "size 3, is full" );
    }

    @Test
    void pop() {
        Stack<Integer> controlStack = new Stack<>();
        IntStream.range(1,4).peek(controlStack::push ).forEach( intStack::push );
        while (!controlStack.isEmpty() ) {
            assertEquals( intStack.pop(), controlStack.pop(), "index: " + controlStack.size() );
        }
        assertEquals( 0, intStack.size(), "intStack is empty when control stack is empty" );
    }

    @Test
    void provisionedSize() {
        assertEquals( 3, intStack.provisionedSize() );
    }
}