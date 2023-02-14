package fr.uca.jgit.model;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CommitTest {

    @Test
    public void testCommitCheckout() throws IOException {
        // Create a test folder with some files in it
        File testFolder = new File("test"),
                file1 = new File(testFolder,"file1.txt"),
                file2 = new File(testFolder,"file2.txt");
        // Create a commit with the test folder as its state
        Folder state = new Folder();
        state.getChildren().put("file1.txt", new TextFile("test content"));
        state.getChildren().put("file2.txt", new TextFile("more content"));
        Commit commit = new Commit(List.of(), state, "msg");

        // Checkout the commit
        commit.checkout();

        // Check that the test folder has been recreated and its contents match the original files
        assertTrue(testFolder.exists());
        assertTrue(testFolder.isDirectory());
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertEquals("test content", Files.readString(file1.toPath()));
        assertEquals("more content", Files.readString(file2.toPath()));

        // Clean up by deleting the test folder
        assertTrue(file1.delete());
        assertTrue(file2.delete());
        assertTrue(testFolder.delete());
    }
}
