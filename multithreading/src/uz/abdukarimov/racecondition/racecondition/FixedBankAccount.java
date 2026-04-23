package uz.abdukarimov.racecondition.racecondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

// ── TUZATILGAN KOD ────────────────────────────────────────────
public class FixedBankAccount {
    private volatile double balance;                        // ✅ 5: volatile
    private final List<String> transactions =
            Collections.synchronizedList(new ArrayList<>());   // ✅ 3: thread-safe list
    private static final AtomicInteger totalAccounts =
            new AtomicInteger(0);                               // ✅ 1: atomic

    public FixedBankAccount(double initial) {
        this.balance = initial;
        totalAccounts.incrementAndGet();   // ✅ atomic increment
    }

    public synchronized void deposit(double amount) { // ✅ 2: synchronized
        if (amount > 0) {
            balance += amount;
            transactions.add("+" + amount);
        }
    }

    public synchronized boolean withdraw(double amount) { // ✅ 4: atomic check-act
        if (balance >= amount) {           // lock ostida tekshiriladi
            balance -= amount;             // lock ostida bajariladi
            transactions.add("-" + amount);
            return true;
        }
        return false;
    }

    // Yoki ReentrantLock bilan:
    private final ReentrantLock lock = new ReentrantLock();

    public boolean withdrawWithLock(double amount)
            throws InterruptedException {
        if (lock.tryLock(1, TimeUnit.SECONDS)) {
            try {
                if (balance >= amount) {
                    balance -= amount;
                    transactions.add("-" + amount);
                    return true;
                }
                return false;
            } finally {
                lock.unlock();
            }
        }
        throw new RuntimeException("Lock olinmadi");
    }

    public double getBalance() {
        return balance;
    }

    public static int getTotalAccounts() {
        return totalAccounts.get();
    }
}