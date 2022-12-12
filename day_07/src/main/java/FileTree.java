import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTree {

    public record File(String name, long sizeB) {

    }

    public static class Node {

        private final Map<String, File> files;
        private final Map<String, Node> folders;
        private final String name;

        Node(String name) {
            this.name = name;
            this.files = new HashMap<>();
            this.folders = new HashMap<>();
        }

        public File addFile(String name, long sizeB) {
            File cur = files.putIfAbsent(name, new File(name, sizeB) );
            if (Objects.nonNull(cur) ) {
                throw new IllegalArgumentException("File already exists!: " + name );
            }
            return cur;
        }

        public Node addFolder(String name) {
            Node cur = folders.putIfAbsent( name, new Node( name ) );
            if (Objects.nonNull(cur) ) {
                throw new IllegalArgumentException("Folder already exists!: " + name );
            }
            return cur;
        }

        public Node getFolder(String folderName) {
            Node node = folders.get(folderName);
            if (Objects.isNull(node) ) {
                throw new IllegalArgumentException(String.format("Folder %s, was not found %s.", folderName) );
            }
            return node;
        }

        Node[] getFolders() {
            return this.folders.values().stream().toArray(Node[]::new);
        }

        public String getName() {
            return name;
        }

        File[] getFiles() {
            return this.files.values().toArray( File[]::new );
        }

        public long getFileSize(String fileName) {
            File file = files.get(fileName);
            if (Objects.isNull(file) ) {
                throw new IllegalArgumentException(String.format("File %s was not found", fileName) );
            }
            return file.sizeB();
        }
    }

    private final Node ROOT;

    public FileTree() {
        ROOT = new Node( "/" );
    }

    Node getRoot() {
        return this.ROOT;
    }

    public Walk walk() {
        return new Walk();
    }

    public class Walk {

        Stack<Node> path;
        Matcher letterOrDigit;

        Walk() {
            path = new Stack<>();
            path.push(getRoot() );
            letterOrDigit = Pattern.compile( "[\\p{Alnum}]" ).matcher("");
        }

        public Node cd(String target) {
            Node cur = path.peek();
            if (target.startsWith( ".." ) ) {
                path.pop();

            }
            else if (target.startsWith("/") ) {
                while (path.size() != 1) {
                    path.pop();
                }

            }
            else if (target.length() > 0 && Character.isLetterOrDigit( target.charAt( 0 ) ) ) {
                String folderName = MatcherUtil.getFirstMatch( target, letterOrDigit );
                Node nextFolder = cur.getFolder( folderName );
                path.push(nextFolder);
            }
            return path.peek();
        }

        public File addFile(String fileName, long sizeB) {
            Node cur = path.peek();
            return cur.addFile( fileName, sizeB );
        }

        public String[] listFolders() {
            Node cur = path.peek();
            return Arrays.stream(cur.getFolders() ).map(Node::getName).toArray(String[]::new);
        }

        public File[] getFiles() {
            Node cur = path.peek();
            return cur.getFiles();
        }

    }



}
