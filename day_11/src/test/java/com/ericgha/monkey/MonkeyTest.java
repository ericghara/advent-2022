package com.ericgha.monkey;

import com.ericgha.parser.MonkeyParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonkeyTest {

    MonkeyParams params = new MonkeyParams();
    Monkey monkey;

    @BeforeEach
    void before() {
        params.setDivisor( 2 );
        params.setItems( new long[]{1, 2, 3, 4, 5} );
        params.setWorryFunction( i -> i ); // no op;
        this.monkey = new Monkey( params, 3 );
    }

    @Test
    void takeTurn() {
        TurnResult result = monkey.takeTurn();
        List<Long> expectedTrue = List.of( 2L, 4L );
        List<Long> expectedFalse = List.of( 1L, 3L, 5L );
        // queue seems to have an identity equals.
        assertEquals( expectedTrue, List.copyOf( result.trueItems() ) );
        assertEquals( expectedFalse, List.copyOf( result.falseItems() ) );
    }

    @Test
    void itemsThrown() {
        TurnResult _result = monkey.takeTurn();
        assertEquals( 5, monkey.itemsThrown() );
    }

    @Test
    void catchItems() {
        Queue<Long> toCatch = new ArrayDeque<Long>( List.of( 6L, 7L, 8L ) );
        monkey.catchItems( toCatch );
        List<Long> expected = List.of( 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L );
        List<Long> found = monkey.items();
        assertEquals( expected, found );
    }
}