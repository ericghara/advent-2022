package com.ericgha;

import com.ericgha.event.Event;

import java.util.stream.Stream;

/**
 * Draws pixels line by line.  In each clock cycle a single line is drawn.
 * When a line is complete it proceeds to the next line. The pixel at each
 * position along line is determined by the overlap with a Sprite at the time
 * the line is drawn.  The sprite's horizontal position is determined by the
 * register value at the time the pixel is drawn.
 */
public class Display {

    private final int LINES = 6; // num scan lines (height)
    private final int WIDTH = 40;

    private final char[][] image;

    public Display() {
        this.image = new char[LINES][WIDTH];
    }

    /**
     * @param time 1 indexed
     * @return 0 indexed time
     */
    private int offsetTime(int time) {
        return time - 1;
    }

    int curLine(int time) {
        return offsetTime( time ) / WIDTH;
    }

    int curColumn(int time) {
        return offsetTime( time ) % WIDTH;
    }

    char calcPixel(int curColumn, int spriteColumn) {
        // sprite width is 3
        int spriteStart = spriteColumn - 1;
        int spriteEnd = spriteColumn + 1;
        if (spriteStart <= curColumn && spriteEnd >= curColumn) {
            return '#';
        }
        return '.';
    }

    void drawPixels(Event event) {
        if (!event.hasCompleted()) {
            throw new IllegalStateException( "Received and event that has not completed" );
        }
        // ignore warning.  Null check by Event::hasCompleted
        int spriteCol = event.registerVal();
        for (int time = event.start(); time < event.end(); time++) {
            int curCol = curColumn( time );
            char pixel = calcPixel( curCol, spriteCol );
            int curLine = curLine( time );
            this.image[curLine][curCol] = pixel;
        }
    }

    /**
     * This can be called multiple times.  The image will update based on each {@code events}
     * {@link java.util.stream.Stream}.  For a partial update (ie max {@code time} <= LINE*WIDTH) updated pixels will
     * remain in the same state is in the previous frame.
     *
     * @param events
     */
    public void drawImage(Stream<Event> events) {
        // times 1 indexed while lines 0 indexed.
        events.takeWhile( event -> offsetTime( event.end() ) <= LINES * WIDTH ).forEach( this::drawPixels );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( WIDTH * ( LINES + 1 ) );
        for (int line = 0; line < LINES; line++) {
            for (int col = 0; col < WIDTH; col++) {
                sb.append( image[line][col] );
            }
            sb.append( '\n' );
        }
        sb.deleteCharAt( sb.length() - 1 );
        return sb.toString();
    }

}
