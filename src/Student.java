import java.util.*;

public class Student {

    private String firstName, lastName, phoneNumber;
    private String userName, password;
    private int level, age;
    private HashMap<Course , Double> enrolledCourses;

    public Student(){
    }
    public Student(String userName, String password, int level, String firstName, String lastName, String phoneNumber, int age){
        if (level <= 0 || level > 4)
            throw new IllegalArgumentException("student level must be in range [1,4]");
        if(age <= 0 )
            throw new IllegalArgumentException("age must be > 0");
        this.userName = userName;
        this.password = password;
        this.level = level;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber= phoneNumber;
        this.age = age;
        enrolledCourses = new HashMap<>();
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void updateStudentUserName() {
        Scanner scanner = new Scanner(System.in);
        HashSet<String> userNames = StudentController.getUserNames();
        System.out.print("Enter the new username or -1 to cancel : ");
        String newUserName = scanner.nextLine();

        while (!newUserName.equals("-1") && userNames.contains(newUserName.toLowerCase())) {
            System.out.print("Sorry this username is already taken! try again or -1 to cancel : ");
            newUserName = scanner.nextLine();
        }

        if (newUserName.equals("-1"))
            System.out.printf("task canceled!%n");

        else {
            userNames.remove(getUserName().toLowerCase());
            setUserName(newUserName);
            userNames.add(newUserName.toLowerCase());
            System.out.printf("username updated to %s%n", newUserName);
        }
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void updatePassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new password or -1 to cancel : ");
        String newPassword = scanner.nextLine();
        if (newPassword.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            setPassword(newPassword);
            System.out.printf("password updated%n");
        }
    }

    public int getLevel(){
        return level;
    }
    public void setLevel(int level){
        this.level=  level;
    }
    public void updateLeve(){
        System.out.printf("Be careful, all enrolled courses will be withdrawn%nEnter new level or -1 to cancel : ");

        int newLevel = Main.getValidInteger();
        while (newLevel != -1 && (newLevel <= 0 || newLevel > 4)) {
            System.out.print("student level must be in range [1, 4] please try again or -1 to cancel : ");
            newLevel = Main.getValidInteger();
        }

        if (newLevel == -1)
            System.out.printf("task canceled!%n");
        else {
            withdrawFromAllCourses();
            enrolledCourses.clear();
            setLevel(newLevel);
            System.out.printf("student level updated to %d%n", newLevel);
        }
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void updateFirstName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new first name or -1 to cancel : ");
        String newFirstName = scanner.next();
        if (newFirstName.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            setFirstName(newFirstName);
            System.out.printf("first name updated to %s%n", newFirstName);
        }
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void updateLastName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new last name or -1 to cancel : ");
        String newLastName = scanner.next();
        if (newLastName.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            setLastName(newLastName);
            System.out.printf("last name updated to %s%n", newLastName);
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void updatePhone() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new phone number or -1 to cancel : ");
        String newPhoneNumber = scanner.next();
        if (newPhoneNumber.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            setPhoneNumber(newPhoneNumber);
            System.out.printf("phone number updated to %s%n", newPhoneNumber);
        }
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void updateAge() {
        System.out.print("Enter the new age or -1 to cancel : ");
        int newAge = Main.getValidInteger();
        while (newAge < -1) {
            System.out.print("age must be > 0 enter valid age : ");
            newAge = Main.getValidInteger();
        }
        if (newAge == -1)
            System.out.printf("task canceled!%n");
        else {
            setAge(newAge);
            System.out.printf("age updated to %d%n", newAge);
        }
    }

    public void modifyStudent() {
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

        if (choice == 1)
            updateStudentUserName();

        else if (choice == 2)
            updatePassword();

        else if (choice == 3)
            updateLeve();

        else if (choice == 4)
            updateFirstName();

        else if (choice == 5)
            updateLastName();

        else if (choice == 6)
            updatePhone();

        else if (choice == 7)
            updateAge();

        else if (choice == 8)
            studentCourseUpdate();

        else if (choice == 9)
            return;

        System.out.println("---------------------------------------------------");
        modifyStudent();
    }
    public ArrayList<Course> getEnrolledCourses() {
        return new ArrayList<>(enrolledCourses.keySet());
    }
    public void setEnrolledCourses(HashMap<Course ,Double> enrolledCourses){
        this.enrolledCourses = enrolledCourses;
    }
    public ArrayList<Course> getAvailableCourses() {
        ArrayList<Course> availableCourses = new ArrayList<>(CourseController.getCourses());
        availableCourses.removeIf(course -> course.getLevel() != getLevel());
        availableCourses.removeAll(getEnrolledCourses());
        if (!availableCourses.isEmpty())
            return availableCourses;
        else {
            System.out.printf("there are no available courses for this student%n");
            return null;
        }
    }
    public void studentCourseUpdate() {
        //select course update operation
        System.out.printf("Student course update operations : %n" +
                "1- Enroll in a course%n" +
                "2- Withdraw from a course%n" +
                "3- Update student's degree in a specific course%n" +
                "4- Go back%n" +
                "please choose what you want to modify for student courses : ");

        int choice = Main.validChoice(4);
        System.out.println("---------------------------------------------------");

        if (choice == 4)
            return;

        else {
            //Enroll in a course
            if (choice == 1)
                enrollInCourse(CourseController.selectCourse(getAvailableCourses(), null), true);

            //Withdraw form a course
            else if (choice == 2)
                withdrawFromCourse(CourseController.selectCourse(getEnrolledCourses(), this), true);

            //Update course degree
            else if (choice == 3)
                updateCourseDegree(CourseController.selectCourse(getEnrolledCourses(), this));

            System.out.println("---------------------------------------------------");
            studentCourseUpdate();
        }
    }
    public void enrollInCourse(Course course, boolean admin) {
        if (course != null) {
            double degree = 0;
            if (admin) {
                System.out.print("please enter student degree in this course or -1 to cancel : ");
                degree = Main.getValidDouble();
                while (degree != -1 && (degree < course.getMinDegree() || degree > course.getMaxDegree())) {
                    System.out.print("sorry, student degree must be in range [min degree, max degree] enter valid degree or -1 to cancel : ");
                    degree = Main.getValidDouble();
                }
            }
            if (degree == -1)
                System.out.printf("task canceled!%n");
            else {
                enrolledCourses.put(course, degree);
                course.enrollStudentInCourse(this);
                System.out.printf("Student enrolled!%n");
            }
        }
    }
    public void withdrawFromCourse(Course course, boolean flag){
        if (course != null) {
            enrolledCourses.remove(course);
            course.withdrawStudent(this);
            if (flag)  System.out.printf("course withdrawn!%n");
        }
    }
    public void withdrawFromAllCourses(){
        for(Course course : enrolledCourses.keySet()){
            course.withdrawStudent(this);
            enrolledCourses.remove(course);
        }
    }

    public double getDegree(Course course){
        return enrolledCourses.get(course);
    }
    public void setDegree(Course course, double degree){
        enrolledCourses.put(course, degree);
    }
    public void updateCourseDegree(Course course) {
        if (course != null) {
            System.out.print("Enter new degree or -1 to cancel : ");
            double newDegree = Main.getValidDouble();
            while (newDegree != -1 && newDegree < course.getMinDegree() || newDegree > course.getMaxDegree()) {
                System.out.print("sorry, student degree must be in range [min degree, max degree] enter valid degree or -1 to cancel : ");
                newDegree = Main.getValidInteger();
            }
            if (newDegree == -1)
                System.out.printf("task canceled!%n");
            else {
                setDegree(course, newDegree);
                System.out.printf("Student degree updated to %.2f%n", newDegree);
            }
        }
    }

    public double calculatePercentage(){
        double percentage= 0;
        for(Map.Entry<Course ,Double> it: enrolledCourses.entrySet())
            percentage += ( (it.getValue() / it.getKey().getMaxDegree()) * 100 );
        if (enrolledCourses.isEmpty()) return 0;
        return (percentage/ enrolledCourses.size());
    }

    public void printStudentCourses(ArrayList<Course> courses) {
        if (courses != null && !courses.isEmpty()) {
            System.out.printf("Student is enrolled in : %n");
            System.out.printf("%-10s%-15s%-25s%-15s%-20s%-20s%n", "Index", "Course Id", "Course Name", "Course Level", "Student Degree", "Student Grade");
            int i = 1;
            for (Course course : courses)
                System.out.printf("%-10d%-15s%-25s%-15d%-20.2f%.2f %%%n", i++, course.getId(), course.getName(), course.getLevel(), getDegree(course), (getDegree(course) / course.getMaxDegree()) * 100);
        }
        else
            System.out.printf("student didn't enroll in any course%n");
    }

    public void printReport(){
        System.out.printf("Student username : %s%nStudent Level : %d%nStudent name : %s %s%nPhone number : %s%nAge : %d%n", getUserName(), getLevel(), getFirstName(), getLastName(), getPhoneNumber(), getAge());
        printStudentCourses(getEnrolledCourses());
        if (!enrolledCourses.isEmpty())  System.out.printf("Student Percentage = %.2f %%%n", calculatePercentage());
    }
}