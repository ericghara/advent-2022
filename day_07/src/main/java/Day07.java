public class Day07 {

    public Day07() {

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        String input = ReaderUtils.readEntireFile( filename ).strip();
    }

}
