package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.objects.Student;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    private TextField studentSurnameField;

    @FXML
    private TextField studentNameField;

    @FXML
    private TextField studentMiddlenameField;

    @FXML
    private ImageView imageView;

    @FXML
    private ComboBox<Student> deletedBox;

    @FXML
    private Button readdBtn;

    private Stage dialogStage;
    private Student student;
    private boolean okClicked = false;
    private static int tempGroup = 0;

    ObservableList<Student> deletedData = FXCollections.observableArrayList();

    @Override
    /**
     * метод, заполняющий Imageview и делащий недоступными кнопки
     */
    public void initialize(URL location, ResourceBundle resources) {
        deletedBox.setDisable(true);
        readdBtn.setDisable(true);
        File file = new File("src/sample/resources/Logo.jpg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }
    //устанавоиваем сцену для этого окна
    public  void setDialogStage (Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * параметризированный конструктор, так же заполняющий combobox информацией об удаленных студентах
     * @param student
     * @param course
     * @param group
     */
    public  void setStudent (Student student, Integer course, Integer group) {
        this.student = student;
        tempGroup = group;
        student.setStudentCourse(course);
        student.setStudentGroup(group);
        studentSurnameField.setText(student.getStudentSurname());
        studentNameField.setText(student.getStudentName());
        studentMiddlenameField.setText(student.getStudentMiddlename());

        String line;
        File file1 = new File("src/sample/resources/DeletedStudents.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file1)))) {
            while ((line = reader.readLine()) != null) {
                Student tempStudent = new Student(line);
                if (tempStudent.getStudentCourse() == course) {
                    deletedData.add(tempStudent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deletedData.size() != 0) {
            deletedBox.setItems(deletedData);
            deletedBox.setDisable(false);
        }
        deletedBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            readdBtn.setDisable(false);
        });
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

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeight(300);
            alert.setWidth(400);
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
                student.setStudentSurname(studentSurnameField.getText());
                student.setStudentName(studentNameField.getText());
                student.setStudentMiddlename(studentMiddlenameField.getText());

                okClicked = true;
                dialogStage.close();
        }
    }

    /**
     * метод, восстанавливающий удаленного студента
     * @throws IOException
     */
    @FXML
    private void handleReadd() throws IOException {
        student.setStudentGroup(deletedBox.getValue().getStudentGroup());
        student.setStudentSurname(deletedBox.getValue().getStudentSurname());
        student.setStudentName(deletedBox.getValue().getStudentName());
        student.setStudentMiddlename(deletedBox.getValue().getStudentMiddlename());
        student.setStudentSession1(deletedBox.getValue().getStudentSession1());
        student.setStudentSession2(deletedBox.getValue().getStudentSession2());

        ArrayList<Student> students = new ArrayList();
        String line;
        File file = new File("src/sample/resources/DeletedStudents.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                Student tempStudent = new Student(line);
                students.add(tempStudent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder data = new StringBuilder();
            for (Student s : students) {
                if (!s.equals(student)) {
                    data.append(s.toString() + "\n");
                }
            }
            writer.write(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        student.setStudentGroup(tempGroup);
        okClicked = true;
        dialogStage.close();
    }

    /**
     *  метод закрытия диалогового окна
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}