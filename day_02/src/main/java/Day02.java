import constants.Outcome;
import constants.Play;
import dto.RoundPlays;
import mapper.DecodeByValue;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day02 {

    private final char[][] encodedRounds;

    public Day02(String filename) {
        this.encodedRounds = loadInput( filename );
    }


    public int getScore(RoundPlays roundPlays) {
        Play opponent = roundPlays.opponent();
        Play you = roundPlays.you();
        if (opponent == you) {
            return you.points + Outcome.DRAW.points;
        } else if (( opponent.ordinal() + 1 ) % 3 == you.ordinal()) {
            return you.points + Outcome.WIN.points;
        } else {
            return you.points + Outcome.LOSS.points;
        }
    }

    private char[][] loadInput(String filename) {
        BufferedReader reader = ReaderUtils.getResourceFileReader( this, filename );
        return reader.lines().filter( s -> !s.isBlank() ).map( String::trim ).map( s -> new char[]{s.charAt( 0 ), s.charAt( 2 )} ).toArray( char[][]::new );
    }

    private Stream<char[]> streamEncodedRounds() {
        return Arrays.stream(encodedRounds);
    }

    public int getYourScore(Function<char[], RoundPlays> decoder) {
        return streamEncodedRounds().map( decoder ).mapToInt(this::getScore).sum();
    }



    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day02 day02 = new Day02( filename );
        DecodeByValue decodeByValue = new DecodeByValue();
        int scoreByGuide = day02.getYourScore(decodeByValue::apply);
        System.out.printf( ">>> Your score playing by guide: %d%n", scoreByGuide );
    }


}
