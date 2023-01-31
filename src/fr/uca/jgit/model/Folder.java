package fr.uca.jgit.model;

import java.util.Map;

public class Folder implements Node {
    // Mapping Name -> Node
    private Map<String, Node> children;

    @Override
    public String hash() {
        return null;
    }

    /** Stores the corresponding object in .git directory (file .git/object/[hash]) **/
    @Override
    public void store() {
    }

    /** Return a list representation of the folder (see doc) **/
    @Override
    public String toString() {
        return null;
    }

    /** Loads the folder corresponding to the given hash (from file .git/object/[hash]). **/
    public static Folder loadFolder(String hash) {
        return null;
    }

    /** Restores the file node at the given path. **/
    @Override
    public void restore(String path) {
    }
}
