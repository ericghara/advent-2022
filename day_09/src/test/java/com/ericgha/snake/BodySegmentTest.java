package com.ericgha.snake;


import com.ericgha.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BodySegmentTest {

    BodySegment bodySegment;

    @BeforeEach
    void setUp() {
        bodySegment = new BodySegment();
    }

    @ParameterizedTest(name = "[{index}] headX {0}, headY {1}")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               headX | headY | shouldMove
                 0   |  0    |  false
                 1   |  0    |  false
                 1   |  1    |  false
                 2   |  0    |  true
                 2   |  1    |  true
            """)
    void syncLocationTest(int headX, int headY, boolean shouldMove) {
        // BodySegment starts at location (0, 0);
        Location curLeadLocation = new Location( headX, headY );
        Followable dummyLeader = new DummyFollowable( curLeadLocation );
        bodySegment.syncWithLeader( dummyLeader );
        boolean didMove = bodySegment.getSeen().size() > 1;
        assertEquals( shouldMove, didMove );
    }

    @Test
    void startsAtOrigin() {
        assertTrue( bodySegment.getSeen().contains( new Location( 0, 0 ) ) );
    }
}