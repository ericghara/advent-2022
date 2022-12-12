import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherUtil {

    public static Matcher getMatcher(String regex) {
        return Pattern.compile(regex).matcher("");
    }

    public static String getFirstMatch(String input, Matcher matcher) {
        matcher.reset(input);
        if (!matcher.find() ) {
            throw new IllegalArgumentException(String.format("Unable to match %s", input) );
        }
        return input.substring( matcher.regionStart(), matcher.regionEnd() );
    }

}
