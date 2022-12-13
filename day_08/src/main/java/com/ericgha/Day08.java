package com.ericgha;

import java.util.PrimitiveIterator;

public class Day08 {

    private final TreeGrid treeGrid;
    private final boolean[] seen;
    private int visibleTrees;

    public Day08(String filename) {
         treeGrid = TreeGrid.fromResource( filename );
         seen = new boolean[treeGrid.getNumTrees()];
         this.visibleTrees = 0;
    }

    private void addFromStack(IntStack stack) {
        while (!stack.isEmpty() ) {
            int treeId = TreeGrid.decodeIndex(stack.pop() );
            if (!seen[treeId]) {
                seen[treeId] = true;
                visibleTrees++;
            }
        }
    }

    private void addFromIterator(PrimitiveIterator.OfInt iterator) {
        TreeVisibility treeVisibility = TreeVisibility.generate( iterator );
        addFromStack( treeVisibility.getDecreasingStack() );
        addFromStack( treeVisibility.getIncreasingStack() );
    }

    private void addCols() {
        for (int col = 0; col < treeGrid.getNumCols(); col++) {
            addFromIterator( treeGrid.colIterator( col ) );
        }
    }

    private void addRows() {
         for (int row = 0; row < treeGrid.getNumRows(); row++) {
             addFromIterator( treeGrid.rowIterator( row ) );
         }
    }

    public int getNumVisibleTrees() {
        this.addCols();
        this.addRows();
        return visibleTrees;
    }

    public long getMaxViewScore() {
        ViewingDistance viewingDistance = new ViewingDistance( treeGrid );
        return viewingDistance.getMaxViewingScore();
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day08 day08 = new Day08( filename );
        System.out.println("Number of visible trees from 4 axes: " + day08.getNumVisibleTrees( ) );
        System.out.printf("Max viewing score is: %d.", day08.getMaxViewScore() );
    }
}


