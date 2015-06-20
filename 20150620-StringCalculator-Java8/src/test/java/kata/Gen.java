package kata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Gen {
    private static Integer integersInRange(int min, int max) {
        return ((int) (Math.random() * (max - min) + min));
    }

    public static Supplier<Integer> integers = () -> integersInRange(-1200, 1200);

    public static Supplier<Integer> nonNegativeIntegers = () -> integersInRange(0, 1200);

    public static Supplier<Character> characters(Predicate<Character> isValid) {
        return () -> {
            while (true) {
                char c = (char) integersInRange(1, 255).intValue();
                if (isValid.test(c)) {
                    return c;
                }
            }
        };
    }

    public static <T> Supplier<List<T>> nonEmptyListOf(Supplier<T> gen) {
        return () -> {
            int upper = integersInRange(1, 10);
            List<T> result = new ArrayList<>(upper);
            for (int i = 0; i < upper; i += 1) {
                result.add(gen.get());
            }
            return result;
        };
    }

    public static <T> Supplier<T> oneOf(List<T> list) {
        return () -> list.get(integersInRange(0, list.size()));
    }


    public static <T> void forAll(Supplier<T> gen, Consumer<T> expr) {
        for (int lp = 0; lp < 1000; lp += 1) {
            expr.accept(gen.get());
        }
    }

    public static <T1, T2> void forAll(Supplier<T1> gen1, Supplier<T2> gen2, BiConsumer<T1, T2> expr) {
        for (int lp = 0; lp < 1000; lp += 1) {
            expr.accept(gen1.get(), gen2.get());
        }
    }
}
