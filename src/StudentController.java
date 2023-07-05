import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class StudentController {
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final HashSet<String> userNames = new HashSet<>();

    public StudentController() {
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
                if (!splitCourses[0].equals("")) {
                    for (String course : splitCourses) {
                        String courseId = course.split(":")[0];
                        double degree = Double.parseDouble(course.split(":")[1]);
                        Course course1 = CourseController.getCourseById(courseId);
                        studentCourses.put(course1, degree);
                        course1.enrollStudentToCourse(newStudent);
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

    public Student login(String userName, String password) {
        for (Student student : students) {
            if (student.getUserName().equalsIgnoreCase(userName) && student.getPassword().equals(password))
                return student;
        }
        return null;
    }

    public void addStudent() {
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

            System.out.print("Enter student level or -1 to cancel : ");
            level = Main.getValidInteger();
            while (level != -1 && (level <= 0 || level > 4)) {
                System.out.print("course level must be in range [1 , 4] please try again or -1 to cancel : ");
                level = Main.getValidInteger();
            }
            if (level == -1)
                System.out.printf("task canceled%n");
            else {
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
                //add Courses for the new student ?
                System.out.print("do you want to enroll this student in a course ? y or n : ");
                char response = Main.validResponse();
                System.out.println("---------------------------------------------------");
                if (response == 'y') {
                    HashMap<Course, Double> studentCourses = enrollInCourse(newStudent, true);
                    newStudent.setEnrolledCourses(studentCourses);
                }
                students.add(newStudent);
                System.out.printf("Student added!%n");
            }
        }
        System.out.println("---------------------------------------------------");
        Main.adminManageStudents();
    }

    public HashMap<Course, Double> enrollInCourse(Student student, boolean admin) {
        HashMap<Course, Double> studentCourses = student.getEnrolledCourses();
        char response = 'y';
        ArrayList<Course> availableCourses = getAvailableCourses(student);
        while (response == 'y') {
            // I want to display all courses with their ids , choose index , input student degree
            if (availableCourses != null) {
                System.out.printf("All available courses are : %n");
                Course selectedCourse = CourseController.selectCourse(availableCourses);

                if (selectedCourse == null)
                    response = 'n';
                else {
                    double degree = 0;
                    if (admin) {
                        System.out.print("please enter student degree in this course or -1 to cancel : ");
                        degree = Main.getValidDouble();
                        while (degree != -1 && (degree < selectedCourse.getMinDegree() || degree > selectedCourse.getMaxDegree())) {
                            System.out.print("sorry, student degree must be in range [min degree, max degree] enter valid degree or -1 to cancel : ");
                            degree = Main.getValidDouble();
                        }
                    }
                    if (degree == -1) {
                        System.out.printf("task canceled!%n");
                        response = 'n';
                    }
                    else {
                        studentCourses.put(selectedCourse, degree);
                        selectedCourse.enrollStudentToCourse(student);
                        System.out.printf("Student enrolled!%n");
                        availableCourses.remove(selectedCourse);
                        if (availableCourses.size() > 0) {
                            System.out.print("do you want to enroll in another course ? y or n : ");
                            response = Main.validResponse();
                        } else
                            response = 'n';
                    }
                }
            }
            else
                response = 'n';
            System.out.println("---------------------------------------------------");
        }
        return studentCourses;
    }

    public ArrayList<Course> getAvailableCourses(Student student) {
        ArrayList<Course> availableCourses = new ArrayList<>(CourseController.getCourses());
        availableCourses.removeIf(course -> course.getLevel() != student.getLevel());
        availableCourses.removeAll(new ArrayList<>(student.getEnrolledCourses().keySet()));
        if (availableCourses.size() > 0)
            return availableCourses;
        else {
            System.out.printf("there are no available courses for this student%n");
            System.out.println("---------------------------------------------------");
            return null;
        }
    }

    public void deleteStudent(Student student) {
        if (student != null) {
            student.withdrawFromAllCourses();
            userNames.remove(student.getUserName());
            students.remove(student);
            System.out.printf("Student removed!%n");
            System.out.println("---------------------------------------------------");
        }
        Main.adminManageStudents();
    }

    public void modifyStudent(Student student) {
        if (student != null) {
            //select update operation
            System.out.printf("Student update Operations : %n" +
                    "1- Update student username%n" +
                    "2- Update student password%n" +
                    "3- Update student level%n" +
                    "4- Update student first name%n" +
                    "5- Update student last name %n" +
                    "6- Update student phone Number%n" +
                    "7- Update student age%n" +
                    "8- Update student courses%n" +
                    "9- Go back%n" +
                    "Please choose what you want to update : ");

            int choice = Main.validChoice(9);
            System.out.println("---------------------------------------------------");
            // go back
            if (choice == 9)
                Main.adminManageStudents();

            else {
                if (choice == 1)
                    updateStudentUserName(student);

                else if (choice == 2)
                    student.updatePassword();

                else if (choice == 3)
                    student.updateLeve();

                else if (choice == 4)
                    student.updateFirstName();

                else if (choice == 5)
                    student.updateLastName();

                else if (choice == 6)
                    student.updatePhone();

                else if (choice == 7)
                    student.updateAge();

                else
                    studentCourseUpdate(student);

                modifyStudent(student);
            }
        }
    }

    public void storeDate() {
        try {
            FileWriter fileWriter = new FileWriter("./src/Students.txt");
            for (Student student : students) {
                StringBuilder studentData = new StringBuilder(String.format(student.getUserName() + "," + student.getPassword() + "," + student.getLevel() + "," + student.getFirstName() + "," + student.getLastName() + "," + student.getPhoneNumber() + "," + student.getAge() + "\n"));
                HashMap<Course, Double> studentCourses = student.getEnrolledCourses();
                for (Map.Entry<Course, Double> it : studentCourses.entrySet())
                    studentData.append(String.format(it.getKey().getId() + ":" + it.getValue() + ","));
                studentData.append("\n");
                fileWriter.write(String.valueOf(studentData));
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    public void updateStudentUserName(Student student) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the new username or -1 to cancel : ");
        String newUserName = scanner.nextLine();

        while (!newUserName.equals("-1") && userNames.contains(newUserName.toLowerCase())) {
            System.out.print("Sorry this username is already taken! try again or -1 to cancel : ");
            newUserName = scanner.nextLine();
        }

        if (newUserName.equals("-1"))
            System.out.printf("task canceled!%n");

        else {
            userNames.remove(student.getUserName().toLowerCase());
            student.setUserName(newUserName);
            userNames.add(newUserName.toLowerCase());
            System.out.printf("username updated to %s%n", newUserName);
        }
        System.out.println("---------------------------------------------------");
    }

    public void updateCourseDegree(Student student) {
        ArrayList<Course> studentCourses = new ArrayList<>(student.getEnrolledCourses().keySet());
        if (studentCourses.size() > 0) {
            Course selectedCourse = CourseController.selectCourse(studentCourses, student);
            if (selectedCourse != null) {
                System.out.print("Enter new degree or -1 to cancel : ");
                double newDegree = Main.getValidDouble();
                while (newDegree != -1 && newDegree < selectedCourse.getMinDegree() || newDegree > selectedCourse.getMaxDegree()) {
                    System.out.print("sorry, student degree must be in range [min degree, max degree] enter valid degree or -1 to cancel : ");
                    newDegree = Main.getValidInteger();
                }
                if (newDegree == -1)
                    System.out.printf("task canceled!%n");
                else {
                    student.setDegree(selectedCourse, newDegree);
                    System.out.printf("Student degree updated to %.2f%n", newDegree);
                }
            }
        }
        else
            System.out.printf("Student didn't enroll in any course%n");
        System.out.println("---------------------------------------------------");
        studentCourseUpdate(student);
    }

    public static Student selectStudent(ArrayList<Student> students){
        printStudentsDetails(students);
        Student student = null;
        if (students.size() > 0) {
            System.out.print("choose student that you want or -1 to cancel : ");
            int index = Main.getValidInteger();
            while (index != -1 && index <= 0 || index > students.size()) {
                System.out.print("please enter valid index or -1 to cancel : ");
                index = Main.getValidInteger();
            }
            if (index == -1)
                System.out.printf("task canceled!%n");
            else
                student = students.get(index - 1);
            System.out.println("---------------------------------------------------");
        }
        return student;
    }

    private void studentCourseUpdate(Student student) {
        //select course update operation
        System.out.printf("Student course update operations : %n" +
                "1- Enroll in a course%n" +
                "2- Withdraw from a course%n" +
                "3- Update student's degree in a specific course%n" +
                "4- Go back%n" +
                "please choose what you want to modify for student courses : ");

        int choice = Main.validChoice(4);
        System.out.println("---------------------------------------------------");
         //Add new course for student
        if (choice == 4)
            modifyStudent(student);

        else {
            if (choice == 1)
                enrollInCourse(student, true);

                //Delete course from student
            else if (choice == 2)
                withdrawFromCourse(student);

                //update course degree
            else if (choice == 3)
                updateCourseDegree(student);

            studentCourseUpdate(student);
        }
    }

    public void withdrawFromCourse(Student student) {
        ArrayList<Course> studentCourses = (new ArrayList<>(student.getEnrolledCourses().keySet()));
        char response = 'y';
        while (response == 'y') {
            if (studentCourses.size() > 0) {
                Course selectedCourse = CourseController.selectCourse(studentCourses, student);
                if (selectedCourse == null)
                    response='n';
                else {
                    studentCourses.remove(selectedCourse);
                    student.withdrawFromCourse(selectedCourse);
                    System.out.printf("course withdrawn!%n");
                    if (studentCourses.size() > 0) {
                        System.out.print("do you want to withdraw form another course ? y or n : ");
                        response = Main.validResponse();
                    }
                    else {
                        System.out.printf("student didn't enroll in any course%n");
                        response = 'n';
                    }
                }
            }
            else {
                System.out.printf("student didn't enroll in any course%n");
                response = 'n';
            }
            System.out.println("---------------------------------------------------");
        }
    }

    public static void printStudentsDetails(ArrayList<Student> students) {
        if (students.size() > 0) {
            students.sort(Comparator.comparingInt(Student::getLevel));
            System.out.printf("%-10s%-20s%-20s%-20s%-20s%-8s%-20s%-20s%n", "Index", "Student username", "Student Name", "Student Level", "Phone Number", "Age", "Number of Courses", "Student Percentage");
            int i = 1;
            for (Student student : students) {
                System.out.printf("%-10d%-20s%-20s%-20d%-20s%-8d%-20d%-2.2f%n", i++, student.getUserName(), student.getFirstName() + " " + student.getLastName(), student.getLevel(), student.getPhoneNumber(), student.getAge(), student.getEnrolledCourses().size(), student.calculatePercentage());
            }
        }
        else
            System.out.printf("There is no any student!%n");
        System.out.println("---------------------------------------------------");
    }
}