package uz.abdukarimov;

public class StringDemo {
    static void main() {
        String s = "  salom dunyo  ";

// Java 11:
        s.isBlank();           // bo'shmi? (faqat whitespace)
        s.strip();             // "salom dunyo" (Unicode-aware trim)
        s.stripLeading();      // "salom dunyo  "
        s.stripTrailing();     // "  salom dunyo"
        "a\nb\nc".lines();      // Stream<String>: ["a", "b", "c"]
        "ha".repeat(3);        // "hahaha"

// Java 12:
        "salom".indent(4);     // "    salom\n"

// Java 15:
        """
                salom
                """.stripIndent();     // bo'shliqlarni olib tashlash

// Java 21 — String.format o'rniga:
        String msg = "Ism: %s, yosh: %d".formatted("Ali", 25);
    }
}
