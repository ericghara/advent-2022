package com.ericgha;

import com.ericgha.event.AddX;
import com.ericgha.event.Event;
import com.ericgha.event.NoOp;
import com.ericgha.event.Terminator;

import java.util.regex.Matcher;
import java.util.stream.Stream;

public class Sequencer {

    private final String NOOP_REGEX = "^\\s*noop\\s*$";
    private final String ADDX_REGEX = "^\\s*addx\\s*(-?\\d+)\\s*$";
    private final String TERMINATOR = "TERMINATOR";

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
        updateState(addX);
        return addX;
    }

    public Terminator createTerminator() {
        Terminator terminator = new Terminator( time );
        updateState( terminator );
        return terminator;
    }

    public Event processLine(String line) {
        if (noOpMatcher.reset(line).matches() ) {
            return createNoOp();
        } else if (addXMatcher.reset(line).matches() ) {
            int amount = Integer.parseInt( addXMatcher.group(1) );
            return createAddX( amount );
        }
//        else if (line.equals( TERMINATOR )) {
//            return createTerminator();
//        }
        throw new IllegalArgumentException("Unrecognized line: " + line);
    }

    public Stream<Event> stream(Stream<String> lineStream) {
//        Stream<String> terminatedStream = Stream.concat( lineStream, Stream.of(TERMINATOR ) );
        return lineStream.map(this::processLine);
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
