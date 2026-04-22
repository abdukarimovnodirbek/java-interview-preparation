package uz.abdukarimov.executer.banktransfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// ─── Transfer service ───────────────────────────────────────────
public class BankTransferService {

    private final ThreadPoolExecutor executor;
    private final Map<String, BankAccount> accounts;
    private final AtomicInteger txCounter = new AtomicInteger(1);
    private final List<TransferResult> history =
            Collections.synchronizedList(new ArrayList<>());

    public BankTransferService(Map<String, BankAccount> accounts) {
        this.accounts = accounts;
        this.executor = new ThreadPoolExecutor(
                3, 8,
                20L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50),
                r -> {
                    Thread t = new Thread(r);
                    t.setName("BankTx-" + txCounter.get());
                    return t;
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    // Async transfer — Future qaytaradi
    public Future<TransferResult> transferAsync(
            String fromId, String toId, double amount) {

        return executor.submit(() -> {
            String txId = "TX-" + String.format("%04d",
                    txCounter.getAndIncrement());
            long start = System.currentTimeMillis();

            BankAccount from = accounts.get(fromId);
            BankAccount to = accounts.get(toId);

            if (from == null || to == null) {
                return new TransferResult(txId, fromId, toId,
                        amount, false, "Hisob topilmadi",
                        System.currentTimeMillis() - start);
            }

            if (amount <= 0) {
                return new TransferResult(txId, fromId, toId,
                        amount, false, "Noto'g'ri summa",
                        System.currentTimeMillis() - start);
            }

            // Deadlock oldini olish: kichik ID ni birinchi lock qilish
            BankAccount first = fromId.compareTo(toId) < 0 ? from : to;
            BankAccount second = fromId.compareTo(toId) < 0 ? to : from;

            boolean success;
            String message;

            first.debit(0);  // lock tartibini belgilash uchun
            // Asl amallar:
            if (from.debit(amount)) {
                to.credit(amount);
                success = true;
                message = String.format(
                        "%.0f so'm %s → %s muvaffaqiyatli",
                        amount, from.getOwner(), to.getOwner());
            } else {
                success = false;
                message = String.format(
                        "%s hisobida mablag' yetarli emas (%.0f so'm)",
                        from.getOwner(), from.getBalance());
            }

            TransferResult result = new TransferResult(
                    txId, fromId, toId, amount, success, message,
                    System.currentTimeMillis() - start
            );
            history.add(result);
            return result;
        });
    }

    // Pool holati
    public void printStats() {
        System.out.println("══════════════════════════════");
        System.out.println("Pool Stats:");
        System.out.printf("  Active threads : %d%n",
                executor.getActiveCount());
        System.out.printf("  Pool size      : %d%n",
                executor.getPoolSize());
        System.out.printf("  Queue size     : %d%n",
                executor.getQueue().size());
        System.out.printf("  Completed tasks: %d%n",
                executor.getCompletedTaskCount());
        System.out.println("══════════════════════════════");
    }

    public void shutdown() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }

    public List<TransferResult> getHistory() {
        return Collections.unmodifiableList(history);
    }
}
