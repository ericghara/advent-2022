import com.ericgha.Day11;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day11IntegrationTest {

    Day11 day11;

    @BeforeEach
    void before() {
        day11 = new Day11( "test0" );
    }

    @Test
    void getMonkeyBusinessScoreDampened() {
        long found = day11.getMonkeyBusinessScoreDampened(20);
        long expected = 10605;
        assertEquals(expected, found);
    }

    @Test
    void getMonkeyBusinessScoreUnDampened() {
        long found = day11.getMonkeyBusinessScoreUnDampened(10_000);
        long expected = 2713310158L;
        assertEquals( expected, found );
    }
}
