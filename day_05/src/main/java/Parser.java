import dto.CargoMove;
import dto.StateAndMoves;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Stream;

public class Parser {

    public Parser() {
    }

    public StateAndMoves parse(String fileName) {
        Stack<char[]> stateBlock;
        Stream<CargoMove> moves;
        try {
            BufferedReader reader = ReaderUtils.getResourceFileReader( this, fileName);
            stateBlock = getInitialStateBlock( reader );
            moves = parseMoves( reader );
        }
        catch (IOException e) {
            throw new IllegalStateException("Encountered an IO error while parsing the input.");
        }
        Map<Character, Stack<Character>> initialState = getStacks(stateBlock);
        return new StateAndMoves( initialState, moves  );
    }

    private Stack<char[]> getInitialStateBlock(BufferedReader reader) throws IOException {
        Stack<char[]> stateLines = new Stack<>();
        while(reader.ready() ) {
            String curLine = reader.readLine();
            if (curLine.isBlank() ) {
                break;
            }
            stateLines.push(curLine.toCharArray() );
        }
        return stateLines;
    }

    private Map<Character, Stack<Character>> getStacks(Stack<char[]> stateBlock) {
        char[] index = stateBlock.pop();
        List<Integer> columns = new ArrayList<>();
        Map<Character, Stack<Character>> state = new HashMap<>();
        for (int i = 0; i < index.length; i++) {
            if (Character.isDigit(index[i]) ) {
                columns.add(i);
            }
        }
        while (!stateBlock.isEmpty() ) {
            char[] line = stateBlock.pop();
            for (int i = 0; i < columns.size(); i++) {
                int colIndex = columns.get( i );
                if (colIndex < 0 || colIndex >= line.length) {
                    continue;
                }
                else if (!Character.isLetter(line[colIndex]) ) {
                    columns.set(i, -1); // blacklist column (can't have floating boxes, so we know it's exhausted)
                }
                else {
                    Character label = index[colIndex];
                    Stack<Character> curStack = state.computeIfAbsent(label, k -> new Stack<>() );
                    curStack.push(line[colIndex]);
                }
            }
        }
        return state;
    }

    private Stream<CargoMove> parseMoves(BufferedReader reader) throws IOException {
        return reader.lines().map(String::strip).map(this::parseMoveLine);
    }

    private CargoMove parseMoveLine(String line) {
        int[] params = new int[3];
        int paramIndex = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (!Character.isDigit( c ) ) {
                if (i > 0 && Character.isDigit(line.charAt(i-1) ) ) {
                    paramIndex++;
                }
            }
            else {
                if (paramIndex == 0) {
                    params[0] = params[0] * 10 + c - '0';
                } else {
                    params[paramIndex] = c;
                }
            }
        }
        if (paramIndex != 2) {
            throw new IllegalArgumentException("Found an improperly formatted line.");
        }
        return new CargoMove( (char) params[1], (char) params[2], params[0]);
    }
}
