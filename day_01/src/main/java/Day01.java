
import java.io.BufferedReader;
import java.io.IOException;

public class Day01 {

    static class CalorieParser {

        private BufferedReader getReader(String filename) {
            return ReaderUtils.getResourceFileReader( this, filename );
        }

        public int getMaxCal(String filename) throws IOException {
            BufferedReader reader = this.getReader( filename );
            int max = 0;
            int totCals = 0;
            while (reader.ready()) {
                String cur = reader.readLine().trim();
                try {
                    totCals += Integer.parseInt( cur );
                } catch (NumberFormatException e) {
                    if (cur.isBlank()) {
                        max = Math.max( totCals, max );
                        totCals = 0;
                    } else {
                        throw new IllegalStateException( "Invalid input file format" );
                    }
                }
            }
            return max;
        }

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        int maxCals;
        try {
            maxCals = new CalorieParser().getMaxCal( filename );
        } catch (IOException e) {
            throw new IllegalArgumentException( String.format( "Could not read the input file %s", filename ) );
        }
        System.out.println( "Max Cals: " + maxCals );
    }
}
