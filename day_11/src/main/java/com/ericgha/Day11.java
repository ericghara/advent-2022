package com.ericgha;

import com.ericgha.monkey.MonkeyGame;
import com.ericgha.parser.MonkeyParams;
import com.ericgha.parser.Parser;

public class Day11 {

    /*
     Notes: com.ericgha.parser.MonkeyParams buisness is the product of number of items b 2 most active monkies
     We simulat for 20 rounds
     - In a single turn com.ericgha.monkey inspects and throws all of its items
     - New items get added to a queue
     */

    private final Parser parser;
    private final MonkeyParams[] params;

    public Day11(String resourceName) {
        this.parser = new Parser();
        params = parser.parseResource( resourceName );
    }

    public long getMonkeyBusinessScoreDampened(int round) {
        MonkeyGame monkeyGame = new MonkeyGame( params, 3 );
        if (monkeyGame.roundsPlayed() != round) {
            monkeyGame.playTo( round );
        }
        return monkeyGame.getMonkeyBusiness();
    }

    public long getMonkeyBusinessScoreUnDampened(int round) {
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
        System.out.printf( "Dampened Monkey Business score on round %d: %d", round20, round20Score );
        int round1000 = 1000;
        long round1000ScoreUn = day11.getMonkeyBusinessScoreUnDampened( round1000 );
        System.out.printf( "UnDampened Monkey Business score on round %d: %d%n", round1000, round1000ScoreUn );
    }

}
