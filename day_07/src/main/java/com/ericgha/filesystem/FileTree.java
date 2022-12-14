package com.ericgha.filesystem;

import com.ericgha.MatcherUtil;
import com.ericgha.tokenizer.tokens.ChangeDir;
import com.ericgha.tokenizer.tokens.Command;
import com.ericgha.tokenizer.tokens.ListDir;

import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileTree {

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
