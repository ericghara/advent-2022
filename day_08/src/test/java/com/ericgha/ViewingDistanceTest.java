package com.ericgha;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ViewingDistanceTest {

    TreeGrid treeGrid;

    void assertEquals(long[][] expected, long[][] found) throws AssertionError {
        for (int r = 0; r < expected.length; r++) {
            for (int c= 0; c < expected[r].length; c++) {
                Assertions.assertEquals(expected[r][c], found[r][c], String.format("r: %d c: %d", r, c) );
            }
        }
    }

    @Test
    void getMaxViewingScoreTest0() {
        treeGrid = TreeGrid.fromResource( "test_input0" );
        long[][] expected = { {0,0,0,0,0},
                              {0,1,4,1,0},
                              {0,6,1,2,0},
                              {0,1,8,3,0},
                              {0,0,0,0,0} };
        ViewingDistance viewingDistance = new ViewingDistance( treeGrid );
        viewingDistance.getMaxViewingScore();
        long[][] found = viewingDistance.getDistances();
        this.assertEquals( expected, found );
    }

    @Test
    void getMaxViewingScoreTest1() {
        treeGrid = TreeGrid.fromResource( "test_input1" );
        long[][] expected = { {0,0,0,0,0},
                              {0,1,1,1,0},
                              {0,1,1,1,0},
                              {0,1,1,1,0},
                              {0,0,0,0,0} };
        ViewingDistance viewingDistance = new ViewingDistance( treeGrid );
        viewingDistance.getMaxViewingScore();
        long[][] found = viewingDistance.getDistances();
        this.assertEquals( expected, found );
    }

}