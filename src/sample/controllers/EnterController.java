package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.objects.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class EnterController implements Initializable {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView imageView;

    /**
     * метод заполняет ImageView при открытии данной сцены
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/sample/resources/Logo.jpg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    /**
     * метод проверки корректности логина и пароля, вход в систему
     * @param actionEvent
     * @throws IOException
     */
    public void enter(ActionEvent actionEvent) throws IOException {
        User user = new User(loginField.getText(), passwordField.getText());
        boolean result = user.checkData();
        if (result) {
            Stage stage = ((Stage)((Node) actionEvent.getSource()).getScene().getWindow());
            stage.setTitle("Главное меню");
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/Base.fxml"));
            stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка авторизации");
            alert.setContentText("Неверный логин или пароль");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(String.valueOf(getClass().getResource("../resources/Logo.jpg"))));
            alert.showAndWait();
            loginField.clear();
            passwordField.clear();
        }
    }
}
