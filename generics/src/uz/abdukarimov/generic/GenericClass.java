package uz.abdukarimov.generic;

public class GenericClass {
    public static class Box<T> {
        private T value;

        public Box(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Box[" + value + "]";
        }
    }

    static void main() {
        Box<String> strBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);
        Box<Double> dblBox = new Box<>(3.14);

        System.out.println(strBox.get());  // Hello
        System.out.println(intBox.get());  // 42
    }
}
