package uz.abdukarimov.boundtypes;

import java.io.Serializable;

public class MultipleBound {
    // T ham Comparable, ham Serializable bo'lishi shart
    public static class SortableBox<T extends Comparable<T> & Serializable> {
        private T value;

        public SortableBox(T value) {
            this.value = value;
        }

        public boolean isLessThan(T other) {
            return value.compareTo(other) < 0;
        }
    }

    static void main() {
        SortableBox<Integer> sortableBox = new SortableBox<>(5);
        System.out.println(sortableBox.isLessThan(6));
    }

}
