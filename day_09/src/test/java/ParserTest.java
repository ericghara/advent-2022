import com.ericgha.Parser;
import com.ericgha.move.Move;
import com.ericgha.move.X;
import com.ericgha.move.Y;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    Parser parser = new Parser();

    @Test
    void stream() {
        String input = """
                U 10
                D 1
                R 100
                L 21
                """;
        BufferedReader reader = new BufferedReader( new StringReader( input ) );
        List<Move> expected = List.of( new Y( 10 ), new Y( -1 ), new X( 100 ), new X( -21 ) );
        List<Move> found = parser.stream( reader ).toList();
        assertEquals( expected, found );
    }
}