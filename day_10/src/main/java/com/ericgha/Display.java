package com.ericgha;

import com.ericgha.event.Event;

import java.util.stream.Stream;

public class Display {

    private final int LINES = 6;
    private final int WIDTH = 40;

    private final char[][] image;

    public Display() {
        this.image = new char[LINES][WIDTH];
    }

    /**
     *
     * @param time 1 indexed
     * @return 0 indexed time
     */
    private int offsetTime(int time) {
        return time-1;
    }

    int curLine(int time) {
        return offsetTime( time ) / WIDTH;
    }

    int curColumn(int time) {
        return offsetTime( time ) % WIDTH;
    }

    char calcPixel(int curColumn, int spriteColumn) {
        // sprite width is 3
        int spriteStart = spriteColumn-1;
        int spriteEnd = spriteColumn+1;
        if (spriteStart <= curColumn && spriteEnd >= curColumn) {
            return '#';
        }
        return '.';
    }

    void drawPixels(Event event) {
        if (!event.hasCompleted()) {
            throw new IllegalStateException("Received and event that has not completed");
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

    public void drawImage(Stream<Event> events) {
        // remember events 1 indexed while lines 0 indexed.
        events.takeWhile( event -> offsetTime(event.end() ) <= LINES*WIDTH ).forEach( this::drawPixels );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(WIDTH*(LINES+1) );
        for (int line = 0; line < LINES; line++) {
            for (int col = 0; col < WIDTH; col++) {
                sb.append( image[line][col] );
            }
            sb.append( '\n' );
        }
        sb.deleteCharAt( sb.length()-1 );
        return sb.toString();
    }

}
