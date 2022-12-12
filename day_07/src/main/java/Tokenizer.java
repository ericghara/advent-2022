import command.ChangeDir;
import command.Command;
import command.ListDir;
import exception.FileReadException;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public class Tokenizer implements Closeable, Iterator<Command> {

    private final Matcher isCommand;
    private final Matcher isChangeDir;
    private final Matcher isListDir;
    private final Matcher isDir;
    private final Matcher isFile;
    private final Stream<Command> commandStream;
    private String nextLine;
    private final BufferedReader reader;

    public Tokenizer(String resourceName) {
        this.isCommand = compileIsCommand();
        this.isChangeDir = compileIsChangeDir();
        this.isListDir = compileIsListDir();
        this.isDir = compileIsDir();
        this.isFile = compileIsFile();
        this.reader = init( resourceName );
        this.commandStream = Stream.iterate( null, this::hasNext, this::nextCommand);
    }

    @Override
    public boolean hasNext() {
        return hasNext(null);
    }

    @Override
    public void close() {
        try (reader) {
        } catch (IOException e) {
            throw new IllegalStateException( "Could not close reader" );
        }
    }

    @Override
    public Command next() {
        return nextCommand(null);
    }

    public Stream<Command> stream() {
        if (hasNext()) {
            return commandStream;
        }
        throw new IllegalStateException( "Nothing to stream.  Has this been closed?" );
    }

    private ListDir createListDirCommand(String line, int startIndex) throws IOException {
        ArrayList<FileTree.File> files = new ArrayList<>();
        ArrayList<String> dirs = new ArrayList<>();
        while (true) {
            String curLine = nextLineOrNull();
            if (Objects.isNull( curLine ) || isCommand.reset( curLine ).find() ) {
                nextLine = curLine;
                break;
            }
            if (isDir.reset( curLine ).find()) {
                String dirName = curLine.substring( isDir.regionEnd(), curLine.length() );
                dirs.add( dirName );
            } else if (isFile.reset( curLine ).find()) {
                long sizeB = Long.parseLong( isFile.group( 1 ) );
                String fileName = curLine.substring( isFile.regionEnd(), curLine.length() );
                files.add( new FileTree.File( fileName, sizeB ) );
            } else {
                throw new IllegalArgumentException( "Unrecognized input: " + curLine );
            }
        }
        return new ListDir( List.copyOf( dirs ), List.copyOf( files ) );
    }

    private ChangeDir createChangeDirCommand(String line, int startIndex) throws IOException {
        if (startIndex == line.length() ) {
            throw new IllegalArgumentException("Received an empty target");
        }
        this.nextLine = nextLineOrNull();
        return new ChangeDir( line.substring( startIndex ) );
    }

    private Command nextCommand(Command notUsed) throws IllegalStateException {
        String line = nextLine;
        nextLine = null;
        if (!isCommand.reset( line ).find()) {
            throw new IllegalArgumentException( "Provided line is not a command: " + line );
        }
        int start = isCommand.regionStart();
        int end = line.length();
        try {
            if (isListDir.reset( line ).region( start, end ).matches()) {
                return createListDirCommand(line, isListDir.regionEnd() );
            } else if (isChangeDir.reset( line ).find()) {
                return createChangeDirCommand(line, isChangeDir.regionEnd() );
            } else {
                throw new IllegalStateException( "Unrecognized command line: " + nextLine );

            }
        } catch (IOException e) {
            throw new FileReadException("File read error");
        }
    }

    private boolean hasNext(Command notUsed) {
        if (Objects.nonNull( nextLine )) {
            return true;
        }
        this.close();
        return false;
    }

    @Nullable
    private String nextLineOrNull() throws IOException {
        if (reader.ready() ) {
            return reader.readLine().strip();
        }
        return null;
    }

    private BufferedReader init(String resourceName) throws IllegalArgumentException {
        try {
            BufferedReader reader = ReaderUtils.getResourceFileReader( resourceName );
            if (!reader.ready()) {
                throw new IllegalArgumentException( "Received an empty file" );
            }
            nextLine = nextLineOrNull();
        } catch (IOException e) {
            throw new IllegalArgumentException( "Encountered an error reading the resource: " + resourceName );
        }
        return reader;
    }

    private Matcher compileIsCommand() {
        String regex = "^\\$\\s*";
        return MatcherUtil.getMatcher( regex );
    }

    private Matcher compileIsChangeDir() {
        String regex = "cd\\s*";
        return MatcherUtil.getMatcher( regex );
    }

    private Matcher compileIsListDir() {
        String regex = "ls$";
        return MatcherUtil.getMatcher( regex );
    }

    private Matcher compileIsDir() {
        String regex = "dir\\s*";
        return MatcherUtil.getMatcher( regex );
    }

    private Matcher compileIsFile() {
        String regex = "((\\d+)\\s*)";
        return MatcherUtil.getMatcher( regex );
    }
}
