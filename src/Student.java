import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Student {

    private String firstName, lastName, phoneNumber;
    private String userName, password;
    private int level, age;
    private HashMap<Course , Double> enrolledCourses;

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
        this.enrolledCourses = new HashMap<>();
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
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
        System.out.println("---------------------------------------------------");
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
        System.out.println("---------------------------------------------------");
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
        System.out.println("---------------------------------------------------");
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
        System.out.println("---------------------------------------------------");
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
        System.out.println("---------------------------------------------------");
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
        System.out.println("---------------------------------------------------");
    }

    public HashMap<Course , Double> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(HashMap<Course ,Double> enrolledCourses){
        this.enrolledCourses = enrolledCourses;
    }

    public double getDegree(Course course){
        return enrolledCourses.get(course);
    }

    public void setDegree(Course course, double degree){
        enrolledCourses.put(course, degree);
    }

    public void withdrawFromCourse(Course course){
        course.withdrawStudent(this);
        enrolledCourses.remove(course);
    }
    public void withdrawFromAllCourses(){
        for(Course course : enrolledCourses.keySet())
            withdrawFromCourse(course);
    }
    public double calculatePercentage(){
        double percentage= 0;
        for(Map.Entry<Course ,Double> it: enrolledCourses.entrySet())
            percentage += ( (it.getValue() / it.getKey().getMaxDegree()) * 100 );
        if (enrolledCourses.size() == 0) return 0;
        return (percentage/ enrolledCourses.size());
    }

    public void printStudentCourses() {
        int i =1;
        if (enrolledCourses.size() > 0) {
            System.out.printf("Student Courses: %n");
            System.out.printf("%-10s%-15s%-30s%-15s%-20s%-20s%n", "Index", "Course Id", "Course Name", "Course Level", "Student Degree", "Student Grade");
            for (Map.Entry<Course, Double> it : enrolledCourses.entrySet()) {
                System.out.printf("%-10d%-15s%-30s%-15d%-20.2f%.2f %%%n", i++, it.getKey().getId(), it.getKey().getName(), it.getKey().getLevel(), it.getValue(), (it.getValue()/it.getKey().getMaxDegree())*100);
            }
        }
        else
            System.out.printf("Student didn't enroll in any course%n");
        System.out.println("---------------------------------------------------");
    }

    public void printReport(){
        System.out.printf("Student username : %s%nStudent Level : %d%nStudent name : %s %s%nPhone number : %s%nAge : %d%n", getUserName(), getLevel(), getFirstName(), getLastName(), getPhoneNumber(), getAge());
        printStudentCourses();
        if (enrolledCourses.size() > 0) System.out.printf("Student Percentage = %.2f %%%n", calculatePercentage());
        System.out.println("---------------------------------------------------");
    }
}