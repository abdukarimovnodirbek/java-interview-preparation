package uz.abdukarimov;

import java.util.Objects;

public class RecordDemo {
    // ── ESKI usul (Java 15 va oldin) ─────────────────────────────
    public final class PointOld {
        private final int x;
        private final int y;

        public PointOld(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x()       { return x; }
        public int y()       { return y; }

        @Override public boolean equals(Object o) {
            if (!(o instanceof PointOld p)) return false;
            return x == p.x && y == p.y;
        }
        @Override public int hashCode() { return Objects.hash(x, y); }
        @Override public String toString() {
            return "Point[x=" + x + ", y=" + y + "]";
        }
    }

    // ── YANGI usul — record (Java 16+) ───────────────────────────
    public record Point(int x, int y) {}
    // Hammasi kompilyator tomonidan yaratiladi!


    // ── Record kengaytirish ────────────────────────────────────────
    public record Person(String name, int age) {

        // Compact constructor — validatsiya uchun
        public Person {
            if (name == null || name.isBlank())
                throw new IllegalArgumentException("Ism bo'sh bo'lmasin");
            if (age < 0 || age > 150)
                throw new IllegalArgumentException("Yosh noto'g'ri: " + age);
            name = name.trim(); // normalizatsiya
        }

        // Qo'shimcha metod
        public String greeting() {
            return "Salom, men " + name + ", yoshim " + age;
        }

        // Static factory metod
        public static Person of(String name, int age) {
            return new Person(name, age);
        }
    }

    // Record interface implement qila oladi
    public interface Printable { void print(); }

    public record Book(String title, String author)
            implements Printable {
        @Override public void print() {
            System.out.println(title + " by " + author);
        }
    }

    // Record generic bo'la oladi
    public record Pair<A, B>(A first, B second) {
        public Pair<B, A> swap() { return new Pair<>(second, first); }
    }

    static void main() {
        // Ishlatish:
        Point p1 = new Point(3, 4);
        System.out.println(p1.x());       // 3
        System.out.println(p1.y());       // 4
        System.out.println(p1);           // Point[x=3, y=4]
        p1.equals(new Point(3, 4));       // true

        Pair<String, Integer> p = new Pair<>("Ali", 25);
        System.out.println(p.swap()); // Pair[first=25, second=Ali]
    }
}
