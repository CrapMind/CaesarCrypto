import java.io.IOException;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public interface UI {
    String userChoice();        // метод возвращающий выбор пользователя
    int generateKey();          // генерация или ручной ввод ключа
    void bruteForce(String fileToLoad, String fileToUpLoad) throws IOException;
    int getUserChoice();        // метод возвращающий сделанный пользователем выбор
    int getKey();               // метод, возвращающий полученное значение ключа
    boolean setFileToUpload(boolean value); // флаг, дающий понять будет ли это файл для выгрузки текста
    String fileName();          // метод создающий валидное имя файла
    void action(String fileToLoad, String fileToUpload, String encryptOrDecipher, int key); //шифр и расшифровка
}
