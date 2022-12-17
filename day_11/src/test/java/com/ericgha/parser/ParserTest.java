package com.ericgha.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    Parser parser = new Parser();

    ParserContext contextOf(String... line) {
        Queue<String> lines = Arrays.stream( line ).collect( Collectors.toCollection( ArrayDeque::new ) );
        return new ParserContext( lines, new MonkeyBuilder() );
    }

    @Test
    void parseMoneyId() {
        String line = "Monkey 1:";
        ParserContext context = contextOf( line );
        int expected = 1;
        int found = parser.parseMoneyId( context ).monkeyBuilder().id();
        assertEquals( expected, found );
    }

    @ParameterizedTest(name = "[{index}] line: {0}")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               line                           | expected
               Starting items: 79, 98         | 79, 98
               Starting items: 54, 65, 75, 74 | 54, 65, 75, 74
               Starting items: 74             | 74
               
            """)
    void parseItems(String line, String expected) {
        ParserContext context = contextOf( line );
        int[] found = parser.parseItems( context ).monkeyBuilder().items();
        String foundStr = Arrays.stream( found ).mapToObj( Integer::toString ).collect( Collectors.joining( ", " ) );
        assertEquals( expected, foundStr );
    }

    @ParameterizedTest(name = "[{index}] line: {0}")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               line                           | expected
               Operation: new = old * 11      | 77
               Operation: new = old + 1       | 8
               Operation: new = old * old     | 49
               
            """)
    void parseWorryFunction(String line, int expected) {
        int OLD = 7;
        ParserContext context = parser.parseWorryFunction( contextOf( line ) );
        int found = context.monkeyBuilder().WorryFunction(Integer.MAX_VALUE).apply( OLD );
        assertEquals( expected, found );
    }

    @Test
    void parseDivisor() {
        String line = "Test: divisible by 19";
        int found = parser.parseDivisor( contextOf( line ) ).monkeyBuilder().divisor();
        int expected = 19;
        assertEquals( expected, found );
    }

    @Test
    void parseTrueCondition() {
        String line = "If true: throw to monkey 19";
        int found = parser.parseTrueCondition( contextOf( line ) ).monkeyBuilder().trueTarget();
        int expected = 19;
        assertEquals( expected, found );
    }

    @Test
    void parseFalseCondition() {
        String line = "If false: throw to monkey 19";
        int found = parser.parseFalseCondition( contextOf( line ) ).monkeyBuilder().falseTarget();
        int expected = 19;
        assertEquals( expected, found );
    }

    @Test
    void parse() {
        MonkeyBuilder[] found = parser.parseResource( "test0" );
        assertEquals( 4, found.length, "array length" );
        for (MonkeyBuilder cur : found) {
            assertNotEquals( Integer.MIN_VALUE, cur.id(), "id" );
            assertNotNull( cur.items() );
            assertNotNull( cur.WorryFunction(Integer.MAX_VALUE) );
            assertNotEquals( Integer.MIN_VALUE, cur.divisor(), "divisor" );
            assertNotEquals( Integer.MIN_VALUE, cur.trueTarget(), "trueTarget" );
            assertNotEquals( Integer.MIN_VALUE, cur.falseTarget(), "falseTarget" );
        }
    }
}