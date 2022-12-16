package com.ericgha.monkey;

import com.ericgha.parser.MonkeyParams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MonkeyGame {

    private final TreeMap<Integer, Monkey> monkeys;
    private int round;
    final private int boredDivisor;

    public MonkeyGame(MonkeyParams[] monkeyParams, int boredDivisor) {
        this.round = 0;
        this.boredDivisor = boredDivisor;
        this.monkeys = initMonkeys( monkeyParams );
    }

    TreeMap<Integer, Monkey> initMonkeys(MonkeyParams[] monkeyParams) {
        var map = Arrays.stream( monkeyParams ).map( param -> new Monkey(param, this.boredDivisor) )
                .collect( Collectors.toMap( Monkey::id, m -> m, (a, b) -> null, TreeMap::new ) );
        if (map.size() != monkeyParams.length) {
            throw new IllegalArgumentException( "Encountered a duplicate key" );
        }
        for (Monkey monkey : map.values()) {
            if (!map.containsKey( monkey.trueTarget() ) || !map.containsKey( monkey.falseTarget() )) {
                throw new IllegalArgumentException( "Target not found for Monkey: " + monkey.id() );
            }
        }
        return map;
    }

    void routeItems(Monkey monkey) {
        TurnResult turnResult = monkey.takeTurn();
        monkeys.get( monkey.trueTarget() ).catchItems( turnResult.trueItems() );
        monkeys.get( monkey.falseTarget() ).catchItems( turnResult.falseItems() );
    }

    public void playTo(int endRound) {
        if (endRound < this.round) {
            throw new IllegalArgumentException("Cannot play to a round in the past.  Currently on round "+ round);
        }
        for (; this.round < endRound; this.round++) {
            for (Map.Entry<Integer, Monkey> entry : monkeys.entrySet()) {
                Monkey monkey = entry.getValue();
                routeItems( monkey );
            }
        }
    }

    public Long getMonkeyBusiness() {
        return monkeys.values().stream().map( Monkey::itemsThrown ).sorted( Comparator.reverseOrder() )
                .limit( 2 ).reduce( (Long x,Long y) -> (x * y) ).orElseThrow();
    }

    public int roundsPlayed() {
        return this.round;
    }

    /**
     * For testing
     *
     * @param index - literally the index in monkeys not necessarily Monkey:id()
     * @return
     */
    Monkey monkey(int index) {
        return monkeys.get( index );
    }


}
