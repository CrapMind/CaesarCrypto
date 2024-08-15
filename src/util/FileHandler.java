package util;

import app.Logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class FileHandler { //класс для хранения и получения валидных файлов
    private Path fileToLoad;
    private Path fileToUpload;

    public void getFilePaths() {
        do {
            fileToLoad = getValidFilePath(UserFiles.FILE_TO_LOAD);
            fileToUpload = getValidFilePath(UserFiles.FILE_TO_UPLOAD);
            if (fileToLoad.equals(fileToUpload)) {
                System.out.println(Messages.SAME_FILE_ERROR);
            }
        } while (fileToLoad.equals(fileToUpload));
    }

    public Path getValidFilePath(UserFiles value) {
        String userPath;
        BufferedReader reader = Logic.getINSTANCE().getUserInputReader();
        System.out.println(value.getMessage());
        while (true) {
            try {
                userPath = reader.readLine();
                Path filePath = Path.of(userPath);
                if (userPath.isBlank() || !Files.isRegularFile(filePath)) {
                    System.out.println(Messages.INVALID_INPUT);
                } else if (!(Files.size(filePath) > 0) && value.equals(UserFiles.FILE_TO_LOAD)) {
                    System.out.println(Messages.FILE_EMPTY);
                    System.out.println(UserFiles.FILE_TO_LOAD.getMessage());
                } else {
                    break;
                }
            } catch (InvalidPathException | IOException e) {
                System.out.println(Messages.INVALID_INPUT);
            }
        }
        return Path.of(userPath);
    }

    public Path getFileToLoad() {
        return fileToLoad;
    }

    public Path getFileToUpload() {
        return this.fileToUpload;
    }
}
