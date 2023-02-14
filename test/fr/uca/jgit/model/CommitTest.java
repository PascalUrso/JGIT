package fr.uca.jgit.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class CommitTest {
    @BeforeClass
    static public void init() {
        File wd = new File(System.getProperty("user.dir"));
        File testrepo = new File(wd, "testrepo");
        if (!testrepo.exists()) testrepo.mkdirs();
        System.setProperty("user.dir", testrepo.getAbsolutePath());
    }

    @AfterClass
    static public void cleanup() throws IOException {
        File wd = new File(System.getProperty("user.dir"));
        System.setProperty("user.dir", wd.getParentFile().getAbsolutePath());
        Stream<Path> stream = Files.walk(wd.toPath());
        stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        wd.delete();
    }

    @Test
    public void testCommitCheckout() throws IOException {
        // Create a test folder with some files in it
        File workingDir = new File(System.getProperty("user.dir"));
        File testFolder = new File(workingDir, "test"),
                file1 = new File(workingDir,"file1.txt"),
                file2 = new File(testFolder,"file2.txt");
        // Create a commit with the test folder as its state
        Folder state = new Folder(), sub = new Folder();
        state.getChildren().put("test", sub);
        state.getChildren().put("file1.txt", new TextFile("test content"));
        sub.getChildren().put("file2.txt", new TextFile("more content"));
        state.store();
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
