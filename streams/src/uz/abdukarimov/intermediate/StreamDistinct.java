package uz.abdukarimov.intermediate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamDistinct {
    static void main() {
        List<Integer> withDups = List.of(1, 2, 2, 3, 3, 3, 4, 4, 5);

        List<Integer> unique = withDups.stream()
                .distinct()
                .collect(Collectors.toList());
        // [1, 2, 3, 4, 5]

        // Boshqa intermediate bilan birgalikda
        List<Integer> result = withDups.stream()
                .filter(n -> n > 2)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        // [5, 4, 3]


        List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6);

        // limit() — dastlabki N ta
        List<Integer> first3 = numbers.stream()
                .limit(3)
                .collect(Collectors.toList()); // [1, 2, 3]

        // skip() — dastlabki N tasini o'tkazib yuborish
        List<Integer> skip3 = numbers.stream()
                .skip(3)
                .collect(Collectors.toList()); // [4, 5, 6, 7, 8, 9, 10]

        // peek() — debug uchun (elementni o'zgartirmaydi)
        List<Integer> peeked = numbers.stream()
                .filter(n -> n % 2 == 0)
                .peek(n -> System.out.println("Filtered: " + n))
                .map(n -> n * 2)
                .peek(n -> System.out.println("Mapped: " + n))
                .collect(Collectors.toList());
    }
}
