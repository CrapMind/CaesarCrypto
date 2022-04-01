import java.io.IOException;

public class Runner {

       public static void main(String[] args) throws IOException {
           System.out.println(Messages.HELLO_MESSAGE);      // приветствие
           UI logic = Logic.getInstance();                  // включаем логику
           while (logic.getUserChoice() != 4) {             // пока не будет выбран выход, программа находится в цикле
               System.out.println(Messages.ENCRYPT_CHOICE);
               System.out.println(logic.userChoice());      // получаем выбор пользователя
               if (logic.getUserChoice() == 4) {            // 4 - выход из программы
                   break;
               }
               System.out.println(Messages.ENTER_FILE_TO_LOAD);
               String fileToLoad = logic.fileName();        // создаем путь к файлу над которым работать
               System.out.println(Messages.ENTER_FILE_TO_UPLOAD);
               logic.setFileToUpload(true);                 // меняем флаг, чтобы работать с файлом для выгрузки
               String fileToUpload = logic.fileName();      // создаем путь к файлу для выгрузки
               while (fileToLoad.equals(fileToUpload)) {    // проверка, что мы не ссылаемся на один и тот же файл
                   System.out.println(Messages.SAME_FILE_ERROR.plus(Messages.ENTER_FILE_TO_UPLOAD));
                   fileToUpload = logic.fileName();
               }
               logic.setFileToUpload(false);
               switch (logic.getUserChoice()) {                 // получаем выбор пользователя
                   case 1 -> {                                   //шифр
                       int key = logic.generateKey();
                       System.out.println(Messages.ENCRYPT_MESSAGE);
                       logic.action(fileToLoad, fileToUpload, "encrypt", key);
                       System.out.println(Messages.ENCRYPT_IS_DONE);
                   }
                   case 2 -> {                                  // расшифровка по ключу
                       int key = logic.getKey();
                       if (key == 0) {
                           System.out.println(Messages.KEY_IS_NULL); // если выбор был сделан впервые ключ будет равен 0 и программа завершится
                           return;
                       }
                       System.out.println(Messages.DECIPHER_MESSAGE);
                       logic.action(fileToLoad, fileToUpload, "decipher", key);
                       System.out.println(Messages.DECIPHER_IS_DONE);
                   }
                   case 3 -> {                                  // расшифровка brute force
                       System.out.println(Messages.DECIPHER_MESSAGE);
                       logic.bruteForce(fileToLoad, fileToUpload);
                       System.out.println(Messages.DECIPHER_IS_DONE);
                   }
               }
           }
       }
}
