package com.ericgha;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

public class Day01 {

    static class CalorieStreamer {

        private final IntStream stream;
        private final BufferedReader reader;


        CalorieStreamer(String filename) {
            reader = ReaderUtils.getResourceFileReader( filename);
            stream = IntStream.iterate( -1, this::hasNext,  this::totalElf ); // seed is unused
        }

        boolean hasNext(int notUsed) {
            try {
                return reader.ready();
            }
            catch (IOException e) {
                throw new IllegalStateException("Encountered an IO error");
            }
        }

        int totalElf(int notUsed) {
            int totCals = 0;
            while (hasNext( -1 ) ) {
                String cur;
                try {
                    cur = reader.readLine().trim();
                }
                catch (IOException e) {
                    throw new IllegalStateException("Encountered an IO error.");
                }
                if (cur.isBlank() ) {
                    return totCals;
                }
                try {
                    totCals += Integer.parseInt( cur );
                } catch (NumberFormatException e) {
                        throw new IllegalStateException( "Invalid input file format", e );
                }
            }
            return totCals;
        }

        IntStream stream() {
            return this.stream;
        }

    }

    public int getMaxCal(String fileName) {
        CalorieStreamer calStreamer = new CalorieStreamer( fileName );
        return calStreamer.stream().max().orElseThrow(() -> new IllegalStateException("Received an Empty input") );
    }

    public int totalTopCals(String fileName, int topN) {
        if (topN <= 0) {
            throw new IllegalArgumentException("topN must be > 0");
        }
        final PriorityQueue<Integer> minPq = new PriorityQueue<>(topN);
        CalorieStreamer calStreamer = new CalorieStreamer( fileName );
        calStreamer.stream().forEach( calTot -> {
            if (minPq.size() < topN) {
                minPq.offer( calTot );
            }
            else if (minPq.peek() < calTot) {
                minPq.remove();
                minPq.offer( calTot );
            }
        } );
        return minPq.stream().mapToInt( i -> i ).sum();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        int maxCals = new Day01().getMaxCal( filename );
        System.out.printf("Max cals: %d%n",  maxCals);
        int sum3 = new Day01().totalTopCals( filename, 3 );
        System.out.printf("Sum of top 3 cal totals: %d%n",  sum3);
    }
}
