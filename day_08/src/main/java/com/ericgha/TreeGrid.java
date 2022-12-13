package com.ericgha;

import com.ericgha.exception.FileReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PrimitiveIterator;

public class TreeGrid {

    private final int numRows;
    private final int numCols;
    private final int N;
    private final int[] flatGrid;
    private static final int heightMask = ( 1 << 4 ) - 1;

    TreeGrid(List<String> grid) {
        this.numRows = grid.size();
        this.numCols = grid.get( 0 ).length();
        this.N = this.numRows * this.numCols;
        this.flatGrid = initFlatGrid( grid );
    }

    public static TreeGrid fromResource(String resourceName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = ReaderUtils.getResourceFileReader( resourceName )) {
            while (reader.ready()) {
                lines.add( reader.readLine().strip() );
            }
        } catch (IOException e) {
            throw new FileReadException( "Error reading: " + resourceName );
        }
        return new TreeGrid( lines );
    }

    public static int decodeHeight(int encoded) {
        return encoded & heightMask;
    }

    public static int decodeIndex(int encoded) {
        return encoded >>> 4;
    }

    public PrimitiveIterator.OfInt rowIterator(int row) {
        return new EncodedRowIterator( row );
    }
    public PrimitiveIterator.OfInt colIterator(int col) {
        return new EncodedColIterator( col );
    }

    public int getI(int r, int c) {
        validateQuery( r, c );
        return r * numCols + c;
    }

    public int getTreeHeight(int r, int c) {
        int i = getI( r, c );
        return decodeHeight( flatGrid[i] );
    }

    public int getNumTrees() {
        return this.N;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int manhattanDistance(int treeA, int treeB) {
        int indexA = decodeIndex( treeA );
        int indexB = decodeIndex( treeB );
        int rowDelta = Math.abs( indexA / numRows - indexB / numRows );
        int colDelta = Math.abs( indexA % numRows - indexB % numRows );
        return colDelta + rowDelta;
    }

    // an int with its index and value encoded.
    // first 28 bits are index, last 4 bits are value (msb -> lsb)
    public int getEncodedVal(int r, int c) {
        int i = getI( r, c );
        return encode( i, flatGrid[i] );
    }

    int encode(int index, int val) {
        return index << 4 | val;
    }

    private int[] initFlatGrid(List<String> grid) {
        validateInput( grid );
        int[] flatGrid = grid.stream().flatMapToInt( String::chars )
                .map( i -> i - '0' ).filter( i -> i >= 0 && i <= 9 ).toArray();
        if (flatGrid.length != N) {
            throw new IllegalArgumentException( "Received a ragged matrix or a character not in range ['0','9']." );
        }
        return flatGrid;
    }

    private void validateInput(List<String> lines) throws IllegalArgumentException {
        if (Objects.isNull( lines ) || lines.size() == 0) {
            throw new IllegalArgumentException( "Received an invalid grid input" );
        }
    }

    private void validateQuery(int r, int c) throws IllegalArgumentException {
        if (r < 0 || r >= numRows || c < 0 || c >= numRows) {
            throw new IllegalArgumentException( "Row or Column is out of range" );
        }
    }

    public class EncodedRowIterator implements PrimitiveIterator.OfInt {

        private final int row;
        private int i;
        private final int end;

        public EncodedRowIterator(int row) {
            this.row = row;
            validateRow();
            i = row * numCols;
            end = i + numCols;
        }

        @Override
        public int nextInt() {
            return encode( i, flatGrid[i++] );
        }

        private void validateRow() throws IllegalArgumentException {
            if (row < 0 || row >= numRows) {
                throw new IllegalArgumentException( "Received a row outside the bounds of the grid." );
            }

        }

        @Override
        public boolean hasNext() {
            return i < end;
        }
    }

    public class EncodedColIterator implements PrimitiveIterator.OfInt {

        private final int col;
        private int i;

        public EncodedColIterator(int col) {
            this.col = col;
            validateCol();
            i = col;
        }

        @Override
        public int nextInt() {
            int curI = i;
            i += numCols;
            return encode( curI, flatGrid[curI] );
        }

        @Override
        public boolean hasNext() {
            return i < N;
        }

        private void validateCol() throws IllegalArgumentException {
            if (col < 0 || col >= numCols) {
                throw new IllegalArgumentException( "Received a col outside the bounds of the grid." );
            }
        }

    }

}
