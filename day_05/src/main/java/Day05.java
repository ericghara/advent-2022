import dto.CargoMove;
import dto.StateAndMoves;

import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Stream;

public class Day05 {

    private final Parser parser;

    public Day05() {
        parser = new Parser();
    }

    private StateAndMoves initialize(String filename) {
        return parser.parse( filename );
    }

    private void move(CargoMove move, Map<Character, Stack<Character>> stacks) {
        Stack<Character> source = stacks.get(move.source() );
        Stack<Character> destination = stacks.get(move.destination() );
        if (Objects.isNull(source) || Objects.isNull( destination ) || source.size() - move.quantity() < 0) {
            throw new IllegalArgumentException(String.format("Cannot perform move: %s", move  ) );
        }
        for (int i = 0; i < move.quantity(); i++) {
            destination.push(source.pop() );
        }
    }

    Map<Character, Stack<Character>> rearrange(StateAndMoves stateAndMoves) {
        Stream<CargoMove> moves = stateAndMoves.moves();
        Map<Character, Stack<Character>> stacks = stateAndMoves.state();
        moves.forEach( curMove -> move(curMove, stacks) );
        return stacks;
    }

    private String listTops(Map<Character, Stack<Character>> stacks) {
        char[] top = new char[stacks.size()];
        for (int i = 0; i < stacks.size(); i++ ) {
            char c = (char) ('1' + i);
            Stack<Character> stack = stacks.get( c );
            if (!stack.isEmpty()) {
                top[i] = stack.peek();
            }
        }
        return new String(top);
    }

    public String rearrangeAndGetTops(String filename) {
        StateAndMoves stateAndMoves = initialize( filename );
        rearrange( stateAndMoves );
        return listTops( stateAndMoves.state() );
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day05 day05 = new Day05();
        String topAfterRearrange = day05.rearrangeAndGetTops(filename);
        System.out.printf( ">>> Tops after rearrangement { %s }", topAfterRearrange );
    }

}

