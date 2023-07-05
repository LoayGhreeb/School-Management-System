import java.util.ArrayList;
import java.util.Scanner;

public class Course {
    private String id, name, description;
    private double maxDegree, minDegree, successDegree;
    int level;
    private final ArrayList<Student> enrolledStudents= new ArrayList<>();

    public Course(String id, int level, String name, String description, double maxDegree, double minDegree, double successDegree) {
        if (maxDegree < 0 || maxDegree < minDegree )
            throw new IllegalArgumentException("max degree must be in range [min degree , inf]");
        if (minDegree < 0)
            throw new IllegalArgumentException("min degree must be in range [0, max degree]");
        if (successDegree > maxDegree || successDegree < minDegree )
            throw new IllegalArgumentException("success degree must be in range [min degree, max degree]");
        if (level <= 0 || level > 4)
            throw new IllegalArgumentException("level must be in range [1,4]");

        this.id = id;
        this.name = name;
        this.level = level;
        this.description = description;
        this.maxDegree = maxDegree;
        this.minDegree = minDegree;
        this.successDegree = successDegree;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void updateName(){
        System.out.print("Enter new name or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        String newName = scanner.nextLine();
        if (newName.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            setName(newName);
            System.out.printf("course name updated to %s%n", newName);
        }
        System.out.println("---------------------------------------------------");
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void updateDescription(){
        System.out.print("Enter the new description or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        String newDescription = scanner.nextLine();
        if (newDescription.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            setDescription(newDescription);
            System.out.printf("course description updated to %s%n", newDescription);
        }
        System.out.println("---------------------------------------------------");
    }

    public double getMaxDegree() {
        return maxDegree;
    }
    public void setMaxDegree(double maxDegree) {
        this.maxDegree = maxDegree;
    }
    public void updateMaxDegree(){
        System.out.print("Enter new max degree or -1 to cancel : ");
        double newMaxDegree = Main.getValidDouble();
        while (newMaxDegree != -1 && (newMaxDegree < 0 || newMaxDegree < minDegree)) {
            System.out.printf("max degree must be in range [min degree , inf]%n try again or -1 to cancel : ");
            newMaxDegree = Main.getValidDouble();
        }
        if (newMaxDegree == -1)
            System.out.printf("task canceled!%n");
        else {
            setMaxDegree(newMaxDegree);
            System.out.printf("max degree updated to %.2f%n", newMaxDegree);
        }
        System.out.println("---------------------------------------------------");
    }

    public double getMinDegree() {
        return minDegree;
    }
    public void setMinDegree(double minDegree) {
        this.minDegree = minDegree;
    }
    public void updateMinDegree(){
        System.out.print("Enter new min degree or -1 to cancel : ");
        double newMinDegree = Main.getValidDouble();
        while (newMinDegree != -1 && (newMinDegree < 0 || newMinDegree > maxDegree)) {
            System.out.printf("min degree must be in range [0 , max degree]%n try again or -1 to cancel : ");
            newMinDegree = Main.getValidDouble();
        }
        if (newMinDegree == -1)
            System.out.printf("task canceled!%n");
        else {
            setMinDegree(newMinDegree);
            System.out.printf("min degree updated to %.2f%n", newMinDegree);
        }
        System.out.println("---------------------------------------------------");
    }

    public double getSuccessDegree() {
        return successDegree;
    }
    public void setSuccessDegree(double successDegree) {
        this.successDegree = successDegree;
    }
    public void updateSuccessDegree(){
        System.out.print("Enter the new success degree or -1 to cancel : ");
        double newSuccessDegree = Main.getValidDouble();
        while (newSuccessDegree != -1 && (newSuccessDegree < minDegree || newSuccessDegree > maxDegree)) {
            System.out.printf("success degree degree must be in range [min degree , max degree]%n try again or -1 to cancel : ");
            newSuccessDegree = Main.getValidDouble();
        }
        if (newSuccessDegree == -1)
            System.out.printf("task canceled!%n");
        else {
            setSuccessDegree(newSuccessDegree);
            System.out.printf("success degree degree updated to %.2f%n", newSuccessDegree);
        }
        System.out.println("---------------------------------------------------");
    }

    public int getLevel(){
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void updateLevel() {
        System.out.printf("Be careful, all students enrolled in this course will be withdrawn%nEnter new level or -1 to cancel : ");
        int newLevel = Main.getValidInteger();
        while (newLevel != -1 && (newLevel <= 0 || newLevel > 4)) {
            System.out.print("course level must be in range [1 , 4] please try again or -1 to cancel : ");
            newLevel = Main.getValidInteger();
        }
        if (newLevel == -1){
            System.out.printf("task canceled%n");
        }
        else {
            withdrawAllStudentsFromCourse();
            setLevel(newLevel);
            System.out.printf("course level updated to %d%n", newLevel);
        }
        System.out.println("---------------------------------------------------");
    }

    public int getNumberOfStudents(){
        return enrolledStudents.size();
    }

    public void enrollStudentToCourse(Student student){
        enrolledStudents.add(student);
    }

    public void withdrawStudent(Student student){
        enrolledStudents.remove(student);
    }

    public void withdrawAllStudentsFromCourse(){
        for(Student student : enrolledStudents){
            student.withdrawFromCourse(this);
            enrolledStudents.remove(student);
        }
    }

    public void viewEnrolledStudents() {
        if (enrolledStudents.size() > 0) {
            System.out.printf("Students enrolled in %s course : %n", getName());
            System.out.printf("%-10s%-25s%-20s%-20s%-20s%-20s%n", "Index", "Student Username", "Student Name", "Student Level", "Student Degree", "Student Grade");
            int index = 1;
            for (Student student : enrolledStudents)
                System.out.printf("%-10d%-25s%-20s%-20d%-20.2f%.2f%%%n", index++, student.getUserName(), student.getFirstName() + " " + student.getLastName(), student.getLevel(), student.getDegree(this), (student.getDegree(this) / getMaxDegree()) * 100);
        }
        else
            System.out.printf("There are no students enrolled for this course!%n");
        System.out.println("---------------------------------------------------");
    }

    public void printReport(){
        System.out.printf("Course Id : %s%nCourse Level : %d%nCourse name : %s%nCourse description: %s%nMax degree : %.2f%nMin degree : %.2f%nSuccess degree: %.2f%n", getId(), getLevel(), getName(), getDescription(), getMaxDegree(), getMinDegree(), getSuccessDegree());
        System.out.println("---------------------------------------------------");
    }
}