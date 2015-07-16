package kata;

public class Gen {
    public static <T> void forAll(Generator<T> gen1, Function<T> function) throws Exception {
        for (int i = 0; i < 1000; i += 1) {
            function.test(gen1.next());
        }
    }

    public static <T1, T2> void forAll(Generator<T1> gen1, Generator<T2> gen2, Function2<T1, T2> function) throws Exception {
        for (int i = 0; i < 1000; i += 1) {
            function.test(gen1.next(), gen2.next());
        }
    }
}
