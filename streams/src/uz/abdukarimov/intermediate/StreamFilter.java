package uz.abdukarimov.intermediate;

import java.util.List;
import java.util.stream.Collectors;

public class StreamFilter {
    static void main() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Juft sonlar
        List<Integer> evens = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        // [2, 4, 6, 8, 10]

        // Bir nechta filter (AND mantiq)
        List<Integer> result = numbers.stream()
                .filter(n -> n > 3)
                .filter(n -> n % 2 != 0)
                .collect(Collectors.toList());
        // [5, 7, 9]
    }
}
