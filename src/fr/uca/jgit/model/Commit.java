package fr.uca.jgit.model;

import java.util.List;

public class Commit implements JGitObject {
    List<Commit> parents;

    @Override
    public String hash() {
        return null;
    }

    /** Stores the corresponding object in .git directory **/
    @Override
    public void store() {
    }

    /** Loads the text file corresponding to the given hash. **/
    public static Commit loadCommit(String hash) {
        return null;
    }

    /** Checkout the commit. **/
    void checkout() {
    }
}
