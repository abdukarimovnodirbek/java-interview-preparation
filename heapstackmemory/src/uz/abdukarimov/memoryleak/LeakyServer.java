package uz.abdukarimov.memoryleak;

/**
 * MASHQ: Quyidagi kodda 5 ta memory leak bor.
 */
public class LeakyServer {
/*
    // LEAK 1: ???
    private static Map<String, Session> sessions = new HashMap<>();

    // LEAK 2: ???
    private static List<RequestLog> allLogs = new ArrayList<>();

    // LEAK 3: ???
    private ThreadLocal<DatabaseConnection> dbConn = new ThreadLocal<>();

    // LEAK 4: ???
    private List<Runnable> shutdownHooks = new ArrayList<>();

    public void handleRequest(String sessionId, String data) {
        // LEAK 1 amali:
        sessions.put(sessionId, new Session(data)); // eski sessionlar hech qachon o'chirilmaydi

        // LEAK 2 amali:
        allLogs.add(new RequestLog(data)); // log o'sib ketadi

        // LEAK 3 amali:
        dbConn.set(new DatabaseConnection());
        processData(data);
        // dbConn.remove() yo'q!

        // LEAK 4 amali:
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("cleanup"); // har request uchun hook!
        }));
    }

    // LEAK 5: ???
    class RequestProcessor {
        // Non-static — LeakyServer.this ni ushlab turadi
        void process(String data) {
        }
    }
}
*/

// ════════════════════════════════════════════════════════════
// TUZATILGAN VERSIYA:
// ════════════════════════════════════════════════════════════
/*
public class FixedServer {

    // FIX 1: TTL bilan cache → eski sessionlar avtomatik o'chadi
    private final Cache<String, Session> sessions = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

    // FIX 2: Circular buffer — hajm cheklangan
    private final Deque<RequestLog> recentLogs =
            new ArrayDeque<>(1000); // max 1000 ta

    // FIX 3: ThreadLocal (to'g'ri ishlatiladi)
    private final ThreadLocal<DatabaseConnection> dbConn =
            new ThreadLocal<>();

    // FIX 4: Bir marta qo'shilgan shutdown hook
    public FixedServer() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));
    }

    public void handleRequest(String sessionId, String data) {
        // FIX 1:
        sessions.put(sessionId, new Session(data));

        // FIX 2:
        if (recentLogs.size() >= 1000) recentLogs.pollFirst();
        recentLogs.addLast(new RequestLog(data));

        // FIX 3: try-finally bilan remove()
        dbConn.set(new DatabaseConnection());
        try {
            processData(data);
        } finally {
            dbConn.remove(); // MUHIM!
        }
    }

    private void cleanup() {
        System.out.println("Server yopilmoqda...");
    }

    // FIX 5: Static inner class
    static class RequestProcessor {
        void process(String data) { }
    }
    */
}