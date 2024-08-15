import org.junit.jupiter.api.Test;
import service.FileService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private final FileService fileService = new FileService();

    @Test
    void testGetValidFile() throws IOException {
        File validFile = Files.createTempFile("test", ".txt").toFile();
        File result = fileService.getValidFile(validFile);
        assertNotNull(result);
        Files.deleteIfExists(validFile.toPath());
    }

    @Test
    void testGetInvalidFile() {
        File invalidFile = new File("invalid.exe");
        File result = fileService.getValidFile(invalidFile);
        assertNull(result);
    }

    @Test
    void testGetInvalidFileWhenDirectory() {
        File directory = new File(System.getProperty("java.io.tmpdir"));
        File result = fileService.getValidFile(directory);
        assertNull(result, "Expected null when a directory is passed instead of a file.");
    }

    @Test
    void testGetInvalidFileWhenShortcut() throws Exception {

        Path tempFile = Files.createTempFile("tempFile", ".txt");
        File shortcut = new File(tempFile.toString() + ".lnk");
        File result = fileService.getValidFile(shortcut);
        assertNull(result, "Expected null when a shortcut is passed instead of a file.");

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testFilesAreIdentical() {
        File file1 = new File("file1.txt");
        fileService.setFileToLoad(file1);
        fileService.setFileToUpload(file1);
        assertTrue(fileService.filesAreIdentical());
    }

    @Test
    void testFilesAreNotIdentical() {
        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");
        fileService.setFileToLoad(file1);
        fileService.setFileToUpload(file2);
        assertFalse(fileService.filesAreIdentical());
    }

    @Test
    void testResetFiles() {
        fileService.setFileToLoad(new File("file1.txt"));
        fileService.setFileToUpload(new File("file2.txt"));
        fileService.resetFiles();
        assertNull(fileService.getFileToLoad());
        assertNull(fileService.getFileToUpload());
    }
}
