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
        while (!username.equals("admin") || !password.equals("123")){
            System.out.printf("wrong username or password! please try again%n");
            System.out.print("Enter admin username : ");
            username = scanner.nextLine();
            System.out.print("Enter admin password : ");
            password = scanner.nextLine();
        }
        System.out.printf("Congrats! You have been successfully logged in as admin!%n");
        System.out.println("---------------------------------------------------");
        adminOperation();
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
                "5- Go back%n" +
                "Please select what you want : ");

        int courseOp = validChoice(5);
        System.out.println("---------------------------------------------------");

        if (courseOp == 5)
            adminOperation();

        else {
            if (courseOp == 1)
                course.addCourse();

            else if (courseOp == 2)
                course.deleteCourse();

            else if (courseOp == 3)
                course.modifyCourse();

            if (courseOp == 4)
                CourseController.printCoursesDetails(CourseController.getCourses(), null);

            adminManageCourses();
        }
    }

    public static void adminManageStudents(){
        System.out.printf("Manage students%n" +
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
                student.deleteStudent();

            //modify student
            else if (studentOp == 3)
                student.modifyStudent();

            //all students
            else if (studentOp == 4)
                StudentController.printStudentsFullDetails(StudentController.getStudents());

            //report for specific student
            else if (studentOp == 5)
                student.selectStudent(StudentController.getStudents()).printReport();

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
        while (student1 == null){
            System.out.printf("wrong username or password! please try again%n");
            System.out.print("Enter student username : ");
            username = scanner.nextLine();
            System.out.print("Enter student password : ");
            password = scanner.nextLine();
            student1 = student.login(username, password);
        }
        System.out.printf("Congrats! You have been successfully logged in as %s %s!%n", student1.getFirstName(), student1.getLastName());
        System.out.println("---------------------------------------------------");
        studentOperation(student1);
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
                "1- View all courses%n" +
                "2- View all registered courses%n" +
                "3- View all available courses to register%n" +
                "4- View full report%n" +
                "5- Go back%n" +
                "Please choose : ");

        int choice = validChoice(5);
        System.out.println("---------------------------------------------------");

        if (choice == 5)
            studentOperation(student1);
        else{

            //view all courses
            if (choice == 1)
                CourseController.printCoursesDetails(CourseController.getCourses(), null);

            //View all registered courses
            else if (choice ==2)
                student1.printStudentCourses();

            //View all available courses
            else if (choice ==3)
                CourseController.printCoursesDetails(student.getAvailableCourses(student1), null);

            //View Full Report
            else if (choice == 4)
                student1.printReport();

            studentViewReports(student1);
        }
    }

    public static void studentMangeHisData(Student student1){
        System.out.printf("Student's Data :%n" +
                "1- Change username%n" +
                "2- Change password%n" +
                "3- Register to a course%n" +
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
                student.updateStudentPassword(student1);

            //Register to a course
            else if (choice ==3)
                student.registerNewCourse(student1, false);

            //Withdraw from a course
            else if(choice == 4)
                student.removeCourseFromStudent(student1);

            studentMangeHisData(student1);
        }
    }

    public static int validChoice(int max) {
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        while (choice <= 0 || choice > max) {
            System.out.print("please enter valid choice : ");
            choice = scanner.nextInt();
        }
        return choice;
    }

    public static void exitProgram(){
        System.out.printf("Thank you & come again! :D%n");
        course.storeData();
        student.storeDate();
        System.out.print("new data saved!");
        System.exit(0);
    }
}