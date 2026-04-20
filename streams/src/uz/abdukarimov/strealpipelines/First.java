package uz.abdukarimov.strealpipelines;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class First {
    record Student(String name, int grade, String city) {
    }

    static void main() {
        List<Student> students = List.of(
                new Student("Ali", 92, "Toshkent"),
                new Student("Vali", 78, "Samarkand"),
                new Student("Sali", 95, "Toshkent"),
                new Student("Jamshid", 88, "Buxoro"),
                new Student("Dilnoza", 95, "Toshkent"),
                new Student("Malika", 72, "Samarkand")
        );

        List<String> top3 = students.stream()
                .sorted(Comparator.comparing(Student::grade).reversed()) // ball bo'yicha sort
                .limit(3)                                                 // dastlabki 3
                .map(s -> s.name() + " (" + s.grade() + ")")             // formatlash
                .collect(Collectors.toList());

        System.out.println(top3);
        // [Sali (95), Dilnoza (95), Ali (92)]
    }
}
