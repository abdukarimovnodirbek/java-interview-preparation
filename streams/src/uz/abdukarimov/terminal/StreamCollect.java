package uz.abdukarimov.terminal;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamCollect {
    static void main() {
        List<String> names = List.of("Ali", "Vali", "Sali", "Ali", "Vali");

        // → List
        List<String> toList = names.stream()
                .distinct()
                .collect(Collectors.toList());

        // → Set
        Set<String> toSet = names.stream().collect(Collectors.toSet());

        // → String join
        String joined = names.stream()
                .distinct()
                .collect(Collectors.joining(", ", "[", "]"));
        // [Ali, Vali, Sali]

        // → Map
        Map<String, Integer> nameLength = names.stream()
                .distinct()
                .collect(Collectors.toMap(
                        name -> name,           // key
                        String::length          // value
                ));
        // {Ali=3, Vali=4, Sali=4}

        // → Grouping
        Map<Integer, List<String>> grouped = names.stream()
                .collect(Collectors.groupingBy(String::length));
        // {3=[Ali, Ali], 4=[Vali, Sali, Vali]}

        // → Counting per group
        Map<Integer, Long> countByLength = names.stream()
                .collect(Collectors.groupingBy(
                        String::length,
                        Collectors.counting()
                ));
        // {3=2, 4=3}

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // → Partitioning (true/false bo'lish)
        Map<Boolean, List<Integer>> parts = numbers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        // {false=[1,3,5,7,9], true=[2,4,6,8,10]}
    }
}
