package com.ericgha.parser;

import java.util.ArrayDeque;
import java.util.Queue;

record ParserContext(Queue<String> lines, MonkeyParams monkeyParams) {

    static ParserContext fromLines(Queue<String> remainingLines) {
        Queue<String> lines = new ArrayDeque<>();
        while (lines.size() < Parser.LINES_PER_MONKEY) {
            lines.offer(remainingLines.poll() );
        }
        if (lines.size() != Parser.LINES_PER_MONKEY) {
            throw new IllegalStateException(String.format("Required %d to parse a com.ericgha.monkey but found %d", Parser.LINES_PER_MONKEY, lines.size() ) );
        }
        return new ParserContext(lines, new MonkeyParams() );
    }

}
