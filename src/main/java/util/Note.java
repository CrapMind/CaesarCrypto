package util;

public enum Note {
    SELECT_UPLOAD_FILE("Select File to upload"), // Prompt for selecting the file to upload
    SELECT_ACTION_FILE("Select File for action"), // Prompt for selecting the file to perform an action on
    ENTER_KEY("Enter key from 1 to "), // Prompt for entering the encryption/decryption key
    INVALID_KEY("Invalid key. Please enter a valid number."), // Error message for invalid key input
    INVALID_FILE("Please browse a valid files."); // Error message for invalid file selection

    final String title; // Holds the string message associated with each enum constant
    Note(String title) {
        this.title = title;
    }

    public String tag() {
        return this.title;
    } // Returns the message associated with the enum constant
}
