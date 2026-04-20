package uz.abdukarimov.terminal;

import java.util.List;
import java.util.Optional;

public class StreamOtherTerminalOperations {
    static void main() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        long total = numbers.stream().count();                         // 10
        long evenCount = numbers.stream().filter(n -> n % 2 == 0).count(); // 5

        List<Integer> nums = List.of(3, 1, 4, 1, 5, 9, 2, 6);

        // min / max
        Optional<Integer> min = nums.stream().min(Integer::compareTo); // 1
        Optional<Integer> max = nums.stream().max(Integer::compareTo); // 9

         // findFirst / findAny
        Optional<Integer> first = nums.stream()
                .filter(n -> n > 4).findFirst();  // 5

        // anyMatch / allMatch / noneMatch
        boolean hasNeg = nums.stream().anyMatch(n -> n < 0);   // false
        boolean allPos = nums.stream().allMatch(n -> n > 0);   // true
        boolean noneNeg = nums.stream().noneMatch(n -> n < 0);  // true

        // toArray
        Object[] arr = nums.stream().toArray();
        Integer[] typedArr = nums.stream().toArray(Integer[]::new);
    }
}
