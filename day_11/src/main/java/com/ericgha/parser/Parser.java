package com.ericgha.parser;

import com.ericgha.MatcherUtil;
import com.ericgha.ReaderUtils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parser {

    // Note item listing is Head to Tail of queue L -> R
    static int LINES_PER_MONKEY = 6;
    String MONKEY_ID_REGEX = "Monkey\\s+(\\d+):";
    String ITEMS_REGEX = "Starting items:\\s*((\\d+,\\s+)*(\\d+))\\s*$";
    String OPERATION_REGEX = "Operation: new = (old|\\d+)\\s([+*])\\s(old|\\d+)";
    String DIVISOR_REGEX = "Test: divisible by\\s*(\\d+)";
    String TRUE_REGEX = "If true: throw to monkey\\s*(\\d+)";
    String FALSE_REGEX = "If false: throw to monkey\\s*(\\d+)";

    private final Matcher monkeyIdMatcher;
    private final Matcher itemsMatcher;
    private final Matcher opMatcher;
    private final Matcher divisorMatcher;
    private final Matcher trueMatcher;
    private final Matcher falseMatcher;

    public Parser() {
        this.monkeyIdMatcher = MatcherUtil.getMatcher( MONKEY_ID_REGEX );
        this.itemsMatcher = MatcherUtil.getMatcher( ITEMS_REGEX );
        this.opMatcher = MatcherUtil.getMatcher( OPERATION_REGEX );
        this.divisorMatcher = MatcherUtil.getMatcher( DIVISOR_REGEX );
        this.trueMatcher = MatcherUtil.getMatcher( TRUE_REGEX );
        this.falseMatcher = MatcherUtil.getMatcher( FALSE_REGEX );
    }

    Queue<String> readResource(String resourceName) {
        return ReaderUtils.getResourceFileReader( resourceName ).lines()
                .filter( s -> !s.isBlank() ).map( String::trim )
                .collect( Collectors.toCollection( ArrayDeque::new ) );
    }

    private void tryMatchNextLine(ParserContext context, Matcher matcher) throws IllegalArgumentException {
        String line = context.lines().poll();
        if (!matcher.reset( line ).matches()) {
            throw new IllegalArgumentException( "Encountered improperly formatted line: " + line );
        }
    }

    ParserContext parseMoneyId(ParserContext context) {
        tryMatchNextLine( context, monkeyIdMatcher );
        int id = Integer.parseInt( monkeyIdMatcher.group( 1 ) );
        context.monkeyBuilder().setId( id );
        return context;
    }

    ParserContext parseItems(ParserContext context) {
        tryMatchNextLine( context, itemsMatcher );
        String[] itemsStr = itemsMatcher.group( 1 ).split( ",\\s+" );
        int[] items = Arrays.stream( itemsStr ).mapToInt( Integer::parseInt ).toArray();
        context.monkeyBuilder().setItems( items );
        return context;
    }

    ParserContext parseWorryFunction(ParserContext context) {
        tryMatchNextLine( context, opMatcher );
        String var0 = opMatcher.group( 1 );
        String var1 = opMatcher.group( 3 );
        char op = opMatcher.group( 2 ).charAt( 0 );
        context.monkeyBuilder().setWorryParams( var0, var1, op );
        return context;
    }

    ParserContext parseDivisor(ParserContext context) {
        tryMatchNextLine( context, divisorMatcher );
        int divisor = Integer.parseInt( divisorMatcher.group( 1 ) );
        context.monkeyBuilder().setDivisor( divisor );
        return context;
    }

    ParserContext parseTrueCondition(ParserContext context) {
        tryMatchNextLine( context, trueMatcher );
        int target = Integer.parseInt( trueMatcher.group( 1 ) );
        context.monkeyBuilder().setTrueTarget( target );
        return context;
    }

    ParserContext parseFalseCondition(ParserContext context) {
        tryMatchNextLine( context, falseMatcher );
        int target = Integer.parseInt( falseMatcher.group( 1 ) );
        context.monkeyBuilder().setFalseTarget( target );
        return context;
    }

    public MonkeyBuilder[] parseResource(String resourceName) {
        Queue<String> lines = readResource( resourceName );
        return IntStream.range( 0, lines.size()/LINES_PER_MONKEY )
                .mapToObj( line -> ParserContext.fromLines( lines ) )
                .map( this::parseMoneyId )
                .map( this::parseItems )
                .map( this::parseWorryFunction )
                .map( this::parseDivisor )
                .map( this::parseTrueCondition )
                .map( this::parseFalseCondition )
                .map( ParserContext::monkeyBuilder )
                .toArray( MonkeyBuilder[]::new );
    }

}
