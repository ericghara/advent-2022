import java.util.Arrays;

public class CharCounter {

    private final int RADIX = 26;
    private final char FIRST_CHAR = 'a';

    private final int[] counter;
    private int unique;

    public CharCounter() {
        this.counter = new int[RADIX];
        this.unique = 0;
    }

    private int getIndex(char c) {
        int index = c-FIRST_CHAR;
        if (index < 0 || index >= counter.length) {
            throw new IllegalArgumentException(String.format("Provided character: %c is outside of range [%c-%c)",
                    c, FIRST_CHAR, FIRST_CHAR+RADIX) );
        }
        return index;
    }

    public void addChar(char c) {
        int count = ++counter[getIndex( c )];
        if (count == 1) {
            unique++;
        }
        else if (count == 2) {
            unique--;
        }
    }

    public void dropChar(char c) {
        int count = --counter[getIndex( c )];
        if ( count == 1) {
            unique++;
        }
        else if (count == 0) {
            unique--;
        }
    }

    public int countUnique() {
        return unique;
    }

    public void reset() {
        Arrays.fill(counter, 0);
        unique = 0;
    }
}
