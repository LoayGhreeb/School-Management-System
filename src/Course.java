import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Course {
    private String id, name, description;
    private double maxDegree, minDegree, successDegree;
    private int level;
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
    public void updateCourseId(){
        HashMap<String ,Course> coursesId = CourseController.getCoursesId();
        System.out.print("Enter the new id or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        String newId = scanner.nextLine();
        while(coursesId.containsKey(newId) && !newId.equals("-1")){
            System.out.print("Sorry this id has been added before please enter another id or enter -1 to cancel : ");
            newId= scanner.nextLine();
        }
        if (newId.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            coursesId.put(newId, this);
            coursesId.remove(getId());
            setId(newId);
            System.out.printf("course id updated to %s%n", newId);
        }
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
    }

    public int getNumberOfStudents(){
        return enrolledStudents.size();
    }

    public void modifyCourse() {
        System.out.printf("Course update operations : %n" +
                "1- Update course id%n" +
                "2- Update course name%n" +
                "3- Update course description %n" +
                "4- Update course max degree%n" +
                "5- Update course min degree%n" +
                "6- Update course success degree%n" +
                "7- Update course level%n" +
                "8- Go back%n" +
                "Please choose what you want to update : ");

        int updateOp = Main.validChoice(8);
        System.out.println("---------------------------------------------------");

        if (updateOp == 1)
            updateCourseId();

        else if (updateOp == 2)
            updateName();

        else if (updateOp == 3)
            updateDescription();

        else if (updateOp == 4)
            updateMaxDegree();

        else if (updateOp == 5)
            updateMinDegree();

        else if (updateOp == 6)
            updateSuccessDegree();

        else if (updateOp == 7)
            updateLevel();

        else if (updateOp == 8)
            return;

        System.out.println("---------------------------------------------------");
        modifyCourse();
    }

    public void enrollStudentInCourse(Student student){
        enrolledStudents.add(student);
    }
    public void withdrawStudent(Student student){
        enrolledStudents.remove(student);
    }
    public void withdrawAllStudentsFromCourse(){
        while (enrolledStudents.size() > 0)
            enrolledStudents.get(0).withdrawFromCourse(this, false);
    }
    public void viewEnrolledStudents() {
        if (enrolledStudents.size() > 0) {
            System.out.printf("Students enrolled in %s course : %n", getName());
            StudentController.printStudentsDetails(enrolledStudents);
        }
        else
            System.out.printf("There are no students enrolled in this course!%n");
    }
}