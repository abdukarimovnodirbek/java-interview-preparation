package uz.abdukarimov.strealpipelines;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Third {
    static void main() {
        String text = "java stream java api stream java lambda stream api";

        Map<String, Long> wordFreq = Arrays.stream(text.split(" "))
                .collect(Collectors.groupingBy(
                        word -> word,               // so'zni kalit qilib
                        Collectors.counting()       // necha marta uchragan
                ));

        // Eng ko'p ishlatiladiganlarini chiqarish
        wordFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        // java:   3
        // stream: 3
        // api:    2
    }
}
