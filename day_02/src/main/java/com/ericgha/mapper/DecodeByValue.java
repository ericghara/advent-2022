package com.ericgha.mapper;

import com.ericgha.constants.Play;
import com.ericgha.dto.RoundPlays;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class DecodeByValue {
            /*
            Strategy Guide (minimizing obvious cheating)
            A | Y  (rock -> paper)
            B | X  (paper -> rock)
            C | Z  (scissors -> scissors)

            opponent encoding| your encoding  | point value
            Rock      A     | x               | 1
            Paper     B     | y               | 2
            Scissors  C     | z               | 3
         */

    private final Map<Character, Play> charToPlay;

    public DecodeByValue(Map<Character, Play> charToPlay) {
        this.charToPlay = charToPlay;
    }

    public DecodeByValue() {
        this(Map.of( 'A', Play.ROCK, 'X', Play.ROCK,
                'B', Play.PAPER, 'Y', Play.PAPER,
                'C', Play.SCISSORS, 'Z', Play.SCISSORS ) );
    }

    public RoundPlays apply(char[] encodedLine) {
        Play opponent = charToPlay.get( encodedLine[0] );
        Play you = charToPlay.get( encodedLine[1] );
        if (Objects.isNull( opponent ) || Objects.isNull( you )) {
            throw new IllegalStateException( String.format( "Unrecognized input line: %s", Arrays.toString( encodedLine ) ) );
        }
        return new RoundPlays(opponent, you);
    }


}
