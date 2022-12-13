package com.ericgha;

import java.util.stream.Stream;

public class DuplicateItemDetector {

    private final int[] priorities;

    public DuplicateItemDetector(int[] priorities) {
        this.priorities = priorities;
    }

    private long charToMask(char c) {
        long priority = priorities[c];
        if (priority == 0) {
            throw new IllegalArgumentException( "Character could not be mapped to a priority" );
        }
        return 1L << ( priority - 1 );
    }

    public int findFirstDuplicateValue(char[] rucksack) {
        long seen = 0;
        if (( rucksack.length & 1 ) == 1) {
            throw new IllegalArgumentException( "Number of items in rucksack must be even." );
        }
        for (int i = 0; i < rucksack.length / 2; i++) {
            char item = rucksack[i];
            seen |= charToMask( item );
        }
        for (int i = rucksack.length / 2; i < rucksack.length; i++) {
            char item = rucksack[i];
            if (( seen & charToMask( item ) ) != 0) {
                return priorities[item];
            }
        }
        throw new IllegalArgumentException( "Rucksack contained no duplicates" );
    }

    private long encodeRucksackContents(char[] rucksack) {
        long contents = 0;
        for (char c : rucksack) {
            contents |= charToMask( c );
        }
        return contents;
    }

    public int findCommonDuplicatePriority(Stream<char[]> group) {
        long identity = ( 1L << ( 52 ) ) - 1; // ie all available items;
        long encodedDuplicate = group.mapToLong( this::encodeRucksackContents ).reduce( identity, (a, b) -> a & b );
        int priority = 1;
        while (priority <= 52) {
            if (( encodedDuplicate & 1 ) == 1) {
                return priority;
            }
            encodedDuplicate >>>= 1;
            priority++;
        }
        throw new IllegalArgumentException( "No common duplicate found" );
    }

}
