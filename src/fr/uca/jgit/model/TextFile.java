package fr.uca.jgit.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextFile implements Node {
    final private String content;

    public TextFile(String content) {
        this.content = content;
    }

    /** Loads the text file corresponding to the given hash (from file .git/object/[hash]). **/
    public static TextFile loadFile(String hash) {
        return new TextFile(Utils.loadObjFile(hash));
    }

    /** Restores the file node at the given path. **/
    @Override
    public void restore(String path) {
        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("IO Error restoring file node: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return content;
    }
}
