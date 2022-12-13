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
        this.intStack = new IntStack( 11 ); // RADIX + 1 sentinel (baseTree)
    }

    public long getMaxViewingScore() {
        for (int row = 0; row < treeGrid.getNumRows(); row++) {
            int base = createBaseTree( row, 0 );
            int bump = createBumpTree( row, treeGrid.getNumCols()-1 );
            calcViewingAxis( treeGrid.rowIterator( row ), base, bump );
        }
        for (int col = 0; col < treeGrid.getNumCols(); col++) {
            int base = createBaseTree( 0, col );
            int bump = createBumpTree( treeGrid.getNumRows()-1, col );
            calcViewingAxis( treeGrid.colIterator( col ), base, bump );
        }
        return Arrays.stream( distances ).max().orElseThrow();
    }

    // for testing/debugging
    long[][] getDistances() {
        long[][] distances2D = new long[treeGrid.getNumRows()][treeGrid.getNumCols()];
        for (int r = 0; r < distances2D.length; r++) {
            for (int c = 0; c < distances2D[r].length; c++) {
                int index = treeGrid.getI( r, c );
                distances2D[r][c] = distances[index];
            }
        }
        return distances2D;
    }

    private long[] initDistances() {
        long[] distances = new long[treeGrid.getNumTrees()];
        Arrays.fill( distances, 1L );
        return distances;
    }

    private int peekHeight(IntStack stack) {
        return TreeGrid.decodeHeight( stack.peek() );
    }

    /*
    Creates a monotonically *strictly* increasing stack
    But the tree prepareFor returns is equivalent to peek() from a monotonically *non-decreasing* stack
    For sequence: { (0,0,1),(0,1,1),(0,2,2) }  # (r,c,height) = an encoded int
    $ initial stack { (0,0,15) }   (baseTree)
    $ prepareFor (0,0,1): { (0,0,15) }
    >> lastVisible: (0,0,15), stack.peek(): (0,0,15)
    $ push (0,0,1): { (0,0,15), (0,0,1) }
    $ prepareFor (0,1,1): { (0,0,15) }
    >> lastVisible: (0,0,1), stack.peek(): (0,0,15) # not difference
    $ push (0,1,1): { (0,0,15), (0,1,1) }
    $ prepareFor (0,2,2): { (0,0,15) }
    >> lastVisible: (0,0,15), stack.peek(): (0,0,15)
    $ push (0,2,2): { (0,0,15), (0,2,2) }
    */
    private void addTree(int thisTree) {
        // lastVisible tree looking backwards from thisTree along iterator axis
        int lastVisible = prepareFor( thisTree );
        updateDistance( thisTree, lastVisible );
        intStack.push( thisTree );
    }

    // forTree is the tree's distance that's getting updated.
    // furthestVisible is the tree from which distance is calculated
    private void updateDistance(int forTree, int furthestVisible) {
        int index = TreeGrid.decodeIndex( forTree );
        distances[index] *= treeGrid.manhattanDistance( forTree, furthestVisible );
    }

    private int prepareFor(int thisTree) {
        int height = TreeGrid.decodeHeight( thisTree );
        int curHeight = peekHeight( intStack );
        while (curHeight <= height) {
            int pastTree = intStack.pop();
            // for pastTree -> thisTree is last visible tree looking forward along iterator axis
            updateDistance( pastTree, thisTree );
            if (curHeight == height) {
                return pastTree;
            }
            curHeight = peekHeight( intStack );
        }
        return intStack.peek();
    }

    private void calcViewingAxis(PrimitiveIterator.OfInt lineOfTrees, int baseTree, int bumpTree) {
        intStack.push( baseTree );
        while (lineOfTrees.hasNext()) {
            addTree( lineOfTrees.nextInt() );
        }
        prepareFor( bumpTree );
        clearStack();
    }

    private void clearStack() {
        while (!intStack.isEmpty()) {
            intStack.pop();
        }
    }

    // Bump tree height 14, index corresponding to r,c
    // Technically bump tree could be equal height to baseTree
    // w/o changing results. The first tree along axis would get
    // counted a 3rd time (improperly) but at time of bump it's
    // already 0, so the 3rd improper multiplication has no effect.
    private int createBumpTree(int r, int c) {
        return (treeGrid.getI( r, c ) << 4) | 0b1110;
    }

    // Base tree height 15, index corresponding to r,c.
    // Cannot be bumped even by bump tree.
    private int createBaseTree(int r, int c) {
        return (treeGrid.getI( r, c ) << 4) | 0b1111;
    }


}
