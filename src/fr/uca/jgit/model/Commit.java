package fr.uca.jgit.model;

import java.util.List;

public class Commit implements JGitObject {
    private List<Commit> parents;
    private Folder state;

    @Override
    public String hash() {
        return null;
    }

    /** Stores the corresponding object in .git directory (to file .git/logs/[hash]). **/
    @Override
    public void store() {
    }

    /** Loads the commit corresponding to the given hash (from file .git/logs/[hash]). **/
    public static Commit loadCommit(String hash) {
        return null;
    }

    /** Checkout the commit.
     * Removes all working directory content and restores the state of this commit.  **/
    void checkout() {
    }
}
