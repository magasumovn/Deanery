package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    /**
     * матод создает окно авторизации в систему
     */
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Enter.fxml"));
        primaryStage.setTitle("Вход");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(744);
        primaryStage.getIcons().add(new Image(this.getClass().getResource("resources/Logo.jpg").toString()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
