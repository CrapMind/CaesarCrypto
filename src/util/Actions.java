package util;

import java.util.Objects;

public enum Actions { // енам хранящий значения действий и не большие описания к ним

    ENCRYPT ("Шифруем файл...", "Файл зашифрован!"),
    DECIPHER("Дешифруем файл...", "Файл дешифрован!"),
    BRUTEFORCE;

    String readyMessage;
    String doneMessage;

    Actions(String readyMessage, String doneMessage) {
        this.readyMessage = readyMessage;
        this.doneMessage = doneMessage;
    }
    Actions(){}

    public String getDoneMsg() {
        return "\r" + Objects.requireNonNullElse(this.doneMessage, "");
    }

    public String getReadyMsg() {
        return "\r" + Objects.requireNonNullElse(readyMessage, "");
    }
}
