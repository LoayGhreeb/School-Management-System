import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static CourseController course= new CourseController();
    public static StudentController student = new StudentController();

    public static void main(String[] args) {
        System.out.printf("Welcome! %n");
        printMainMenu();
    }

    public static void printMainMenu(){
        System.out.printf("Main menu :%n" +
                "1- Login as admin%n" +
                "2- Login as student%n" +
                "3- Exit program%n" +
                "Please choose : ");

        int choice = validChoice(3);

        //Admin menu
        if (choice ==1)
            adminLogin();

        //Student menu
        else if (choice ==2)
            studentLogin();

        //Sava data & exit
        else
            exitProgram();
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
        if (response == 'y') {
            System.out.printf("Congrats! You have been successfully logged in as admin!%n");
            System.out.println("---------------------------------------------------");
            adminOperation();
        }
        else {
            System.out.println("---------------------------------------------------");
            printMainMenu();
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
            printMainMenu();

        //Save data & exit
        else if (choice ==4)
            exitProgram();
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

        if (courseOp == 6)
            adminOperation();

        else {
            if (courseOp == 1)
                course.addCourse();

            else if (courseOp == 2)
                course.deleteCourse(CourseController.selectCourse(CourseController.getCourses()));

            else if (courseOp == 3)
                course.modifyCourse(CourseController.selectCourse(CourseController.getCourses()));

            else if (courseOp == 4)
                CourseController.printCoursesDetails(CourseController.getCourses());

            else if (courseOp == 5)
                course.viewEnrolledStudentsInCourse();

            adminManageCourses();
        }
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

        if (studentOp == 6)
            adminOperation();

        else {
            //Add new student
            if (studentOp == 1)
                student.addStudent();

            //delete student
            else if (studentOp == 2)
                student.deleteStudent(StudentController.selectStudent(StudentController.getStudents()));

            //modify student
            else if (studentOp == 3)
                student.modifyStudent(StudentController.selectStudent(StudentController.getStudents()));


            //all students
            else if (studentOp == 4)
                StudentController.printStudentsDetails(StudentController.getStudents());

            //report for specific student
            else if (studentOp == 5) {
                Student s = StudentController.selectStudent(StudentController.getStudents());
                if (s != null) s.printReport();
            }

            adminManageStudents();
        }
    }

    public  static void studentLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student username : ");
        String username = scanner.nextLine();
        System.out.print("Enter student password : ");
        String password = scanner.nextLine();
        Student student1 = student.login(username, password);
        char response = 'y';
        while (response=='y' && student1 == null){
            System.out.print("wrong username or password! do you want to try again ? y or n : ");
            response = validResponse();
            if (response == 'y') {
                System.out.print("Enter student username : ");
                username = scanner.nextLine();
                System.out.print("Enter student password : ");
                password = scanner.nextLine();
                student1 = student.login(username, password);
            }
        }
        if (response == 'y') {
            System.out.printf("Congrats! You have been successfully logged in as %s %s!%n", student1.getFirstName(), student1.getLastName());
            System.out.println("---------------------------------------------------");
            studentOperation(student1);
        }
        else {
            System.out.println("---------------------------------------------------");
            printMainMenu();
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
            printMainMenu();

        //Exit & save data
        else if (choice == 4)
            exitProgram();

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

        if (choice == 4)
            studentOperation(student1);
        else{
            //View all registered courses
             if (choice == 1)
                student1.printStudentCourses();

            //View all available courses
            else if (choice == 2) {
                 ArrayList<Course> available = student.getAvailableCourses(student1);
                 if (available != null)
                     CourseController.printCoursesDetails(available);
             }
            //View Full Report
            else if (choice == 3)
                student1.printReport();

            studentViewReports(student1);
        }
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

        if (choice == 5)
            studentOperation(student1);
        else {
            //change username
            if (choice == 1)
                student.updateStudentUserName(student1);

            //change password
            else if (choice ==2)
                student1.updatePassword();

            //Register to a course
            else if (choice ==3)
                student.enrollInCourse(student1, false);

            //Withdraw from a course
            else if(choice == 4)
                student.withdrawFromCourse(student1);

            studentMangeHisData(student1);
        }
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
        course.storeData();
        student.storeDate();
        System.out.print("new data saved!");
        System.exit(0);
    }
}