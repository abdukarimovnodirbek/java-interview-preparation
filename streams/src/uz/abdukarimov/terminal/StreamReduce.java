package uz.abdukarimov.terminal;

import java.util.List;
import java.util.Optional;

public class StreamReduce {
    static void main() {
        List<Integer> nums = List.of(1, 2, 3, 4, 5);

        // Yig'indi (identity bilan)
        int sum = nums.stream()
                .reduce(0, Integer::sum);
        // 15

        // Ko'paytma
        int product = nums.stream()
                .reduce(1, (a, b) -> a * b);
        // 120

        // Identity siz (Optional qaytaradi)
        Optional<Integer> max = nums.stream()
                .reduce((a, b) -> a > b ? a : b);
        max.ifPresent(System.out::println); // 5

        // String birlashtirish
        List<String> words = List.of("Java", " ", "Stream", " ", "API");
        String sentence = words.stream()
                .reduce("", String::concat);
        // "Java Stream API"
    }
}
