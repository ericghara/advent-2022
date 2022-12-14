package com.ericgha.tokenizer;

import com.ericgha.MatcherUtil;
import com.ericgha.ReaderUtils;
import com.ericgha.exception.FileReadException;
import com.ericgha.filesystem.File;
import com.ericgha.tokenizer.tokens.ChangeDir;
import com.ericgha.tokenizer.tokens.Command;
import com.ericgha.tokenizer.tokens.ListDir;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 * This implements closable, manual closing is only necessary when stream is incompletely read, or the instance is never
 * streamed.  For most use cases, there is no need to close or use try-with resources.
 */
public class Tokenizer implements Closeable {

    private static final String COMMAND_REGEX = "^\\$\\s*";
    private static final String CHANGE_DIR_REGEX = "cd\\s*";
    private static final String LIST_DIR_REGEX = "ls$";
    private static final String DIR_REGEX = "dir\\s*";
    private static final String FILE_REGEX = "((\\d+)\\s*)";

    private final Matcher isCommand;
    private final Matcher isChangeDir;
    private final Matcher isListDir;
    private final Matcher isDir;
    private final Matcher isFile;
    private final Stream<Command> commandStream;
    private String nextLine;
    private final BufferedReader reader;

    public Tokenizer(String resourceName) {
        // matchers
        this.isCommand = MatcherUtil.getMatcher( COMMAND_REGEX );
        this.isChangeDir = MatcherUtil.getMatcher( CHANGE_DIR_REGEX );
        this.isListDir = MatcherUtil.getMatcher( LIST_DIR_REGEX );
        this.isDir = MatcherUtil.getMatcher( DIR_REGEX );
        this.isFile = MatcherUtil.getMatcher( FILE_REGEX );
        // rest
        this.reader = init( resourceName );
        this.nextLine = nextLineOrNull();
        this.commandStream = Stream.iterate( nextCommand( null ), this::hasNext, this::nextCommand );
    }

    @Override
    public void close() {
        try (reader) {
        } catch (IOException e) {
            throw new IllegalStateException( "Could not close reader" );
        }
    }

    public Stream<Command> stream() {
        return commandStream;
    }

    private ListDir createListDirCommand(String line, int startIndex) throws IOException {
        ArrayList<File> files = new ArrayList<>();
        ArrayList<String> dirs = new ArrayList<>();
        while (true) {
            String curLine = nextLineOrNull();
            if (Objects.isNull( curLine ) || isCommand.reset( curLine ).find()) {
                nextLine = curLine;
                break;
            }
            if (isDir.reset( curLine ).find()) {
                String dirName = curLine.substring( isDir.end(), curLine.length() );
                dirs.add( dirName );
            } else if (isFile.reset( curLine ).find()) {
                long sizeB = Long.parseLong( isFile.group( 2 ) );
                String fileName = curLine.substring( isFile.end(), curLine.length() );
                files.add( new File( fileName, sizeB ) );
            } else {
                throw new IllegalArgumentException( "Unrecognized input: " + curLine );
            }
        }
        return new ListDir( List.copyOf( dirs ), List.copyOf( files ) );
    }

    private ChangeDir createChangeDirCommand(String line, int startIndex) throws IOException {
        if (startIndex == line.length()) {
            throw new IllegalArgumentException( "Received an empty target" );
        }
        this.nextLine = nextLineOrNull();
        return new ChangeDir( line.substring( startIndex ) );
    }

    private Command nextCommand(Command notUsed) throws IllegalStateException {
        String line = nextLine;
        if (Objects.isNull( line )) {
            return null;
        }
        if (!isCommand.reset( line ).find()) {
            throw new IllegalArgumentException( "Provided line is not a command: " + line );
        }
        int start = isCommand.end();
        int end = line.length();
        try {
            if (isListDir.reset( line ).region( start, end ).matches()) {
                return createListDirCommand( line, isListDir.end() );
            } else if (isChangeDir.reset( line ).find()) {
                return createChangeDirCommand( line, isChangeDir.end() );
            } else {
                throw new IllegalStateException( "Unrecognized command line: " + nextLine );
            }
        } catch (IOException e) {
            throw new FileReadException( "File read error" );
        }
    }

    private boolean hasNext(Command lastCommand) {
        if (Objects.nonNull( lastCommand )) {
            return true;
        }
        this.close();
        return false;
    }

    @Nullable
    private String nextLineOrNull() throws FileReadException {
        try {
            if (reader.ready()) {
                return reader.readLine().strip();
            }
        } catch (IOException e) {
            throw new FileReadException( "Error reading file" );
        }

        return null;
    }

    private BufferedReader init(String resourceName) throws IllegalArgumentException {
        BufferedReader newReader;
        try {
            newReader = ReaderUtils.getResourceFileReader( resourceName );
            if (!newReader.ready()) {
                throw new IllegalArgumentException( "Received an empty file" );
            }
        } catch (IOException e) {
            throw new IllegalArgumentException( "Encountered an error reading the resource: " + resourceName );
        }
        return newReader;
    }
}
