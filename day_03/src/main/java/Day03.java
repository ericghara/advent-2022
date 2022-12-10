import java.io.BufferedReader;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day03 {



    /*

     */

    private final char[][] rucksacks;
    private int[] itemPriorities;

    public Day03(String filename) {
        this.rucksacks = loadInput( filename );
        this.itemPriorities = calcPriorities();

    }

    private char[][] loadInput(String filename) {
        BufferedReader reader = ReaderUtils.getResourceFileReader( this, filename );
        return reader.lines().filter( s -> !s.isBlank() ).map( String::trim )
                .map(String::toCharArray).toArray( char[][]::new );
    }

    private int[] calcPriorities() {
        int[] priorities = new int['z'+1];
        for (int c = 0; c < priorities.length; c++) {
            if (Character.isUpperCase( c ) ) {
                priorities[c] = 27 + c-'A'; // // 27-52 : A - Z
            }
            else if (Character.isLowerCase( c ) ) {
                priorities[c] = 1 + c-'a'; // 1-26 : a - z
            }
            // non alpha characters fall through
        }
        return priorities;
    }

    private Stream<char[]> streamRucksacks() {
        return Arrays.stream(rucksacks);
    }

    public int getPriority(int item) {
        if ( ('A' <= item && item <= 'Z') || ('a' <= item && item <= 'z') ) {
            return itemPriorities[item];
        }
        throw new IllegalArgumentException("Item must be an ASCII Alphabetic character");
    }

    public int totalDuplicatePriorities() {
        DuplicateItemDetector detector = new DuplicateItemDetector();
        return streamRucksacks().mapToInt(detector::findFirstDuplicate)
                .map( this::getPriority ).sum();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day03 day03 = new Day03( filename );
        int totalDuplicatePriorities = day03.totalDuplicatePriorities();
        System.out.printf("Total priority of all duplicate items: %d", totalDuplicatePriorities);
    }

}
