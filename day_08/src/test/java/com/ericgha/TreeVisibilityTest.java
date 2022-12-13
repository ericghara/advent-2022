package com.ericgha;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.PrimitiveIterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeVisibilityTest {

    TreeGrid treeGrid = TreeGrid.fromResource( "test_input0" );

    @ParameterizedTest(name = "[{index}] {0}")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
                   row | expected inc | expected dec
                   0   | 2            | 2
                   1   | 2            | 2
                   2   | 1            | 4
                   3   | 3            | 1
                   4   | 3            | 2
            """)
    void generateOnRows(int row, int expectedInc, int expectedDec) {
        PrimitiveIterator.OfInt rowIter = treeGrid.rowIterator( row );
        TreeVisibility treeVisibility = TreeVisibility.generate( rowIter );
        assertEquals( expectedInc, treeVisibility.getIncreasingStack().size(), "Increasing Stack row: " + row );
        assertEquals( expectedDec, treeVisibility.getDecreasingStack().size(), "Decreasing Stack row: " + row );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
                   col | expected inc | expected dec
                   0   | 2            | 2
                   1   | 2            | 1
                   2   | 2            | 2
                   3   | 2            | 1
                   4   | 2            | 2
            """)
    // essentially the same as last test, just more test cases
    void generateOnCols(int col, int expectedInc, int expectedDec) {
        PrimitiveIterator.OfInt colIter = treeGrid.colIterator( col );
        TreeVisibility treeVisibility = TreeVisibility.generate( colIter );
        assertEquals( expectedInc, treeVisibility.getIncreasingStack().size(), "Increasing Stack col: " + col );
        assertEquals( expectedDec, treeVisibility.getDecreasingStack().size(), "Decreasing Stack col: " + col );
    }
}