package uz.abdukarimov.intermediate;

import java.util.List;
import java.util.stream.Collectors;

public class StreamMap {
    static void main() {
        List<String> names = List.of("ali", "vali", "sali");

        // Katta harfga o'tkazish
        List<String> upper = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        // [ALI, VALI, SALI]

        // String → Integer
        List<String> strNums = List.of("1", "2", "3", "4");
        List<Integer> ints = strNums.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        // [1, 2, 3, 4]

        // Object field ni olish
        record Student(String name, int grade) {
        }

        List<Student> students = List.of(
                new Student("Ali", 90),
                new Student("Vali", 85),
                new Student("Sali", 92)
        );

        List<String> studentNames = students.stream()
                .map(Student::name)
                .collect(Collectors.toList());
        // [Ali, Vali, Sali]
    }
}
