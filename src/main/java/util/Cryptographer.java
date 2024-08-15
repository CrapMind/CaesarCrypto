package util;

public class Cryptographer {

    // Defines the alphabet used for encryption and decryption
    private final static String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyz,. /\\\"'!@#$+_-=;:?<>&(){}[]*%";

    // Processes a character by shifting it according to the specified key and action
    public char processChar(char symbol, int key, Action action) {

        if (ALPHABET.contains("" + symbol)) {
            int index = ALPHABET.indexOf(Character.toLowerCase(symbol));
            index = switch (action) {
                case ENCRYPT -> index + key >= ALPHABET.length() ? (index + key) % ALPHABET.length() : index + key;
                case DECRYPT, BRUTEFORCE -> index - key < 0 ? ALPHABET.length() + (index - key) : index - key;
            };
            return ALPHABET.charAt(index);
        }
        return symbol; // Return the original symbol if it's not in the ALPHABET
    }

    // Returns the length of the alphabet used for encryption/decryption
    public static int getAlphabetLength() {
        return ALPHABET.length();
    }
}

