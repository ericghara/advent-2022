import java.util.Arrays;

public class DuplicateItemDetector {

    private final boolean[] seen;

    public DuplicateItemDetector() {
        this.seen = new boolean['z' + 1];
    }

    private void resetCounts() {
        Arrays.fill( seen, false);
    }

    public char findFirstDuplicate(char[] rucksack) {
        if (( rucksack.length&1 ) == 1) {
            throw new IllegalArgumentException("Number of items in rucksack must be even.");
        }
        resetCounts();
        for (int i = 0; i < rucksack.length/2; i++) {
            char item = rucksack[i];
            seen[item] = true;
        }
        for (int i = rucksack.length/2; i < rucksack.length; i++) {
            char item = rucksack[i];
            if (seen[item]) {
                return item;
            }
        }
        throw new IllegalArgumentException("Rucksack contained no duplicates");
    }

}
