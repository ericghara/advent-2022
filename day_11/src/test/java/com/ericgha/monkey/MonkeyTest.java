package com.ericgha.monkey;

import com.ericgha.parser.MonkeyBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonkeyTest {

    MonkeyBuilder params = new MonkeyBuilder();
    Monkey monkey;

    @BeforeEach
    void before() {
        params.setTrueTarget( 2 );
        params.setFalseTarget( 3 );
        params.setId( 1 );
        params.setDivisor( 2 );
        params.setItems( new int[]{1, 2, 3, 4, 5} );
        params.setWorryParams( "old", "1", '*' ); // Identity, no op;
        this.monkey = new Monkey( params, 1, Integer.MAX_VALUE );
    }

    @Test
    void takeTurn() {
        TurnResult result = monkey.takeTurn();
        List<Integer> expectedTrue = List.of( 2, 4 );
        List<Integer> expectedFalse = List.of( 1, 3, 5 );
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
        Queue<Integer> toCatch = new ArrayDeque<Integer>( List.of( 6, 7, 8 ) );
        monkey.catchItems( toCatch );
        List<Integer> expected = List.of( 1, 2, 3, 4, 5, 6, 7, 8 );
        List<Integer> found = monkey.items();
        assertEquals( expected, found );
    }

    @ParameterizedTest(name = "[{index}] vars: {0} {1} {2} ")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               var0       |  var1  | op  |  expected
                old       |   1    |  +  |    8
                old       |   1    |  *  |    7
                old       |  old   |  +  |    14
                old       |  old   |  *  |    49
                 2        |  old   |  +  |    9
                 2        |  old   |  *  |    14
            """)
    void createWorryFunction(String var0, String var1, char op, int expected) {
        int OLD = 7;
        params.setWorryParams( var0, var1, op );
        params.setDivisor( Integer.MAX_VALUE );
        int found = new Monkey( params, 2, Integer.MAX_VALUE ).worryFunction().apply( OLD );
        assertEquals( expected, found );
    }
}