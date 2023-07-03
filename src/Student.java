import java.util.HashMap;
import java.util.Map;

public class Student {

    private String firstName, lastName, phoneNumber;
    private String userName, password;
    private int age;
    private HashMap<Course , Double> courses;

    public Student(String userName, String password, String firstName, String lastName, String phoneNumber, int age){
        if(age <= 0 )
            throw new IllegalArgumentException("age must be > 0");
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber= phoneNumber;
        this.age = age;
        this.courses = new HashMap<>();
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age <= 0)
            throw new IllegalArgumentException("age must be > 0");
        this.age = age;
    }

    public HashMap<Course , Double> getCourses() {
        return courses;
    }

    public void setCourses(HashMap<Course ,Double> courses){
        this.courses = courses;
    }

    public double getDegree(Course course){
        return courses.get(course);
    }

    public void setDegree(Course course, double degree){
        courses.put(course, degree);
    }

    public void removeCourse(Course course){
        courses.remove(course);
    }

    public double calculatePercentage(){
        double percentage= 0;
        for(Map.Entry<Course ,Double> it: courses.entrySet())
            percentage += ( (it.getValue() / it.getKey().getMaxDegree()) * 100 );
        if (courses.size() == 0) return 0;
        return (percentage/courses.size());
    }

    public void printStudentCourses() {
        int i =1;
        if (courses.size() > 0) {
            System.out.printf("Student Courses: %n");
            System.out.printf("%-10s%-15s%-30s%-20s%-20s%n", "Index", "Course Id", "Course Name", "Degree", "Grade");
            for (Map.Entry<Course, Double> it : courses.entrySet()) {
                System.out.printf("%-10d%-15s%-30s%-20.2f%.2f %%%n", i++, it.getKey().getId(), it.getKey().getName(), it.getValue(), (it.getValue()/it.getKey().getMaxDegree())*100);
            }
        }
    }

    public void printReport(){
        System.out.printf("Student username : %s%nStudent name : %s %s%nPhone number : %s%nAge : %d%n", getUserName(), getFirstName(), getLastName(), getPhoneNumber(), getAge());
        printStudentCourses();
        if (courses.size() > 0) System.out.printf("Student Percentage = %.2f %%%n", calculatePercentage());
        System.out.println("------------------------------------------");
    }
}