package uz.abdukarimov;

public class SwitchExpressionsDemo {
/*
// ── ESKI switch statement ─────────────────────────────────────
        String dayTypeOld;
        switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                dayTypeOld = "Ish kuni";
                break;
            case SATURDAY:
            case SUNDAY:
                dayTypeOld = "Dam olish";
                break;
            default:
                throw new IllegalArgumentException();
        }

// ── YANGI switch expression ───────────────────────────────────
        String dayType = switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Ish kuni";
            case SATURDAY, SUNDAY                             -> "Dam olish";
        };

// ── yield — ko'p qatorli case ────────────────────────────────
        int numLetters = switch (day) {
            case MONDAY, FRIDAY, SUNDAY -> 6;
            case TUESDAY                -> 7;
            case THURSDAY, SATURDAY     -> 8;
            case WEDNESDAY              -> {
                System.out.println("Eng uzun kun nomi");
                yield 9; // ← return emas, yield!
            }
        };

// ── Switch + Pattern Matching (Java 21) ───────────────────────
        sealed interface Animal permits Dog, Cat, Bird {}
        record Dog(String name) implements Animal {}
        record Cat(String name, boolean indoor) implements Animal {}
        record Bird(String name, String species) implements Animal {}

        String describe(Animal a) {
            return switch (a) {
                case Dog d           -> d.name() + " — it";
                case Cat c when c.indoor() -> c.name() + " — uy mushuki";
                case Cat c           -> c.name() + " " + " — ko'cha mushuki";
                case Bird(var n, var s)    -> n + " — " + s;
            };
        }

// ── Switch null ni ham qabul qiladi (Java 21) ─────────────────
        String process(String input) {
            return switch (input) {
                case null        -> "null qiymat";
                case "hi", "hey" -> "Salom!";
                case String s when s.length() > 10 -> "Uzun: " + s;
                default          -> "Boshqa: " + input;
            };
        }
*/
}
