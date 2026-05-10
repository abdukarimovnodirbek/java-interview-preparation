package uz.abdukarimov.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// ── Annotatsiyalar ────────────────────────────────────────────
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonField {
    String name() default "";     // custom nom
    boolean ignore() default false;
    boolean required() default false;
}