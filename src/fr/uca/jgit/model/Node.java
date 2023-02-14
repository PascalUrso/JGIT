package fr.uca.jgit.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Node extends JGitObject {
    /** Restores the file node at the given path. **/
    default public String hash() {
        return Utils.hash(this.toString());
    }

    default public void store() {
        Utils.store(this);
    }

    void restore(String path);
}
