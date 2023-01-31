package fr.uca.jgit.model;

public interface Node extends JGitObject {
    /** Restores the file node at the given path. **/
    void restore(String path);
}
