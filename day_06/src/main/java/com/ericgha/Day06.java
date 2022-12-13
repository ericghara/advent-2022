package com.ericgha;

public class Day06 {

    private final CharCounter counter;

    public Day06() {
        counter = new CharCounter();
    }

    public int findFirstUniqueRange(String input, int uniqueLength) {
        counter.reset();
        for (int i = 0; i < Math.min( input.length(), uniqueLength - 1 ); i++) {
            counter.addChar( input.charAt( i ) );
        }
        int start = 0;
        for (int i = uniqueLength - 1; i < input.length(); i++) {
            counter.addChar( input.charAt( i ) );
            if (counter.countUnique() == uniqueLength) {
                return i + 1;
            }
            counter.dropChar( input.charAt( start++ ) );
        }
        return -1;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        String input = ReaderUtils.readEntireFile( filename ).strip();
        Day06 day06 = new Day06();
        int firstUniqueRange4 = day06.findFirstUniqueRange( input, 4 );
        System.out.printf( "Characters parsed before group of 4 unique characters: %d%n", firstUniqueRange4 );
        int firstUniqueRange14 = day06.findFirstUniqueRange( input, 14 );
        System.out.printf( "Characters parsed before group of 4 unique characters: %d%n", firstUniqueRange14 );
    }

}
