package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.objects.Student;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class BaseController implements Initializable {

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> studentSurname;

    @FXML
    private TableColumn<Student, String> studentName;

    @FXML
    private TableColumn<Student, String> studentMiddlename;

    @FXML
    private TableColumn<Student, Integer> studentSession1;

    @FXML
    private TableColumn<Student, Integer> studentSession2;

    @FXML
    private ComboBox<Integer> courseBox;

    @FXML
    private ComboBox<Integer> groupBox;

    @FXML
    private Button deleteStudentBtn;

    @FXML
    private Button editStudentBtn;

    @FXML
    private Button moveStudentBtn;

    @FXML
    private Button addStudentBtn;

    @FXML
    private ImageView imageView;

    ObservableList<Integer> courseList = FXCollections.observableArrayList();
    ObservableList<Integer> groupList = FXCollections.observableArrayList();
    ObservableList<Student> studentData = FXCollections.observableArrayList();

    /**
     * метод, инициализирующий combobox'ы и таблицу
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/sample/resources/Logo.jpg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        studentTable.setVisible(false);
        addStudentBtn.setDisable(true);
        groupBox.setDisable(true);
        courseList.add(1);
        courseList.add(2);
        courseList.add(3);
        courseList.add(4);
        courseBox.setItems(courseList);
        courseBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            groupBox.setDisable(false);
            studentTable.setVisible(false);
            deleteStudentBtn.setDisable(true);
            editStudentBtn.setDisable(true);
            moveStudentBtn.setDisable(true);
            addStudentBtn.setDisable(true);
            groupList.clear();
            if (newValue != null) {
                initGroups(newValue);
                groupBox.setItems(groupList);
            }
        });
        groupBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                studentData.clear();
                studentTable.setVisible(true);
                setStudentTable(newValue);
                if(studentData.size() >= Student.limit) {
                    addStudentBtn.setDisable(true);
                }
                else {
                    addStudentBtn.setDisable(false);
                }
                studentSurname.setCellValueFactory(new PropertyValueFactory<>("studentSurname"));
                studentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
                studentMiddlename.setCellValueFactory(new PropertyValueFactory<>("studentMiddlename"));
                studentSession1.setCellValueFactory(new PropertyValueFactory<>("studentSession1"));
                studentSession2.setCellValueFactory(new PropertyValueFactory<>("studentSession2"));
                studentTable.setItems(studentData);
            }
        });
        deleteStudentBtn.setDisable(false);
        editStudentBtn.setDisable(false);
        moveStudentBtn.setDisable(false);
        chooseStudent(null);

        studentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> chooseStudent(newValue));
    }

    /**
     * метод, заполняющий таблицу данными о студентах выбранных курса и группы
     * @param newValue
     */
    public void setStudentTable(Integer newValue) {

        String line;
        File file = new File("src/sample/resources/Students.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                Student tempStudent = new Student(line);
                if (tempStudent.getStudentGroup().equals(newValue)) {
                    studentData.add(new Student(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод, удаляющий информации о выбранном студенте из таблицы
     * @throws IOException
     */
    @FXML
    private void handleDeleteStudent() throws IOException {
        int selectedIndex = studentTable.getSelectionModel().getSelectedIndex();
        deleteStudentBtn.setDisable(false);
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Отчисление студента");
        alert.setHeaderText("Вы уверены, что хотите отчислить выбранного студента?");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(String.valueOf(getClass().getResource("../resources/Logo.jpg"))));
        alert.setContentText(selectedStudent.getStudentSurname() + " " + selectedStudent.getStudentName() + " " + selectedStudent.getStudentMiddlename() );
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            studentTable.getItems().remove(selectedIndex);
            Student.writeDelete(selectedStudent);

            File file = new File("src/sample/resources/DeletedStudents.txt");
            try (FileWriter writer = new FileWriter(file, true)) {
                StringBuilder data = new StringBuilder();
                data.append(selectedStudent.toString() + "\n");
                writer.write(data.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(studentData.size() >= Student.limit) {
                addStudentBtn.setDisable(true);
            }
            else {
                addStudentBtn.setDisable(false);
            }
        }
    }

    /**
     * метод, проверяющий выбран ли студент
     * @param student
     */
    public void chooseStudent (Student student) {
        if (student != null) {
            deleteStudentBtn.setDisable(false);
            editStudentBtn.setDisable(false);
            moveStudentBtn.setDisable(false);
        }
        else {
            deleteStudentBtn.setDisable(true);
            editStudentBtn.setDisable(true);
            moveStudentBtn.setDisable(true);
        }
    }

    /**
     * метод, вызывающий диалоговое окно для изменения информации о выбранном студенте в таблице
     * @param student
     * @param course
     * @param group
     * @return
     * @throws CloneNotSupportedException
     */
    public boolean showStudentEditDialog(Student student, Integer course, Integer group) throws CloneNotSupportedException {
        try {
            // Загружаем FXML-файл и создаем новую сцену для всплывающего диалогового окна
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EditController.class.getResource("../fxml/Edit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаем диалоговое окно Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Изменение информации о студенте");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setWidth(1000);
            dialogStage.setHeight(744);
            dialogStage.setMinWidth(1000);
            dialogStage.setMinHeight(744);
            dialogStage.getIcons().add(new Image(this.getClass().getResource("../resources/Logo.jpg").toString()));
            dialogStage.setScene(scene);

            // Передаем адресата в контроллер
            EditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStudent(student, course, group);

            // Отображаем диалоговое окно ждем, пока пользоваьель его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * метод, вызывающий диалоговое окно для добавлении информации о новом студенте в таблицу
     * @param student
     * @param course
     * @param group
     * @return
     */
    public boolean showStudentAddDialog(Student student, Integer course, Integer group) {
        try {
            // Загружаем FXML-файл и создаем новую сцену для всплывающего диалогового окна
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EditController.class.getResource("../fxml/Add.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаем диалоговое окно Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Зачисление студента");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setWidth(1000);
            dialogStage.setHeight(744);
            dialogStage.setMinWidth(1000);
            dialogStage.setMinHeight(744);
            dialogStage.getIcons().add(new Image(this.getClass().getResource("../resources/Logo.jpg").toString()));
            dialogStage.setScene(scene);

            // Передаем адресата в контроллер
            AddController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStudent(student, course, group);

            // Отображаем диалоговое окно ждем, пока пользоваьель его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * метод, вызывающий диалоговое окно для перевода студента в другую группу
     * @param student
     * @param course
     * @return
     */
    public boolean showStudentMoveDialog(Student student, Integer course) throws CloneNotSupportedException {
        try {
            // Загружаем FXML-файл и создаем новую сцену для всплывающего диалогового окна
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EditController.class.getResource("../fxml/Move.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаем диалоговое окно Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Смена группы");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setWidth(1000);
            dialogStage.setHeight(744);
            dialogStage.setMinWidth(1000);
            dialogStage.setMinHeight(744);
            dialogStage.getIcons().add(new Image(this.getClass().getResource("../resources/Logo.jpg").toString()));
            dialogStage.setScene(scene);

            // Передаем адресата в контроллер
            MoveController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.moveStudent(student, course);

            // Отображаем диалоговое окно ждем, пока пользоваьель его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    /**
     * метод, добовляющий нового студента в таблицу
     */
    private void handleNewStudent() {
        Student tempStudent = new Student();

        boolean okClicked = this.showStudentAddDialog(tempStudent, courseBox.getValue(), groupBox.getValue());
        if (okClicked) {
            studentData.add(tempStudent);
            Student.writeAdd(tempStudent);
            if (studentData.size() >= Student.limit) {
                addStudentBtn.setDisable(true);
            }
            else {
                addStudentBtn.setDisable(false);
            }
        }
    }

    /**
     * метод, изменяющий информацию о выбранном студенте
     */
    @FXML
    private void handleEditStudent() {
        try {
            Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            Student firstStudent = selectedStudent.clone();
            if (selectedStudent != null) {
                boolean okClicked = showStudentEditDialog(selectedStudent, courseBox.getValue(), groupBox.getValue());
                if (okClicked) {
                    int selectedIndex = studentTable.getSelectionModel().getSelectedIndex();
                    studentData.set(selectedIndex, selectedStudent);
                    Student.writeEdit(selectedStudent, firstStudent);
                }
            }
        }
        catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * метод, переводящий всех студентов на следующий курс
     * @param actionEvent
     */
    public void handleMoveStudent(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Смена курса");
        alert.setHeaderText("Вы уверены, что хотите перевести студентов на следующий курс?");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(String.valueOf(getClass().getResource("../resources/Logo.jpg"))));
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            studentData.clear();
            ArrayList<Student> students = new ArrayList();
            String line;
            File file = new File("src/sample/resources/Students.txt");
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
                    if (s.getStudentCourse() != 4) {
                        s.setStudentCourse(s.getStudentCourse() + 1);
                        s.setStudentGroup(s.getStudentGroup() + 10);
                        data.append(s.toString() + "\n");
                    }
                }
                writer.write(data.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            groupBox.getSelectionModel().clearSelection();
            courseBox.getSelectionModel().clearSelection();

        }

    }

    /**
     * метод, переводящий выбранного студента в другую группу
     * @param actionEvent
     */
    public void moveStudentToGroup(ActionEvent actionEvent) throws CloneNotSupportedException {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            boolean okClicked = showStudentMoveDialog(selectedStudent, courseBox.getValue());
            if (okClicked) {
                int selectedIndex = studentTable.getSelectionModel().getSelectedIndex();
                studentData.set(selectedIndex, selectedStudent);
                studentTable.getItems().remove(selectedIndex);
                if (studentData.size() >= Student.limit) {
                    addStudentBtn.setDisable(true);
                }
                else {
                    addStudentBtn.setDisable(false);
                }
            }
        }
    }

    /**
     * метод, инициализирующий информацию о количестве групп каждого курса
     * @param course
     */
    private void initGroups(int course) {
        String line;
        File file = new File("src/sample/resources/Groups.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                if (Integer.parseInt(data[0]) == course) {
                    for (int i = 1; i < data.length; i++) {
                        groupList.add(Integer.parseInt(data[i]));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
