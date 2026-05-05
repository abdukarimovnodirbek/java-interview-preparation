package uz.abdukarimov;

import java.util.ArrayList;
import java.util.HashMap;

public class VarDemo {
    static void main() {
// var — compiler type ni o'zi aniqlaydi
        var list = new ArrayList<String>();  // ArrayList<String>
        var map = new HashMap<String, Integer>(); // HashMap<String,Integer>

// for loop da ham ishlaydi
        for (var entry : map.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

// Lambda da ISHLAMAYDI:
// var fn = (x) -> x * 2; // ❌ — lambda type yo'q

// Primitive da ishlaydi:
        var x = 42;       // int
        var pi = 3.14;    // double

/*// Qachon ishlatmaslik:
        var r = getResult(); // ❌ — type noaniq, o'qilish qiyinlashadi
        Result r = getResult(); // ✅ — aniq
        */
    }
}
