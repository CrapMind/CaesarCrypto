package util;

public class Cipher { //класс выполняющий перебор символа по алфавиту
    private final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyz,. /\\\"'!@#$+_-=;:?<>&(){}[]*%";

    public char processChar(char symbol, int key, Actions action) { //в зависимости от выбранного действия сдвигает символ

        if (ALPHABET.contains("" + symbol)) { // если алфавит содержит символ - сдвигаем символ в зависимости от действия
            int index = ALPHABET.indexOf(Character.toLowerCase(symbol));
            index = switch (action) {
                case ENCRYPT -> index + key >= ALPHABET.length() ? (index + key) % ALPHABET.length() : index + key;
                case DECIPHER, BRUTEFORCE -> index - key < 0 ? ALPHABET.length() + (index - key) : index - key;
            };
            return ALPHABET.charAt(index);
        }
        return symbol; // если символа нет в алфавите - вовзращаем исходное значение
    }

    public int getAlphabetLength() {
        return ALPHABET.length();
    }
}

