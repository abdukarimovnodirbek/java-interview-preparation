package uz.abdukarimov;

// ── 3. Annotatsiyalarni qo'llash ──────────────────────────────
@CustomAnnotation.Entity(tableName = "users")
public class UserAnnotationDemo {
    @CustomAnnotation.Validate(notNull = true,
            minLength = 2,
            maxLength = 50,
            message = "Ism 2-50 ta harf")
    private String name;

    @CustomAnnotation.Validate(notNull = true,
            pattern = "^[\\w.]+@[\\w]+\\.[a-z]+$",
            message = "Email format noto'g'ri")
    private String email;

    @CustomAnnotation.Validate(minLength = 8,
            message = "Parol 8+ belgidan iborat bo'lsin")
    private String password;

    private int age; // annotatsiya yo'q — validatsiya yo'q

    // getter/setter...
}
