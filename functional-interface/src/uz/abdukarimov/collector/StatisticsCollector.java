package uz.abdukarimov.collector;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class StatisticsCollector implements Collector<Double,
        StatisticsCollector.Stats,
        StatisticsCollector.Stats> {

    public static class Stats {
        long count = 0;
        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        void add(double value) {
            count++;
            sum += value;
            if (value < min) min = value;
            if (value > max) max = value;
        }

        void merge(Stats other) {
            count += other.count;
            sum += other.sum;
            if (other.min < min) min = other.min;
            if (other.max > max) max = other.max;
        }

        public double average() {
            return count == 0 ? 0 : sum / count;
        }

        @Override
        public String toString() {
            return String.format(
                    "Stats{count=%d, sum=%.1f, min=%.1f, max=%.1f, avg=%.1f}",
                    count, sum, min, max, average()
            );
        }
    }

    @Override
    public Supplier<Stats> supplier() {
        return Stats::new;                  // bo'sh Stats yarat
    }

    @Override
    public BiConsumer<Stats, Double> accumulator() {
        return Stats::add;                  // har bir elementni qo'sh
    }

    @Override
    public BinaryOperator<Stats> combiner() {
        return (s1, s2) -> {
            s1.merge(s2);
            return s1;
        }; // parallel birlashtir
    }

    @Override
    public Function<Stats, Stats> finisher() {
        return Function.identity();         // Stats → Stats (o'zgarmaydi)
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.IDENTITY_FINISH);
    }
}
