package com.ericgha.signal_strength;

import com.ericgha.event.Event;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class StrengthReader {

    private final Map<Integer, Long> timeStrength;
    private final int[] times;
    private int timesI;

    public StrengthReader(int[] times) {
        validateTimes( times );
        this.times = times;
        this.timesI = 0;
        this.timeStrength = new HashMap<>();
    }

    private void validateTimes(int[] times) throws IllegalArgumentException {
        if (Arrays.stream( times ).anyMatch( i -> i < 1 )) {
            throw new IllegalArgumentException( "Time points must be >=1 " );
        }
    }

    /**
     * @param event completed event that overlaps with time
     * @return strength at time
     */
    void tryRecordEvent(Event event) {
        if (!event.hasCompleted()) {
            throw new IllegalStateException( "Encountered an uncompleted event" );
        }
        if (!shouldRecord( event )) {
            return;
        }
        int time = times[timesI];
        // ignore warning.  Null check happens above
        long strength = (long) event.registerVal() * time;
        timeStrength.put( time, strength );
        this.timesI++;
    }

    boolean shouldRecord(Event event) {
        if (timesI >= times.length) {
            return false;
        }
        int nextTime = times[timesI];
        return event.start() <= nextTime && event.end() > nextTime;
    }

    public Stream<Event> read(Stream<Event> events) {
        return events.peek( this::tryRecordEvent );
    }

    public long getStrength(int time) {
        Long strength = timeStrength.get( time );
        if (Objects.isNull( strength )) {
            throw new IllegalArgumentException( "No reading was made at the provided time point: " + time );
        }
        return strength;
    }

    public long getSumOfReadings() {
        if (timesI != times.length) {
            throw new IllegalStateException( "Strength reader has not recoded all events." );
        }
        return Arrays.stream( times ).mapToLong( this::getStrength ).sum();
    }

}
