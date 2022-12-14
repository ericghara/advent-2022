package com.ericgha;

import com.ericgha.exception.FileReadException;
import com.ericgha.move.Move;
import com.ericgha.move.X;
import com.ericgha.move.Y;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Stream;


public class Parser implements Closeable {

    private final String matchLine = "^\\s*([UDRL])\\s*(\\d*)\\s*$";
    private final Matcher lineMatcher;
    private BufferedReader reader;

    public Parser() {
        this.lineMatcher = MatcherUtil.getMatcher( matchLine );
    }

    private Move matchLine(String line) {
        if (!lineMatcher.reset( line ).matches()) {
            throw new IllegalArgumentException(
                    String.format( "Unable to parse line: %s Unrecognized line format.", line ) );
        }
        String direction = lineMatcher.group( 1 );
        int absDistance = Integer.parseInt( lineMatcher.group( 2 ) );
        return switch (direction) {
            case "U" -> new Y( absDistance );
            case "D" -> new Y( -1 * absDistance );
            case "R" -> new X( absDistance );
            case "L" -> new X( -1 * absDistance );
            default -> throw new IllegalArgumentException( String.format( "Unrecognized direction %s", direction ) );
        };
    }

    public Stream<Move> stream(BufferedReader reader) {
        close();
        this.reader = reader;
        return reader.lines().map( this::matchLine );
    }

    public Stream<Move> stream(String resourceName) {
        return stream( ReaderUtils.getResourceFileReader( resourceName ) );
    }

    @Override
    public void close() {
        if (Objects.isNull( reader )) {
            return;
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new FileReadException( "Encountered an error while closing the reader.", e );
        }
    }
}
