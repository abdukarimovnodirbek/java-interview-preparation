package uz.abdukarimov.functionalinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SupplierExample {
    static void main() {
        Supplier<String> greeting = () -> "Salom, dunyo!";
        Supplier<List<?>> newList = ArrayList::new;
        Supplier<Double> random = Math::random;

        System.out.println(greeting.get()); // "Salom, dunyo!"

        // Lazy initialization — faqat kerak bo'lganda yaratiladi
        //Supplier<HeavyObject> lazy = () -> new HeavyObject(); // hali yaratilmagan
        //HeavyObject obj = lazy.get(); // endi yaratiladi

        // orElseGet bilan kombinatsiya
        Optional<String> opt = Optional.empty();
        String result = opt.orElseGet(greeting); // "Salom, dunyo!"
    }
}
