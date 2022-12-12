package com.ericgha;

import com.ericgha.filesystem.FileTree;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class DiskCleanUp {


    private TreeMap<Long, Integer> dirSizes;
    private FileTree.Walk walk;

    DiskCleanUp(FileTree fileTree) {
        this.dirSizes = new TreeMap<>();
        this.walk = fileTree.walk();
        postOrder();
    }

    private long sumFiles() {
        FileTree.File[] files = walk.getFiles();
        return Arrays.stream( files ).mapToLong( FileTree.File::sizeB ).sum();
    }

    private long postOrder() {
        long curSize = sumFiles();
        String[] dirs = walk.listDirs();
        for (String dir : dirs) {
            walk.changeDir( dir );
            curSize += postOrder();
            walk.changeDir( ".." );
        }
        dirSizes.put( curSize, dirSizes.getOrDefault( curSize, 0 ) + 1 );
        return curSize;
    }

    public long sizeOfSmallDirs(long sizeCutoff) {
        long sum = 0;
        for (Map.Entry<Long, Integer> entry : dirSizes.headMap( sizeCutoff, true ).entrySet()) {
            long size = entry.getKey();
            int numFiles = entry.getValue();
            sum += size * numFiles;
        }
        return sum;
    }

    public long sizeOfSmallestDirLargerThan(long ceilingSize) {
        return dirSizes.ceilingKey( ceilingSize );
    }

    public long getTotalSize() {
        return dirSizes.lastKey();
    }

}
