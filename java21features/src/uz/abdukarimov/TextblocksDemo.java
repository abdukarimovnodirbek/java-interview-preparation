package uz.abdukarimov;

public class TextblocksDemo {
    static void main() {
        // ── ESKI usul — chalkash ──────────────────────────────────────
        String jsonOld = "{\n" +
                "  \"name\": \"Ali\",\n" +
                "  \"age\": 25,\n" +
                "  \"city\": \"Toshkent\"\n" +
                "}";

        String htmlOld = "<html>\n" +
                "  <body>\n" +
                "    <h1>Salom!</h1>\n" +
                "  </body>\n" +
                "</html>";

        // ── YANGI usul — Text Block ───────────────────────────────────
        String json = """
                {
                  "name": "Ali",
                  "age": 25,
                  "city": "Toshkent"
                }
                """;

        String html = """
                <html>
                  <body>
                    <h1>Salom!</h1>
                  </body>
                </html>
                """;

        String sql = """
                SELECT u.name, u.email,
                       COUNT(o.id) AS order_count
                FROM users u
                LEFT JOIN orders o ON u.id = o.user_id
                WHERE u.active = true
                GROUP BY u.id
                ORDER BY order_count DESC
                """;

        // ── Interpolatsiya — formatted() bilan ───────────────────────
        String name = "Ali";
        int age = 25;

        String message = """
                Foydalanuvchi ma'lumoti:
                  Ism:  %s
                  Yosh: %d
                  Shahar: Toshkent
                """.formatted(name, age);

        // ── Chiziq oxirlarini boshqarish ──────────────────────────────
        String noNewline = """
                Bu satr \
                bitta qatorda""";
        // "Bu satr bitta qatorda"

        String escaped = """
                Qo\'shtirnoq: "salom"
                Apostrof: don\'t
                """;
    }
}
