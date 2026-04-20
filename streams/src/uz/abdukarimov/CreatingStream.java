package uz.abdukarimov;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CreatingStream {
    static void main() {
        // 1. Collection dan
        List<String> list = List.of("A", "B", "C");
        Stream<String> s1 = list.stream();
        Stream<String> s2 = list.parallelStream();   // parallel

        // 2. Array dan
        Stream<Integer> s3 = Arrays.stream(new Integer[]{1, 2, 3});

        // 3. of() bilan
        Stream<String> s4 = Stream.of("X", "Y", "Z");

        // 4. Range (IntStream)
        IntStream range = IntStream.range(1, 6);       // 1,2,3,4,5
        IntStream rangeCl = IntStream.rangeClosed(1, 5); // 1,2,3,4,5

        // 5. generate / iterate
        Stream<Double> randoms = Stream.generate(Math::random).limit(5);
        Stream<Integer> evens = Stream.iterate(0, n -> n + 2).limit(6);
        // 0, 2, 4, 6, 8, 10
    }
}
