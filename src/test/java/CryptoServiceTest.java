import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CryptoService;
import util.Action;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class CryptoServiceTest {

    private CryptoService cryptoService;
    private File fileToLoad;
    private File fileToUpload;

    @BeforeEach
    void setUp() throws Exception {
        cryptoService = new CryptoService();
        fileToLoad = Files.createTempFile("load", ".txt").toFile();
        fileToUpload = Files.createTempFile("upload", ".txt").toFile();
        Files.writeString(fileToLoad.toPath(), "test one two three");
    }

    @Test
    void testPerformActionEncrypt() throws Exception {
        cryptoService.performAction(fileToLoad, fileToUpload, Action.ENCRYPT, 3);
        String content = Files.readString(fileToUpload.toPath());
        assertNotEquals("test", content);
    }

    @Test
    void testPerformActionDecrypt() throws Exception {
        cryptoService.performAction(fileToLoad, fileToUpload, Action.ENCRYPT, 3);
        cryptoService.performAction(fileToUpload, fileToLoad, Action.DECRYPT, 3);
        String content = Files.readString(fileToLoad.toPath());
        assertEquals("test", content);
    }

    @Test
    void testBruteForceWithLongText() throws Exception {
        String originalText = "This is a test sentence. It includes punctuation!";
        Files.writeString(fileToLoad.toPath(), originalText);

        cryptoService.performAction(fileToLoad, fileToUpload, Action.ENCRYPT, 3);
        cryptoService.bruteForce(fileToUpload, fileToLoad);
        String content = Files.readString(fileToLoad.toPath());

        assertEquals(originalText, content);
        assertEquals(3, cryptoService.getBruteForceKey());
    }
}
