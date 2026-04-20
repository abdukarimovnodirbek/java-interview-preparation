package uz.abdukarimov.intermediate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamSorted {
    record Student(String name, int grade, String city) {
    }

    static void main() {
        List<Integer> nums = List.of(5, 3, 8, 1, 9, 2);

        // Natural order
        List<Integer> asc = nums.stream()
                .sorted()
                .collect(Collectors.toList());
        // [1, 2, 3, 5, 8, 9]

        // Teskari
        List<Integer> desc = nums.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        // [9, 8, 5, 3, 2, 1]

        List<Student> students = List.of(
                new Student("Ali", 92, "Toshkent"),
                new Student("Vali", 78, "Samarkand"),
                new Student("Sali", 95, "Toshkent"),
                new Student("Jamshid", 88, "Buxoro"),
                new Student("Dilnoza", 95, "Toshkent"),
                new Student("Malika", 72, "Samarkand")
        );

        // Object field bo'yicha saralash
        List<Student> byGrade = students.stream()
                .sorted(Comparator.comparing(Student::grade).reversed())
                .collect(Collectors.toList());
        // [Sali(92), Ali(90), Vali(85)]

        // Ko'p maydon bo'yicha
        List<Student> multiSort = students.stream()
                .sorted(Comparator.comparing(Student::grade)
                        .thenComparing(Student::name))
                .collect(Collectors.toList());
    }
}
