package com.ericgha;

import com.ericgha.exception.FileReadException;
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
        this.nextLine = nextLineOrNull();
        this.commandStream = Stream.iterate( nextCommand(null ), this::hasNext, this::nextCommand);
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
                String dirName = curLine.substring( isDir.end(), curLine.length() );
                dirs.add( dirName );
            } else if (isFile.reset( curLine ).find()) {
                long sizeB = Long.parseLong( isFile.group( 2 ) );
                String fileName = curLine.substring( isFile.end(), curLine.length() );
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
        if (Objects.isNull(line) ) {
            return null;
        }
        if (!isCommand.reset( line ).find()) {
            throw new IllegalArgumentException( "Provided line is not a com.ericgha.command: " + line );
        }
        int start = isCommand.end();
        int end = line.length();
        try {
            if (isListDir.reset( line ).region( start, end ).matches()) {
                return createListDirCommand(line, isListDir.end() );
            } else if (isChangeDir.reset( line ).find()) {
                return createChangeDirCommand(line, isChangeDir.end() );
            } else {
                throw new IllegalStateException( "Unrecognized com.ericgha.command line: " + nextLine );

            }
        } catch (IOException e) {
            throw new FileReadException("File read error");
        }
    }

    private boolean hasNext(Command lastCommand) {
        if (Objects.nonNull( lastCommand ) || Objects.nonNull(nextLine) ) {
            return true;
        }
        this.close();
        return false;
    }

    @Nullable
    private String nextLineOrNull() throws FileReadException {
        try {
            if (reader.ready() ) {
                return reader.readLine().strip();
            }
        }
        catch (IOException e) {
            throw new FileReadException("Error reading file");
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
