package com.ericgha;

import com.ericgha.monkey.MonkeyGame;
import com.ericgha.parser.MonkeyBuilder;
import com.ericgha.parser.Parser;

public class Day11 {

    /*
     Notes: com.ericgha.parser.MonkeyBuilder buisness is the product of number of items b 2 most active monkies
     We simulat for 20 rounds
     - In a single turn com.ericgha.monkey inspects and throws all of its items
     - New items get added to a queue
     */

    private final Parser parser;
    private final MonkeyBuilder[] params;

    public Day11(String resourceName) {
        this.parser = new Parser();
        params = parser.parseResource( resourceName );
    }

    public Long getMonkeyBusinessScoreDampened(int round) {
        MonkeyGame monkeyGame = new MonkeyGame( params, 3 );
        if (monkeyGame.roundsPlayed() != round) {
            monkeyGame.playTo( round );
        }
        return monkeyGame.getMonkeyBusiness();
    }

    public Long getMonkeyBusinessScoreUnDampened(int round) {
        MonkeyGame monkeyGame = new MonkeyGame( params, 1 );
        if (monkeyGame.roundsPlayed() != round) {
            monkeyGame.playTo( round );
        }
        return monkeyGame.getMonkeyBusiness();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day11 day11 = new Day11( filename );
        int round20 = 20;
        long round20Score = day11.getMonkeyBusinessScoreDampened( round20 );
        System.out.printf( "Dampened Monkey Business score on round %d: %d%n", round20, round20Score );
        int round10_000 = 10_000;
        long round1000ScoreUn = day11.getMonkeyBusinessScoreUnDampened( round10_000 );
        System.out.printf( "UnDampened Monkey Business score on round %d: %d%n", round10_000, round1000ScoreUn );
    }

}
