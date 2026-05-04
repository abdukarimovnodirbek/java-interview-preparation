package uz.abdukarimov.memoryleak;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// Sabab 2 — HashMap da to'g'ri hashCode/equals yo'q
public class MemoryLeak2 {

    // ❌ MUAMMO: hashCode/equals override qilinmagan klass
    public class BadKey {
        String id;
        BadKey(String id) { this.id = id; }
        // hashCode() va equals() — yo'q!
    }

     void mainMethod() {
        Map<BadKey, String> map = new HashMap<>();
        for (int i = 0; i < 100_000; i++) {
            //map.put(new BadKey("key"), "value"); // har safar YANGI key!
        }
     // map.size() = 100_000 — hech biri bir xil emas!
    }

    // ✅ YECHIM: hashCode va equals ni to'g'ri implement qilish
    public class GoodKey {
        String id;
        GoodKey(String id) { this.id = id; }

        @Override public int hashCode()  { return Objects.hash(id); }
        @Override public boolean equals(Object o) {
            return o instanceof GoodKey gk && Objects.equals(id, gk.id);
        }
    }

}
