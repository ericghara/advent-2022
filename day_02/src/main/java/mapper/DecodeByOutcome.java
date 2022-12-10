package mapper;

import constants.Outcome;
import constants.Play;
import dto.RoundPlays;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class DecodeByOutcome {

    /*
    X lose
    Y draw
    Z win
     */
    private final Map<Character, Play> charToOpponentPlay;
    private final Map<Character, Outcome>  charToOutcome;

    public DecodeByOutcome(Map<Character, Play> charToOpponentPlay, Map<Character, Outcome> charToOutcome) {
        this.charToOpponentPlay = charToOpponentPlay;
        this.charToOutcome = charToOutcome;
    }

    public DecodeByOutcome() {
        this.charToOutcome = Map.of('X', Outcome.LOSS, 'Y', Outcome.DRAW, 'Z', Outcome.WIN);
        this.charToOpponentPlay = Map.of('A', Play.ROCK, 'B', Play.PAPER, 'C', Play.SCISSORS);
    }

    // outcome with respect to YOU NOT Opponent
    private RoundPlays generateYourOutcome(Play opponent, Outcome outcome) {
        Play you;
        Play[] plays = Play.values();
        you = switch (outcome) {
            case WIN -> plays[(opponent.ordinal() + 1) % 3];
            case LOSS -> plays[(3+opponent.ordinal() - 1) % 3];
            case DRAW -> opponent;
            default -> throw new IllegalArgumentException( "Did not recognize opponents play" );
        };
        return new RoundPlays( opponent, you );
    }

    public RoundPlays apply(char[] encodedLine) {
        Play opponent = charToOpponentPlay.get( encodedLine[0] );
        Outcome outcome = charToOutcome.get( encodedLine[1] );
        if (Objects.isNull( opponent ) || Objects.isNull( outcome )) {
            throw new IllegalStateException( String.format( "Unrecognized input line: %s", Arrays.toString( encodedLine ) ) );
        }
        return generateYourOutcome( opponent, outcome );
    }
}
