package uz.abdukarimov.pecsprinsiple;

import java.util.List;

public class Producer {
    // Sonlar yig'indisi — listdan faqat O'QIYMIZ
    public static double sum(List<? extends Number> list) {
        double total = 0;
        for (Number n : list) {      // Number sifatida o'qisa bo'ladi ✅
            total += n.doubleValue();
        }
        // list.add(1.5); // ❌ yozib bo'lmaydi!
        return total;
    }

    static void main() {
        List<Integer> ints = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.1, 2.2, 3.3);
        List<Long> longs = List.of(10L, 20L, 30L);

        System.out.println(sum(ints));    // 6.0
        System.out.println(sum(doubles)); // 6.6
        System.out.println(sum(longs));   // 60.0
    }
}
