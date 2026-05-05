package uz.abdukarimov;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class SequencedCollectionsDemo {
    static void main() {
// Java 21 — List, Set, Map uchun yagona interfeys
// Birinchi va oxirgi elementga bir xil API

        List<String> list = List.of("a", "b", "c");

// YANGI SequencedCollection metodlari:
        list.getFirst();   // "a"  (List.get(0) o'rniga)
        list.getLast();    // "c"  (List.get(size-1) o'rniga)
        list.reversed();   // ["c", "b", "a"]

// LinkedHashSet ham ishlatadi:
        LinkedHashSet<Integer> set = new LinkedHashSet<>(List.of(1, 2, 3));
        set.getFirst(); // 1
        set.getLast();  // 3

// SequencedMap:
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.firstEntry();  // a=1
        map.lastEntry();   // b=2
        map.reversed();    // b=2, a=1
    }
}
