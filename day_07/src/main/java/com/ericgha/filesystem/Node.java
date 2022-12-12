package com.ericgha.filesystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Node {

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
