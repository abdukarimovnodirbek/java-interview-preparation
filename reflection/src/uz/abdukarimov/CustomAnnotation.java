package uz.abdukarimov;

import java.lang.annotation.*;

public class CustomAnnotation {
// ── 1. Meta-annotatsiyalar ────────────────────────────────────
// @Target  — qayerda ishlatilishi
// @Retention — qachongacha saqlanishi
// @Documented — Javadoc ga kiritilishi
// @Inherited — subklassga o'tishi

    // ── 2. Asosiy custom annotation ──────────────────────────────
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Validate {
        // elementlar default qiymat bilan
        boolean notNull() default false;

        int minLength() default 0;

        int maxLength() default Integer.MAX_VALUE;

        String pattern() default "";

        String message() default "Validatsiya xato";
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Entity {
        String tableName() default "";
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Log {
        boolean timing() default true;

        String level() default "INFO";
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Retry {
        int times() default 3;

        long delayMs() default 1000;

        Class<? extends Exception>[] on()
                default {Exception.class};
    }
}
