package fr.uca.jgit.model;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextFileTest {
    private static final String CONTENT = "Hello, world!";

    @Test
    public void testStoreAndRestore() throws IOException {
        TextFile textFile = new TextFile(CONTENT);

        // Store the object
        textFile.store();

        // Verify that the object was stored with the correct hash
        String expectedHash = Utils.hash(textFile.toString());
        String actualHash = textFile.hash();
        assertEquals(expectedHash, actualHash);

        // Restore the object
        String fileName = "restored.txt";
        textFile.restore(fileName);

        // Verify that the file was restored correctly
        String restoredContent = new String(Files.readAllBytes(new File(fileName).toPath()));
        assertEquals(CONTENT, restoredContent);

        // Cleanup
        assertTrue(new File(fileName).delete());
    }

    @Test
    public void testLoadFile() {
        // Store the object
        TextFile textFile = new TextFile(CONTENT);
        textFile.store();

        // Load the object using its hash
        String expectedHash = textFile.hash();
        TextFile loadedFile = TextFile.loadFile(expectedHash);

        // Verify that the loaded file has the correct content
        assertEquals(CONTENT, loadedFile.toString());

        // Cleanup
        assertTrue(Utils.getObjectFile(expectedHash, false).delete());
    }
}
