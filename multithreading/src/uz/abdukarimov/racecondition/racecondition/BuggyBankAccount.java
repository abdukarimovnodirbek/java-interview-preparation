package uz.abdukarimov.racecondition.racecondition;

import java.util.ArrayList;
import java.util.List;

// ── MUAMMOLI KOD — 5 ta xato toping ──────────────────────────
public class BuggyBankAccount {
    private double balance;
    private List<String> transactions = new ArrayList<>();
    private static int totalAccounts = 0;

    public BuggyBankAccount(double initial) {
        this.balance = initial;
        totalAccounts++;                    // ❌ 1: race condition (static)
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;              // ❌ 2: atomic emas
            transactions.add("+" + amount); // ❌ 3: ArrayList thread-safe emas
        }
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {            // ❌ 4: check-then-act (race)
            balance -= amount;              // boshqa thread bu orada o'zgartirishi mumkin!
            transactions.add("-" + amount);
            return true;
        }
        return false;
    }

    public double getBalance() {
        return balance;
    } // ❌ 5: volatile emas
}