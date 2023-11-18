import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class StudentController {
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final HashSet<String> userNames = new HashSet<>();

    private StudentController() {}

    public static void readData(){
        try {
            ResultSet resultSet = DatabaseHelper.connection.createStatement().executeQuery("SELECT * FROM Students");
            while (resultSet.next()) {
                Student newStudent = new Student();
                newStudent.setUserName(resultSet.getString("username"));
                newStudent.setPassword(resultSet.getString("password"));
                newStudent.setLevel(resultSet.getInt("level"));
                newStudent.setFirstName(resultSet.getString("first_name"));
                newStudent.setLastName(resultSet.getString("last_name"));
                newStudent.setPhoneNumber(resultSet.getString("phone_number"));
                newStudent.setAge(resultSet.getInt("age"));

                userNames.add(newStudent.getUserName());

                HashMap<Course, Double> studentCourses = new HashMap<>();
                ResultSet courses = DatabaseHelper.connection.createStatement().executeQuery("SELECT * FROM Student_Courses WHERE username = '" + newStudent.getUserName() + "'");
                while (courses.next()) {
                    String courseId = courses.getString("course_id");
                    double degree = courses.getDouble("grade");
                    Course course = CourseController.getCourseById(courseId);
                    studentCourses.put(course, degree);
                    course.enrollStudentInCourse(newStudent);
                }
                newStudent.setEnrolledCourses(studentCourses);
                students.add(newStudent);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static ArrayList<Student> getStudents() {
        return students;
    }
    public static HashSet<String> getUserNames(){
        return userNames;
    }

    public static Student login(String userName, String password) {
        for (Student student : students) {
            if (student.getUserName().equalsIgnoreCase(userName) && student.getPassword().equals(password))
                return student;
        }
        return null;
    }

    public static void addStudent() {
        String firstName, lastName, phoneNumber, userName, password;
        int age, level;
        Scanner scanner = new Scanner(System.in);
        //read student data
        System.out.printf("To add new student : %nEnter username or -1 to cancel : ");
        userName = scanner.nextLine();
        while (!userName.equals("-1") && userNames.contains(userName.toLowerCase())) {
            System.out.print("Sorry this username is already taken! try again or -1 to cancel : ");
            userName = scanner.nextLine();
        }
        if (userName.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            System.out.print("Enter password : ");
            password = scanner.nextLine();

            System.out.print("Enter student level : ");
            level = Main.getValidInteger();
            while (level <= 0 || level > 4) {
                System.out.print("student level must be in range [1 , 4] please try again : ");
                level = Main.getValidInteger();
            }
            scanner = new Scanner(System.in);
            System.out.print("Enter first name: ");
            firstName = scanner.next();

            System.out.print("Enter last name : ");
            lastName = scanner.next();

            System.out.print("Enter phone number : ");
            phoneNumber = scanner.next();

            System.out.print("Enter age : ");
            age = Main.getValidInteger();
            while (age <= 0) {
                System.out.print("age must be > 0 enter valid age : ");
                age = Main.getValidInteger();
            }

            Student newStudent = new Student(userName, password, level, firstName, lastName, phoneNumber, age);
            userNames.add(userName.toLowerCase());
            students.add(newStudent);
            System.out.printf("Student added!%n");
        }
    }

    public static void deleteStudent(Student student) {
        if (student != null) {
            student.withdrawFromAllCourses();
            userNames.remove(student.getUserName());
            students.remove(student);
            System.out.printf("Student removed!%n");
        }
    }

    public static Student selectStudent(ArrayList<Student> students){
        printStudentsDetails(students);
        Student student = null;
        if (students!= null && !students.isEmpty()) {
            System.out.println("---------------------------------------------------");
            System.out.print("choose student that you want or -1 to cancel : ");
            int index = Main.getValidInteger();
            while (index != -1 && index <= 0 || index > students.size()) {
                System.out.print("please enter valid index or -1 to cancel : ");
                index = Main.getValidInteger();
            }
            if (index == -1)
                System.out.printf("task canceled!%n");
            else {
                student = students.get(index - 1);
                System.out.println("---------------------------------------------------");
            }
        }
        return student;
    }

    public static void printStudentsDetails(ArrayList<Student> students) {
        if (students != null && !students.isEmpty()) {
            students.sort(Comparator.comparingInt(Student::getLevel));
            System.out.printf("%-10s%-20s%-20s%-20s%-20s%-8s%-20s%-20s%n", "Index", "Student username", "Student Name", "Student Level", "Phone Number", "Age", "Number of Courses", "Student Percentage");
            int i = 1;
            for (Student student : students)
                System.out.printf("%-10d%-20s%-20s%-20d%-20s%-8d%-20d%-2.2f%n", i++, student.getUserName(), student.getFirstName() + " " + student.getLastName(), student.getLevel(), student.getPhoneNumber(), student.getAge(), student.getEnrolledCourses().size(), student.calculatePercentage());
        }
        else
            System.out.printf("There is no any student!%n");
    }

    public static void storeDate() {
        try{
            //Delete all the students that are currently stored in the database.
            DatabaseHelper.connection.createStatement().executeUpdate("DELETE FROM Student_Courses");
            DatabaseHelper.connection.createStatement().executeUpdate("DELETE FROM Students");
            //Insert new data
            PreparedStatement studentInsert = DatabaseHelper.connection.prepareStatement("INSERT INTO Students values (? , ?, ?, ?, ?, ?, ?)");
            for(Student student : students){
                studentInsert.setString(1, student.getUserName());
                studentInsert.setString(2, student.getPassword());
                studentInsert.setInt(3, student.getLevel());
                studentInsert.setString(4, student.getFirstName());
                studentInsert.setString(5, student.getLastName());
                studentInsert.setString(6, student.getPhoneNumber());
                studentInsert.setInt(7, student.getAge());

                studentInsert.executeUpdate();
                ArrayList<Course> studentCourses = student.getEnrolledCourses();
                PreparedStatement studentCoursesInsert = DatabaseHelper.connection.prepareStatement("INSERT INTO Student_Courses values (?, ?, ?)");
                for(Course course : studentCourses){
                    studentCoursesInsert.setString(1, student.getUserName());
                    studentCoursesInsert.setString(2, course.getId());
                    studentCoursesInsert.setDouble(3, student.getDegree(course));
                    studentCoursesInsert.executeUpdate();
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}