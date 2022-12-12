package com.ericgha;

import com.ericgha.filesystem.File;
import com.ericgha.tokenizer.Tokenizer;
import com.ericgha.tokenizer.tokens.ChangeDir;
import com.ericgha.tokenizer.tokens.Command;
import com.ericgha.tokenizer.tokens.ListDir;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void streamMultiLine() {
        Tokenizer tokenizer = new Tokenizer("day07_test0");
        Command lsRoot = new ListDir(List.of("dir0", "dir1"),
                List.of(new File("file0", 0), new File("file1", 1) ) );
        List<Command> expected = List.of(new ChangeDir("/"),
                lsRoot,
                new ChangeDir("dir0"),
                new ListDir(List.of(), List.of(new File("file2", 2) ) ),
                new ChangeDir(".."),
                lsRoot
        );
        List<Command> found = tokenizer.stream().toList();
        assertEquals(expected, found);
    }

    @Test
    void streamSingleLine() {
        Tokenizer tokenizer = new Tokenizer("day07_test1");
        List<Command> expected = List.of(new ChangeDir( "/" ) );
        assertEquals( expected, tokenizer.stream().toList() );
    }

    @Test
    void streamActualInputDoesNotThrow() {
        Tokenizer tokenizer = new Tokenizer( "input_day07" );
        assertDoesNotThrow( () -> tokenizer.stream().forEach(System.out::println) );
    }

    @Test
    void streamEmptyThrows() {
        assertThrows( IllegalArgumentException.class, () -> new Tokenizer("empty") );
    }
}