package uz.abdukarimov.memoryleak;

public class MemoryLeak4 {
/*
    // ❌ MUAMMO: ThreadPool bilan ThreadLocal → thread qayta ishlatilsa
    // eski qiymat saqlanib qoladi!
    private static ThreadLocal<HeavyObject> threadLocal =
            new ThreadLocal<>();

    // Servlet/Request Handler:
    public void handleRequest(Request req) {
        threadLocal.set(new HeavyObject(req)); // set qilinadi
        process();
        // threadLocal.remove() unutildi!
        // Thread pool dagi thread → keyingi request uchun qo'llaniladi
        // HeavyObject saqlanib qoladi → LEAK!
    }

    // ✅ YECHIM: har doim try-finally da remove()
    public void handleRequest(Request req) {
        threadLocal.set(new HeavyObject(req));
        try {
            process();
        } finally {
            threadLocal.remove(); // MUHIM!
        }
    }
*/
}
