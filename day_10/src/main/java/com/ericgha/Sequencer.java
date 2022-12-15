package com.ericgha;

import com.ericgha.event.AddX;
import com.ericgha.event.Event;
import com.ericgha.event.NoOp;

import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 * Reads in an input file which programs operations on a single register. Operations are {@code noop} and {@code addx}.
 * {@code addx} has a parameter {@code amount} which is the value the register is incremented by.  {@code addx} requires
 * 2 clock cycles.  {@code noop} requires 1 clock cycle. The input is transformed into an
 * {@link java.util.stream.Stream}<{@link Event}>.  Each {@link Event} represents a time/clock interval where the
 * register remains constant at a value.
 */
public class Sequencer {

    private final String NOOP_REGEX = "^\\s*noop\\s*$";
    private final String ADDX_REGEX = "^\\s*addx\\s*(-?\\d+)\\s*$";

    private final Matcher noOpMatcher;
    private final Matcher addXMatcher;
    private int time;
    private int registerVal;

    public Sequencer() {
        this.noOpMatcher = MatcherUtil.getMatcher( NOOP_REGEX );
        this.addXMatcher = MatcherUtil.getMatcher( ADDX_REGEX );
        this.time = 1;
        this.registerVal = 1;
    }

    private void updateState(Event event) {
        time = event.end();
        event.setRegisterVal( this.registerVal );
        if (event instanceof AddX addX) {
            this.registerVal += addX.amount();
        }
    }

    public NoOp createNoOp() {
        NoOp noOp = new NoOp( time );
        updateState( noOp );
        return noOp;
    }

    public AddX createAddX(int amount) {
        AddX addX = new AddX( time, amount );
        updateState( addX );
        return addX;
    }

    public Event processLine(String line) {
        if (noOpMatcher.reset( line ).matches()) {
            return createNoOp();
        } else if (addXMatcher.reset( line ).matches()) {
            int amount = Integer.parseInt( addXMatcher.group( 1 ) );
            return createAddX( amount );
        }
        throw new IllegalArgumentException( "Unrecognized line: " + line );
    }

    public Stream<Event> stream(Stream<String> lineStream) {
        return lineStream.map( this::processLine );
    }

    // for testing
    int getTime() {
        return this.time;
    }

    // for testing;
    int getRegisterVal() {
        return this.registerVal;
    }

}
