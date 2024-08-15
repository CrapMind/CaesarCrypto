package util;

public enum UserFiles { // храним значения выбранных файлов с описанием для действия
    FILE_TO_LOAD("Введите путь к файлу с текстом для обработки:"),
    FILE_TO_UPLOAD("Введите путь к файлу, в который хотите выгрузить полученный текст:");

    final String message;

    UserFiles(String message){
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}
