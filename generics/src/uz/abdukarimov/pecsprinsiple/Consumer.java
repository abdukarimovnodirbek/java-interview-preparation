package uz.abdukarimov.pecsprinsiple;

import java.util.ArrayList;
import java.util.List;

public class Consumer {
    // Listga Integer YOZAMIZ
    public static void addNumbers(List<? super Integer> list) {
        list.add(1);     // ✅
        list.add(2);     // ✅
        list.add(3);     // ✅
        // Integer n = list.get(0); // ❌ faqat Object sifatida o'qisa bo'ladi
    }

    static void main() {
        List<Integer> intList = new ArrayList<>();
        List<Number> numList = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();

        addNumbers(intList);    // ✅ Integer >= Integer
        addNumbers(numList);    // ✅ Number  >= Integer
        addNumbers(objectList); // ✅ Object  >= Integer

        System.out.print(intList.stream().toList());
        // List<Double> doubleList = new ArrayList<>();
        // addNumbers(doubleList); // ❌ Double Integer'ni qabul qilmaydi
    }

}
