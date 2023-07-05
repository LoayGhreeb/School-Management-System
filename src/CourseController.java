import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class CourseController {

    private static final ArrayList<Course> courses = new ArrayList<>();
    private static final HashMap<String,Course> coursesId = new HashMap<>();

    public CourseController(){
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

    public static Course getCourseById(String id){
        return coursesId.get(id);
    }

    public void addCourse() {
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
            System.out.print("Enter course level or -1 to cancel : ");
            level = Main.getValidInteger();
            while (level != -1 && (level <= 0 || level > 4)) {
                System.out.print("course level must be in range [1 , 4] please try again or -1 to cancel : ");
                level = Main.getValidInteger();
            }
            if (level == -1)
                System.out.printf("task canceled%n");
            else {
                scanner = new Scanner(System.in);
                System.out.print("Enter name : ");
                name = scanner.nextLine();

                System.out.print("Enter description : ");
                description = scanner.nextLine();

                System.out.print("Enter max degree : ");
                maxDegree = Main.getValidDouble();

                System.out.print("Enter min degree : ");
                minDegree = Main.getValidDouble();

                System.out.print("Enter success degree : ");
                successDegree = Main.getValidDouble();

                try {
                    Course newCourse = new Course(id, level, name, description, maxDegree, minDegree, successDegree);
                    courses.add(newCourse);
                    coursesId.put(id, newCourse);
                    System.out.printf("Course added!%n");

                } catch (IllegalArgumentException e) {
                    System.out.printf("%s %s%n", "Course can't be added! ", e.getMessage());
                }
            }
        }
        System.out.println("---------------------------------------------------");
        Main.adminManageCourses();
    }

    public void deleteCourse(Course course) {
        if (course != null) {
            coursesId.remove(course.getId());
            course.withdrawAllStudentsFromCourse();
            courses.remove(course);
            System.out.printf("Course removed!%n");
        }
        System.out.println("---------------------------------------------------");
        Main.adminManageCourses();
    }

    public void modifyCourse(Course course) {
        if (course != null) {
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

            if (updateOp == 8)
               Main.adminManageCourses();

            else {
                if (updateOp == 1)
                    updateCourseId(course);

                else if (updateOp == 2)
                    course.updateName();

                else if (updateOp == 3)
                    course.updateDescription();

                else if (updateOp == 4)
                    course.updateMaxDegree();

                else if (updateOp == 5)
                    course.updateMinDegree();

                else if (updateOp == 6)
                    course.updateSuccessDegree();

                else
                    course.updateLevel();

                modifyCourse(course);
            }
        }
        System.out.println("---------------------------------------------------");
    }

    public void storeData(){
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

    public void viewEnrolledStudentsInCourse() {
        if (courses.size() > 0) {
            Course selectedCourse = selectCourse(courses);
            if (selectedCourse != null)
                selectedCourse.viewEnrolledStudents();
        }
    }

    public static Course selectCourse(ArrayList<Course> courses, Student student){
        printCoursesDetails(courses, student);
        return select(courses);
    }
    public static Course selectCourse(ArrayList<Course> courses){
       printCoursesDetails(courses);
        return select(courses);
    }
    private static Course select(ArrayList<Course> courses) {
        System.out.print("choose the course that you want or -1 to cancel : ");
        int index = Main.getValidInteger();
        while (index != -1 && index <= 0 || index > courses.size()) {
            System.out.print("please enter valid index or -1 to cancel : ");
            index = Main.getValidInteger();
        }
        System.out.println("---------------------------------------------------");
        if (index == -1) {
            System.out.printf("task canceled!%n");
            return null;
        }
        else return courses.get(index -1);
    }

    private void updateCourseId(Course course){
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
            coursesId.put(newId, course);
            coursesId.remove(course.getId());
            course.setId(newId);
            System.out.printf("course id updated to %s%n", newId);
        }
        System.out.println("---------------------------------------------------");
    }

    public static void printCoursesDetails(ArrayList<Course> courses, Student student){
        courses.sort(Comparator.comparingInt(Course::getLevel));
        if (courses.size() > 0) {
                System.out.printf("%-10s%-15s%-25s%-15s%-20s%-20s%n", "Index", "Course Id", "Course Name", "Course Level", "Student Degree", "Student Grade");
            int i = 1;
            for (Course course : courses)
                System.out.printf("%-10d%-15s%-25s%-15d%-20.2f%.2f %%%n", i++, course.getId(), course.getName(), course.getLevel(), student.getDegree(course), (student.getDegree(course) / course.getMaxDegree()) * 100);
        }
        else
            System.out.printf("There is no any course!%n");
        System.out.println("---------------------------------------------------");
    }
    public static void printCoursesDetails(ArrayList<Course> courses){
        courses.sort(Comparator.comparingInt(Course::getLevel));
        if (courses.size() > 0) {
            System.out.printf("%-10s%-15s%-25s%-15s%-20s%-20s%-20s%-20s%n", "Index", "Course Id", "Course Name", "Course Level", "Min Degree", "Max Degree", "Success Degree", "Number Of Students");
            int i = 1;
            for (Course course : courses)
                System.out.printf("%-10s%-15s%-25s%-15s%-20s%-20s%-20s%-20s%n", i++, course.getId(), course.getName(), course.getLevel(), course.getMinDegree(), course.getMaxDegree(), course.getSuccessDegree(), course.getNumberOfStudents());
        }
        System.out.println("---------------------------------------------------");
    }
}