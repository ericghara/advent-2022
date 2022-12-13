package com.ericgha;

import java.util.Arrays;
import java.util.PrimitiveIterator;

public class ViewingDistance {

    private final TreeGrid treeGrid;
    private final long[] distances;
    private final IntStack intStack;

    public ViewingDistance(TreeGrid treeGrid) {
        this.treeGrid = treeGrid;
        this.distances = initDistances();
        this.intStack = new IntStack( Math.max( treeGrid.getNumCols(), treeGrid.getNumRows() ) + 1 );
    }

    long[][] getDistances() {
        long[][] out = new long[treeGrid.getNumRows()][treeGrid.getNumCols()];
        for (int r = 0; r < out.length; r++) {
            for (int c = 0; c < out[r].length; c++) {
                int index = treeGrid.getI( r, c );
                out[r][c] = distances[index];
            }
        }
        return out;
    }

    private long[] initDistances() {
        long[] distances = new long[treeGrid.getNumTrees()];
        Arrays.fill( distances, 1L );
        return distances;
    }

    private int peekHeight(IntStack stack) {
        return TreeGrid.decodeHeight( stack.peek() );
    }

    private void updateDistance(int forTree, int blockingTree) {
        int index = TreeGrid.decodeIndex( forTree );
        distances[index] *= treeGrid.manhattanDistance( forTree, blockingTree );
    }

    private int prepareFor(int thisTree) {
        int height = TreeGrid.decodeHeight( thisTree );
        int curHeight = peekHeight( intStack );
        while (curHeight <= height) {
            int lastTree = intStack.pop();
            updateDistance( lastTree, thisTree );
            if (curHeight == height) {
                return lastTree;
            }
            curHeight = peekHeight( intStack );
        }
        return intStack.peek();
    }

    /*
    Creates a monotonically *strictly* increasing stack
    But the tree prepareFor returns would be equivalent to peeking at a monotonically *non-decreasing* stack
    For encoded int ( (r,c,height) ) sequence { (0,0,1),(0,1,1),(0,2,2) }
    init stack { (0,0,15) }   (baseTree)
    prepare for item 0: { (0,0,15) }  prepareFor returns (0,0,15) stack.peek() would return (0,0,15)
    >> push Item 0: { (0,0,15), (0,0,1) }
    prepare for item 1: { (0,0,15) } prepareFor returns (0,0,1)  stack.peek() would return (0,0,15)
    >> push Item 1: { (0,0,15), (0,1,1) }
    prepare for item 2 { (0,0,15) } prepareFor returns (0,0,15)  stack.peek() would return (0,0,15)
    >> push Item 2: { (0,0,15), (0,2,2) }
    */
    private void addTree(int thisTree) {
        int treeBehind = prepareFor( thisTree );
        updateDistance( thisTree, treeBehind );
        intStack.push( thisTree );
    }

    private void calcViewingAxis(PrimitiveIterator.OfInt lineOfTrees, int baseTree, int bumpTree) {
        intStack.push( baseTree );
        while (lineOfTrees.hasNext()) {
            addTree( lineOfTrees.nextInt() );
        }
        prepareFor( bumpTree );
        clearStack();
    }

    void clearStack() {
        while (!intStack.isEmpty()) {
            intStack.pop();
        }
    }

    // Bump tree height 14
    int createBumpTree(int r, int c) {
        return treeGrid.getEncodedVal( r, c ) | 0b1110;
    }

    // Base tree height 15.  Cannot be bumped even by bump tree.
    int createBaseTree(int r, int c) {
        return treeGrid.getEncodedVal( r, c ) | 0b1111;
    }

    public long getMaxViewingScore() {
        for (int row = 0; row < treeGrid.getNumRows(); row++) {
            int base = createBaseTree( row, 0 );
            int bump = createBaseTree( row, treeGrid.getNumCols() - 1 );
            calcViewingAxis( treeGrid.rowIterator( row ), base, bump );
        }
        for (int col = 0; col < treeGrid.getNumCols(); col++) {
            int base = createBaseTree( 0, col );
            int bump = createBumpTree( treeGrid.getNumRows() - 1, col );
            calcViewingAxis( treeGrid.colIterator( col ), base, bump );
        }
        return Arrays.stream( distances ).max().orElseThrow();
    }


}
