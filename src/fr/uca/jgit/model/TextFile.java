package fr.uca.jgit.model;

public class TextFile implements Node {
    private String content;

    @Override
    public String hash() {
        return null;
    }

    /** Stores the corresponding object in .git directory **/
    @Override
    public void store() {
    }

    /** Loads the text file corresponding to the givebn hash. **/
    public static TextFile loadFile(String hash) {
        return null;
    }

    @Override
    public void restore(String path) {
    }
}
