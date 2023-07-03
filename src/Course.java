import java.util.ArrayList;

public class Course {
    private String id, name, description;
    private double maxDegree, minDegree, successDegree;
    private final ArrayList<Student> students= new ArrayList<>();

    public Course(String id, String name, String description, double maxDegree, double minDegree, double successDegree) {
        if (maxDegree < 0 || maxDegree < minDegree )
            throw new IllegalArgumentException("max degree must be in range [min degree , inf]");
        if (minDegree < 0)
            throw new IllegalArgumentException("min degree must be in range [0, max degree]");
        if (successDegree > maxDegree || successDegree < minDegree )
            throw new IllegalArgumentException("success degree must be in range [min degree, max degree]");

        this.id = id;
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMaxDegree() {
        return maxDegree;
    }

    public void setMaxDegree(double maxDegree) {
        if (maxDegree < minDegree)
            throw new IllegalArgumentException("max degree must be in range [min degree , inf] ");
        this.maxDegree = maxDegree;
    }

    public double getMinDegree() {
        return minDegree;
    }

    public void setMinDegree(double minDegree) {
        if (minDegree < 0 || minDegree > maxDegree)
            throw new IllegalArgumentException("min degree must be in range [0, max degree]");
        this.minDegree = minDegree;
    }

    public double getSuccessDegree() {
        return successDegree;
    }

    public void setSuccessDegree(double successDegree) {
        if (successDegree > maxDegree || successDegree < minDegree)
            throw new IllegalArgumentException("success degree must be in range [min degree, max degree]");
        this.successDegree = successDegree;
    }

    public int getNumberOfStudents(){
        return students.size();
    }

    public void addStudentToCourse(Student student){
        students.add(student);
    }

    public void removeAllStudentsFromCourse(){
        for(Student student : students){
            student.removeCourse(this);
        }
    }

    public void printReport(){
        System.out.printf("Course Id : %s%nCourse name : %s%nCourse description: %s%nMax degree : %.2f%nMin degree : %.2f%nSuccess degree: %.2f%n", getId(), getName(), getDescription(), getMaxDegree(), getMinDegree(), getSuccessDegree());
        System.out.println("---------------------------------------------------");
    }
}