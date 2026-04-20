package uz.abdukarimov.pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    static void main(String[] args) {

        // Turli xil Pair yaratish
        Pair<String, Integer> person = Pair.of("Ali", 25);
        Pair<Double, Boolean> result = Pair.of(3.14, true);
        Pair<String, String> coords = Pair.of("41.2995° N", "69.2401° E");

        System.out.println(person);              // (Ali, 25)
        System.out.println(person.getFirst());   // Ali
        System.out.println(person.getSecond());  // 25

        // Swap
        Pair<Integer, String> swapped = person.swap();
        System.out.println(swapped);             // (25, Ali)

        // Map — transform
        Pair<String, Integer> upper = person.mapFirst(String::toUpperCase);
        System.out.println(upper);               // (ALI, 25)

        Pair<String, String> ageStr = person.mapSecond(Object::toString);
        System.out.println(ageStr);              // (Ali, 25)

        // Pair listini saralash
        List<Pair<String, Integer>> students = new ArrayList<>();
        students.add(Pair.of("Vali", 88));
        students.add(Pair.of("Ali", 95));
        students.add(Pair.of("Sali", 72));

        // Ball bo'yicha saralash:
        students.sort(Comparator.comparing(Pair::getSecond));
        students.forEach(System.out::println);
        // (Sali, 72)
        // (Vali, 88)
        // (Ali, 95)

        // Pair ni Map ga aylantirish:
        Map<String, Integer> map = person.toMap();
        System.out.println(map); // {Ali=25}
    }
}
