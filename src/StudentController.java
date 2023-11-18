import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public final class StudentController {
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final HashSet<String> userNames = new HashSet<>();

    private StudentController() {}

    public static void readData(){
        File file = new File("./src/Students.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                //Student data
                String[] studentData = scanner.nextLine().split(",");
                Student newStudent = new Student(studentData[0], studentData[1], Integer.parseInt(studentData[2]), studentData[3], studentData[4], studentData[5], Integer.parseInt(studentData[6]));
                userNames.add(studentData[0].toLowerCase());
                //Courses that student registered
                HashMap<Course, Double> studentCourses = new HashMap<>();
                String[] splitCourses = scanner.nextLine().split(",");
                if (!splitCourses[0].isEmpty()) {
                    for (String course : splitCourses) {
                        String courseId = course.split(":")[0];
                        double degree = Double.parseDouble(course.split(":")[1]);
                        Course course1 = CourseController.getCourseById(courseId);
                        studentCourses.put(course1, degree);
                        course1.enrollStudentInCourse(newStudent);
                    }
                }
                newStudent.setEnrolledCourses(studentCourses);
                students.add(newStudent);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
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
        try {
            FileWriter fileWriter = new FileWriter("./src/Students.txt");
            for (Student student : students) {
                StringBuilder studentData = new StringBuilder(String.format(student.getUserName() + "," + student.getPassword() + "," + student.getLevel() + "," + student.getFirstName() + "," + student.getLastName() + "," + student.getPhoneNumber() + "," + student.getAge() + "\n"));
                ArrayList<Course> studentCourses = student.getEnrolledCourses();
                for (Course course : studentCourses)
                    studentData.append(String.format(course.getId() + ":" + student.getDegree(course)+ ","));
                studentData.append("\n");
                fileWriter.write(String.valueOf(studentData));
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}