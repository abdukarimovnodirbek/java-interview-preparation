package uz.abdukarimov.generic;

public class MultipleTypeParameter {
    public static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    static void main() {
        Pair<String, Integer> pair = new Pair<>("age", 25);
        System.out.println(pair.getKey() + " = " + pair.getValue()); // age = 25
    }

}
