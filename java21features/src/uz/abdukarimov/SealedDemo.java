package uz.abdukarimov;

public class SealedDemo {
    // ── ESKI usul — nazorat yo'q ──────────────────────────────────
    public abstract class ShapeOld { }
    // Har kim extends qila oladi — nazorat yo'q!
    class Trianglee extends ShapeOld { }     // kutilgan
    class WeirdShape extends ShapeOld { }   // kutilmagan!

    // ── YANGI usul — sealed ───────────────────────────────────────
    // permits — faqat shu klasslar meros olishi mumkin
    public sealed interface Shape
            permits Circle, Rectangle, Triangle { }

    // Subklass 3 variantdan birini tanlashi shart:
    public record Circle(double radius)
            implements Shape { }           // final — meros qolmaydi

    public record Rectangle(double w, double h)
            implements Shape { }           // final

    public non-sealed class Triangle   // kim ham extends qilishi mumkin
            implements Shape {
        private final double base, height;
        public Triangle(double b, double h) {
            base = b; height = h;
        }
        public double area() { return 0.5 * base * height; }
    }

    // ── Sealed class + Pattern Matching = kuchli kombinatsiya ─────
    public double area(Shape shape) {
        return switch (shape) {
            case Circle c       -> Math.PI * c.radius() * c.radius();
            case Rectangle r    -> r.w() * r.h();
            case Triangle t     -> t.area();
            // default kerak emas — compiler hammasi qamralganini biladi!
        };
    }

    // ── Sealed ierarxiya ──────────────────────────────────────────
    sealed interface Expr permits Num, Add, Mul { }
    record Num(int value)        implements Expr { }
    record Add(Expr l, Expr r)   implements Expr { }
    record Mul(Expr l, Expr r)   implements Expr { }

    // Rekursiv interpreter
    static int eval(Expr e) {
        return switch (e) {
            case Num(var v)       -> v;
            case Add(var l, var r)-> eval(l) + eval(r);
            case Mul(var l, var r)-> eval(l) * eval(r);
        };
    }

    static void main() {
        // Test: (2 + 3) * 4 = 20
        Expr expr = new Mul(new Add(new Num(2), new Num(3)), new Num(4));
        System.out.println(eval(expr)); // 20
    }
}
