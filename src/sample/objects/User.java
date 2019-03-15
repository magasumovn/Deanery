package sample.objects;

import java.io.*;

/**
 * класс, объекты которого содержат информацию о логинах и паролях пользователей системы
 */
public class User {
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public boolean checkData() {
        String line;
        File file = new File("src/sample/resources/Users.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                if (login.equals(data[0])) {
                    return password.equals(data[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
