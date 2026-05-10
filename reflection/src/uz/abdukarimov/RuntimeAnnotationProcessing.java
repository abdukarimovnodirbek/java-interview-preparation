package uz.abdukarimov;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RuntimeAnnotationProcessing {
    // ── Validatsiya natijasi ──────────────────────────────────────
    public record ValidationResult(
            boolean valid,
            List<String> errors
    ) {
        public static ValidationResult ok() {
            return new ValidationResult(true, List.of());
        }

        public static ValidationResult fail(List<String> errors) {
            return new ValidationResult(false, errors);
        }
    }

    // ── Reflection bilan validator ────────────────────────────────
    public class AnnotationValidator {

        public static ValidationResult validate(Object obj) {
            List<String> errors = new ArrayList<>();
            Class<?> clazz = obj.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                CustomAnnotation.Validate ann = field.getAnnotation(CustomAnnotation.Validate.class);
                if (ann == null) continue;

                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    continue;
                }

                String fieldName = field.getName();

                // notNull tekshiruvi
                if (ann.notNull() && value == null) {
                    errors.add(fieldName + ": " + ann.message() +
                            " (null bo'lmasin)");
                    continue;
                }
                if (value == null) continue;

                String str = value.toString();

                // minLength
                if (str.length() < ann.minLength()) {
                    errors.add(String.format("%s: minimum %d belgi " +
                                    "(hozir: %d). %s",
                            fieldName, ann.minLength(),
                            str.length(), ann.message()));
                }

                // maxLength
                if (str.length() > ann.maxLength()) {
                    errors.add(String.format("%s: maksimum %d belgi " +
                                    "(hozir: %d). %s",
                            fieldName, ann.maxLength(),
                            str.length(), ann.message()));
                }

                // pattern
                if (!ann.pattern().isEmpty() &&
                        !str.matches(ann.pattern())) {
                    errors.add(fieldName + ": " + ann.message());
                }
            }

            return errors.isEmpty()
                    ? ValidationResult.ok()
                    : ValidationResult.fail(errors);
        }
    }

    // ── @Log annotation processor — AOP o'rniga ──────────────────
    public class LoggingProxy {

        @SuppressWarnings("unchecked")
        public static <T> T createProxy(T target) {
            return (T) Proxy.newProxyInstance(
                    target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(),
                    (proxy, method, args) -> {

                        CustomAnnotation.Log logAnn = method.getAnnotation(CustomAnnotation.Log.class);
                        if (logAnn == null) {
                            return method.invoke(target, args);
                        }

                        long start = System.currentTimeMillis();
                        System.out.printf("[%s] %s() boshlandi%n",
                                logAnn.level(), method.getName());
                        try {
                            Object result = method.invoke(target, args);
                            if (logAnn.timing()) {
                                long elapsed =
                                        System.currentTimeMillis() - start;
                                System.out.printf("[%s] %s() tugadi: %dms%n",
                                        logAnn.level(), method.getName(), elapsed);
                            }
                            return result;
                        } catch (InvocationTargetException e) {
                            System.err.printf("[ERROR] %s() xato: %s%n",
                                    method.getName(), e.getCause().getMessage());
                            throw e.getCause();
                        }
                    });
        }
    }

    // ── @Retry annotation processor ──────────────────────────────
    public class RetryHandler {

        public static Object invokeWithRetry(
                Object target, Method method, Object[] args)
                throws Throwable {

            CustomAnnotation.Retry retry = method.getAnnotation(CustomAnnotation.Retry.class);
            if (retry == null) {
                return method.invoke(target, args);
            }

            int attempts = retry.times();
            long delay = retry.delayMs();
            Throwable last = null;

            for (int i = 1; i <= attempts; i++) {
                try {
                    System.out.printf("Urinish %d/%d: %s()%n",
                            i, attempts, method.getName());
                    return method.invoke(target, args);
                } catch (InvocationTargetException e) {
                    last = e.getCause();
                    // Retry qilinadigan exception ekanini tekshirish
                    Throwable finalLast = last;
                    boolean shouldRetry = Arrays.stream(retry.on())
                            .anyMatch(ec -> ec.isInstance(finalLast));
                    if (!shouldRetry || i == attempts) throw last;
                    System.out.printf("Xato, %dms kutib qayta urinish%n",
                            delay);
                    Thread.sleep(delay);
                }
            }
            throw last;
        }
    }

    // ── Ishlatish ─────────────────────────────────────────────────
    public static void main(String[] args) {
        /*
        User user = new User();
        user.setName("A");               // juda qisqa
        user.setEmail("noto'g'ri-email");
        user.setPassword("qisqa");

        ValidationResult result =
                AnnotationValidator.validate(user);

        if (!result.valid()) {
            System.out.println("Validatsiya xatolari:");
            result.errors().forEach(e ->
                    System.out.println("  - " + e));
        }
        */
        // Validatsiya xatolari:
        //   - name: Ism 2-50 ta harf (minimum 2 belgi, hozir: 1)
        //   - email: Email format noto'g'ri
        //   - password: Parol 8+ belgidan iborat bo'lsin
    }
}
