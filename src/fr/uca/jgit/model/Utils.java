package fr.uca.jgit.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    /**Computes the hash of a Node based on its content. **/
    static public String hash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(content.getBytes());
            StringBuilder hash = new StringBuilder();
            for (byte b : hashBytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No SHA1 algorithm");
        }
    }

    /** File corresponding to hash (.jgit/object/[hash]). **/
    static File getObjectFile(String hash, boolean create) {
        File objectDir = new File(".jgit/objects/" + hash.substring(0, 2));
        if (!objectDir.exists()) {
            if (create) objectDir.mkdirs();
            else throw new RuntimeException("IO Error retriving object in .jgit directory: " + hash);
        }
        return new File(objectDir, hash.substring(2));
    }

    /** Store a single node object (to file .jgit/object/[hash]). **/
    static public void store(Node n) {
        String hash = n.hash();
        File objectFile = getObjectFile(hash, true);
        try (FileOutputStream fos = new FileOutputStream(objectFile)) {
            fos.write(n.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("IO Error storing object in .jgit directory: " + e.getMessage());
        }
    }

    /** Loads the content of the file corresponding to the given hash (from file .jgit/object/[hash]). **/
    public static String loadFile(String hash) {
        File objectFile = getObjectFile(hash, false);
        if (!objectFile.exists()) {
            throw new RuntimeException("IO Error retriving object in .jgit directory: " + hash);
        }
        try {
            byte[] bytes = new byte[(int) objectFile.length()];
            java.io.FileInputStream fis = new java.io.FileInputStream(objectFile);
            fis.read(bytes);
            fis.close();
            return new String(bytes);
        } catch (IOException e) {
            System.out.println("IO Error loading object from .jgit directory: " + e.getMessage());
            return null;
        }
    }
}
