package com.ericgha;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeVisibilityTest {

    TreeGrid treeGrid = TreeGrid.fromResource( "test_input0" );

    @ParameterizedTest(name = "[{index}] {0}")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
                   row | expected
                   0   | 3
                   1   | 4
                   2   | 4
                   3   | 3
                   4   | 4
            """)
    void processRows(int row, int expected) {
        TreeVisibility treeVisibility = new TreeVisibility( treeGrid );
        treeVisibility.processAxis( treeGrid.rowIterator(row) );
        assertEquals( expected, treeVisibility.getVisibleTrees(), "row: " + row );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
                   col | expected
                   0   | 3
                   1   | 3
                   2   | 4
                   3   | 2
                   4   | 3
            """)
    // essentially the same as last test, just more test cases
    void processCols(int col, int expected) {
        TreeVisibility treeVisibility = new TreeVisibility( treeGrid );
        treeVisibility.processAxis( treeGrid.colIterator(col) );
        assertEquals( expected, treeVisibility.getVisibleTrees(), "col: " + col );
    }
}