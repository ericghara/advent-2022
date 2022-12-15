import com.ericgha.MatcherUtil;
import com.ericgha.ReaderUtils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class Parser {

    // Note item listing is Head to Tail of queue L -> R

    String MONKEY_ID_REGEX = "Monkey\\s+(\\d+):";
    String ITEMS_REGEX = "Starting items:\\s*((\\d+,\\s+)*(\\d+))\\s*$";
    String OPERATION_REGEX = "(old|\\d+)\\s([+*])\\s(old|\\d+)";
    String DIVISOR_REGEX = "Test: divisible by\\s*(\\d+)";
    String TRUE_REGEX = "If true: throw to monkey\\s*(\\d+)";
    String FALSE_REGEX = "If false: throw to monkey\\s*(\\d+)";

    private final Queue<String> lines;
    private final MonkeyParams[] monkeys;
    private final Matcher monkeyIdMatcher;
    private final Matcher itemsMatcher;
    private final Matcher opMatcher;
    private final Matcher divisorMatcher;
    private final Matcher trueMatcher;
    private final Matcher falseMatcher;

    public Parser(String resourceName) {
        this.lines = readResource( resourceName );
        this.monkeys = initMonkeys();
        this.monkeyIdMatcher = MatcherUtil.getMatcher( MONKEY_ID_REGEX );
        this.itemsMatcher = MatcherUtil.getMatcher( ITEMS_REGEX );
        this.opMatcher = MatcherUtil.getMatcher( OPERATION_REGEX );
        this.divisorMatcher = MatcherUtil.getMatcher( DIVISOR_REGEX );
        this.trueMatcher = MatcherUtil.getMatcher( TRUE_REGEX );
        this.falseMatcher = MatcherUtil.getMatcher( FALSE_REGEX );
    }

    public MonkeyParams[] monkeys() {
        if (Objects.isNull( monkeys[0].getWorryFunction() )) { // parse has not occurred.
            parse();
        }
        return this.monkeys;
    }

    Queue<String> readResource(String resourceName) {
        return ReaderUtils.getResourceFileReader( resourceName ).lines()
                .filter( String::isBlank ).map( String::trim )
                .collect( Collectors.toCollection( ArrayDeque::new ) );
    }

    private static int product(int multiplicand, int multiplier) {
        return multiplicand * multiplier;
    }

    /**
     * @param var0      {@code OLD_KEY} or a string representation of a number
     * @param var1      {@code OLD_KEY} or a string representation of a number
     * @param operation '*' or '+'
     * @return
     */
    Function<Integer, Integer> createWorryFunction(String var0, String var1, char operation) {
        final String OLD_KEY = "old";
        BiFunction<Integer, Integer, Integer> opFunction = switch (operation) {
            case '+' -> Integer::sum;
            case '*' -> Parser::product;
            default -> throw new IllegalArgumentException( "Unregonized operation char: " + operation );
        };
        if (var0.equals( OLD_KEY ) && var1.equals( OLD_KEY )) {
            return old -> opFunction.apply( old, old );
        }
        int arg;
        if (var0.equals( OLD_KEY )) {
            arg = Integer.parseInt( var1 );
        } else if (var1.equals( OLD_KEY )) {
            arg = Integer.parseInt( var0 );
        } else {
            throw new IllegalArgumentException( String.format( "Unrecognized input vars: %s, %s", var0, var1 ) );
        }
        return old -> opFunction.apply( old, arg );
    }

    private void tryMatchNextLine(Matcher matcher) throws IllegalArgumentException {
        String line = lines.poll();
        if (!matcher.reset( line ).matches()) {
            throw new IllegalArgumentException( "Encountered improperly formated line: " + line );
        }
    }

    MonkeyParams parseMoneyId(MonkeyParams monkeyParams) {
        tryMatchNextLine( monkeyIdMatcher );
        int id = Integer.parseInt( monkeyIdMatcher.group( 1 ) );
        monkeyParams.setId( id );
        return monkeyParams;
    }

    MonkeyParams parseItems(MonkeyParams monkeyParams) {
        tryMatchNextLine( itemsMatcher );
        String[] itemsStr = itemsMatcher.group( 1 ).split( ",\\s+" );
        int[] items = Arrays.stream( itemsStr ).mapToInt( Integer::parseInt ).toArray();
        monkeyParams.setItems( items );
        return monkeyParams;
    }

    MonkeyParams parseWorryFunction(MonkeyParams monkeyParams) {
        tryMatchNextLine( opMatcher );
        String var0 = itemsMatcher.group( 1 );
        String var1 = itemsMatcher.group( 3 );
        char op = itemsMatcher.group( 2 ).charAt( 0 );
        Function<Integer, Integer> worryFunction = createWorryFunction( var0, var1, op );
        monkeyParams.setWorryFunction( worryFunction );
        return monkeyParams;
    }

    MonkeyParams parseDivisor(MonkeyParams monkeyParams) {
        tryMatchNextLine( divisorMatcher );
        int divisor = Integer.parseInt( divisorMatcher.group( 1 ) );
        monkeyParams.setDivisor( divisor );
        return monkeyParams;
    }

    MonkeyParams parseTrueCondition(MonkeyParams monkeyParams) {
        tryMatchNextLine( trueMatcher );
        int target = Integer.parseInt( trueMatcher.group( 1 ) );
        monkeyParams.setTrueTarget( target );
        return monkeyParams;
    }

    MonkeyParams parseFalseCondition(MonkeyParams monkeyParams) {
        tryMatchNextLine( falseMatcher );
        int target = Integer.parseInt( falseMatcher.group( 1 ) );
        monkeyParams.setFalseTarget( target );
        return monkeyParams;
    }

    void parse() {
        Arrays.stream( monkeys ).map( this::parseMoneyId )
                .map( this::parseItems )
                .map( this::parseWorryFunction ).
                map( this::parseDivisor )
                .map( this::parseTrueCondition )
                .forEach( this::parseFalseCondition );
    }

    private MonkeyParams[] initMonkeys() {
        MonkeyParams[] params = new MonkeyParams[lines.size() / 6];
        for (int i = 0; i < params.length; i++) {
            params[i] = new MonkeyParams();
        }
        return params;
    }


}
