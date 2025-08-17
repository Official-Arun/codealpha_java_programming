import java.util.ArrayList;
import java.util.Scanner;

class Student {
    String name;
    double grade;

    // Constructor
    Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }
}

public class StudentGradesManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        System.out.println("=== Student Grades Manager ===");
        System.out.print("Enter number of students: ");
        int n = sc.nextInt();
        sc.nextLine(); // consume newline

        // Input student data
        for (int i = 0; i < n; i++) {
            System.out.print("Enter student name: ");
            String name = sc.nextLine();

            System.out.print("Enter grade for " + name + ": ");
            double grade = sc.nextDouble();
            sc.nextLine(); // consume newline

            students.add(new Student(name, grade));
        }

        // Calculate statistics
        double sum = 0;
        double highest = Double.MIN_VALUE;
        double lowest = Double.MAX_VALUE;
        String highestStudent = "", lowestStudent = "";

        for (Student s : students) {
            sum += s.grade;
            if (s.grade > highest) {
                highest = s.grade;
                highestStudent = s.name;
            }
            if (s.grade < lowest) {
                lowest = s.grade;
                lowestStudent = s.name;
            }
        }

        double average = sum / students.size();

        // Display report
        System.out.println("\n=== Summary Report ===");
        for (Student s : students) {
            System.out.println("Student: " + s.name + " | Grade: " + s.grade);
        }

        System.out.println("\nAverage Score: " + average);
        System.out.println("Highest Score: " + highest + " (by " + highestStudent + ")");
        System.out.println("Lowest Score: " + lowest + " (by " + lowestStudent + ")");

        sc.close();
    }
}
