package uz.abdukarimov.deadlock;

import java.util.Random;

public class LivelockDemo {

    static class Corridor {
        private String name;
        private volatile boolean moving;

        public Corridor(String name) {
            this.name = name;
            this.moving = true;
        }

        public boolean isMoving() {
            return moving;
        }

        public void setMoving(boolean b) {
            moving = b;
        }

        public String getName() {
            return name;
        }
    }

    // ── Livelock yaratish ──────────────────────────────────────
    // Ikki kishi koridorda to'qnashadi va yo'l berishga harakat qiladi
    public static void createLivelock() {

        Corridor person1 = new Corridor("Ali");
        Corridor person2 = new Corridor("Vali");

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                // person2 ham o'tishga harakat qilyaptimi?
                while (person2.isMoving()) {
                    System.out.println(person1.getName() +
                            ": Vali o'tayapti, men chekinaman");
                    person1.setMoving(false);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    person1.setMoving(true); // qayta urinaman
                }
                System.out.println(person1.getName() + ": O'tdim!");
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                while (person1.isMoving()) {
                    System.out.println(person2.getName() +
                            ": Ali o'tayapti, men chekinaman");
                    person2.setMoving(false);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    person2.setMoving(true);
                }
                System.out.println(person2.getName() + ": O'tdim!");
            }
        });

        t1.start();
        t2.start();
        // Ikkalasi ham "chekinmoqda" deb xabar beradi,
        // lekin hech biri o'tishga muvaffaq bo'lmaydi!
    }

    // ── Livelock yechimi: tasodifiy kutish ─────────────────────
    public static void livelockFix() {
        Random rand = new Random();

        Corridor p1 = new Corridor("Ali");
        Corridor p2 = new Corridor("Vali");

        Thread t1 = new Thread(() -> {
            while (p2.isMoving()) {
                p1.setMoving(false);
                try {
                    // Tasodifiy vaqt kutish — bir-birini "sinxronlamaydi"
                    Thread.sleep(rand.nextInt(100) + 10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                p1.setMoving(true);
            }
            System.out.println("Ali o'tdi!");
        });

        Thread t2 = new Thread(() -> {
            while (p1.isMoving()) {
                p2.setMoving(false);
                try {
                    Thread.sleep(rand.nextInt(100) + 10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                p2.setMoving(true);
            }
            System.out.println("Vali o'tdi!");
        });

        t1.start();
        t2.start();
    }
}