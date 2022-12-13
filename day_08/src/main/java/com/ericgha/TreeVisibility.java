package com.ericgha;

import java.util.PrimitiveIterator;

public class TreeVisibility {

    private final int RADIX = 10;
    // this is a strictly increasing stack, with the first seen tree taking precedence for height ties
    private final IntStack increasingStack;

    // this is a strictly decreasing stack with the last seen tree taking precedence for height ties
    private final IntStack decreasingStack;
    private final TreeGrid treeGrid;
    private final boolean[] seen;
    private int visibleTrees;

    public TreeVisibility(TreeGrid treeGrid) {
        this.treeGrid = treeGrid;
        this.increasingStack = new IntStack( RADIX );
        this.decreasingStack = new IntStack( RADIX );
        this.seen = new boolean[treeGrid.getNumTrees()];
        this.visibleTrees = 0;
    }

    public int getNumVisibleTrees() {
        for (int col = 0; col < treeGrid.getNumCols(); col++) {
            processAxis( treeGrid.colIterator( col ) );
        }
        for (int row = 0; row < treeGrid.getNumRows(); row++) {
            processAxis( treeGrid.rowIterator( row ) );
        }
        return visibleTrees;
    }

    // open for testing
    int getVisibleTrees() {
        return visibleTrees;
    }

    // open for testing
    void processAxis(PrimitiveIterator.OfInt lineOfTrees) {
        while (lineOfTrees.hasNext() ) {
            int encodedTree = lineOfTrees.nextInt();
            addDecreasing( encodedTree );
            tryAddIncreasing( encodedTree );
        }
        synchronizeSeen( decreasingStack );
        synchronizeSeen( increasingStack );
    }

    private int peekHeight(IntStack stack) {
        return TreeGrid.decodeHeight( stack.peek() );
    }

    private void tryAddIncreasing(int encodedTree) {
        int height = TreeGrid.decodeHeight( encodedTree );
        if (increasingStack.isEmpty() || height > peekHeight( increasingStack ) ) {
            increasingStack.push( encodedTree );
        }
    }

    private void addDecreasing(int encodedTree) {
        int height = TreeGrid.decodeHeight( encodedTree );
        while (!decreasingStack.isEmpty() && peekHeight( decreasingStack ) <= height ) {
            decreasingStack.pop();
        }
        decreasingStack.push( encodedTree );
    }


    private void synchronizeSeen(IntStack stack) {
        while (!stack.isEmpty() ) {
            int treeId = TreeGrid.decodeIndex(stack.pop() );
            if (!seen[treeId]) {
                seen[treeId] = true;
                visibleTrees++;
            }
        }
    }
}
