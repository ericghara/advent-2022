package com.ericgha.filesystem;

import com.ericgha.tokenizer.Tokenizer;
import com.ericgha.tokenizer.tokens.ChangeDir;
import com.ericgha.tokenizer.tokens.Command;
import com.ericgha.tokenizer.tokens.ListDir;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileTreeTest {

    @Test
    void addFrom() {
        Command lsRoot = new ListDir( List.of("dir0", "dir1"),
                List.of(new File("file0", 0), new File("file1", 1) ) );
        Stream<Command> commands = Stream.of(new ChangeDir("/"),
                lsRoot,
                new ChangeDir("dir0"),
                new ListDir(List.of(), List.of(new File("file2", 2) ) ),
                new ChangeDir("..")
        );
        FileTree fileTree = new FileTree();
        assertDoesNotThrow(() -> fileTree.addFrom( commands ) );
    }

    @Test
    void addFromInput() {
        Tokenizer tokenizer = new Tokenizer( "input_day07" );
        FileTree fileTree = new FileTree();
        assertDoesNotThrow(() -> fileTree.addFrom( tokenizer.stream() ) );
        FileTree.Walk walk = fileTree.walk();
    }
}