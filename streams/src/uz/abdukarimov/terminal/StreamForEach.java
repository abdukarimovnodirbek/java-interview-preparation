package uz.abdukarimov.terminal;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class StreamForEach {
    static void main() {
        List<String> names = List.of("Ali", "Vali", "Sali");

        // Chiqarish
        names.stream()
                .forEach(System.out::println);

        // Map ga forEach
        Map<String, Integer> scores = Map.of("Ali", 90, "Vali", 85);
        scores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }
}
