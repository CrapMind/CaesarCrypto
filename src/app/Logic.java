package app;

import util.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Logic implements Work { // содержит основную логику программы
    private static final Logic INSTANCE = new Logic();
    private final Cipher cipher = new Cipher();
    private final FileHandler fileHandler = new FileHandler();
    private final BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));


    public static Logic getINSTANCE() {
        if (INSTANCE == null) {
            return new Logic();
        }
        return INSTANCE;
    }

    private int getKey(Actions value) { // метод на получение или генерацию ключа от пользователя
        int key = 0;

        do {
            try {
                switch (value) {
                    case DECIPHER -> key = 1;
                    case ENCRYPT -> {
                        System.out.println(Messages.KEY_CHOICE);
                        key = Integer.parseInt(userInputReader.readLine());
                    }
                }
                switch (key) {
                    case 1 -> {
                        System.out.println(Messages.ENTER_KEY.plus(cipher.getAlphabetLength()));
                        key = Integer.parseInt(userInputReader.readLine());
                        System.out.println(Messages.ENTERED_KEY.plus(key));
                        return key;
                    }
                    case 2 -> {
                        System.out.println(Messages.GENERATING_KEY);
                        key = (int) (1 + Math.random() * cipher.getAlphabetLength());
                        System.out.println(Messages.KEY_WAS_GENERATED.toString() + key);
                        return key;
                    }
                }
            } catch (InputMismatchException | IOException e) {
                System.out.println(Messages.INVALID_INPUT);
            }
        } while (true);
    }

    private void performAction(Path fileToLoad, Path fileToUpload, Actions action, int key) { // метод для шифрования и расшифровки

        try (BufferedReader reader = Files.newBufferedReader(fileToLoad);
             BufferedWriter writer = Files.newBufferedWriter(fileToUpload)) {

            System.out.print(action.getReadyMsg());

            while (reader.ready()) {
                char symbol = (char) reader.read();
                symbol = cipher.processChar(symbol, key, action);
                writer.write(symbol);
            }

            System.out.print(action.getDoneMsg());

        } catch (IOException e) {
            System.err.println(Messages.UNKNOWN_ERROR);
        }
    }

    private void bruteForce(Path fileToLoad, Path fileToUpLoad) { // метод для расшифровки brute force. Работает только с большими текстами
        int key = 1;
        int maxOfSplitWords = 0;

        for (int i = 0; i < cipher.getAlphabetLength(); i++) {

            performAction(fileToLoad, fileToUpLoad, Actions.BRUTEFORCE, i);

            try (BufferedReader reader = Files.newBufferedReader(fileToUpLoad)) {
                while (reader.ready()) {
                    String line = reader.readLine();
                    String[] splitWords = line.split(" ");
                    int punctuationCount = 0;

                    for (String word : splitWords) {
                        if (word.isBlank() || word.length() > 25) break;
                        if (word.matches(".*[.,!?;:'\"-]$")) punctuationCount++;
                    }

                    if (punctuationCount > 0) {
                        maxOfSplitWords = Math.max(maxOfSplitWords, splitWords.length);
                        if (maxOfSplitWords == splitWords.length) {
                            key = i;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println(Messages.UNKNOWN_ERROR);
                e.printStackTrace();
            }
        }
        System.out.println(Messages.KEY_HAS_BEEN_FOUND.plus(key));
        performAction(fileToLoad, fileToUpLoad, Actions.DECIPHER, key);
    }

    public BufferedReader getUserInputReader() {
        return userInputReader;
    }

    @Override
    public void start() { // метод, который отвечает за работу программы, пока не будет выбран выход: 4
        int key;
        String userInput = "";
        System.out.println(Messages.HELLO_MESSAGE);
        while (!userInput.equals("4")) {
            try {
                System.out.println(Messages.ENCRYPT_CHOICE);
                userInput = userInputReader.readLine();
                switch (userInput) {
                    case "1" -> {
                        System.out.println(Messages.CIPHER_CHOICE);
                        fileHandler.getFilePaths();
                        key = getKey(Actions.ENCRYPT);
                        performAction(fileHandler.getFileToLoad(), fileHandler.getFileToUpload(), Actions.ENCRYPT, key);
                    }
                    case "2" -> {
                        System.out.println(Messages.DECIPHER_CHOICE);
                        fileHandler.getFilePaths();
                        key = getKey(Actions.DECIPHER);
                        performAction(fileHandler.getFileToLoad(), fileHandler.getFileToUpload(), Actions.DECIPHER, key);
                    }
                    case "3" -> {
                        System.out.println(Messages.BRUTEFORCE_CHOICE);
                        fileHandler.getFilePaths();
                        bruteForce(fileHandler.getFileToLoad(), fileHandler.getFileToUpload());
                    }
                    case "4" -> end();
                }
            } catch (NoSuchElementException | IOException e) {
                System.out.println(Messages.INVALID_INPUT);
            }
        }
    }

    @Override
    public void end() { // завершение работы программы
        try {
            userInputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Messages.EXIT);
        System.exit(0);
    }
}
