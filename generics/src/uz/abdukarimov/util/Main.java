package uz.abdukarimov.util;

import uz.abdukarimov.generic.MultipleTypeParameter;

import java.util.List;
import java.util.Map;

public class Main {
    void main() {

        List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6);
        List<String> words = List.of("apple", "banana", "avocado", "cherry", "blueberry");

        // MAX
        System.out.println(GenericUtils.max(numbers)); // 9
        System.out.println(GenericUtils.max(words));   // cherry

        // FILTER
        List<Integer> evens = GenericUtils.filter(numbers, n -> n % 2 == 0);
        System.out.println(evens); // [4, 2, 6]

        List<String> aWords = GenericUtils.filter(words, w -> w.startsWith("a"));
        System.out.println(aWords); // [apple, avocado]

        // MAP
        List<String> strNums = GenericUtils.map(numbers, String::valueOf);
        System.out.println(strNums); // [3, 1, 4, 1, 5, 9, 2, 6]

        List<Integer> lengths = GenericUtils.map(words, String::length);
        System.out.println(lengths); // [5, 6, 7, 6, 9]

        // ZIP
        List<String> names = List.of("Ali", "Vali", "Sali");
        List<Integer> scores = List.of(95, 87, 92);
        List<MultipleTypeParameter.Pair<String, Integer>> zipped = GenericUtils.zip(names, scores);
        zipped.forEach(p -> System.out.println(p.getKey() + " → " + p.getValue()));
        // Ali → 95 | Vali → 87 | Sali → 92

        // GROUP BY
        Map<Integer, List<String>> byLength = GenericUtils.groupBy(words, String::length);
        System.out.println(byLength);
        // {5=[apple], 6=[banana, cherry], 7=[avocado], 9=[blueberry]}

        // FLATTEN
        List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4), List.of(5, 6));
        System.out.println(GenericUtils.flatten(nested)); // [1, 2, 3, 4, 5, 6]
    }
}
