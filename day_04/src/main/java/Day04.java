import dto.Interval;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day04 {


    private final Interval[][] intervalPairs;

    public Day04(String fileName) {
        this.intervalPairs = parseIntervalPairs( fileName );
    }

    private Interval[][] parseIntervalPairs(String fileName) {
        BufferedReader reader = ReaderUtils.getResourceFileReader( this, fileName );
        return reader.lines().map(String::strip).filter(s -> !s.isBlank() )
                .map(this::lineToIntervalPair)
                .toArray(Interval[][]::new);
    }

    private Interval[] lineToIntervalPair(String line) {
        int[] coords = new int[4];
        int coordIndex = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isDigit( c ) ) {
                coords[coordIndex] = coords[coordIndex]*10 + c - '0';
            }
            else if (Character.isDigit(line.charAt(i-1) ) ) {
                coordIndex++;
            }
        }
        return new Interval[] {new Interval(coords[0],
                coords[1]), new Interval(coords[2], coords[3])};
    }

    private boolean fullyOverlaps(Interval[] intervalPair) {
        if (intervalPair[0].start() == intervalPair[1].start() ) {
            return true;
        }
        if (intervalPair[0].start() < intervalPair[1].start() ) {
            return intervalPair[0].end() >= intervalPair[1].end();
        }
        return intervalPair[0].end() <= intervalPair[1].end();
    }

    public Stream<Interval[]> streamIntervalPairs() {
        return Stream.of(intervalPairs);
    }

    public long countFullOverlaps() {
        return streamIntervalPairs().filter( this::fullyOverlaps ).count();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day04 day04 = new Day04( filename );
        long fullOverlaps = day04.countFullOverlaps();
        System.out.printf(">>> Full Overlaps: %d", fullOverlaps );
    }

}
