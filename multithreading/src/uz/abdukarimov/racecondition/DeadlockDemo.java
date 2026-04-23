package uz.abdukarimov.racecondition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockDemo {

    private final Object lockA = new Object();
    private final Object lockB = new Object();

    // ❌ DEADLOCK — T1: A→B, T2: B→A
    public void thread1Task() {
        synchronized (lockA) {              // A ni oldi
            System.out.println("T1: A oldi, B kutmoqda");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            synchronized (lockB) {          // B ni kutayapti — DEADLOCK!
                System.out.println("T1: B oldi");
            }
        }
    }

    public void thread2Task() {
        synchronized (lockB) {              // B ni oldi
            System.out.println("T2: B oldi, A kutmoqda");
            synchronized (lockA) {          // A ni kutayapti — DEADLOCK!
                System.out.println("T2: A oldi");
            }
        }
    }

    // ✅ YECHIM 1 — bir xil tartibda lock olish
    public void safeThread1() {
        synchronized (lockA) {      // har doim A → B
            synchronized (lockB) {
                System.out.println("Safe T1: A va B olindi");
            }
        }
    }

    public void safeThread2() {
        synchronized (lockA) {      // har doim A → B (tartib bir xil!)
            synchronized (lockB) {
                System.out.println("Safe T2: A va B olindi");
            }
        }
    }

    // ✅ YECHIM 2 — tryLock timeout bilan
    ReentrantLock rlA = new ReentrantLock();
    ReentrantLock rlB = new ReentrantLock();

    public void safeWithTryLock() throws InterruptedException {
        while (true) {
            if (rlA.tryLock(100, TimeUnit.MILLISECONDS)) {
                try {
                    if (rlB.tryLock(100, TimeUnit.MILLISECONDS)) {
                        try {
                            System.out.println("Ikkala lock olindi!");
                            return;
                        } finally {
                            rlB.unlock();
                        }
                    }
                } finally {
                    rlA.unlock();
                }
            }
            Thread.sleep(10); // bir oz kuting va qayta urining
        }
    }
}
