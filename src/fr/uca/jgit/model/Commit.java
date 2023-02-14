package fr.uca.jgit.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Commit implements JGitObject {
    private List<Commit> parents;
    private Folder state;
    private LocalDateTime timestamp;
    private String message;

    Commit(List<Commit> parents, Folder state, LocalDateTime timestamp, String message) {
        this.parents = parents;
        this.state = state;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Commit(List<Commit> parents, Folder state, String message) {
        this(parents, state, LocalDateTime.now(), message);
    }

    public List<Commit> getParents() {
        return parents;
    }

    public Folder getState() {
        return state;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String hash() {
        return Utils.hash(toString());
    }

    /** Stores the corresponding object in .git directory (to file .git/logs/[hash]). **/
    @Override
    public void store() {
    }

    /** Loads the commit corresponding to the given hash (from file .git/logs/[hash]). **/
    public static Commit loadCommit(String hash) {
        String contents = Utils.loadFile(".git/logs/" + hash);
        String[] parts = contents.split("\n");

        List<Commit> parents = new ArrayList<>();
        String[] parentHashes = parts[0].split(" ");
        for (String parentHash : parentHashes) {
            if (!parentHash.isEmpty()) {
                parents.add(loadCommit(parentHash));
            }
        }
        LocalDateTime commitDateTime = LocalDateTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm:ss-dd/MM/yyyy"));
        String message = parts[2];
        String stateHash = parts[3];
        Folder state = Folder.loadFolder(stateHash);
        return new Commit(parents, state, commitDateTime, message);
    }

    /** Checkout the commit.
     * Removes all working directory content and restores the state of this commit.  **/
    public void checkout() {
        // Get the current working directory
        Path workingDir = Paths.get(System.getProperty("user.dir"));

        // Delete all files and directories in the working directory
        try (Stream<Path> stream = Files.walk(workingDir)) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException("IO Error deleting files and directories in the working directory: " + e.getMessage());
        }
        // Restore the state of this commit
        state.restore(workingDir.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(";", parents.stream().map(c -> c.hash()).toList()));
        sb.append(timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss-dd/MM/yyyy"))).append("\n");
        sb.append(message).append("\n");
        sb.append(state.hash());
        return sb.toString();
    }
}
