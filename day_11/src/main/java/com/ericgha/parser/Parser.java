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

    private static long product(long multiplicand, long multiplier) {
        return multiplicand * multiplier;
    }

    /**
     * @param var0      {@code OLD_KEY} or a string representation of a number
     * @param var1      {@code OLD_KEY} or a string representation of a number
     * @param operation '*' or '+'
     * @return
     */
    Function<Long, Long> createWorryFunction(String var0, String var1, char operation) {
        final String OLD_KEY = "old";
        BiFunction<Long, Long, Long> opFunction = switch (operation) {
            case '+' -> Long::sum;
            case '*' -> Parser::product;
            default -> throw new IllegalArgumentException( "Unregonized operation char: " + operation );
        };
        if (var0.equals( OLD_KEY ) && var1.equals( OLD_KEY )) {
            return old -> opFunction.apply( old, old );
        }
        long arg;
        if (var0.equals( OLD_KEY )) {
            arg = Long.parseLong( var1 );
        } else if (var1.equals( OLD_KEY )) {
            arg = Long.parseLong( var0 );
        } else {
            throw new IllegalArgumentException( String.format( "Unrecognized input vars: %s, %s", var0, var1 ) );
        }
        return old -> opFunction.apply( old, arg );
    }

    private void tryMatchNextLine(ParserContext context, Matcher matcher) throws IllegalArgumentException {
        String line = context.lines().poll();
        if (!matcher.reset( line ).matches()) {
            throw new IllegalArgumentException( "Encountered improperly formated line: " + line );
        }
    }

    ParserContext parseMoneyId(ParserContext context) {
        tryMatchNextLine( context, monkeyIdMatcher );
        int id = Integer.parseInt( monkeyIdMatcher.group( 1 ) );
        context.monkeyParams().setId( id );
        return context;
    }

    ParserContext parseItems(ParserContext context) {
        tryMatchNextLine( context, itemsMatcher );
        String[] itemsStr = itemsMatcher.group( 1 ).split( ",\\s+" );
        long[] items = Arrays.stream( itemsStr ).mapToLong( Integer::parseInt ).toArray();
        context.monkeyParams().setItems( items );
        return context;
    }

    ParserContext parseWorryFunction(ParserContext context) {
        tryMatchNextLine( context, opMatcher );
        String var0 = opMatcher.group( 1 );
        String var1 = opMatcher.group( 3 );
        char op = opMatcher.group( 2 ).charAt( 0 );
        Function<Long, Long> worryFunction = createWorryFunction( var0, var1, op );
        context.monkeyParams().setWorryFunction( worryFunction );
        return context;
    }

    ParserContext parseDivisor(ParserContext context) {
        tryMatchNextLine( context, divisorMatcher );
        int divisor = Integer.parseInt( divisorMatcher.group( 1 ) );
        context.monkeyParams().setDivisor( divisor );
        return context;
    }

    ParserContext parseTrueCondition(ParserContext context) {
        tryMatchNextLine( context, trueMatcher );
        int target = Integer.parseInt( trueMatcher.group( 1 ) );
        context.monkeyParams().setTrueTarget( target );
        return context;
    }

    ParserContext parseFalseCondition(ParserContext context) {
        tryMatchNextLine( context, falseMatcher );
        int target = Integer.parseInt( falseMatcher.group( 1 ) );
        context.monkeyParams().setFalseTarget( target );
        return context;
    }

    public MonkeyParams[] parseResource(String resourceName) {
        Queue<String> lines = readResource( resourceName );
        return IntStream.range( 0, lines.size()/LINES_PER_MONKEY )
                .mapToObj( line -> ParserContext.fromLines( lines ) )
                .map( this::parseMoneyId )
                .map( this::parseItems )
                .map( this::parseWorryFunction )
                .map( this::parseDivisor )
                .map( this::parseTrueCondition )
                .map( this::parseFalseCondition )
                .map( ParserContext::monkeyParams )
                .toArray( MonkeyParams[]::new );
    }

}
