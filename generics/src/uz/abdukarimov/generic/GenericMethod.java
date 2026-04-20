package uz.abdukarimov.generic;

import java.util.ArrayList;
import java.util.List;

public class GenericMethod {
    public static class Utils {
        // Static generic metod
        public static <T> void swap(T[] arr, int i, int j) {
            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        public static <T> List<T> repeat(T item, int times) {
            List<T> result = new ArrayList<>();
            for (int i = 0; i < times; i++) result.add(item);
            return result;
        }
    }

    static void main() {
        // Ishlatish
        Integer[] nums = {1, 2, 3, 4};
        Utils.swap(nums, 0, 3);  // {4, 2, 3, 1}

        List<String> repeated = Utils.repeat("Hi", 3); // [Hi, Hi, Hi]
        System.out.println(repeated);
    }
}
