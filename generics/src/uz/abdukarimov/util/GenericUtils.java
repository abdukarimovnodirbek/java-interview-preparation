package uz.abdukarimov.util;

import uz.abdukarimov.generic.MultipleTypeParameter;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class GenericUtils {

    // ─── 1. MAX topish ───────────────────────────────────────────
    public static <T extends Comparable<T>> T max(List<? extends T> list) {
        if (list.isEmpty()) throw new NoSuchElementException("List is empty");
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) max = item;
        }
        return max;
    }

    // ─── 2. FILTER ───────────────────────────────────────────────
    public static <T> List<T> filter(
            List<? extends T> list,
            Predicate<? super T> predicate
    ) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) result.add(item);
        }
        return result;
    }

    // ─── 3. MAP (transform) ──────────────────────────────────────
    public static <T, R> List<R> map(
            List<? extends T> list,
            Function<? super T, ? extends R> mapper
    ) {
        List<R> result = new ArrayList<>();
        for (T item : list) {
            result.add(mapper.apply(item));
        }
        return result;
    }

    // ─── 4. ZIP ──────────────────────────────────────────────────
    public static <A, B> List<MultipleTypeParameter.Pair<A, B>> zip(
            List<? extends A> listA,
            List<? extends B> listB
    ) {
        List<MultipleTypeParameter.Pair<A, B>> result = new ArrayList<>();
        int size = Math.min(listA.size(), listB.size());
        for (int i = 0; i < size; i++) {
            result.add(new MultipleTypeParameter.Pair<>(listA.get(i), listB.get(i)));
        }
        return result;
    }

    // ─── 5. GROUP BY ─────────────────────────────────────────────
    public static <T, K> Map<K, List<T>> groupBy(
            List<? extends T> list,
            Function<? super T, ? extends K> classifier
    ) {
        Map<K, List<T>> result = new LinkedHashMap<>();
        for (T item : list) {
            K key = classifier.apply(item);
            result.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        return result;
    }

    // ─── 6. FLATTEN ──────────────────────────────────────────────
    public static <T> List<T> flatten(List<? extends List<? extends T>> nested) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> inner : nested) {
            result.addAll(inner);
        }
        return result;
    }
}