package fr.uca.jgit.model;

public interface JGitObject {
    /** Hash of the jgit object **/
    String hash();

    /** Stores the corresponding object in .git directory **/
    void store();
}
