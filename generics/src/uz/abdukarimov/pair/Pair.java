package uz.abdukarimov.pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class Pair<A, B> {

    private final A first;
    private final B second;

    // ─── Constructor ────────────────────────────────────────────
    private Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    // Factory method — of() orqali yaratish
    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    // ─── Getterlar ──────────────────────────────────────────────
    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    // ─── Swap — A va B larni almashtiradi ───────────────────────
    public Pair<B, A> swap() {
        return Pair.of(second, first);
    }

    // ─── Map — birinchi yoki ikkinchini o'zgartirish ─────────────
    public <C> Pair<C, B> mapFirst(Function<? super A, ? extends C> f) {
        return Pair.of(f.apply(first), second);
    }

    public <C> Pair<A, C> mapSecond(Function<? super B, ? extends C> f) {
        return Pair.of(first, f.apply(second));
    }

    // ─── toList, toMap ──────────────────────────────────────────
    public List<Object> toList() {
        return List.of(first, second);
    }

    public Map<A, B> toMap() {
        return Map.of(first, second);
    }

    // ─── equals, hashCode, toString ─────────────────────────────
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pair)) return false;
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(first, other.first) &&
                Objects.equals(second, other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}