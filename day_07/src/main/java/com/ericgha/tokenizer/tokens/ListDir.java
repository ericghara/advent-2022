package com.ericgha.tokenizer.tokens;

import com.ericgha.filesystem.FileTree;

import java.util.List;

public record ListDir(List<String> dirNames, List<FileTree.File> files) implements Command {
}
