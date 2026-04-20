package uz.abdukarimov.intermediate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamFlatMap {
    static void main() {
        // map() vs flatMap() farqi:
        List<List<Integer>> nested = List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6),
                List.of(7, 8, 9)
        );

        // map() — List<Stream> qaytaradi (noto'g'ri)
        Stream<Stream<Integer>> wrong = nested.stream()
                .map(List::stream);

        // flatMap() — yassilaydi (to'g'ri)
        List<Integer> flat = nested.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        // [1, 2, 3, 4, 5, 6, 7, 8, 9]

        // Amaliy misol — so'zlarni harflarga bo'lish
        List<String> sentences = List.of("Hello World", "Java Stream");

        List<String> words = sentences.stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .collect(Collectors.toList());
        // [Hello, World, Java, Stream]

        // Har bir harfni olish
        List<String> letters = sentences.stream()
                .flatMap(s -> Arrays.stream(s.split("")))
                .filter(l -> !l.equals(" "))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
