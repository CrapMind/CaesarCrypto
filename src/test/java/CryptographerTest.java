import org.junit.jupiter.api.Test;
import util.Action;
import util.Cryptographer;
import static org.junit.jupiter.api.Assertions.*;

class CryptographerTest {

    private final Cryptographer cryptographer = new Cryptographer();

    @Test
    void testEncrypt() {
        char result = cryptographer.processChar('а', 3, Action.ENCRYPT);
        assertEquals('г', result);
    }

    @Test
    void testDecrypt() {
        char result = cryptographer.processChar('d', 3, Action.DECRYPT);
        assertEquals('a', result);
    }

    @Test
    void testProcessCharNonAlphabet() {
        char result = cryptographer.processChar('!', 3, Action.ENCRYPT);
        assertEquals('$', result);
    }

    @Test
    void testEncryptWithWrapAround() {
        char result = cryptographer.processChar('%', 3, Action.ENCRYPT);
        assertEquals('в', result);
    }

    @Test
    void testDecryptWithWrapAround() {
        char result = cryptographer.processChar('а', 3, Action.DECRYPT);
        assertEquals(']', result);
    }

    @Test
    void testGetAlphabetLength() {
        assertEquals(88, Cryptographer.getAlphabetLength());
    }
}
