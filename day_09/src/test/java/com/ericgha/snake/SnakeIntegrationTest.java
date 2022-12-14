package com.ericgha.snake;

import com.ericgha.Location;
import com.ericgha.Parser;
import com.ericgha.move.Move;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SnakeIntegrationTest {

    Parser parser = new Parser();
    Snake snake;

    Stream<Move> getMoves(String stringInput) {
        BufferedReader reader = new BufferedReader( new StringReader( stringInput ) );
        return parser.stream( reader );
    }

    Set<Location> createExpected(String expectedStr) {
        String[] lines = expectedStr.split( "\\n" );
        int y = lines.length - 1;
        Set<Location> expected = new HashSet<>();
        for (String s : lines) {
            char[] line = s.toCharArray();
            for (int col = 0; col < line.length; col++) {
                if (line[col] != '.') {
                    expected.add( new Location( col, y ) );
                }
            }
            y--;
        }
        return expected;
    }

    @Test
    void twoSegmentSnake() {
        String input = """
                R 4
                U 4
                L 3
                D 1
                R 4
                D 1
                L 5
                R 2
                """;
        String expectedStr = """
                ..##..
                ...##.
                .####.
                ....#.
                s###..
                """;
        snake = new Snake( 1 );
        snake.model( getMoves( input ) );
        Set<Location> expected = createExpected( expectedStr );
        Set<Location> found = snake.getSegment( 0 ).getSeen();
        assertEquals( expected, found );
    }

    @Test
    void fiveSegmentSnake() {
        String input = """
                R 5
                """;
        String expectedStr = """
                ......
                ......
                ......
                ......
                s#....
                """;
        snake = new Snake( 4 );
        snake.model( getMoves( input ) );
        Set<Location> expected = createExpected( expectedStr );
        Set<Location> found = snake.getSegment( 3 ).getSeen();
        assertEquals( expected, found );
    }

    @Test
    void tenSegmentSnake() {
        String input = """
                R 5
                U 8
                L 8
                D 3
                R 17
                D 10
                L 25
                U 20
                """;
        String expectedStr = """
                ..........................
                ..........................
                ..........................
                ..........................
                ..........................
                ..........................
                ..........................
                ..........................
                ..........................
                #.........................
                #.............###.........
                #............#...#........
                .#..........#.....#.......
                ..#..........#.....#......
                ...#........#.......#.....
                ....#......s.........#....
                .....#..............#.....
                ......#............#......
                .......#..........#.......
                ........#........#........
                .........########.........
                """;
        snake = new Snake( 9 );
        snake.model( getMoves( input ) );
        Set<Location> expected = createExpected( expectedStr );
        Set<Location> found = snake.getSegment( 8 ).getSeen();
        assertEquals( expected.size(), found.size() );
    }

}
