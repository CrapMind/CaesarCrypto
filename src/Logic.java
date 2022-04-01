import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

public class Logic implements UI {
    private static final Logic INSTANCE = new Logic();
    private boolean isItFileToUpload;
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyz,. /\\\"'!@#$+_-=;:?<>&(){}[]*%";
    private String choice;
    private int key;

    private Logic(){}
    @Override
    public String userChoice() {
        Map<Integer, String> choices = new HashMap<>();
        choices.put(1, "Выбран Шифр");
        choices.put(2, "Выбрана Дешифровка");
        choices.put(3, "Выбрана Дешифровка Brute Force");
        choices.put(4, "Выход");
        Scanner scanner = new Scanner(System.in);
        String userChoice = scanner.nextLine();
        while (!(userChoice.equals("1") || userChoice.equals("2") || userChoice.equals("3") || userChoice.equals("4"))) {
            System.out.println(Messages.ENCRYPT_CHOICE);
            userChoice = scanner.nextLine();
        }
        this.choice = userChoice;
        return choices.get(Integer.parseInt(userChoice));
    }

    @Override
    public int generateKey() {
        System.out.println(Messages.KEY_CHOICE);
        Scanner scanner = new Scanner(System.in);
        int key = 0;
        try {
            while (key < 1 || key > 2) {
                key = scanner.nextInt();
            }
            switch (key) {
                case 1 -> {
                    System.out.println(Messages.ENTER_KEY.plus(ALPHABET.length()));
                    key = scanner.nextInt();
                    System.out.println(Messages.ENTERED_KEY.plus(key));
                    this.key = key;
                    return key;
                }
                case 2 -> {
                    System.out.println(Messages.GENERATE_KEY);
                    key = (int) (1 + Math.random() * ALPHABET.length());
                    System.out.println(Messages.KEY_WAS_GENERATED.toString() + key);
                    this.key = key;
                    return key;
                }
            }
        }
        catch (InputMismatchException e) {
            return generateKey();
        }
        return key;
    }
    public String fileName() {
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        try {
        while (!(Files.isRegularFile(Path.of(fileName)))) {
                if (isItFileToUpload) {
                    System.out.println(Messages.ENTER_FILE_TO_UPLOAD);
                    if (!(Files.exists(Path.of(fileName)))) {
                        Files.createFile(Path.of(fileName));
                    }
                }
                if (Files.isRegularFile(Path.of(fileName))) {
                    return fileName;
                }
                else {
                    System.out.println(Messages.ENTER_FILE_TO_LOAD);
                    fileName = scanner.nextLine();
                }
            }
        } catch (IOException | InvalidPathException e) {
            return fileName();
        }
        return fileName;
    }
    public void action(String fileToLoad, String fileToUpload, String encryptOrDecipher, int key) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad));
             BufferedWriter writer = new BufferedWriter(new FileWriter(fileToUpload))) {
            while (reader.ready()) {
                int data = reader.read();
                char symbol = (char) data;
                if (ALPHABET.contains("" + symbol)) {
                    int index = ALPHABET.indexOf(Character.toLowerCase(symbol));
                    index = switch (encryptOrDecipher) {
                        case "encrypt" -> index + key >= ALPHABET.length() ? (index + key) % ALPHABET.length() : index + key;
                        case "decipher" -> index - key < 0 ? ALPHABET.length() + (index - key) : index - key;
                        default -> throw new IllegalStateException("Неизвестное значение " + encryptOrDecipher);
                    };
                    symbol = ALPHABET.charAt(index);
                }
                data = symbol;
                writer.write(data);
            }
        } catch (IOException e) {
            System.out.println(Messages.UNKNOWN_ERROR);
            e.printStackTrace();
        }
    }
    @Override
    public void bruteForce(String fileToLoad, String fileToUpLoad) throws IOException {
       int iterator = 1;
       int key = iterator;
       int maxOfSplitWords = 0;

        while (iterator < ALPHABET.length()) {
            action(fileToLoad, fileToUpLoad, "decipher", iterator);
            Scanner scanner = new Scanner(Path.of(fileToUpLoad));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] splitWords = line.split(" ");
                maxOfSplitWords = maxOfSplitWords > splitWords.length ? maxOfSplitWords : splitWords.length;
                if (maxOfSplitWords == splitWords.length) {
                    key = iterator;
                }
            }
            iterator++;
        }
        System.out.println("Ключ подобран: " + key);
        action(fileToLoad, fileToUpLoad, "decipher", key);
    }
    public int getKey() {
        return this.key;
    }
    public int getUserChoice() {
        if (choice == null) {
            return 0;
        }
        return Integer.parseInt(this.choice);
    }
    public static Logic getInstance() {
        return INSTANCE;
    }
    public boolean setFileToUpload(boolean isItFileToUpload) {
       return this.isItFileToUpload = isItFileToUpload;
    }
}
