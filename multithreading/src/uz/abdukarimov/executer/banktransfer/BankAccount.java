package uz.abdukarimov.executer.banktransfer;

import java.util.concurrent.locks.ReentrantLock;

// ─── Model ──────────────────────────────────────────────────────
public class BankAccount {
    private final String id;
    private final String owner;
    private volatile double balance;
    private final ReentrantLock lock = new ReentrantLock();

    public BankAccount(String id, String owner, double balance) {
        this.id      = id;
        this.owner   = owner;
        this.balance = balance;
    }

    public boolean debit(double amount) {
        lock.lock();
        try {
            if (balance < amount) return false;
            Thread.sleep(10); // bank tekshiruvi simulate
            balance -= amount;
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void credit(double amount) {
        lock.lock();
        try {
            Thread.sleep(10); // kredit simulate
            balance += amount;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    // Getterlar
    public String getId()      { return id; }
    public String getOwner()   { return owner; }
    public double getBalance() { return balance; }
}