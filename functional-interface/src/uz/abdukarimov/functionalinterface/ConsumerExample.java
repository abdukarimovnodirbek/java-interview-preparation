package uz.abdukarimov.functionalinterface;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ConsumerExample {
    static void main() {
        Consumer<String> print = System.out::println;
        Consumer<String> log = s -> System.out.println("[LOG] " + s);

        // andThen — zanjir: bir xil qiymatni ketma-ket iste'mol qiladi
        Consumer<String> both = print.andThen(log);
        both.accept("Xabar");
        // Output: Xabar
        //         [LOG] Xabar

        // forEach bilan
        List<Integer> nums = List.of(1, 2, 3);
        nums.forEach(n -> System.out.print(n + " ")); // 1 2 3

        // BiConsumer — ikki argument
        BiConsumer<String, Integer> show = (s, n) ->
                System.out.println(s + " = " + n);
        show.accept("Yosh", 25);
    }
}
