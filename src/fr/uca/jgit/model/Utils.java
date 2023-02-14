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

    static File getObjectFile(String hash) {
        return getObjectFile(hash, false);
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

    public static String loadFile(File file) {
        if (!file.exists()) {
            throw new RuntimeException("IO Error retriving object in .jgit directory: " + file.getPath());
        }
        try {
            byte[] bytes = new byte[(int) file.length()];
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            fis.read(bytes);
            fis.close();
            return new String(bytes);
        } catch (IOException e) {
            System.out.println("IO Error loading object from .jgit directory: " + e.getMessage());
            return null;
        }
    }

    /** Loads the content of the file corresponding to the given hash (from file .jgit/object/[hash]). **/
    public static String loadObjFile(String hash) {
        File objectFile = getObjectFile(hash, false);
        return loadFile(objectFile);
    }

    /** Loads the content of the file corresponding to the given hash (from file .jgit/logs/[hash]). **/
    public static String loadLogFile(String hash) {
        File objectFile = new File(".jgit/logs/" + hash);
        return loadFile(objectFile);
    }
}
