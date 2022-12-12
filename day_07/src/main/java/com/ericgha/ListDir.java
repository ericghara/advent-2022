package com.ericgha;

import java.util.List;

public record ListDir(List<String> dirNames, List<FileTree.File> files) implements Command {
}
