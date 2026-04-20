package uz.abdukarimov.pecsprinsiple;

import java.util.ArrayList;
import java.util.List;

public class Example {
    // src dan O'QIB, dest ga YOZAMIZ
    public static <T> void copy(List<? extends T> src,   // Producer  — extends
                                List<? super T> dest     // Consumer  — super
    ) {
        dest.addAll(src);
    }

    static void main() {
        List<Integer> source = List.of(1, 2, 3);
        List<Number> destination = new ArrayList<>();

        copy(source, destination);
        System.out.println(destination); // [1, 2, 3]
    }

}
