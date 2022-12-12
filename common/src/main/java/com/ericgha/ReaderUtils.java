package com.ericgha;

import com.ericgha.exception.FileReadException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A collection of functions to create {@link Reader}s from common text sources.
 */
public class ReaderUtils {

    private static BufferedReader getFileReader(Path filePath) throws FileReadException {
        try {
            return new BufferedReader( new FileReader( filePath.toFile() ) );
        } catch (Exception e) {
            throw new FileReadException( "Unable to instantiate reader.", e );
        }
    }

    private static Reader getStringReader(String csv) {
        return new StringReader( csv );
    }

    /**
     * Convenience function to stream a resource file as a {@link Reader}.
     * <p>
     * Usage:
     *
     * <pre>
     *     TestDir.getResourceFile(this, "aFile.csv");
     * </pre>
     *
     * @param filename name of the resource
     * @return {@link Reader} character stream of the matching resource file
     * @throws FileReadException If resource cannot be found or read
     * @see ClassLoader#getResource(String)
     */
    public static BufferedReader getResourceFileReader(String filename) throws FileReadException {
        Path path = getResourceURI( filename );
        return getFileReader( path );
    }

    public static String readEntireFile(String filename) {
        return getResourceFileReader( filename ).lines().collect( Collectors.joining() );
    }

    public static Path getResourceURI(String filename) throws FileReadException {
        URL url = Thread.currentThread().getContextClassLoader().getResource( filename );
        URI uri;
        if (Objects.isNull( url )) {
            throw new FileReadException( "Could not open the resource " + filename );
        }
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            throw new FileReadException( "Could not convert resource to a path:  " + filename );
        }
        return Path.of( uri );
    }
}
