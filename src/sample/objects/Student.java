package sample.objects;


import java.io.*;
import java.util.*;

/**
 * класс, обекты которого содержат информацию о студентах
 */
public class Student implements Cloneable {

    private Integer studentCourse;
    private Integer studentGroup;
    private String studentSurname;
    private String studentName;
    private String studentMiddlename;
    private Integer studentSession1;
    private Integer studentSession2;

    public Student() {
        studentCourse = 0;
        studentGroup = 0;
        studentSurname = "";
        studentName = "";
        studentMiddlename = "";
        studentSession1 = 0;
        studentSession2 = 0;
    }

    public final static int limit = 5;

    public Integer getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(Integer studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Integer getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(Integer studentGroup) {
        this.studentGroup = studentGroup;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentMiddlename() {
        return studentMiddlename;
    }

    public void setStudentMiddlename(String studentMiddlename) {
        this.studentMiddlename = studentMiddlename;
    }

    public Integer getStudentSession1() {
        return studentSession1;
    }

    public void setStudentSession1(Integer studentSession1) {
        this.studentSession1 = studentSession1;
    }

    public Integer getStudentSession2() {
        return studentSession2;
    }

    public void setStudentSession2(Integer studentSession2) {
        this.studentSession2 = studentSession2;
    }

    public Student(String line) {
        String[] data = line.split(" ");
        this.studentCourse = Integer.valueOf(data[0]);
        this.studentGroup = Integer.valueOf(data[1]);
        this.studentSurname = data[2];
        this.studentName = data[3];
        this.studentMiddlename = data[4];
        this.studentSession1 = Integer.valueOf(data[5]);
        this.studentSession2 = Integer.valueOf(data[6]);
    }

    /**
     * Строковое представление обьекта Student
     * @return
     */
    @Override
    public String toString() {
        return studentCourse + " " + studentGroup + " " + studentSurname + " " + studentName + " " + studentMiddlename + " " +
                studentSession1 + " " + studentSession2;
    }

    /**
     * метод для записи информации о новом студенте в файл
     * @param student
     */
    public static void writeAdd(Student student){
        File file = new File("src/sample/resources/Students.txt");
        try (FileWriter writer = new FileWriter(file, true)) {
            StringBuilder data = new StringBuilder();
                data.append(student.toString() + "\n");
            writer.write(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод сравнения двух обектов
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentCourse, student.studentCourse) &&
                Objects.equals(studentGroup, student.studentGroup) &&
                Objects.equals(studentSurname, student.studentSurname) &&
                Objects.equals(studentName, student.studentName) &&
                Objects.equals(studentMiddlename, student.studentMiddlename) &&
                Objects.equals(studentSession1, student.studentSession1) &&
                Objects.equals(studentSession2, student.studentSession2);
    }

    /**
     * метод клонирования объекта
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Student clone() throws CloneNotSupportedException {
        return (Student) super.clone();
    }

    /**
     * метод удаления информации о студенте из файла
     * @param student
     */
    public static void writeDelete(Student student) {
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
                if (!s.equals(student)) {
                    data.append(s.toString() + "\n");
                }
            }
            writer.write(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод изменения информации об объекте в файле
     * @param selectedStudent
     * @param firstStudent
     */
    public static void writeEdit(Student selectedStudent, Student firstStudent) {
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
                if (s.equals(firstStudent)) {
                    data.append(selectedStudent.toString() + "\n");
                }
                else {
                    data.append(s.toString() + "\n");
                }
            }
            writer.write(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
