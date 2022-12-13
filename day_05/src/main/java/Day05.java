import com.ericgha.dto.CargoMove;
import com.ericgha.dto.StateAndMoves;

import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Day05 {

    private final Parser parser;

    public Day05() {
        parser = new Parser();
    }

    private StateAndMoves initialize(String filename) {
        return parser.parse( filename );
    }

    private void validateMove(CargoMove move, Stack<Character> source, Stack<Character> destination) throws IllegalArgumentException {
        if (Objects.isNull(source) || Objects.isNull( destination ) || source.size() - move.quantity() < 0) {
            throw new IllegalArgumentException(String.format("Cannot perform groupMove: %s", move  ) );
        }
    }
    public void groupMove(CargoMove move, Map<Character, Stack<Character>> stacks) {
        Stack<Character> source = stacks.get(move.source() );
        Stack<Character> destination = stacks.get(move.destination() );
        validateMove( move, source, destination );
        for (int i = source.size()-move.quantity(); i < source.size(); i++) {
            destination.push(source.get(i) );
        }
        int finalSourceSize = source.size()-move.quantity();
        while (source.size() > finalSourceSize ) {
            source.pop();
        }
    }

    public void sequentialMove(CargoMove move, Map<Character, Stack<Character>> stacks) {
        Stack<Character> source = stacks.get(move.source() );
        Stack<Character> destination = stacks.get(move.destination() );
        validateMove( move, source, destination );
        for (int i = 0; i < move.quantity(); i++) {
            destination.push(source.pop() );
        }
    }

    Map<Character, Stack<Character>> rearrange(StateAndMoves stateAndMoves, 
                                               BiConsumer<CargoMove, Map<Character, Stack<Character>>> mover) {
        Stream<CargoMove> moves = stateAndMoves.moves();
        Map<Character, Stack<Character>> stacks = stateAndMoves.state();
        moves.forEach( curMove -> mover.accept(curMove, stacks) );
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
    
    public String rearrangeAndGetTops(String filename, BiConsumer<CargoMove, Map<Character, Stack<Character>>> mover) {
        StateAndMoves stateAndMoves = initialize( filename );
        rearrange( stateAndMoves, mover );
        return listTops( stateAndMoves.state() );
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        Day05 day05 = new Day05();
        String topAfterSequentialRearrange = day05.rearrangeAndGetTops(filename, day05::sequentialMove );
        System.out.printf( ">>> Tops after sequential rearrangement { %s }%n", topAfterSequentialRearrange );
        String topAfterGroupRearrange = day05.rearrangeAndGetTops(filename, day05::groupMove );
        System.out.printf( ">>> Tops after group rearrangement { %s }%n", topAfterGroupRearrange );
    }

}

