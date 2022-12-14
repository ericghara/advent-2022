package com.ericgha.snake;

import com.ericgha.move.Move;

import java.util.stream.Stream;

public class Snake {

    private final Head head;
    private final BodySegment[] body;

    public Snake(int bodyLength) {
        validateNumSegments( bodyLength );
        this.head = new Head();
        this.body = createSegments( bodyLength );
    }

    public void model(Stream<Move> moves) {
        head.move( moves );
    }

    /**
     * @param segment zero indexed segment after head. For a body length of 1 (head with one segment) the only segment
     *                would have an index of 0.
     * @return the number of unique positions visited by this segment.
     */
    public int numPositionsVisited(int segment) {
        if (segment < 0 || segment >= body.length) {
            throw new IllegalArgumentException( String.format( "Segment must be in range [0, %d)", body.length ) );
        }
        return body[segment].getSeen().size();
    }

    // for testing
    BodySegment getSegment(int segment) {
        return body[segment];
    }

    private void validateNumSegments(int length) throws IllegalArgumentException {
        if (length <= 0) {
            throw new IllegalArgumentException(
                    "Body Length must be at least 1 (to produce a snake with 1 Head and at minimum 1 BodySegment). " );
        }
    }

    private BodySegment[] createSegments(int bodyLength) {
        BodySegment[] segments = new BodySegment[bodyLength];
        Followable last = head;
        for (int i = 0; i < segments.length; i++) {
            segments[i] = new BodySegment();
            last.setFollower( segments[i] );
            last = segments[i];
        }
        return segments;
    }
}
