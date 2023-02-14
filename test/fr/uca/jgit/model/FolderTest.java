package fr.uca.jgit.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class FolderTest {


    @Test
    public void testHash() {
        Node textFile = new TextFile("Hello, world!");
        Folder folder = new Folder();
        folder.getChildren().put("file.txt", textFile);
        Folder subFolder = new Folder();
        folder.getChildren().put("sub", subFolder);
        String expectedHash = Utils.hash("sub;d;" + subFolder.hash() + "\nfile.txt;t;" + textFile.hash());
        assertEquals(expectedHash, folder.hash());
    }

    @Test
    public void testStore() throws IOException {
        String tempDir = Files.createTempDirectory("test").toString();
        Node textFile = new TextFile("Hello, world!");
        Folder folder = new Folder();
        folder.getChildren().put("file.txt", textFile);
        Folder subFolder = new Folder();
        folder.getChildren().put("sub", subFolder);
        folder.store();
        assertTrue(Utils.getObjectFile(folder.hash(), false).exists());
        assertTrue(Utils.getObjectFile(subFolder.hash(), false).exists());
        // Cleanup
        assertTrue(Utils.getObjectFile(folder.hash(), false).delete());
        assertTrue(Utils.getObjectFile(textFile.hash(), false).delete());
        assertTrue(Utils.getObjectFile(subFolder.hash(), false).delete());
    }

    @Test
    public void testLoadFolder() throws IOException {
        String tempDir = Files.createTempDirectory("test").toString();
        File folderFile = new File(tempDir, "folder.txt");
        Node textFile = new TextFile("Hello, world!");
        Folder folder = new Folder();

        folder.getChildren().put("file.txt", textFile);
        Folder subFolder = new Folder();
        folder.getChildren().put("sub", subFolder);
        folder.store();
        String folderHash = Utils.hash("sub;d;" + subFolder.hash() + "\nfile.txt;t;" + textFile.hash());
        Folder loadedFolder = Folder.loadFolder(folderHash);
        assertEquals(2, loadedFolder.getChildren().size());
        assertTrue(loadedFolder.getChildren().containsKey("file.txt"));
        assertTrue(loadedFolder.getChildren().containsKey("sub"));
        assertEquals(textFile.hash(), loadedFolder.getChildren().get("file.txt").hash());
        assertEquals(subFolder.hash(), loadedFolder.getChildren().get("sub").hash());
    }

    @Test
    public void testRestore() throws IOException {
        String tempDir = Files.createTempDirectory("test").toString();
        Node textFile = new TextFile("Hello, world!");
        Folder folder = new Folder();
        folder.getChildren().put("file.txt", textFile);
        Folder subFolder = new Folder();
        folder.getChildren().put("sub", subFolder);
        subFolder.getChildren().put("fil.txt", textFile);

        folder.restore(tempDir);
        File file = new File(tempDir + "/file.txt");
        assertTrue(file.exists());
        assertEquals("Hello, world!", Files.readString(file.toPath()));
        File subFolderFile = new File(tempDir, "sub");
        assertTrue(subFolderFile.exists());
        File fil = new File(subFolderFile, "fil.txt");
        assertTrue(fil.exists());
        assertEquals("Hello, world!", Files.readString(fil.toPath()));
    }
}
