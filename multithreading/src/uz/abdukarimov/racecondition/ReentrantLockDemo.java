package uz.abdukarimov.racecondition;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    // ── Asosiy ishlatish ───────────────────────────────────────
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock(); // finally da bo'lishi SHART!
        }
    }

    // ── tryLock — kutmasdan sinash ─────────────────────────────
    public boolean tryIncrement() {
        if (lock.tryLock()) {           // darhol qaytadi
            try {
                count++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false; // lock band — kutmadi
    }

    // ── tryLock timeout bilan ──────────────────────────────────
    public boolean timedIncrement() throws InterruptedException {
        if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
            try {
                count++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false; // 500ms kutdi, olmadi
    }

    // ── lockInterruptibly — interrupt ga javob beradi ──────────
    public void interruptibleIncrement() throws InterruptedException {
        lock.lockInterruptibly(); // interrupt bo'lsa — exception
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    // ── Condition — wait/notify o'rniga ───────────────────────
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 5;

    public void produce(int item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();     // wait() o'rniga
            }
            queue.offer(item);
            notEmpty.signal();       // notify() o'rniga
        } finally {
            lock.unlock();
        }
    }

    public int consume() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            int item = queue.poll();
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }

    // ── ReentrantLock ma'lumotlari ─────────────────────────────
    public void printInfo() {
        System.out.println("Locked: " + lock.isLocked());
        System.out.println("Hold count: " + lock.getHoldCount()); // reentrant
        System.out.println("Queue len: " + lock.getQueueLength());
        System.out.println("Fair: " + lock.isFair());
    }

    public int getCount() {
        return count;
    }
}