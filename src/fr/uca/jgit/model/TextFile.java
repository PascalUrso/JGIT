package fr.uca.jgit.model;

public class TextFile implements Node {
    private String content;

    @Override
    public String hash() {
        return null;
    }

    /** Stores the corresponding object in .git directory (to file .git/object/[hash]). **/
    @Override
    public void store() {
    }

    /** Loads the text file corresponding to the given hash (from file .git/object/[hash]). **/
    public static TextFile loadFile(String hash) {
        return null;
    }

    /** Restores the file node at the given path. **/
    @Override
    public void restore(String path) {
    }
}
