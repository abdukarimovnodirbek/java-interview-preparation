package uz.abdukarimov.optional;

import java.util.NoSuchElementException;
import java.util.Optional;

public class OptionalExample {
    static void main() {
        // === Yaratish usullari ===
        Optional<String> full = Optional.of("Java");        // null bo'lsa NPE
        Optional<String> safe = Optional.ofNullable(null);  // null bo'lsa empty
        Optional<String> empty = Optional.empty();           // bo'sh

        // === Qiymat olish ===
        String val1 = full.get();                         // "Java" — bo'sh bo'lsa exception
        String val2 = safe.orElse("default");             // "default"
        //String val3 = safe.orElseGet(() -> compute());    // lazy — faqat kerak bo'lsa chaqiriladi
        String val4 = safe.orElseThrow(NoSuchElementException::new);

        // === Tekshirish ===
        if (full.isPresent()) {
            System.out.println(full.get());
        }
        full.ifPresent(System.out::println);  // cleaner variant

        // === map va flatMap ===
        Optional<Integer> length = full.map(String::length);           // Optional[4]
        Optional<String> upper = full.map(String::toUpperCase);      // Optional["JAVA"]

        // === filter ===
        Optional<String> result = full.filter(s -> s.startsWith("J")); // Optional["Java"]
    }
}
