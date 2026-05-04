package uz.abdukarimov.memoryleak;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

// Sabab 3 — Listener/Observer ro'yxatdan chiqarmaslik
public class MemoryLeak3 {
    // ❌ MUAMMO: Listener qo'shiladi, hech qachon olib tashlanmaydi
    public class EventBus {
        private static List<EventListener> listeners = new ArrayList<>();

        public static void register(EventListener l) {
            listeners.add(l); // ob'ekt tirik qoladi — GC olmaydi!
        }
        // unregister() metodi chaqirilmasa → leak
    }

    // ✅ YECHIM 1: WeakReference listener list
    private static List<WeakReference<EventListener>> listeners =
            new ArrayList<>();

    public static void register(EventListener l) {
        listeners.add(new WeakReference<>(l));
        // GC kerak bo'lsa WeakReference ni tozalaydi
    }

    // ✅ YECHIM 2: AutoCloseable pattern
    public class MyComponent implements AutoCloseable {
        public MyComponent() {
            //EventBus.register(this::onEvent);
        }

        @Override
        public void close() {
            //EventBus.unregister(this::onEvent); // cleanup
        }
    }
}
// try-with-resources bilan:
/*try (MyComponent c = new MyComponent()) {
        // ishlash
    } // close() avtomatik chaqiriladi
}*/
