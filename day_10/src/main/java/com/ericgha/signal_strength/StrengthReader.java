package com.ericgha.signal_strength;

import com.ericgha.event.Event;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class StrengthReader {

    private final Map<Integer,Long> timeStrength;
    private final int[] times;
    private int timesI;
    private int time; // maybe don't need
    public StrengthReader(int[] times) {
        validateTimes( times );
        this.times = times;
        this.timesI = 0;
        this.timeStrength = new HashMap<>();
    }

    private void validateTimes(int[] times) throws IllegalArgumentException {
        if (Arrays.stream(times).anyMatch( i -> i < 1 ) ) {
            throw new IllegalArgumentException("Time points must be >=1 ");
        }
    }

    /**
     *
     * @param event completed event that overlaps with time
     * @return strength at time
     */
    long recordEvent(Event event) {
        if (!event.hasCompleted() ) {
            throw new IllegalStateException("Encountered an uncompleted event");
        }
        // ignore warning.  Null check happens above
        long strength = (long) event.registerVal() * times[timesI];
        timeStrength.put(time, strength);
        this.timesI++;
        return strength;
    }

    boolean shouldRecord(Event event) {
        if (timesI >= times.length) {
            return false;
        }
        int nextTime = times[timesI];
        return event.start() <= nextTime && event.end() > nextTime;
    }

    public long read(Stream<Event> events) {
        return events.filter( this::shouldRecord ).mapToLong(this::recordEvent).sum();
    }

    public long getStrength(int time) {
        Long strength = timeStrength.get(time);
        if (Objects.isNull(strength) ) {
            throw new IllegalArgumentException("No reading was made at the provided time point");
        }
        return strength;
    }

}
