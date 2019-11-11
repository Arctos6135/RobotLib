package com.arctos6135.robotlib;

import java.io.File;

/**
 * A class that contains many useful static methods for testing.
 * 
 * @author Tyler Tian
 */
public final class TestUtils {

    private TestUtils() {
    }

    /**
     * Deletes a directory, including all of its contents, recursively.
     * 
     * @param dir The directory to delete
     */
    public static void deleteDirectoryAndContents(File dir) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                deleteDirectoryAndContents(f);
            } else {
                f.delete();
            }
        }
        dir.delete();
    }

    /**
     * Adds a shutdown hook to have the given directory, including all of its
     * contents, deleted on VM exit.
     * 
     * @param dir The directory to delete
     */
    public static void deleteDirectoryOnExit(File dir) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                deleteDirectoryAndContents(dir);
            }
        });
    }
}
