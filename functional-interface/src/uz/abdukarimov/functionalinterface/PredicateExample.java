package uz.abdukarimov.functionalinterface;

import java.util.List;
import java.util.function.Predicate;

public class PredicateExample {
    static void main() {
        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isLong = s -> s.length() > 5;
        Predicate<Integer> isEven = n -> n % 2 == 0;

        // Mantiqiy operatorlar
        Predicate<String> notEmpty = isEmpty.negate();          // !
        Predicate<String> both = isEmpty.and(isLong);       // &&
        Predicate<String> either = isEmpty.or(isLong);        // ||

        // Amaliy misol — filterda ishlatish
        List<String> names = List.of("Ali", "Shahzod", "Bob", "Murod");
        names.stream()
                .filter(isLong)
                .forEach(System.out::println); // "Shahzod", "Murod"

        // Predicate.not — Java 11+
        names.stream()
                .filter(Predicate.not(String::isEmpty))
                .forEach(System.out::println);
    }
}
