package com.ericgha;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day08Test {

    @Test
    void getNumVisibleTrees() {
        Day08 day08 = new Day08( "test_input0" );
        int num = day08.getNumVisibleTrees();
        assertEquals(21, num);
    }

}