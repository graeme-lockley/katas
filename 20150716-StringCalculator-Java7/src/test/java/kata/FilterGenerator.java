package kata;

public class FilterGenerator<T> implements Generator<T> {
    private final Generator<T> generator;
    private final Predicate<T> predicate;

    public FilterGenerator(Generator<T> generator, Predicate<T> predicate) {
        this.generator = generator;
        this.predicate = predicate;
    }

    @Override
    public T next() {
        while (true) {
            T result = generator.next();

            if (predicate.evaluate(result)) {
                return result;
            }
        }
    }
}
