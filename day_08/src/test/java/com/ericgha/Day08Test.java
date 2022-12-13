package com.ericgha;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {

    Day08 day08 = new Day08( "input_day08" ); // note this is the actual exam input

    @Test
    void getMaxVisibleTrees() {
        int found = day08.getMaxVisibleTrees();
        assertEquals( 1_843, found );
    }

    @Test
    void getMaxViewScore() {
        long found = day08.getMaxViewScore();
        assertEquals( 180_000, found );
    }

}