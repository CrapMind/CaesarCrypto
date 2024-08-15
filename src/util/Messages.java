package util;

public enum Messages {      // Класс для текстовых выводов
    HELLO_MESSAGE("Запускаем криптоанализатор..."),
    ENCRYPT_CHOICE("\nВведите вариант: | 1 - Шифр | 2 - Дешифровка | 3 - Дешифровка BruteForce | 4 - Выход |"),
    GENERATING_KEY("Генерируем ключ..."),
    KEY_WAS_GENERATED("Ключ сгенерирован: "),
    SAME_FILE_ERROR("Это один и тот же файл!"),
    KEY_CHOICE("| 1 - Ввод ключа | 2 - Генерация ключа |"),
    ENTER_KEY("Введите ключ от 1 до "),
    UNKNOWN_ERROR("Неизвестная ошибка!"),
    INVALID_INPUT("Неверный ввод"),
    ENTERED_KEY("Введен ключ: "),
    FILE_EMPTY("Файл пуст"),
    CIPHER_CHOICE("Выбран Шифр"),
    DECIPHER_CHOICE("Выбрана Дешифровка"),
    BRUTEFORCE_CHOICE("Выбрана Дешифровка Brute Force"),
    KEY_HAS_BEEN_FOUND("Ключ подобран: "),
    EXIT("Выход");

    final String title;
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
        return this.title + numb;
    }

}
