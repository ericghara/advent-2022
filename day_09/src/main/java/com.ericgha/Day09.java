package com.ericgha;

import com.ericgha.snake.Snake;

public class Day09 {

    private final Snake snake;

    public Day09(int bodyLength, String resourceName) {
        this.snake = new Snake( bodyLength );
        this.snake.model( new Parser().stream( resourceName ) );
    }

    public int getNumLocationsSeenBy(int bodySegment) {
        return snake.numPositionsVisited( bodySegment );
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day09 day09 = new Day09( 9 , filename);
        int numSeenBodySeg0 = day09.getNumLocationsSeenBy( 0 );
        System.out.printf( "Number of unique positions seen by two segment snake: %d.%n", numSeenBodySeg0 );
        int numSeenBodySeg8 = day09.getNumLocationsSeenBy( 8 );
        System.out.printf( "Number of unique positions seen by 10 segment snake: %d.%n", numSeenBodySeg8 );
    }

}
