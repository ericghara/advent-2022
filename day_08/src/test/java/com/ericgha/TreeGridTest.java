package com.ericgha;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PrimitiveIterator;

import static org.junit.jupiter.api.Assertions.*;

class TreeGridTest {

    TreeGrid treeGrid = TreeGrid.fromResource( "test_input0" );


    @Test
    void treeGridProperEncode() {
        List<Integer> expected = List.of(0, 1, 2, 3, 4);
        List<Integer> found = new ArrayList<>();
        PrimitiveIterator.OfInt rowIter = treeGrid.rowIterator( 0 );
        while (rowIter.hasNext() ) {
            found.add( TreeGrid.decodeIndex(rowIter.next() ) );
        }
        assertEquals(expected, found);
    }

    private List<Integer> toInts(String s) {
        String[] split = s.split("");
        return Arrays.stream(split).map(Integer::parseInt).toList();
    }

    @ParameterizedTest(name="[{index}] {0}")
    @CsvSource(useHeadersInDisplayName=true, delimiter = '|', textBlock = """
           row num | expected
           0 | 30373
           1 | 25512
           2 | 65332
           3 | 33549
           4 | 35390
    """)
    void rowIterator(int row, String expected) {
        List<Integer> expectedInts = toInts(expected);
        List<Integer> found = new ArrayList<>();
        PrimitiveIterator.OfInt rowIter = treeGrid.rowIterator( row );
        while (rowIter.hasNext() ) {
            found.add( TreeGrid.decodeHeight(rowIter.next() ) );
        }
        assertEquals(expectedInts, found);
    }

    @ParameterizedTest(name="[{index}] {0}")
    @CsvSource(useHeadersInDisplayName=true, delimiter = '|', textBlock = """
               col num | expected
               0  | 32633
               1  | 05535
               2  | 35353
               3  | 71349
               4  | 32290
            """)
    void colIterator(int col, String expected) {
        List<Integer> found = new ArrayList<>();
        List<Integer> expectedInts = toInts( expected );
        PrimitiveIterator.OfInt colIter = treeGrid.colIterator( col );
        while (colIter.hasNext()) {
            found.add( TreeGrid.decodeHeight(colIter.next() ) );
        }
        assertEquals( expectedInts, found );
    }

    @ParameterizedTest(name="[{index}] {0}")
    @CsvSource(useHeadersInDisplayName=true, delimiter = '|', textBlock = """
               row a | col a | row b | col b
                 0   |  0    |  4    |  4
                 4   |  4    |  0    |  0
                 1   |  1    |  2    |  2
                 1   |  0    |  1    |  0
                 4   |  4    |  3    |  3
                 1   |  4    |  4    |  1
            """)
    void manhattanDistanceTest(int rowA, int colA, int rowB, int colB) {
        int treeA = treeGrid.getEncodedVal(rowA, colA);
        int treeB = treeGrid.getEncodedVal(rowB, colB);
        int expected = Math.abs(rowA-rowB) + Math.abs(colA-colB);
        assertEquals(expected, treeGrid.manhattanDistance( treeA, treeB ) );
    }
}