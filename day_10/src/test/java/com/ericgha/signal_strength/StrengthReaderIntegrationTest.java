package com.ericgha.signal_strength;

import com.ericgha.ReaderUtils;
import com.ericgha.Sequencer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrengthReaderIntegrationTest {

    Sequencer sequencer;
    StrengthReader strengthReader;

    @BeforeEach
    void before() {
        sequencer = new Sequencer();
    }

    @Test
    void input0() {
        int[] measureTimes = {20, 60, 100, 140, 180, 220};
        long[] expectedStrength = {420, 1140, 1800, 2940, 2880, 3960};
        String INPUT = "test0";
        Stream<String> inputStream = ReaderUtils.getResourceFileReader( INPUT ).lines();
        strengthReader = new StrengthReader( measureTimes );
        long _count = strengthReader.read( sequencer.stream( inputStream ) ).count(); // count ignored. Used to convert to hot stream
        for (int i = 0; i < measureTimes.length; i++) {
            long found = strengthReader.getStrength( measureTimes[i] );
            long expected = expectedStrength[i];
            assertEquals( expected, found, "Measurement at time " + measureTimes[i] );
        }
    }
}
