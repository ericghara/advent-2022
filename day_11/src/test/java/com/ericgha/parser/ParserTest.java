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
        return new ParserContext( lines, new MonkeyParams() );
    }

    @ParameterizedTest(name = "[{index}] vars: {0} {1} {2} ")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
               var0       |  var1  | op  |  expected
                old       |   1    |  +  |    8
                old       |   1    |  *  |    7
                old       |  old   |  +  |    14
                old       |  old   |  *  |    49
                 2        |  old   |  +  |    9
                 2        |  old   |  *  |    14
            """)
    void createWorryFunction(String var0, String var1, char op, int expected) {
        Long OLD = 7L;
        Long found = parser.createWorryFunction( var0, var1, op ).apply( OLD );
        assertEquals( expected, found );
    }

    @Test
    void parseMoneyId() {
        String line = "Monkey 1:";
        ParserContext context = contextOf( line );
        int expected = 1;
        int found = parser.parseMoneyId( context ).monkeyParams().Id();
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
        long[] found = parser.parseItems( context ).monkeyParams().Items();
        String foundStr = Arrays.stream( found ).mapToObj( Long::toString ).collect( Collectors.joining( ", " ) );
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
        long OLD = 7;
        ParserContext context = parser.parseWorryFunction( contextOf( line ) );
        Long found = context.monkeyParams().WorryFunction().apply( OLD );
        assertEquals( expected, found );
    }

    @Test
    void parseDivisor() {
        String line = "Test: divisible by 19";
        int found = parser.parseDivisor( contextOf( line ) ).monkeyParams().Divisor();
        int expected = 19;
        assertEquals( expected, found );
    }

    @Test
    void parseTrueCondition() {
        String line = "If true: throw to com.ericgha.monkey 19";
        int found = parser.parseTrueCondition( contextOf( line ) ).monkeyParams().TrueTarget();
        int expected = 19;
        assertEquals( expected, found );
    }

    @Test
    void parseFalseCondition() {
        String line = "If false: throw to com.ericgha.monkey 19";
        int found = parser.parseFalseCondition( contextOf( line ) ).monkeyParams().FalseTarget();
        int expected = 19;
        assertEquals( expected, found );
    }

    @Test
    void parse() {
        MonkeyParams[] found = parser.parseResource( "test0" );
        assertEquals( 4, found.length, "array length" );
        for (MonkeyParams cur : found) {
            assertNotEquals( Integer.MIN_VALUE, cur.Id(), "id" );
            assertNotNull( cur.Items() );
            assertNotNull( cur.WorryFunction() );
            assertNotEquals( Integer.MIN_VALUE, cur.Divisor(), "divisor" );
            assertNotEquals( Integer.MIN_VALUE, cur.TrueTarget(), "trueTarget" );
            assertNotEquals( Integer.MIN_VALUE, cur.FalseTarget(), "falseTarget" );
        }
    }
}