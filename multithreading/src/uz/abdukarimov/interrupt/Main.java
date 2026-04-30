package uz.abdukarimov.interrupt;

public class Main {
    static void main() throws InterruptedException {
        Thread worker = new Thread(() -> {
            System.out.println("Worker boshlandi");

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("Ish bajarilmoqda...");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // sleep interrupt qabul qildi — flagni qayta o'rnatish!
                    Thread.currentThread().interrupt();
                    System.out.println("Interrupt qabul qilindi, to'xtatilmoqda");
                    break; // ← yoki return
                }
            }

            System.out.println("Worker tugadi");
        });

        worker.start();
        Thread.sleep(1500);
        worker.interrupt(); // signal yuborish
        worker.join();
        System.out.println("Main tugadi");

    }
}
