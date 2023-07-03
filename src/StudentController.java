import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Scanner;

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
                Student newStudent = new Student(studentData[0], studentData[1], studentData[2], studentData[3], studentData[4], Integer.parseInt(studentData[5]));
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
                        course1.addStudentToCourse(newStudent);
                    }
                }
                newStudent.setCourses(studentCourses);
                students.add(newStudent);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        }
    }

    public static ArrayList<Student> getStudents(){
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
        String firstName, lastName, phoneNumber;
        String userName, password;
        int age;
        char addStudent = 'y';
        while (addStudent == 'y') {
            Scanner scanner = new Scanner(System.in);
            //read student data
            System.out.printf("To add new student : %n");

            System.out.print("Enter username or -1 to cancel : ");
            userName= scanner.nextLine();
            while (!userName.equals("-1") && userNames.contains(userName.toLowerCase())){
                System.out.print("Sorry this username is already taken! try again or -1 to cancel : ");
                userName= scanner.nextLine();
            }

            if (userName.equals("-1")){
                System.out.printf("task canceled!%n");
            }
            else {
                System.out.print("Enter password : ");
                password = scanner.nextLine();

                System.out.print("Enter first name: ");
                firstName = scanner.next();

                System.out.print("Enter last name : ");
                lastName = scanner.next();

                System.out.print("Enter phone number : ");
                phoneNumber = scanner.next();

                System.out.print("Enter age : ");
                age = scanner.nextInt();
                while (age <= 0) {
                    System.out.print("age must be > 0 enter valid age : ");
                    age = scanner.nextInt();
                }

                Student newStudent = new Student(userName, password, firstName, lastName, phoneNumber, age);
                userNames.add(userName.toLowerCase());
                //add Courses for the new student ?
                System.out.print("Do you want to add courses for this new student ? y or n : ");
                char response = validResponse();
                if (response == 'y') {
                    System.out.println("---------------------------------------------------");
                    HashMap<Course, Double> studentCourses = registerNewCourse(newStudent, true);
                    newStudent.setCourses(studentCourses);
                }
                students.add(newStudent);
                System.out.printf("Student added!%n");
            }
            System.out.print("You want to add another student ? y or n : ");
            addStudent = validResponse();
            System.out.println("---------------------------------------------------");
        }
    }

    public HashMap<Course, Double> registerNewCourse(Student student, boolean admin) {

        Scanner scanner = new Scanner(System.in);
        HashMap<Course, Double> studentCourses = student.getCourses();
        char response = 'y';
        ArrayList<Course> availableCourses = getAvailableCourses(student);
        while (response == 'y') {
            // I want to display all courses with their ids , choose index , input student degree
            if (availableCourses.size() > 0) {
                System.out.printf("All available courses are : %n");
                Course selectedCourse = CourseController.selectCourse(availableCourses, null);

                if (selectedCourse == null)
                    response = 'n';
                else {
                    double degree = 0;
                    if (admin) {
                        System.out.print("Please enter student degree in this course or -1 to cancel : ");
                        degree = scanner.nextDouble();
                        while (degree != -1 && (degree < selectedCourse.getMinDegree() || degree > selectedCourse.getMaxDegree())) {
                            System.out.print("sorry, student degree must be in range [min degree, max degree] enter valid degree or -1 to cancel : ");
                            degree = scanner.nextDouble();
                        }
                    }
                    if (degree == -1) {
                        System.out.printf("task canceled!%n");
                        response = 'n';
                    }
                    else {
                        studentCourses.put(selectedCourse, degree);
                        selectedCourse.addStudentToCourse(student);
                        System.out.print("Course added!");
                        availableCourses.remove(selectedCourse);
                        if (availableCourses.size() > 0) {
                            System.out.print("do you want to add another new course ? y or n : ");
                            response = validResponse();
                        }
                        else {
                            System.out.printf("you have added all available courses%n");
                            response = 'n';
                        }
                    }
                }
            }
            else {
                System.out.printf("you have added all available courses%n");
                response = 'n';
            }
            System.out.println("---------------------------------------------------");
        }
        return studentCourses;
    }

    public ArrayList<Course> getAvailableCourses(Student student){
        ArrayList<Course> availableCourses = new ArrayList<>(CourseController.getCourses());
        availableCourses.removeAll(new ArrayList<>(student.getCourses().keySet()));
        return availableCourses;
    }

    public void deleteStudent() {
        Scanner scanner = new Scanner(System.in);

        char delete = 'y';
        while (delete == 'y') {
            if (students.size() > 0) {
                Student selectedStudent = selectStudent(students);
                if (selectedStudent== null)
                    delete = 'n';

                else {
                    userNames.remove(selectedStudent.getUserName());
                    students.remove(selectedStudent);
                    System.out.printf("Student removed!%n");
                    if (students.size() > 0) {
                        System.out.print("Delete another student ? y or n  : ");
                        delete = scanner.next().charAt(0);
                        while (delete != 'y' && delete != 'n') {
                            System.out.print("please enter y or n  : ");
                            delete = scanner.next().charAt(0);
                        }
                    }
                    else {
                        System.out.printf("you don't have any student!%n");
                        delete = 'n';
                    }
                }
            }
            else {
                System.out.printf("you don't have any student!%n");
                delete = 'n';
            }
            System.out.println("---------------------------------------------------");
        }
    }

    public void modifyStudent() {
        char modify = 'y';
        while (modify == 'y') {
            //Select student
            if (students.size() > 0) {
                Student selectedStudent = selectStudent(students);
                if (selectedStudent == null)
                    modify = 'n';
                else {
                    //select update operation
                    char sameStudent = 'y';
                    while (sameStudent == 'y') {
                        System.out.printf("Student update Operations : %n" +
                                "1- Update student username%n" +
                                "2- Update student password%n" +
                                "3- Update student first name%n" +
                                "4- Update student last name %n" +
                                "5- Update student phone Number%n" +
                                "6- Update student age%n" +
                                "7- Update student courses%n" +
                                "8- cancel%n" +
                                "Please choose what you want to update : ");

                        int choice = validChoice(8);
                        System.out.println("---------------------------------------------------");
                        // cancel
                        if (choice == 8) {
                            System.out.printf("task canceled!%n");
                            sameStudent = 'n';
                        }
                        else {
                            if (choice == 1)
                                updateStudentUserName(selectedStudent);

                            else if (choice == 2)
                                updateStudentPassword(selectedStudent);

                            else if (choice == 3)
                                updateStudentFirstName(selectedStudent);

                            else if (choice == 4)
                                updateStudentLastName(selectedStudent);

                            else if (choice == 5)
                                updateStudentPhone(selectedStudent);

                            else if (choice == 6)
                                updateStudentAge(selectedStudent);

                            else
                                studentCourseUpdate(selectedStudent);

                            System.out.print("you want to update other data for the selected student ? y or n : ");
                            sameStudent = validResponse();
                        }
                        System.out.println("---------------------------------------------------");
                    }
                    System.out.print("You want to update another student ? y or n : ");
                    modify = validResponse();
                }
            }
            else{
                System.out.printf("you don't have any student!%n");
                modify ='n';
            }
            System.out.println("---------------------------------------------------");
        }
    }

    public void storeDate(){
        try {
            FileWriter fileWriter = new FileWriter("./src/Students.txt");
            for(Student student : students){
               StringBuilder studentData= new StringBuilder(String.format(student.getUserName() + "," + student.getPassword() + "," + student.getFirstName() + "," + student.getLastName() + "," + student.getPhoneNumber() + "," + student.getAge() + "\n"));
               HashMap<Course, Double > studentCourses = student.getCourses();
               for(Map.Entry<Course, Double> it : studentCourses.entrySet() )
                   studentData.append(String.format(it.getKey().getId() + ":" + it.getValue() + ","));
               studentData.append("\n");
               fileWriter.write(String.valueOf(studentData));
            }
            fileWriter.close();
        }catch (IOException e){
            System.out.print(e.getMessage());
        }
    }

    public void updateStudentUserName(Student student){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the new username or -1 to cancel : ");
        String newUserName = scanner.next();

        while (!newUserName.equals("-1") && userNames.contains(newUserName.toLowerCase())){
            System.out.print("Sorry this username is already taken! try again or -1 to cancel : ");
            newUserName= scanner.next();
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

    public void updateStudentPassword(Student student){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new password or -1 to cancel : ");
        String newPassword = scanner.next();
        if (newPassword.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            student.setPassword(newPassword);
            System.out.printf("password updated%n");
        }
        System.out.println("---------------------------------------------------");
    }

    private void updateStudentFirstName(Student student) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new first name or -1 to cancel : ");
        String newFirstName = scanner.next();
        if (newFirstName.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            student.setFirstName(newFirstName);
            System.out.printf("first name updated to %s%n", newFirstName);
        }
        System.out.println("---------------------------------------------------");
    }

    private void updateStudentLastName(Student student) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new last name or -1 to cancel : ");
        String newLastName = scanner.next();
        if (newLastName.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            student.setLastName(newLastName);
            System.out.printf("last name updated to %s%n", newLastName);
        }
        System.out.println("---------------------------------------------------");
    }

    public void updateStudentPhone(Student student) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new phone number or -1 to cancel : ");
        String newPhoneNumber = scanner.next();
        if (newPhoneNumber.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            student.setPhoneNumber(newPhoneNumber);
            System.out.printf("phone number updated to %s%n", newPhoneNumber);
        }
        System.out.println("---------------------------------------------------");
    }

    private void updateStudentAge(Student student) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new age or -1 to cancel : ");
        int newAge = scanner.nextInt();
        while (newAge < -1) {
            System.out.print("age must be > 0 enter valid age : ");
            newAge = scanner.nextInt();
        }
        if (newAge == -1)
            System.out.printf("task canceled!%n");
        else {
            student.setAge(newAge);
            System.out.printf("age updated to %d%n", newAge);
        }
        System.out.println("---------------------------------------------------");
    }

    private void updateCourseDegree(Student student) {
        ArrayList<Course> studentCourses = new ArrayList<>(student.getCourses().keySet());
        Scanner scanner = new Scanner(System.in);

        char response = 'y';
        while (response == 'y') {
            if (studentCourses.size() > 0) {
                Course selectedCourse = CourseController.selectCourse(studentCourses, student);

                if (selectedCourse == null)
                    response= 'n';
                else {
                    System.out.print("Enter new degree or -1 to cancel : ");
                    double newDegree = scanner.nextDouble();
                    while (newDegree != -1 && newDegree < selectedCourse.getMinDegree() || newDegree > selectedCourse.getMaxDegree()) {
                        System.out.print("sorry, student degree must be in range [min degree, max degree] enter valid degree or -1 to cancel : ");
                        newDegree = scanner.nextInt();
                    }
                    if (newDegree == -1)
                        System.out.printf("task canceled!%n");
                    else {
                        student.setDegree(selectedCourse, newDegree);
                        System.out.printf("Student degree updated to %.2f%n", newDegree);
                    }
                    System.out.print("Do you want to update degree for another course ? y or n : ");
                    response = validResponse();
                }
            }
            else {
                System.out.printf("sorry there is no courses to update!%n");
                response ='n';
            }
            System.out.println("---------------------------------------------------");
        }
    }

    public Student selectStudent(ArrayList<Student> students){
        printStudentsFullDetails(students);
        System.out.print("choose student that you want or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();
        while (index != -1 && index <= 0 || index > students.size()) {
            System.out.print("please enter valid index : ");
            index = scanner.nextInt();
        }
        System.out.println("---------------------------------------------------");
        if (index == -1) {
            System.out.printf("task canceled!%n");
            return null;
        }
        else return students.get(index - 1);
    }

    private void studentCourseUpdate(Student student) {
        char courseUpdate = 'y';
        while (courseUpdate == 'y') {
            //select course update operation
            System.out.printf("Student course update operations : %n"+
                    "1- register course for the student%n" +
                    "2- remove course from the student%n" +
                    "3- update student degree for specific course%n" +
                    "4- cancel%n" +
                    "please choose what you want to modify for student courses : ");

            int choice =validChoice(4);
            System.out.println("---------------------------------------------------");

            if (choice == 4) {
                System.out.printf("task canceled!%n");
                courseUpdate = 'n';
            }
            else {
                //Add new course for student
                if (choice == 1)
                    registerNewCourse(student, true);

                    //Delete course from student
                else if (choice == 2)
                    removeCourseFromStudent(student);

                    //update course degree
                else
                    updateCourseDegree(student);

                System.out.print("You want perform another course modify ? y or n : ");
                courseUpdate = validResponse();
            }
            System.out.println("---------------------------------------------------");
        }
    }

    public void removeCourseFromStudent(Student student) {
        ArrayList<Course> studentCourses = (new ArrayList<>(student.getCourses().keySet()));
        char response = 'y';
        while (response == 'y') {
            if (studentCourses.size() > 0) {
                Course selectedCourse = CourseController.selectCourse(studentCourses, student);
                if (selectedCourse == null)
                    response='n';

                else {
                    student.removeCourse(selectedCourse);
                    studentCourses.remove(selectedCourse);
                    System.out.printf("course removed!%n");
                    if (studentCourses.size() > 0) {
                        System.out.print("do you want to remover another course ? y or n : ");
                        response = validResponse();
                    }
                    else {
                        System.out.printf("there is no courses to delete!%n");
                        response = 'n';
                    }
                }
            }
            else {
                System.out.printf("sorry there is no courses to delete!%n");
                response = 'n';
            }
            System.out.println("---------------------------------------------------");
        }
    }

    public void printStudentsName(ArrayList<Student> students) {
        System.out.printf("All students : %n");
        for (int i = 0; i < students.size(); i++)
            System.out.printf("%d- %s %s%n", i + 1, students.get(i).getFirstName(), students.get(i).getLastName());
        System.out.println("---------------------------------------------------");
    }

    public static void printStudentsFullDetails(ArrayList<Student> students) {
        System.out.printf("%-10s%-20s%-20s%-20s%-8s%-20s%-20s%n", "Index", "Student username", "Student Name", "Phone Number", "Age", "Number of Courses", "Student Percentage");
        int i=1;
        for (Student student : students) {
            System.out.printf("%-10d%-20s%-20s%-20s%-8d%-20d%-2.2f%n", i++, student.getUserName(), student.getFirstName() + " " + student.getLastName(), student.getPhoneNumber(), student.getAge(), student.getCourses().size() , student.calculatePercentage());
        }
        System.out.println("---------------------------------------------------");
    }

    private int validChoice(int max){
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        while (choice <= 0 || choice > max) {
            System.out.print("please enter valid choice : ");
            choice = scanner.nextInt();
        }
        return choice;
    }

    private char validResponse(){
        Scanner scanner = new Scanner(System.in);
        char response = scanner.next().charAt(0);
        while (response != 'y' && response != 'n') {
            System.out.print("please enter y or n  : ");
            response = scanner.next().charAt(0);
        }
        return response;
    }
}