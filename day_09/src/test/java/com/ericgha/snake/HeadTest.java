package com.ericgha.snake;

import com.ericgha.Location;
import com.ericgha.move.X;
import com.ericgha.move.Y;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeadTest {

    Head head;
    MirroringLogger logger;

    @BeforeEach
    void before() {
        head = new Head();
        logger = new MirroringLogger();
        head.setFollower( logger );
    }

    @Test
    void stepUp() {
        Y yMove = new Y( 5 );
        head.step( yMove );
        List<Location> expected = List.of( new Location( 0, 1 ), new Location( 0, 2 ),
                new Location( 0, 3 ), new Location( 0, 4 ), new Location( 0, 5 ) );
        List<Location> found = logger.getLog();
        assertEquals( expected, found );
    }

    @Test
    void stepDown() {
        Y yMove = new Y( -1 );
        head.step( yMove );
        List<Location> expected = List.of( new Location( 0, -1 ) );
        List<Location> found = logger.getLog();
        assertEquals( expected, found );
    }

    @Test
    void stepLeft() {
        X xMove = new X( -3 );
        head.step( xMove );
        List<Location> expected = List.of( new Location( -1, 0 ), new Location( -2, 0 ),
                new Location( -3, 0 ) );
        List<Location> found = logger.getLog();
        assertEquals( expected, found );
    }

    @Test
    void stepRight() {
        X xMove = new X( 2 );
        head.step( xMove );
        List<Location> expected = List.of( new Location( 1, 0 ), new Location( 2, 0 ) );
        List<Location> found = logger.getLog();
        assertEquals( expected, found );
    }

    @Test
    void StepNowhere() {
        X xMove = new X( 0 );
        head.step( xMove );
        List<Location> expected = List.of();
        List<Location> found = logger.getLog();
        assertEquals( expected, found );
    }
}