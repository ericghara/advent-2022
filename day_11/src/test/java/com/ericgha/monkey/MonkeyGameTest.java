package com.ericgha.monkey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ericgha.parser.MonkeyBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MonkeyGameTest {

    MonkeyBuilder[] params;

    @BeforeEach
    void before() {
        MonkeyBuilder params1 = new MonkeyBuilder()
                .setId( 1 )
                .setItems( new int[]{1, 2, 3} )
                .setTrueTarget( 2 )
                .setFalseTarget( 3 )
                .setDivisor( 2 )
                .setWorryParams( "old", "1", '*' ); // no op (old*1)
        // monkey 2
        MonkeyBuilder params2 = new MonkeyBuilder()
                .setId( 2 )
                .setItems( new int[]{4, 5, 6} )
                .setTrueTarget( 3 )
                .setFalseTarget( 1 )
                .setDivisor( 2 )
                .setWorryParams( "old", "1", '*' ); // no op (old*1)
        // monkey 3
        MonkeyBuilder params3 = new MonkeyBuilder()
                .setId( 3 )
                .setItems( new int[]{7, 8, 9} )
                .setTrueTarget( 1 )
                .setFalseTarget( 2 )
                .setDivisor( 2 )
                .setWorryParams( "old", "1", '*' ); // no op (old*1)
        // set-up
        // worryFunction -> no op (identity)
        // divisor = 2
        // true (even) -> pass forward
        // false (odd) -> pass backwards
        params = new MonkeyBuilder[]{params1, params2, params3};
    }

    @Test
    void playUnDampened() {
        MonkeyGame game = new MonkeyGame( params, 1 );
        game.playTo( 1 );
        List<Integer> expected1 = List.of(5,0,4,6,2);
        List<Integer> found1 = game.monkey( 1 ).items();
        assertEquals(expected1, found1, "monkey 1");
        List<Integer> expected2 = List.of(7,1,1,3);
        List<Integer> found2 = game.monkey( 2 ).items();
        assertEquals( expected2, found2, "monkey 2" );
        List<Integer> expected3 = List.of();
        List<Integer> found3 = game.monkey(3).items();
        assertEquals( expected3, found3, "monkey3" );
    }

    @Test
    void playDampened() {
        MonkeyGame game = new MonkeyGame( params, 3 );
        game.playTo( 1 );
        List<Integer> expected1 = List.of(1,1,2,0,0,0,0,0,0);
        List<Integer> found1 = game.monkey( 1 ).items();
        assertEquals(expected1, found1, "monkey 1");
        List<Integer> expected2 = List.of();
        List<Integer> found2 = game.monkey( 2 ).items();
        assertEquals( expected2, found2, "monkey 2" );
        List<Integer> expected3 = List.of();
        List<Integer> found3 = game.monkey(3).items();
        assertEquals( expected3, found3, "monkey3" );
    }

    @Test
    void getMonkeyBusinessUnDampened() {
        MonkeyGame game = new MonkeyGame( params, 1 );
        game.playTo(1);
        assertEquals(32, game.getMonkeyBusiness() );
    }
    @Test
    void getMonkeyBusinessDampened() {
        MonkeyGame game = new MonkeyGame( params, 3 );
        game.playTo(1);
        assertEquals(35, game.getMonkeyBusiness() );
    }

}
