public enum Messages {      // Класс для текстовых выводов
    HELLO_MESSAGE("Запускаем криптоанализатор..."),
    ENCRYPT_CHOICE("\nВведите вариант: | 1 - Шифр | 2 - Дешифровка | 3 - Дешифровка BruteForce | 4 - Выход |"),
    GENERATE_KEY("Генерируем ключ..."),
    KEY_WAS_GENERATED("Ключ сгенерирован: "),
    ENTER_FILE_TO_LOAD ("Введите абсолютный путь к файлу с исходным текстом:"),
    ENTER_FILE_TO_UPLOAD("Введите абсолютный путь к файлу, в который хотите выгрузить зашифрованный текст:"),
    SAME_FILE_ERROR("Это файл с исходным текстом! "),
    ENCRYPT_MESSAGE("Шифруем файл..."),
    ENCRYPT_IS_DONE("Файл зашифрован!"),
    DECIPHER_MESSAGE("Дешифруем файл..."),
    DECIPHER_IS_DONE("Файл дешифрован!"),
    KEY_IS_NULL("Ключ равен 0, расшифровки не будет"),
    KEY_CHOICE("| 1 - Ввод ключа | 2 - Генерация ключа |"),
    ENTER_KEY("Введите ключ от 1 до "),
    UNKNOWN_ERROR("Неизвестная ошибка!"),
    ENTERED_KEY("Введен ключ: ");

    String title;
    Messages(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

    public String plus (Messages message) {
        return this.title + message.title;
    }
    public String plus (int numb) {
        return this.title + "" + numb;
    }

}
