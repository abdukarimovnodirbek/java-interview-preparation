package uz.abdukarimov;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamExample {
    static void main() {
        List<String> names = List.of("Ali", "Vali", "Sali");

        // Eski usul (imperative):
        List<String> result1 = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("A")) {
                result1.add(name.toUpperCase());
            }
        }

        // Stream usuli (declarative):
        List<String> result2 = names.stream()
                .filter(name -> name.startsWith("A"))
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println(result1);
        System.out.println(result2);
    }
}
