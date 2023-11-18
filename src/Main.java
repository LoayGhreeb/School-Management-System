import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseHelper.initializeDB();
        CourseController.readData();
        StudentController.readData();
        System.out.printf("Welcome! %n");
        printMainMenu();
        exitProgram();
    }

    public static void printMainMenu(){
        System.out.printf("Main menu :%n" +
                "1- Login as admin%n" +
                "2- Login as student%n" +
                "3- Exit program%n" +
                "Please choose : ");

        int choice = validChoice(3);
        System.out.println("---------------------------------------------------");
        //Admin menu
        if (choice ==1)
            adminLogin();

        //Student menu
        else if (choice ==2)
            studentLogin();

        //Sava data & exit
        else
            return;

        printMainMenu();
    }

    public static void adminLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter admin username : ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password : ");
        String password = scanner.nextLine();
        char response = 'y';
        while (response == 'y' && (!username.equals("admin") || !password.equals("123"))){
            System.out.print("wrong username or password! do you want to try again ? y or n : ");
            response = validResponse();
            if (response == 'y') {
                System.out.print("Enter admin username : ");
                username = scanner.nextLine();
                System.out.print("Enter admin password : ");
                password = scanner.nextLine();
            }
        }
        System.out.println("---------------------------------------------------");
        if (response == 'y') {
            System.out.printf("Congrats! You have been successfully logged in as admin!%n");
            System.out.println("---------------------------------------------------");
            adminOperation();
        }
    }

    public static void adminOperation(){
        System.out.printf("Admin operations :%n"+
                "1- Manage students%n" +
                "2- Manage courses%n" +
                "3- Logout%n" +
                "4- Exit Program%n" +
                "Please choose : " );

        int choice = validChoice(4);
        System.out.println("---------------------------------------------------");
        //Manage Students
        if (choice ==1)
            adminManageStudents();

        //Manage Courses
        else if (choice == 2)
            adminManageCourses();

        //Logout
        else if (choice == 3)
            return;

        //Save data & exit
        else if (choice ==4)
            exitProgram();

        adminOperation();
    }

    public static void adminManageCourses(){
        System.out.printf("Manage Courses :%n" +
                "1- Add a new course%n" +
                "2- Delete a course%n" +
                "3- Modify a course%n" +
                "4- View all courses report%n" +
                "5- View students enrolled in a specific course%n" +
                "6- Go back%n" +
                "Please select what you want : ");

        int courseOp = validChoice(6);
        System.out.println("---------------------------------------------------");

        if (courseOp == 1)
            CourseController.addCourse();

        else if (courseOp == 2)
            CourseController.deleteCourse(CourseController.selectCourse(CourseController.getCourses(), null));

        else if (courseOp == 3) {
            Course selectedCourse= CourseController.selectCourse(CourseController.getCourses(), null);
            if (selectedCourse != null) selectedCourse.modifyCourse();
        }

        else if (courseOp == 4)
            CourseController.printCoursesDetails(CourseController.getCourses());

        else if (courseOp == 5) {
            Course selectedCourse = CourseController.selectCourse(CourseController.getCourses(), null);
            if (selectedCourse != null) selectedCourse.viewEnrolledStudents();
        }

        else if (courseOp == 6)
            return;

        System.out.println("---------------------------------------------------");
        adminManageCourses();
    }

    public static void adminManageStudents(){
        System.out.printf("Manage students : %n" +
                "1- Add a new student%n" +
                "2- Delete a student%n" +
                "3- Modify a student%n" +
                "4- View all students%n" +
                "5- View report for specific student%n" +
                "6- Go back%n" +
                "Please select what you want : ");

        int studentOp = validChoice(6);
        System.out.println("---------------------------------------------------");

        //Add new student
        if (studentOp == 1)
            StudentController.addStudent();

        //delete student
        else if (studentOp == 2)
            StudentController.deleteStudent(StudentController.selectStudent(StudentController.getStudents()));

        //modify student
        else if (studentOp == 3) {
            Student selectedStudent = StudentController.selectStudent(StudentController.getStudents());
            if (selectedStudent != null)  selectedStudent.modifyStudent();
        }

        //all students
        else if (studentOp == 4)
            StudentController.printStudentsDetails(StudentController.getStudents());

        //report for specific student
        else if (studentOp == 5) {
            Student selectedStudent = StudentController.selectStudent(StudentController.getStudents());
            if (selectedStudent != null) selectedStudent.printReport();
        }

        else if (studentOp == 6)
           return;

        System.out.println("---------------------------------------------------");
        adminManageStudents();
    }

    public  static void studentLogin(){
        if (StudentController.getStudents().isEmpty()){
            System.out.printf("There is no any student!%nYou must login as admin first, add students.%n");
            System.out.println("---------------------------------------------------");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student username : ");
        String username = scanner.nextLine();
        System.out.print("Enter student password : ");
        String password = scanner.nextLine();
        Student student1 = StudentController.login(username, password);
        char response = 'y';
        while (response=='y' && student1 == null){
            System.out.print("wrong username or password! do you want to try again ? y or n : ");
            response = validResponse();
            if (response == 'y') {
                System.out.print("Enter student username : ");
                username = scanner.nextLine();
                System.out.print("Enter student password : ");
                password = scanner.nextLine();
                student1 = StudentController.login(username, password);
            }
        }
        System.out.println("---------------------------------------------------");
        if (response == 'y') {
            System.out.printf("Congrats! You have been successfully logged in as %s %s!%n", student1.getFirstName(), student1.getLastName());
            System.out.println("---------------------------------------------------");
            studentOperation(student1);
        }
    }

    public static void studentOperation(Student student1){
        System.out.printf("Student operations :%n" +
                "1- View Reports%n" +
                "2- Manage your data%n" +
                "3- Logout%n" +
                "4- Exit Program%n" +
                "Please choose : " );
        int choice = validChoice(4);
        System.out.println("---------------------------------------------------");

        if (choice == 1)
            studentViewReports(student1);

        else if (choice ==2)
            studentMangeHisData(student1);

        //Logout
        else if(choice == 3)
           return;

        //Exit & save data
        else if (choice == 4)
            exitProgram();

        studentOperation(student1);
    }

    public static void studentViewReports(Student student1){
        System.out.printf("Student reports :%n" +
                "1- View all enrolled courses%n" +
                "2- View all available courses to enroll%n" +
                "3- View full report%n" +
                "4- Go back%n" +
                "Please choose : ");

        int choice = validChoice(4);
        System.out.println("---------------------------------------------------");


        //View all registered courses
         if (choice == 1)
            student1.printStudentCourses(student1.getEnrolledCourses());

        //View all available courses
        else if (choice == 2) {
             ArrayList<Course> available = student1.getAvailableCourses();
             if (available != null)
                 CourseController.printCoursesDetails(available);
         }
        //View Full Report
        else if (choice == 3)
            student1.printReport();

        else if (choice == 4)
             return;

        System.out.println("---------------------------------------------------");
        studentViewReports(student1);
    }

    public static void studentMangeHisData(Student student1){
        System.out.printf("Student's Data :%n" +
                "1- Change username%n" +
                "2- Change password%n" +
                "3- Enroll to a course%n" +
                "4- Withdraw from a course%n" +
                "5- Go back%n" +
                "Please choose : " );

        int choice = validChoice(5);
        System.out.println("---------------------------------------------------");
        //change username
        if (choice == 1)
            student1.updateStudentUserName();

        //change password
        else if (choice ==2)
            student1.updatePassword();

        //Enroll in a course
        else if (choice ==3) {
            if (student1.getAvailableCourses() != null)
                student1.enrollInCourse( CourseController.selectCourse(student1.getAvailableCourses(), null), false);
        }

        //Withdraw from a course
        else if(choice == 4)
            student1.withdrawFromCourse(CourseController.selectCourse(student1.getEnrolledCourses(), student1), true);

        else if (choice == 5)
            return;

        System.out.println("---------------------------------------------------");
        studentMangeHisData(student1);
    }

    public static int validChoice(int max) {
        int choice = getValidInteger();
        while (choice <= 0 || choice > max) {
            System.out.print("please enter valid choice : ");
            choice = getValidInteger();
        }
        return choice;
    }
    public static int getValidInteger(){
        int input= 0;
        boolean flag;
        do{
            try{
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextInt();
                flag =false;
            }catch (Exception e){
                System.out.print("please enter valid number : ");
                flag = true;
            }
        }while (flag);
        return input;
    }
    public static double getValidDouble(){
        double input= 0;
        boolean flag;
        do{
            try{
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextDouble();
                flag =false;
            }catch (Exception e){
                System.out.print("please enter valid number : ");
                flag = true;
            }
        }while (flag);
        return input;
    }
    public static char validResponse(){
        Scanner scanner = new Scanner(System.in);
        char response = scanner.next().charAt(0);
        while (response != 'y' && response != 'n') {
            System.out.print("please enter y or n  : ");
            response = scanner.next().charAt(0);
        }
        return response;
    }
    public static void exitProgram(){
        System.out.printf("Thank you & come again! :D%n");
        CourseController.storeData();
        StudentController.storeDate();
        System.out.print("new data saved!");
        DatabaseHelper.close();
        System.exit(0);
    }
}