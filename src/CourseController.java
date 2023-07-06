import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public final class CourseController {

    private static final ArrayList<Course> courses = new ArrayList<>();
    private static final HashMap<String,Course> coursesId = new HashMap<>();

    private CourseController(){
    }

    public static void readData(){
        File file = new File("./src/Courses.txt");
        try {
            Scanner scanner= new Scanner(file);
            while (scanner.hasNextLine()){
                String [] courseData = scanner.nextLine().split(",");
                Course newCourse = new Course(courseData[0], Integer.parseInt(courseData[1]), courseData[2], courseData[3], Double.parseDouble(courseData[4]), Double.parseDouble(courseData[5]), Double.parseDouble(courseData[6]));
                courses.add(newCourse);
                coursesId.put(newCourse.getId(), newCourse);
            }
        }catch (FileNotFoundException e){
            System.out.printf("File Not Found!%n");
        }
    }

    public static ArrayList<Course> getCourses () {
        return courses;
    }
    public static HashMap<String, Course> getCoursesId(){
        return coursesId;
    }
    public static Course getCourseById(String id){
        return coursesId.get(id);
    }

    public static void addCourse() {
        String id, name, description;
        double maxDegree, minDegree, successDegree;
        int level;
        Scanner scanner = new Scanner(System.in);
        System.out.printf("To add new course : %nEnter id or -1 to cancel : ");
        id = scanner.nextLine();
        while (coursesId.containsKey(id) && !id.equals("-1")) {
            System.out.print("Sorry this id has been added before please enter another id or enter -1 to cancel : ");
            id = scanner.nextLine();
        }
        if (id.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            System.out.print("Enter course level : ");
            level = Main.getValidInteger();
            while (level <= 0 || level > 4) {
                System.out.print("course level must be in range [1 , 4] please try again : ");
                level = Main.getValidInteger();
            }

            scanner = new Scanner(System.in);
            System.out.print("Enter name : ");
            name = scanner.nextLine();

            System.out.print("Enter description : ");
            description = scanner.nextLine();

            System.out.print("Enter max degree : ");
            maxDegree = Main.getValidDouble();
            while (maxDegree <= 0){
                System.out.print("max degree must be > 0 please try again : ");
                maxDegree = Main.getValidDouble();
            }

            System.out.print("Enter min degree : ");
            minDegree = Main.getValidDouble();
            while (minDegree >= maxDegree){
                System.out.print("min degree must be < max degree please try again : ");
                minDegree = Main.getValidDouble();
            }

            System.out.print("Enter success degree : ");
            successDegree = Main.getValidDouble();
            while (successDegree > maxDegree || successDegree < minDegree){
                System.out.print("success degree must be in range [min degree, max degree] please try again : ");
                successDegree = Main.getValidDouble();
            }
            Course newCourse = new Course(id, level, name, description, maxDegree, minDegree, successDegree);
            courses.add(newCourse);
            coursesId.put(id, newCourse);
            System.out.println("---------------------------------------------------");
            System.out.printf("Course added!%n");
        }
    }

    public static void deleteCourse(Course course) {
        if (course != null) {
            coursesId.remove(course.getId());
            course.withdrawAllStudentsFromCourse();
            courses.remove(course);
            System.out.printf("Course removed!%n");
        }
    }

    public static Course selectCourse(ArrayList<Course> courses, Student student) {
        if (student != null) student.printStudentCourses(student.getEnrolledCourses());
        else printCoursesDetails(courses);
        Course course = null;
        if (courses != null && courses.size() > 0) {
            System.out.println("---------------------------------------------------");
            System.out.print("choose the course that you want or -1 to cancel : ");
            int index = Main.getValidInteger();
            while (index != -1 && index <= 0 || index > courses.size()) {
                System.out.print("please enter valid index or -1 to cancel : ");
                index = Main.getValidInteger();
            }
            if (index == -1)
                System.out.printf("task canceled!%n");
            else {
                course = courses.get(index - 1);
                System.out.println("---------------------------------------------------");
            }
        }
        return course;
    }

    public static void printCoursesDetails(ArrayList<Course> courses) {
        if (courses != null && courses.size() > 0) {
            courses.sort(Comparator.comparingInt(Course::getLevel));
            System.out.printf("%-10s%-15s%-25s%-15s%-20s%-20s%-20s%-20s%n", "Index", "Course Id", "Course Name", "Course Level", "Min Degree", "Max Degree", "Success Degree", "Number Of Students");
            int i = 1;
            for (Course course : courses)
                System.out.printf("%-10s%-15s%-25s%-15s%-20s%-20s%-20s%-20s%n", i++, course.getId(), course.getName(), course.getLevel(), course.getMinDegree(), course.getMaxDegree(), course.getSuccessDegree(), course.getNumberOfStudents());
        }
        else
            System.out.printf("There is no any course!%n");
    }

    public static void storeData(){
        try {
            FileWriter fileWriter = new FileWriter("./src/Courses.txt");
            for(Course course : courses){
                String courseData = String.format(course.getId()+ "," + course.getLevel() + "," + course.getName() + "," + course.getDescription() + "," + course.getMaxDegree() + "," + course.getMinDegree() + "," + course.getSuccessDegree() + "\n");
                fileWriter.write(courseData);
            }
            fileWriter.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}