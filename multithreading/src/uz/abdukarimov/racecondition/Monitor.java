package uz.abdukarimov.racecondition;

public class Monitor {
    static void main() {
        // Har bir object = bir monitor
        Object obj = new Object();

        // synchronized blok — obj ning monitorini ishlatadi
        synchronized (obj) {
            // faqat bitta thread bu blokda bo'lishi mumkin
        }
    }

    // synchronized metod — this ning monitorini ishlatadi
    public synchronized void method() {
    }

    // static synchronized — Class.class monitorini ishlatadi
    public static synchronized void staticMethod() {
    }
}
