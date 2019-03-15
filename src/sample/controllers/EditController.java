package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.objects.Student;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditController implements Initializable {

    @FXML
    private TextField studentSurnameField;

    @FXML
    private TextField studentNameField;

    @FXML
    private TextField studentMiddlenameField;

    @FXML
    private TextField studentSession1Field;

    @FXML
    private TextField studentSession2Field;

    @FXML
    private ImageView imageView;

    private Stage dialogStage;
    private Student student;
    private boolean okClicked = false;

    /**
     * метод, заполняющий Imageview и делащий недоступными кнопки
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/sample/resources/Logo.jpg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }
    //устанавоиваем сцену для этого окна
    public  void setDialogStage (Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public  void setStudent (Student student, Integer course, Integer group) {
        this.student = student;
        student.setStudentCourse(course);
        student.setStudentGroup(group);
        studentSurnameField.setText(student.getStudentSurname());
        studentNameField.setText(student.getStudentName());
        studentMiddlenameField.setText(student.getStudentMiddlename());
        studentSession1Field.setText(student.getStudentSession1().toString());
        studentSession2Field.setText(student.getStudentSession2().toString());
    }

    /**
     * метод, проверяющий нажатие кнопки подтверждения
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * метод, проверяющий корректность введенных данных о студенте
     * @return
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (studentSurnameField.getText() == null ||
                !(studentSurnameField.getText().matches("[А-Я]{1}[а-я]*")) ||
                studentSurnameField.getText().length() == 0) {
            errorMessage += "Фамилия должна состоять только из первой заглавной и строчных букв русского алфавита\n";
        }
        if (studentNameField.getText() == null ||
                !(studentNameField.getText().matches("[А-Я]{1}[а-я]*")) ||
                studentNameField.getText().length() == 0) {
            errorMessage += "Имя должно состоять только из первой заглавной и строчных букв русского алфавита\n";
        }
        if (studentMiddlenameField.getText() == null ||
                !(studentMiddlenameField.getText().matches("[А-Я]{1}[а-я]*")) ||
                studentMiddlenameField.getText().length() == 0) {
            errorMessage += "Отчество должно состоять только из первой заглавной и строчных букв русского алфавита\n";
        }
        if (studentSession1Field.getText() == null ||
                !(studentSession1Field.getText().matches("[0-5]")) ||
                studentSession1Field.getText().length() == 0) {
            errorMessage += "Оценка должна иметь только целые значения от 0 до 5\n";
        }
        if (studentSession2Field.getText() == null ||
                !(studentSession2Field.getText().matches("[0-5]")) ||
                studentSession2Field.getText().length() == 0) {
            errorMessage += "Оценка должна иметь только целые значения от 0 до 5\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeight(350);
            alert.setWidth(450);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Введите корректную информацию");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(String.valueOf(getClass().getResource("../resources/Logo.jpg"))));
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    /**
     * метод, сохраняющий новую информацию о студенте
     * @throws IOException
     */
    @FXML
    private void handleOk() throws IOException {
        if (isInputValid()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Изменение информации");
            alert.setHeaderText("Вы уверены, что хотите принять изменения?");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(String.valueOf(getClass().getResource("../resources/Logo.jpg"))));
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {
                student.setStudentSurname(studentSurnameField.getText());
                student.setStudentName(studentNameField.getText());
                student.setStudentMiddlename(studentMiddlenameField.getText());
                student.setStudentSession1(Integer.valueOf(studentSession1Field.getText()));
                student.setStudentSession2(Integer.valueOf(studentSession2Field.getText()));
                okClicked = true;
                dialogStage.close();
            }
            else {
                dialogStage.close();
            }
        }
    }

    /**
     *  метод закрытия диалогового окна
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
