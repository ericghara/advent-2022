package com.ericgha;

import java.util.PrimitiveIterator;

public class TreeVisibility {

    private final int RADIX = 10;
    // this is a strictly increasing stack, with the first seen tree taking precedence for height ties
    private final IntStack increasingStack;

    // this is a strictly decreasing stack with the last seen tree taking precedence for height ties
    private final IntStack decreasingStack;

    private TreeVisibility() {
        this.increasingStack = new IntStack( RADIX );
        this.decreasingStack = new IntStack( RADIX );
    }

    public static TreeVisibility generate(PrimitiveIterator.OfInt lineOfTrees) {
        TreeVisibility treeVisibility = new TreeVisibility();
        while (lineOfTrees.hasNext() ) {
            int encodedTree = lineOfTrees.nextInt();
            treeVisibility.tryAddDecreasing( encodedTree );
            treeVisibility.tryAddIncreasing( encodedTree );
        }
        return treeVisibility;
    }

    // no defensive copy
    public IntStack getIncreasingStack() {
        return increasingStack;
    }

    // no defensive copy
    public IntStack getDecreasingStack() {
        return decreasingStack;
    }

    private int peekHeight(IntStack stack) {
        return TreeGrid.decodeHeight( stack.peek() );
    }

    void tryAddIncreasing(int encodedTree) {
        int height = TreeGrid.decodeHeight( encodedTree );
        if (increasingStack.isEmpty() || height > peekHeight( increasingStack ) ) {
            increasingStack.push( encodedTree );
        }
    }

    void tryAddDecreasing(int encodedTree) {
        int height = TreeGrid.decodeHeight( encodedTree );
        while (!decreasingStack.isEmpty() && peekHeight( decreasingStack ) <= height ) {
            decreasingStack.pop();
        }
        decreasingStack.push( encodedTree );
    }
}
