import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class Day02 {

    enum Play {

        ROCK( 1 ), PAPER( 2 ), SCISSORS( 3 );

        final int points;

        Play(int points) {
            this.points = points;
        }
    }

    enum Outcome {
        LOSS( 0 ), DRAW( 3 ), WIN( 6 );

        final int points;

        Outcome(int points) {
            this.points = points;
        }
    }

    record RoundPlays(Play you, Play opponent) {
    }

    private final char[][] encodedRounds;
    public final PlayByGuide playByGuide;

    public Day02(String filename) {
        this.encodedRounds = loadInput( filename );
        this.playByGuide = new PlayByGuide();
    }


    public int getScore(Play opponent, Play you) {
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

    class PlayByGuide {
        /*
            Strategy Guide (minimizing obvious cheating)
            A | Y  (rock -> paper)
            B | X  (paper -> rock)
            C | Z  (scissors -> scissors)

            opponent encoding| your encoding | point value
            Rock      A     | x               | 1
            Paper     B     | y               | 2
            Scissors  C     | z               | 3

            outcome point values
            loss | 0
            draw | 3
            win  | 6

            score_for_round = shape_point_value + outcome_point_value

         */

        public PlayByGuide() {
        }

        private final Map<Character, Play> charToPlay = Map.of( 'A', Play.ROCK, 'X', Play.ROCK,
                'B', Play.PAPER, 'Y', Play.PAPER,
                'C', Play.SCISSORS, 'Z', Play.SCISSORS );

        public int scoreAllRounds() {
            int curScore = 0;
            for (char[] encodedPlays : encodedRounds) {
                Play opponent = charToPlay.get( encodedPlays[0] );
                Play you = charToPlay.get( encodedPlays[1] );
                if (Objects.isNull( opponent ) || Objects.isNull( you )) {
                    throw new IllegalStateException( String.format( "Unrecognized input line: %s", Arrays.toString( encodedPlays ) ) );
                }
                curScore += getScore( opponent, you );
            }
            return curScore;
        }


    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day02 day02 = new Day02( filename );
        int scoreByGuide = day02.playByGuide.scoreAllRounds();
        System.out.printf( ">>> Your score playing by guide: %d%n", scoreByGuide );
    }


}
