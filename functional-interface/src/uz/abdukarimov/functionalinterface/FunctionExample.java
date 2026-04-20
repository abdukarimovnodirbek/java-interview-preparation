package uz.abdukarimov.functionalinterface;

import java.util.function.Function;

public class FunctionExample {
    static void main() {
        Function<String, Integer> length = String::length;
        Function<Integer, String> toStr = Object::toString;

        // andThen — pipeline: f, keyin g
        Function<String, String> pipeline = length.andThen(toStr);
        System.out.println(pipeline.apply("Hello")); // "5"

        // compose — teskari: g, keyin f
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addThree = x -> x + 3;
        Function<Integer, Integer> combined = doubleIt.compose(addThree); // (x+3)*2
        System.out.println(combined.apply(5)); // (5+3)*2 = 16

        // identity — o'zini qaytaradi
        Function<String, String> identity = Function.identity();
    }
}
