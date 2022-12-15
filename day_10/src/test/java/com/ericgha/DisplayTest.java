package com.ericgha;

import com.ericgha.event.AddX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisplayTest {

    Display display;

    @BeforeEach
    void before() {
        this.display = new Display();
    }


    @ParameterizedTest(name = "[{index}] time: {0} ")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               curColumn |  spriteColumn | expectedPixel
               0         |   -2          |    .
               0         |   -1          |    #
               0         |   0           |    #
               0         |   1           |    #
               0         |   2           |    .
               39        |   37          |    .
               39        |   38          |    #
            """)
    void calcPixel(int curColumn, int spriteColumn, char expectedPixel) {
        char found = display.calcPixel( curColumn, spriteColumn );
        assertEquals( expectedPixel, found );
    }

    @ParameterizedTest(name = "[{index}] time: {0} ")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               time |  line
               1    |   0
               40   |   0
               41   |   1
               80   |   1
               201  |   5
               240  |   5
            """)
    void curLine(int time, int expectedLine) {
        int foundLine = display.curLine( time );
        assertEquals( expectedLine, foundLine );
    }

    @ParameterizedTest(name = "[{index}] time: {0} ")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               time |  column
               1    |   0
               40   |   39
               41   |   0
               80   |   39
               201  |   0
               240  |   39
            """)
    void curColumn(int time, int expectedColumn) {
        int foundColumn = display.curColumn( time );
        assertEquals( expectedColumn, foundColumn );
    }

    @Test
    void drawPixels() {
        AddX addX = new AddX( 1, -1 ); // amount doesn't matter for this test
        // event corresponds to pixels (inclusive): (line 0, col 0), (line 0, col 1)
        addX.setRegisterVal( -1 );         // sprite's CENTER is at -1, so it covers cols -2,-1,0
        display.drawPixels( addX );
        String image = display.toString();
        assertEquals( '#', image.charAt( 0 ) );     // first pixel aligns with sprite, it is drawn
        assertEquals( '.', image.charAt( 1 ) );     // second pixel does not, it is not drawn
    }

}