package com.ericgha;

import com.ericgha.event.AddX;
import com.ericgha.event.Event;
import com.ericgha.event.NoOp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class SequencerTest {

    Sequencer sequencer;

    @BeforeEach
    void before() {
        this.sequencer = new Sequencer();
    }

    @Test
        // also testing NoOp a little here.  I know...
    void createNoOp() {
        int expectedStart = sequencer.getTime();
        int expectedStop = sequencer.getTime() + 1;
        int expectedRegisterVal = sequencer.getRegisterVal();
        NoOp noop = sequencer.createNoOp();
        assertEquals( expectedStart, noop.start(), "NoOp start time" );
        assertEquals( expectedStop, noop.end(), "NoOp end time" );
        assertEquals( expectedRegisterVal, noop.registerVal() );
    }

    @Test
    void createAddX() {
        int expectedStart = sequencer.getTime();
        int expectedStop = sequencer.getTime() + 2;
        int expectedRegisterVal = sequencer.getRegisterVal();
        AddX addX = sequencer.createAddX( 100 );
        assertEquals( expectedStart, addX.start(), "AddX start time" );
        assertEquals( expectedStop, addX.end(), "AddX end time" );
        assertEquals( expectedRegisterVal, addX.registerVal() );
    }

    @Test
    void updateState() {
        int expectedTime = sequencer.getTime() + 2;
        int expectedRegisterVal = sequencer.getRegisterVal() + 100;
        AddX _addX = sequencer.createAddX( 100 );
        assertEquals( expectedTime, sequencer.getTime() );
        assertEquals( expectedRegisterVal, sequencer.getRegisterVal() );
    }

    @ParameterizedTest(name = "[{index}] line: {0} ")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               line       |  amount
               addx 5     |   5
               addx -2    |  -2
               addx -14   |  -14
               addx -40   |  -40
               addx 3     |   3
               addx -24   |  -24
            """)
    void processLineAddX(String line, int amount) {
        Event event = sequencer.processLine( line );
        assertInstanceOf( AddX.class, event );
        AddX addX = (AddX) event;
        assertEquals( amount, addX.amount() );
    }

    @Test
    void processLineNoOp() {
        String line = "noop";
        Event event = sequencer.processLine( line );
        assertInstanceOf( NoOp.class, event );
    }

    @Test
    void stream() {
    }
}