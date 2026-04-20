package uz.abdukarimov.strealpipelines;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Second {
    static void main() {
        List<First.Student> students = List.of(
                new First.Student("Ali", 92, "Toshkent"),
                new First.Student("Vali", 78, "Samarkand"),
                new First.Student("Sali", 95, "Toshkent"),
                new First.Student("Jamshid", 88, "Buxoro"),
                new First.Student("Dilnoza", 95, "Toshkent"),
                new First.Student("Malika", 72, "Samarkand")
        );

        Map<String, Double> avgByCity = students.stream()
                .collect(Collectors.groupingBy(
                        First.Student::city,                          // shahar bo'yicha guruhlash
                        Collectors.averagingInt(First.Student::grade) // o'rtacha ball
                ));

        // Natijani tartiblangan chiqarish
        avgByCity.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(e -> System.out.printf("%-12s → %.1f%n", e.getKey(), e.getValue()));

        // Toshkent    → 89.0
        // Buxoro      → 88.0
        // Samarkand   → 75.0
    }
}
