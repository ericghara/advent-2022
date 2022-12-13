package com.ericgha;

import java.util.PrimitiveIterator;

public class Day08 {

    private final TreeGrid treeGrid;

    public Day08(String filename) {
         treeGrid = TreeGrid.fromResource( filename );
    }

    public long getMaxViewScore() {
        ViewingDistance viewingDistance = new ViewingDistance( treeGrid );
        return viewingDistance.getMaxViewingScore();
    }

    public int getMaxVisibleTrees() {
        TreeVisibility treeVisibility = new TreeVisibility(treeGrid);
        return treeVisibility.getNumVisibleTrees();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day08 day08 = new Day08( filename );
        System.out.printf("Number of visible trees from 4 axes: %s%n", day08.getMaxVisibleTrees( ) );
        System.out.printf("Max viewing score is: %d.%n", day08.getMaxViewScore() );
    }
}


