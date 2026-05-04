package uz.abdukarimov.memoryleak;

// Sabab 1 — Static kolleksiya
public class MemoryLeak1 {
/*
    // ❌ MUAMMO: static List ga qo'shib, hech narsa o'chirilmaydi
    public class UserCache {
        private static final List<User> ALL_USERS = new ArrayList<>();

        public void addUser(User u) {
            ALL_USERS.add(u);  // GC hech qachon tozalay olmaydi!
        }
    }

    // ✅ YECHIM: WeakReference yoki hajm chegarasi
    private static final int MAX = 1000;

    public void addUser(User u) {
        if (ALL_USERS.size() >= MAX) ALL_USERS.remove(0);
        ALL_USERS.add(u);
    }

    // Yoki cache framework: Caffeine, Guava Cache
    Cache<String, User> cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
*/
}
