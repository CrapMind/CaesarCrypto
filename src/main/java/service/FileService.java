package service;

import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;

public class FileService {
    private File fileToLoad; // File selected for encryption/decryption
    private File fileToUpload; // File selected for output

    // Getters and Setters for files
    public File getFileToLoad() {
        return this.fileToLoad;
    }
    public File getFileToUpload() {
        return this.fileToUpload;
    }
    public void setFileToLoad(File fileToLoad) {
        this.fileToLoad = fileToLoad;
    }
    public void setFileToUpload(File fileToUpload) {
        this.fileToUpload = fileToUpload;
    }

    // Validates the selected file based on its extension
    public File getValidFile(File file) {
        if (file != null) {
            if (file.isFile()) {
                // List of valid file extensions
                List<String> suffixes = List.of("txt", "doc", "docx", "csv", "rtf");
                // Check if the file's extension is one of the valid ones
                if (suffixes.stream().anyMatch(file.getName()::endsWith)) {
                    return file;
                }
            }
        }
        return null; // Return null if the file is invalid
    }

    // Checks if the selected input and output files are identical
    public boolean filesAreIdentical() {
        if (fileToLoad == null || fileToUpload == null) {
            return false;
        }
        return fileToLoad.equals(fileToUpload); // True if both files are the same
    }

    // Opens a file chooser dialog to select a file
    File chooseUserFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(null); // Returns the selected file
    }

    // Resets the selected files
    public void resetFiles() {
        this.fileToLoad = null;
        this.fileToUpload = null;
    }

}
