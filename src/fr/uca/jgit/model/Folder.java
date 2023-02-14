package fr.uca.jgit.model;

import java.io.File;
import java.util.*;

public class Folder implements Node {
    // Mapping Name -> Node
    private Map<String, Node> children;

    Map<String, Node> getChildren() {
        return children;
    }

    public Folder() {
        this.children = new HashMap<>();
    }

    /** Stores the corresponding object in .git directory (file .git/object/[hash]) **/
    @Override
    public void store() {
        for (Node child : children.values()) {
            child.store();
        }
        // Store folder
        Utils.store(this);
    }

    /** Return a list representation of the folder (see doc) **/
    @Override
    public String toString() {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, Node> entry : children.entrySet()) {
            String name = entry.getKey();
            Node child = entry.getValue();
            String type = child instanceof TextFile ? "t" : "d";
            lines.add(name + ";" + type + ";" + child.hash());
        }
        return String.join("\n", lines);
    }

    /** Loads the folder corresponding to the given hash (from file .git/object/[hash]). **/
    public static Folder loadFolder(String hash) {
        String contents = Utils.loadFile(hash);
        Folder folder = new Folder();
        if (!"".equals(contents)) {
            String[] lines = contents.split("\n");
            for (String line : lines) {
                String[] parts = line.split(";");
                String name = parts[0];
                String type = parts[1];
                String childHash = parts[2];
                Node child = null;
                if (type.equals("t")) {
                    child = TextFile.loadFile(childHash);
                } else {
                    child = Folder.loadFolder(childHash);
                }
                folder.getChildren().put(name, child);
            }
        }
        return folder;
    }

    /** Restores the file node at the given path. **/
    @Override
    public void restore(String path) {
        File folderDir = new File(path);
        if (!folderDir.exists()) {
            if (!folderDir.mkdirs()) {
                throw new RuntimeException("IO Error : Unable to create directory " + path);
            }
        }
        for (Map.Entry<String, Node> entry : children.entrySet()) {
            String name = entry.getKey();
            Node child = entry.getValue();
            String childPath = path + "/" + name;
            child.restore(childPath);
        }
    }
}
