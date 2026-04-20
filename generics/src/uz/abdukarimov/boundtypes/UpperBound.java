package uz.abdukarimov.boundtypes;

public class UpperBound {
    // Faqat Number va uning subclasslari: Integer, Double, Long...
    public static class NumberBox<T extends Number> {
        private T value;

        public NumberBox(T value) {
            this.value = value;
        }

        public double doubled() {
            return value.doubleValue() * 2; // Number metodlarini ishlatsa bo'ladi!
        }

        public boolean isGreaterThan(T other) {
            return value.doubleValue() > other.doubleValue();
        }
    }

    // Generic metod bilan Upper Bound
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static void main() {

        NumberBox<Integer> intBox = new NumberBox<>(10);
        NumberBox<Double> doubleBox = new NumberBox<>(3.14);
        // NumberBox<String> strBox  = new NumberBox<>("Hi"); // Compile xato! ❌

        System.out.println(intBox.doubled());           // 20.0
        System.out.println(intBox.isGreaterThan(5));    // true

        System.out.println(max(10, 20));        // 20
        System.out.println(max("Apple", "Zebra")); // Zebra
    }
}