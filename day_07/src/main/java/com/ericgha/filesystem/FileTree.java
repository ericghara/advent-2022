package com.ericgha.filesystem;

import com.ericgha.tokenizer.MatcherUtil;
import com.ericgha.tokenizer.tokens.ChangeDir;
import com.ericgha.tokenizer.tokens.Command;
import com.ericgha.tokenizer.tokens.ListDir;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileTree {

    public static record File(String name, long sizeB) {
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

        public File addFile(File file) {
            File cur = files.putIfAbsent( file.name(), file );
            if (Objects.nonNull( cur )) {
                throw new IllegalArgumentException( "File already exists!: " + name );
            }
            return cur;
        }

        public Node addDir(String dirName) {
            Node cur = folders.putIfAbsent( dirName, new Node( dirName ) );
            if (Objects.nonNull( cur )) {
                throw new IllegalArgumentException( "Folder already exists!: " + dirName );
            }
            return cur;
        }

        public Node getFolder(String folderName) {
            Node node = folders.get( folderName );
            if (Objects.isNull( node )) {
                throw new IllegalArgumentException( String.format( "Folder %s, was not found", folderName ) );
            }
            return node;
        }

        Node[] getFolders() {
            return this.folders.values().stream().toArray( Node[]::new );
        }

        public String getName() {
            return name;
        }

        File[] getFiles() {
            return this.files.values().toArray( File[]::new );
        }

        public long getFileSize(String fileName) {
            File file = files.get( fileName );
            if (Objects.isNull( file )) {
                throw new IllegalArgumentException( String.format( "File %s was not found", fileName ) );
            }
            return file.sizeB();
        }
    }

    private final Node root;

    public FileTree() {
        root = new Node( "/" );
    }

    Node getRoot() {
        return this.root;
    }

    public Walk walk() {
        return new Walk();
    }

    public void addFrom(Stream<Command> commands) {
        Walk walk = walk();
        commands.forEach( walk::synchronizeWith );
    }

    public class Walk {

        Stack<Node> path;
        Matcher isLetterOrDigit;

        Walk() {
            path = new Stack<>();
            path.push( getRoot() );
            isLetterOrDigit = Pattern.compile( "[\\p{Alnum}]" ).matcher( "" );
        }

        public Node changeDir(String target) {
            Node cur = path.peek();
            if (target.equals( ".." )) {
                path.pop();

            } else if (target.equals( "/" )) {
                while (path.size() != 1) {
                    path.pop();
                }

            } else if (target.length() > 0 && Character.isLetterOrDigit( target.charAt( 0 ) )) {
                String folderName = MatcherUtil.getFirstMatch( target, isLetterOrDigit );
                Node nextFolder = cur.getFolder( folderName );
                path.push( nextFolder );
            }
            return path.peek();
        }

        public File addFile(String fileName, long sizeB) {
            Node cur = path.peek();
            return cur.addFile( new File( fileName, sizeB ) );
        }

        public File addFile(File file) {
            Node cur = path.peek();
            return cur.addFile( file );
        }

        public Node addDir(String dirName) {
            Node cur = path.peek();
            return cur.addDir( dirName );
        }

        public String[] listDirs() {
            Node cur = path.peek();
            return Arrays.stream( cur.getFolders() ).map( Node::getName ).toArray( String[]::new );
        }

        public File[] getFiles() {
            Node cur = path.peek();
            return cur.getFiles();
        }

        void synchronizeWith(Command command) {
            if (command instanceof ListDir) {
                ListDir ls = (ListDir) command;
                ls.dirNames().forEach( this::addDir );
                ls.files().forEach( this::addFile );
            } else if (command instanceof ChangeDir) {
                ChangeDir cd = (ChangeDir) command;
                this.changeDir( cd.target() );
            } else {
                throw new IllegalArgumentException( "Unrecognized Command" + command );
            }
        }
    }
}
