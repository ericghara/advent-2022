import com.ericgha.ReaderUtils;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day03 {

    private final char[][] rucksacks;
    private final DuplicateItemDetector detector;

    public Day03(String filename) {
        this.rucksacks = loadInput( filename );
        this.detector = new DuplicateItemDetector( calcPriorities() );
    }

    private char[][] loadInput(String filename) {
        BufferedReader reader = ReaderUtils.getResourceFileReader( filename );
        return reader.lines().filter( s -> !s.isBlank() ).map( String::trim )
                .map( String::toCharArray ).toArray( char[][]::new );
    }

    private int[] calcPriorities() {
        int[] priorities = new int['z' + 1];
        for (int c = 0; c < priorities.length; c++) {
            if (Character.isUpperCase( c )) {
                priorities[c] = 27 + c - 'A'; // // 27-52 : A - Z
            } else if (Character.isLowerCase( c )) {
                priorities[c] = 1 + c - 'a'; // 1-26 : a - z
            }
            // non alpha characters fall through
        }
        return priorities;
    }

    private Stream<char[]> streamRucksacks() {
        return Arrays.stream( rucksacks );
    }

    private Stream<Stream<char[]>> streamGroups(int groupSize) {
        if (rucksacks.length % groupSize != 0) {
            throw new IllegalStateException( "Group size Invalid. Partial groups are not allowed" );
        }
        return IntStream.iterate( 0, cur -> cur < rucksacks.length, cur -> cur + 3 )
                .mapToObj( i -> Arrays.stream( rucksacks, i, i + groupSize ) );
    }

    public int totalDuplicatePriorities() {
        return streamRucksacks().mapToInt( detector::findFirstDuplicateValue ).sum();
    }

    public int totalGroupsDuplicatePriorities() {
        return streamGroups( 3 ).mapToInt( detector::findCommonDuplicatePriority ).sum();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day03 day03 = new Day03( filename );
        int totalDuplicatePriorities = day03.totalDuplicatePriorities();
        System.out.printf( "Total priority of all pocket0 ∩ pocket1 duplicates: %d%n", totalDuplicatePriorities );
        int groupDuplicatePriorities = day03.totalGroupsDuplicatePriorities();
        System.out.printf( "Total priority of duplicates among groups of 3: %d%n", groupDuplicatePriorities );
    }

}
