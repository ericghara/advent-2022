package com.ericgha.monkey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ericgha.parser.MonkeyParams;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MonkeyGameTest {

    MonkeyParams[] params;

    @BeforeEach
    void before() {
        Function<Long, Long> worryFunction = i -> i;
        MonkeyParams params1 = new MonkeyParams()
                .setId( 1 )
                .setItems( new long[]{1, 2, 3} )
                .setTrueTarget( 2 )
                .setFalseTarget( 3 )
                .setDivisor( 2 )
                .setWorryFunction( worryFunction );
        // com.ericgha.monkey 2
        MonkeyParams params2 = new MonkeyParams()
                .setId( 2 )
                .setItems( new long[]{4, 5, 6} )
                .setTrueTarget( 3 )
                .setFalseTarget( 1 )
                .setDivisor( 2 )
                .setWorryFunction( worryFunction );
        // com.ericgha.monkey 3
        MonkeyParams params3 = new MonkeyParams()
                .setId( 3 )
                .setItems( new long[]{7, 8, 9} )
                .setTrueTarget( 1 )
                .setFalseTarget( 2 )
                .setDivisor( 2 )
                .setWorryFunction( worryFunction );
        // set-up
        // worryFunction -> no op (identity)
        // divisor = 2
        // true (even) -> pass forward
        // false (odd) -> pass backwards
        params = new MonkeyParams[]{params1, params2, params3};
    }

    @Test
    void playDampened() {
        MonkeyGame game = new MonkeyGame( params, 3 );
        game.playTo( 1 );
        List<Integer> expected1 = List.of(5,8,4,6,2);
        List<Long> found1 = game.monkey( 1 ).items();
        assertEquals(expected1, found1, "monkey 1");
        List<Integer> expected2 = List.of(7,9,1,3);
        List<Long> found2 = game.monkey( 2 ).items();
        assertEquals( expected2, found2, "monkey 2" );
        List<Integer> expected3 = List.of();
        List<Long> found3 = game.monkey(3).items();
        assertEquals( expected3, found3, "monkey3" );
    }

    @Test
    void getMonkeyBusinessDampened() {
        MonkeyGame game = new MonkeyGame( params, 3 );
        game.playTo(1);
        assertEquals(8*4, game.getMonkeyBusiness() );
    }

}
