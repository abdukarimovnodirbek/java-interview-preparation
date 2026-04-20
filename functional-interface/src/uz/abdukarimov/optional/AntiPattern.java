package uz.abdukarimov.optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class AntiPattern {
//    // ❌ Anti-pattern 2: Optional ni field sifatida ishlatish
//    public static class User {
//        private Optional<String> name;  // NOTO'G'RI — Serializable emas!
//    }
//
//    // ✅ To'g'ri: oddiy null yoki @Nullable annotatsiya
//    public static class User1 {
//        private String name; // null bo'lishi mumkin
//    }
//
//    static void main() {
//        // ❌ Anti-pattern 1: isPresent() + get() — eski uslub
//        if (optional.isPresent()) {
//            return optional.get();  // bu null tekshirishdan farqi yo'q!
//        }
//
//// ✅ To'g'ri: orElse yoki map ishlatish
//        return optional.orElse("default");
//
//// ❌ Anti-pattern 3: Collection ichida Optional
//        List<Optional<String>> list = new ArrayList<>();  // KERAKSIZ murakkablik
//
//// ✅ To'g'ri: bo'sh qiymatlarni to'g'ridan-to'g'ri filter bilan olib tashlash
//        List<String> names = list.stream()
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//// ❌ Anti-pattern 4: orElse ichida og'ir hisob
//        String val = optional.orElse(heavyDatabaseCall()); // har doim chaqiriladi!
//
//// ✅ To'g'ri: orElseGet lazy baholaydi
//        String val = optional.orElseGet(() -> heavyDatabaseCall()); // faqat kerak bo'lganda
//
//// ❌ Anti-pattern 5: Optional<Optional<T>>
//        Optional<Optional<String>> nested = Optional.of(Optional.of("hi")); // mantiqsiz
//
//// ✅ To'g'ri: flatMap ishlatish
//        Optional<String> flat = nestedOptional.flatMap(o -> o);
//    }
}
