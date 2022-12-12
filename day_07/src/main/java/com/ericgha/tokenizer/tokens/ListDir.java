package com.ericgha.tokenizer.tokens;

import com.ericgha.filesystem.File;

import java.util.List;

public record ListDir(List<String> dirNames, List<File> files) implements Command {
}
