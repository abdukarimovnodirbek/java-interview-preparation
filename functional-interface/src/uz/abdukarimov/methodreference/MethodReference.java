package uz.abdukarimov.methodreference;

import java.util.ArrayList;
import java.util.function.*;

public class MethodReference {
    static void main() {
        // 1. Static method reference
        Function<String, Integer> parse = Integer::parseInt;
        // = s -> Integer.parseInt(s)

        // 2. Instance method — muayyan object
        String prefix = "Hello";
        Predicate<String> startsWithHello = prefix::startsWith;
        // = s -> prefix.startsWith(s)   ❌ NOTO'G'RI!
        // = s -> s.startsWith(prefix)   ✅ TO'G'RI

        Consumer<String> printer = System.out::println;
        // = s -> System.out.println(s)

        // 3. Instance method — ixtiyoriy object (type bo'yicha)
        Function<String, String> upper = String::toUpperCase;
        Function<String, Integer> len = String::length;
        Predicate<String> empty = String::isEmpty;
        // = s -> s.toUpperCase()
        // = s -> s.length()

        // 4. Constructor reference
        Supplier<ArrayList<String>> newList = ArrayList::new;
        Function<Integer, int[]> newArray = int[]::new;
        BiFunction<String, Integer, StringBuilder> newSB = (str, cap) -> new StringBuilder(str).append(" ".repeat(cap));
        // = () -> new ArrayList<>()
        // = n -> new int[n]
    }
}
