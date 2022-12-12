package com.ericgha;

import com.ericgha.filesystem.FileTree;
import com.ericgha.tokenizer.Tokenizer;

public class Day07 {

    private FileTree fileTree;

    public Day07(String filename) {
        fileTree = new FileTree();
        synchronize( filename );
    }

    private void synchronize(String filename) {
        Tokenizer tokenizer = new Tokenizer( filename );
        fileTree.addFrom( tokenizer.stream() );
    }

    public DiskCleanUp getDiskCleanUp() {
        return new DiskCleanUp( fileTree );
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException( "The only argument should be the name of the file resource" );
        }
        String filename = args[0];
        DiskCleanUp diskCleanUp = new Day07( filename ).getDiskCleanUp();
        long sumSmall = diskCleanUp.sizeOfSmallDirs( 100_000 );
        System.out.println( "Sum of directories smaller than 100,000 bytes: " + sumSmall );
        long spaceNeeded = 30_000_000;
        long spaceAvail = 70_000_000 - diskCleanUp.getTotalSize();
        long smallestDirLargerThan = diskCleanUp.sizeOfSmallestDirLargerThan( Math.max( 0, spaceNeeded - spaceAvail ) );
        System.out.printf( "Delete dir of size: %d to have at least %d free space", smallestDirLargerThan, spaceNeeded );
    }

}
