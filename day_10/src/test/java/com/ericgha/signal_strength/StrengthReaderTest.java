package com.ericgha.signal_strength;

import com.ericgha.event.NoOp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StrengthReaderTest {

    StrengthReader strengthReader;

    @Test
    void recordEvent() {
        NoOp noOp = new NoOp( 3 );
        strengthReader = new StrengthReader( new int[]{3} );
        noOp.setRegisterVal( 3 );
        long expectedStrength = 9L; // 3*3
        strengthReader.tryRecordEvent( noOp );
        long recordedStrength = strengthReader.getStrength( 3 );
        assertEquals( expectedStrength, recordedStrength );
    }

    @ParameterizedTest(name = "[{index}] line: {0} ")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               start  |  shouldRecord
                 2   |  false
                 3   |  true
                 4   |  false
            """)
    void shouldRecord(int startTime, boolean shouldRecord) {
        NoOp noOp = new NoOp( startTime );
        strengthReader = new StrengthReader( new int[]{3} );
        boolean found = strengthReader.shouldRecord( noOp );
        assertEquals( shouldRecord, found );
    }
}