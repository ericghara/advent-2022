import com.ericgha.Display;
import com.ericgha.ReaderUtils;
import com.ericgha.Sequencer;
import com.ericgha.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisplayIntegrationTest {

    Display display;
    Sequencer sequencer;

    @BeforeEach
    void before() {
        this.display = new Display();
        this.sequencer = new Sequencer();
    }

    @Test
    void input0() {
        String INPUT = "test0";
        Stream<String> inputStream = ReaderUtils.getResourceFileReader( INPUT ).lines();
        Stream<Event> events = sequencer.stream( inputStream );
        display.drawImage( events );
        String expected = """
                ##..##..##..##..##..##..##..##..##..##..
                ###...###...###...###...###...###...###.
                ####....####....####....####....####....
                #####.....#####.....#####.....#####.....
                ######......######......######......####
                #######.......#######.......#######.....
                """;
        String found = display.toString();
        assertEquals( expected.strip(), found );
    }
}
