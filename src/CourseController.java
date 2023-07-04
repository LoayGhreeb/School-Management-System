import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
                Course newCourse = new Course(courseData[0], courseData[1], courseData[2], Double.parseDouble(courseData[3]), Double.parseDouble(courseData[4]), Double.parseDouble(courseData[5]));
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

    public void addCourse(){
        String id, name,description;
        double maxDegree, minDegree, successDegree;
        char addNew= 'y';
        while(addNew == 'y') {
            Scanner scanner = new Scanner(System.in);
            System.out.printf("To add new course : %nEnter id or -1 to cancel : ");
            id = scanner.nextLine();
            while (coursesId.containsKey(id) && !id.equals("-1")) {
                System.out.print("Sorry this id has been added before please enter another id or enter -1 to cancel : ");
                id = scanner.nextLine();
            }
            if (id.equals("-1")) {
                System.out.printf("task canceled!%n");
                addNew = 'n';
            }
            else {
                System.out.print("Enter name : ");
                name = scanner.nextLine();

                System.out.print("Enter description : ");
                description = scanner.nextLine();

                System.out.print("Enter max degree : ");
                maxDegree = scanner.nextDouble();

                System.out.print("Enter min degree : ");
                minDegree = scanner.nextDouble();

                System.out.print("Enter success degree : ");
                successDegree = scanner.nextDouble();

                try {
                    Course newCourse = new Course(id, name, description, maxDegree, minDegree, successDegree);
                    courses.add(newCourse);
                    coursesId.put(id, newCourse);
                    System.out.printf("Course added!%n");

                } catch (IllegalArgumentException e) {
                    System.out.printf("%s %s%n","Course can't be added! " , e.getMessage());
                }

                System.out.print("You want to add another course ? y or n : ");
                addNew = validResponse();
            }
            System.out.println("---------------------------------------------------");
        }
    }

    public void deleteCourse(){
        char delete= 'y';
        while (delete == 'y') {
            if (courses.size() > 0) {
                Course selectedCourse = selectCourse(courses, null);
                if (selectedCourse == null)
                    delete = 'n';

                else {
                    coursesId.remove(selectedCourse.getId());
                    selectedCourse.removeAllStudentsFromCourse();
                    courses.remove(selectedCourse);

                    System.out.printf("Course removed!%n");
                    if (courses.size() > 0) {
                        System.out.print("Delete another course ? y or n  : ");
                        delete = validResponse();
                    }
                    else {
                        System.out.printf("there is no course left you can't perform any delete operation%n");
                        delete = 'n';
                    }
                }
            }
            else{
                System.out.printf("there is no course left you can't perform any delete operation%n");
                delete = 'n';
            }
            System.out.println("---------------------------------------------------");
        }
    }

    public void modifyCourse() {
        char update = 'y';
        while (update == 'y') {
            if (courses.size() > 0) {

                Course selectedCourse = selectCourse(courses, null);

                if (selectedCourse == null)
                    update = 'n';
                else {
                    char sameCourse = 'y';
                    while (sameCourse == 'y') {
                        System.out.printf("Course update operations : %n"+
                                "1- Update course id%n" +
                                "2- Update course name%n" +
                                "3- Update course description %n" +
                                "4- Update course max degree%n" +
                                "5- Update course min degree%n" +
                                "6- Update course success degree%n" +
                                "7- cancel%n" +
                                "Please choose what you want to update : ");

                        int updateOp = validChoice(7);
                        System.out.println("---------------------------------------------------");

                        if (updateOp == 7) {
                            System.out.printf("task canceled!%n");
                            sameCourse = 'n';
                        }
                        else {
                            if (updateOp == 1)
                                updateCourseId(selectedCourse);

                            else if (updateOp == 2)
                                updateCourseName(selectedCourse);

                            else if (updateOp == 3)
                                updateCourseDescription(selectedCourse);

                            else if (updateOp == 4)
                                updateCourseMaxDegree(selectedCourse);

                            else if (updateOp == 5)
                                updateCourseMinDegree(selectedCourse);

                            else
                                updateCourseSuccessDegree(selectedCourse);

                            System.out.print("you want to update other data for the selected course ? y or n : ");
                            sameCourse = validResponse();
                        }
                    }
                    System.out.println("---------------------------------------------------");
                    System.out.print("You want to update another course ? y or n : ");
                    update = validResponse();
                }
            }
            else{
                System.out.printf("sorry there is no courses!%n");
                update= 'n';
            }
            System.out.println("---------------------------------------------------");
        }
    }

    public void storeData(){
        try {
            FileWriter fileWriter = new FileWriter("./src/Courses.txt");
            for(Course course : courses){
                String courseData = String.format(course.getId()+ "," + course.getName() + "," + course.getDescription() + "," + course.getMaxDegree() + "," + course.getMinDegree() + "," + course.getSuccessDegree() + "\n");
                fileWriter.write(courseData);
            }
            fileWriter.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void viewStudentsRegisteredInCourse(){
        char view = 'y';
        while (view == 'y') {
            if (courses.size() > 0) {
                Course selectedCourse = selectCourse(courses, null);
                if (selectedCourse == null)
                    view= 'n';
                else {
                    selectedCourse.viewStudents();
                    System.out.print("you want to view students for another course ? y or n : ");
                    view = validResponse();
                }
            }
            else{
                System.out.printf("sorry there is no courses!%n");
                view = 'n';
            }
            System.out.println("---------------------------------------------------");
        }
    }
    public static Course selectCourse(ArrayList<Course> courses, Student student){
        printCoursesDetails(courses, student);
        System.out.print("choose the course that you want or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();
        while (index != -1 && index <= 0 || index > courses.size()) {
            System.out.print("please enter valid index : ");
            index = scanner.nextInt();
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

    private void updateCourseName(Course course){
        System.out.print("Enter the new name or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        String newName = scanner.nextLine();
        if (newName.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            course.setName(newName);
            System.out.printf("course name updated to %s%n", newName);
        }
        System.out.println("---------------------------------------------------");
    }

    private void updateCourseDescription(Course course){
        System.out.print("Enter the new description or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        String newDescription = scanner.nextLine();
        if (newDescription.equals("-1"))
            System.out.printf("task canceled!%n");
        else {
            course.setDescription(newDescription);
            System.out.printf("course description updated to %s%n", newDescription);
        }
        System.out.println("---------------------------------------------------");
    }

    private void updateCourseMaxDegree(Course course){
        System.out.print("Enter the new max degree or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        double newMaxDegree = scanner.nextDouble();
        if (newMaxDegree == -1)
            System.out.printf("task canceled!%n");
        else {
            try {
                course.setMaxDegree(newMaxDegree);
                System.out.printf("max degree updated to %.2f%n", newMaxDegree);

            } catch (IllegalArgumentException e) {
                System.out.printf("can't update max degree! %s%n", e.getMessage());
            }
        }
        System.out.println("---------------------------------------------------");
    }

    private void updateCourseMinDegree(Course course){
        System.out.print("Enter the new min degree or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        double newMinDegree = scanner.nextDouble();
        if (newMinDegree == -1)
            System.out.printf("task canceled!%n");
        else {
            try {
                course.setMinDegree(newMinDegree);
                System.out.printf("min degree updated to %.2f%n", newMinDegree);
            } catch (IllegalArgumentException e) {
                System.out.printf("can't update min degree! %s%n", e.getMessage());
            }
        }
        System.out.println("---------------------------------------------------");
    }

    private void updateCourseSuccessDegree(Course course){
        System.out.print("Enter the new success degree or -1 to cancel : ");
        Scanner scanner = new Scanner(System.in);
        double newSuccessGrade = scanner.nextDouble();
        if (newSuccessGrade == -1)
            System.out.printf("task canceled!%n");
        else {
            try {
                course.setSuccessDegree(newSuccessGrade);
                System.out.printf("success degree updated to %.2f%n", newSuccessGrade);

            } catch (IllegalArgumentException e) {
                System.out.printf("can't update success degree! %s%n", e.getMessage());
            }
        }
        System.out.println("---------------------------------------------------");
    }

    public static void printCoursesName(){
        System.out.printf("All courses : %n");
        for(int i= 0; i < courses.size(); i++)
            System.out.printf("%d- %s%n", i + 1, courses.get(i).getName());
        System.out.println("---------------------------------------------------");
    }

    public static void printCoursesDetails(ArrayList<Course> courses, Student student){
        if (student == null)
            System.out.printf("%-10s%-15s%-25s%-20s%-20s%-20s%-20s%n", "Index", "Course Id", "Course Name", "Min Degree", "Max Degree", "Success Degree", "Number Of Students");
        else
            System.out.printf("%-10s%-15s%-25s%-20s%-20s%n", "Index", "Course Id", "Course Name","Student Degree", "Student Grade");
        int i= 1;
        for(Course course : courses)
            if (student == null)
                System.out.printf("%-10d%-15s%-25s%-20s%-20s%-20s%-20s%n", i++, course.getId(), course.getName(), course.getMinDegree(), course.getMaxDegree(), course.getSuccessDegree(), course.getNumberOfStudents());

            else
                System.out.printf("%-10d%-15s%-25s%-20.2f%.2f %%%n", i++, course.getId(), course.getName(), student.getDegree(course), (student.getDegree(course)/course.getMaxDegree())*100 );

        System.out.println("---------------------------------------------------");
    }

    private int validChoice(int max){
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        while (choice <= 0 || choice > max) {
            System.out.print("please enter valid choice : ");
            choice = scanner.nextInt();
        }
        return choice;
    }

    private char validResponse(){
        Scanner scanner = new Scanner(System.in);
        char response = scanner.next().charAt(0);
        while (response != 'y' && response != 'n') {
            System.out.print("please enter y or n  : ");
            response = scanner.next().charAt(0);
        }
        return response;
    }
}