package com.ericgha;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void stream() {
        Tokenizer tokenizer = new Tokenizer("day07_test0");
        Command lsRoot = new ListDir(List.of("dir0", "dir1"),
                List.of(new FileTree.File("file0", 0), new FileTree.File("file1", 1) ) );
        List<Command> expected = List.of(new ChangeDir("/"),
                lsRoot,
                new ChangeDir("dir0"),
                new ListDir(List.of(), List.of(new FileTree.File("file2", 2) ) ),
                new ChangeDir(".."),
                lsRoot
        );
        List<Command> found = tokenizer.stream().toList();
        assertEquals(expected, found);
    }
}