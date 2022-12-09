
import exception.FileReadException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * A collection of functions to create {@link Reader}s from common text sources.
 */
public class ReaderUtils {

    private static BufferedReader getFileReader(Path filePath) throws FileReadException {
        try {
            return new BufferedReader(new FileReader(filePath.toFile() ));
        } catch (Exception e) {
            throw new FileReadException("Unable to instantiate reader.", e);
        }
    }

    private static Reader getStringReader(String csv) {
        return new StringReader(csv);
    }

    /**
     * Convenience function to stream a resource file as a {@link Reader}.
     *
     * Usage:
     *
     * <pre>
     *     TestDir.getResourceFile(this, "aFile.csv");
     * </pre>
     *
     * @param aThis an object where the {@link ClassLoader} should be retrieved from.
     * @param fileName name of the resource
     * @return {@link Reader} character stream of the matching resource file
     * @throws FileReadException If resource cannot be found or read
     * @see ClassLoader#getResource(String)
     *
     */
    public static BufferedReader getResourceFileReader(Object aThis, String fileName) throws FileReadException {
        URL url = aThis.getClass().getResource(fileName);
        if (Objects.isNull(url) ) {
            throw new FileReadException("Could not open the resource " + fileName);
        }
        try {
            Path csv = Paths.get(url.toURI() );
            return getFileReader(csv);
        } catch(URISyntaxException e) {
            throw new FileReadException("Could not convert resource to a path:  " + fileName);
        }
    }
}
