package uz.abdukarimov.atomics;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConnectionPool {

    // ── Connection modeli ──────────────────────────────────────
    public static class Connection {
        private final int id;
        private final long createdAt = System.currentTimeMillis();
        private volatile boolean inUse = false;

        public Connection(int id) {
            this.id = id;
        }

        public void execute(String query) throws InterruptedException {
            System.out.printf("[Conn-%d] '%s' bajarilmoqda...%n", id, query);
            Thread.sleep(200 + (long) (Math.random() * 300));
            System.out.printf("[Conn-%d] '%s' tugadi%n", id, query);
        }

        public int getId() {
            return id;
        }

        public boolean isInUse() {
            return inUse;
        }

        public void setInUse(boolean b) {
            inUse = b;
        }
    }

    // ── Connection Pool ────────────────────────────────────────
    private final Semaphore semaphore;
    private final Queue<Connection> pool = new ConcurrentLinkedQueue<>();
    private final int maxSize;
    private final AtomicInteger totalRequests = new AtomicInteger();
    private final AtomicInteger rejectedRequests = new AtomicInteger();
    private final AtomicLong totalWaitTime = new AtomicLong();

    public ConnectionPool(int maxConnections) {
        this.maxSize = maxConnections;
        this.semaphore = new Semaphore(maxConnections, true); // fair=true

        // Connectionlarni oldindan yaratish
        for (int i = 1; i <= maxConnections; i++) {
            pool.offer(new Connection(i));
        }
        System.out.println("Pool yaratildi: " + maxConnections + " ta connection");
    }

    // ── Connection olish ───────────────────────────────────────
    public Connection acquire() throws InterruptedException {
        totalRequests.incrementAndGet();
        long waitStart = System.currentTimeMillis();

        semaphore.acquire(); // permit bo'lguncha kutadi

        long waited = System.currentTimeMillis() - waitStart;
        totalWaitTime.addAndGet(waited);

        Connection conn = pool.poll();
        if (conn != null) {
            conn.setInUse(true);
            System.out.printf("[Pool] Connection-%d berildi " +
                            "(permits qoldi: %d, kutish: %dms)%n",
                    conn.getId(), semaphore.availablePermits(), waited);
        }
        return conn;
    }

    // ── Timeout bilan olish ────────────────────────────────────
    public Connection tryAcquire(long timeout, TimeUnit unit)
            throws InterruptedException {
        totalRequests.incrementAndGet();

        if (!semaphore.tryAcquire(timeout, unit)) {
            rejectedRequests.incrementAndGet();
            System.out.println("[Pool] Timeout! Connection berilmadi");
            return null;
        }

        Connection conn = pool.poll();
        if (conn != null) conn.setInUse(true);
        return conn;
    }

    // ── Connection qaytarish ───────────────────────────────────
    public void release(Connection conn) {
        if (conn != null) {
            conn.setInUse(false);
            pool.offer(conn);
            semaphore.release(); // permit qaytarish
            System.out.printf("[Pool] Connection-%d qaytarildi " +
                            "(permits: %d)%n",
                    conn.getId(), semaphore.availablePermits());
        }
    }

    // ── Statistika ─────────────────────────────────────────────
    public void printStats() {
        int total = totalRequests.get();
        int rejected = rejectedRequests.get();
        long avgWait = total > 0 ? totalWaitTime.get() / total : 0;

        System.out.println("\n=== Pool Statistika ===");
        System.out.printf("Jami so'rovlar  : %d%n", total);
        System.out.printf("Rad etilganlar  : %d%n", rejected);
        System.out.printf("O'rtacha kutish : %dms%n", avgWait);
        System.out.printf("Mavjud permits  : %d/%d%n",
                semaphore.availablePermits(), maxSize);
        System.out.printf("Kutayotganlar   : %d%n",
                semaphore.getQueueLength());
    }

    // ── Main — simulatsiya ─────────────────────────────────────
    static void main() throws InterruptedException {

        ConnectionPool dbPool = new ConnectionPool(3); // 3 ta connection
        ExecutorService workers = Executors.newFixedThreadPool(10);
        CountDownLatch allDone = new CountDownLatch(10);

        System.out.println("\n=== 10 ta parallel so'rov, 3 ta connection ===\n");

        for (int i = 1; i <= 10; i++) {
            final int workerId = i;
            workers.submit(() -> {
                try {
                    System.out.printf("Worker-%d: connection so'rayapti%n",
                            workerId);

                    Connection conn = dbPool.tryAcquire(2, TimeUnit.SECONDS);

                    if (conn == null) {
                        System.out.printf("Worker-%d: connection olmadi!%n",
                                workerId);
                        return;
                    }

                    try {
                        conn.execute("SELECT * FROM orders WHERE user=" +
                                workerId);
                    } finally {
                        dbPool.release(conn); // har doim finally da!
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    allDone.countDown();
                }
            });
        }

        allDone.await();
        dbPool.printStats();
        workers.shutdown();
    }
}
