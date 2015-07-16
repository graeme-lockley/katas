package kata;

public class FilterGenerator<T> implements Generator<T> {
    private final Generator<T> generator;

    public FilterGenerator(Generator<T> generator) {
        this.generator = generator;
    }

    @Override
    public T next() {
        while (true) {
            T result = generator.next();

            if (!filter(result)) {
                return result;
            }
        }
    }

    protected boolean filter(T result) {
        return false;
    }
}
