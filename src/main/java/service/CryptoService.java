package service;

import util.Action;
import util.Cryptographer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CryptoService {
    private final Cryptographer cryptographer = new Cryptographer(); // Handles character encryption/decryption
    private int bruteForceKey; // Stores the key found by brute force

    // Performs encryption or decryption on the provided files using the specified key
    public void performAction(File fileToLoad, File fileToUpload, Action action, int key) {

        try (BufferedReader reader = Files.newBufferedReader(fileToLoad.toPath());
             BufferedWriter writer = Files.newBufferedWriter(fileToUpload.toPath())) {

            while (reader.ready()) {
                char symbol = (char) reader.read();
                symbol = cryptographer.processChar(symbol, key, action); // Process each character with the cryptographic key
                writer.write(symbol);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Attempts to decrypt a file using brute force by trying all possible keys
    public void bruteForce(File fileToLoad, File fileToUpLoad) {
        int key = 1;
        int maxOfSplitWords = 0;

        for (int i = 0; i < Cryptographer.getAlphabetLength(); i++) {

            performAction(fileToLoad, fileToUpLoad, Action.BRUTEFORCE, i); // Try decrypting with key 'i'

            try (BufferedReader reader = Files.newBufferedReader(fileToUpLoad.toPath())) {
                while (reader.ready()) {
                    String line = reader.readLine();
                    String[] splitWords = line.split(" ");
                    int punctuationCount = 0;

                    for (String word : splitWords) {
                        // Heuristic to check word validity based on length and punctuation
                        if (word.isBlank() || word.length() > 25) break;
                        if (word.matches(".*[.,!?;:'\"-]$")) punctuationCount++;
                    }

                    // Keep track of the key that results in the most valid words
                    if (punctuationCount > 0) {
                        maxOfSplitWords = Math.max(maxOfSplitWords, splitWords.length);
                        if (maxOfSplitWords == splitWords.length) {
                            key = i; // Set the key if the current one yields the most valid words
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        performAction(fileToLoad, fileToUpLoad, Action.DECRYPT, key); // Decrypt the file with the best found key
        this.bruteForceKey = key; // Store the found key
    }

    // Returns the key found by brute force
    public int getBruteForceKey() {
        return bruteForceKey;
    }
    // Sets the brute force key (used for resetting after a successful operation)
    public void setBruteForceKey(int bruteForceKey) {
        this.bruteForceKey = bruteForceKey;
    }
}
