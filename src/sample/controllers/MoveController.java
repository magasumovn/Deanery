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
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class MoveController implements Initializable {

    @FXML
    private ComboBox<Integer> groupBox;

    @FXML
    private Label warning;

    @FXML
    private Button okBtn;

    @FXML
    private ImageView imageView;

    private Stage dialogStage;
    private boolean okClicked = false;
    HashMap<Integer, Integer>[] hashMaps = new HashMap[4];
    ArrayList<Student> students = new ArrayList();
    Student student1 = new Student();

    /**
     * метод, инициализирующий информацию о студенческих группах
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file1 = new File("src/sample/resources/Logo.jpg");
        Image image = new Image(file1.toURI().toString());
        imageView.setImage(image);
        okBtn.setDisable(true);
        hashMaps[0] = new HashMap<>();
        hashMaps[1] = new HashMap<>();
        hashMaps[2] = new HashMap<>();
        hashMaps[3] = new HashMap<>();
        String line;
        File file = new File("src/sample/resources/Groups.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                for (int i = 1; i < data.length; i++) {
                    hashMaps[Integer.parseInt(data[0])-1].put(Integer.parseInt(data[i]), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //устанавоиваем сцену для этого окна
    public  void setDialogStage (Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * метод, переводящий студента в выбранную группу
     * @param student
     * @param course
     */
    public  void moveStudent (Student student, Integer course) throws CloneNotSupportedException {
        ObservableList<Integer> groupList = FXCollections.observableArrayList();
        student1 = student.clone();
        String line;
        File file = new File("src/sample/resources/Students.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                Student tempStudent = new Student(line);
                if (tempStudent.getStudentCourse() == course) {
                    int current = hashMaps[course - 1].get(tempStudent.getStudentGroup());
                    hashMaps[course - 1].put(tempStudent.getStudentGroup(), ++current);
                }
                students.add(tempStudent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Integer key : hashMaps[course-1].keySet()) {
            if (hashMaps[course-1].get(key) < Student.limit) {
                groupList.add(key);
            }
        }
        groupList.remove(student.getStudentGroup());
        if (groupList.size() != 0) {
            groupBox.setItems(groupList);
            groupBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                okBtn.setDisable(false);
            });
        }
        else {
            warning.setText("Отсутствуют свободные группы");
            groupBox.setDisable(true);
        }
    }

    /**
     * метод, проверяющий нажатие кнопки подтверждения
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * метод, сохраняющий новую информацию о студента
     * @throws IOException
     */
    @FXML
    private void handleOk() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Перевод");
        alert.setHeaderText("Вы уверены, что хотите перевести студента в выбранную группу?");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(String.valueOf(getClass().getResource("../resources/Logo.jpg"))));
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
                File file = new File("src/sample/resources/Students.txt");
                try (FileWriter writer = new FileWriter(file)) {
                    StringBuilder data = new StringBuilder();
                    for (Student s : students) {
                        if (s.equals(student1)) {
                            s.setStudentGroup(groupBox.getValue());
                        }
                        data.append(s.toString() + "\n");
                    }
                    writer.write(data.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            okClicked = true;
            dialogStage.close();
        }
        else {
            okClicked = false;
            dialogStage.close();
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