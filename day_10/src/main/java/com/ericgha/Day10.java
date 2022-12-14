package com.ericgha;

import com.ericgha.event.Event;
import com.ericgha.signal_strength.StrengthReader;

import java.util.Arrays;
import java.util.stream.Stream;

public class Day10 {
    // priority -> tic cycle -> check counter if req'd -> run command
    // addx(v) ends on check cycle: not counted
    // 1. what's horz position of sprite (3 pixels wide)?
    // 2. what pixel is being drawn?
    //  -> if sprite overlaps w/ current pixel ? 1 : 0;

    private StrengthReader strengthReader;
    private final Display display;

    public Day10(String resourceName, int[] measurementTimes) {
        this.display = new Display();
        run(resourceName, measurementTimes);
    }

    private void run(String resourceName, int[] measurementTimes) {
        Stream<Event> events = eventSource( resourceName );
        Stream<Event> loggedEvents = addStrengthReader( events, measurementTimes );
        display.drawImage( loggedEvents );
    }

    private Stream<Event> eventSource(String resourceName) {
        Stream<String> inputStream = ReaderUtils.getResourceFileReader( resourceName ).lines();
        Sequencer sequencer = new Sequencer();
        return sequencer.stream( inputStream );
    }

    /**
     * Doesn't modify stream.  Just peeks;
     * @param events
     * @param measurementTimes
     * @return
     */
    private Stream<Event> addStrengthReader(Stream<Event> events, int[] measurementTimes) {
        this.strengthReader = new StrengthReader( measurementTimes );
        return strengthReader.read( events );
    }

    public String getImage() {
        return display.toString();
    }

    public long sumReadingsAt() {
        return strengthReader.getSumOfReadings();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        int[] times = {20, 60, 100, 140, 180, 220};
        Day10 day10 = new Day10( filename, times );
        long sumReadings = day10.sumReadingsAt();
        System.out.printf( "The sum of all readings for time-points %s is: %d%n", Arrays.toString( times ), sumReadings );
        String image = day10.getImage();
        System.out.printf("%30s%n%s%n", "======Current image======", image);
    }
}
