import com.ericgha.Day06;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class Day06Test {

    Day06 day06 = new Day06();

    @ParameterizedTest(name="[{index}] {0}")
    @CsvSource(useHeadersInDisplayName=true, delimiter = '|', textBlock = """
            Input                               | length | expected
            bvwbjplbgvbhsrlpgdmjqwftvncz        | 4      | 5 
            nppdvjthqldpwncqszvftbrmjlhg        | 4      | 6  
            nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg   | 4      | 10
            zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw    | 4      | 11
            mjqjpqmgbljsphdztnvjfqwrcgsmlb      | 14     | 19
            bvwbjplbgvbhsrlpgdmjqwftvncz        | 14     | 23
            nppdvjthqldpwncqszvftbrmjlhg        | 14     | 23
            nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg   | 14     | 29
            zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw    | 14     | 26
            """)
    void findFirstUniqueRange(String input, int length, int  expected) {
        assertEquals(expected, day06.findFirstUniqueRange( input, length ) );
    }
}