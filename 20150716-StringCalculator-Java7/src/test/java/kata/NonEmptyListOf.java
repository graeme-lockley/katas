package kata;

import java.util.ArrayList;
import java.util.List;

public class NonEmptyListOf<T> implements Generator<List<T>> {
    private final Generator<T> generator;

    public NonEmptyListOf(Generator<T> generator) {
        this.generator = generator;
    }

    @Override
    public List<T> next() {
        int width = new IntegerInRange(1, 10).next();
        List<T> result = new ArrayList<>(width);
        for (int i = 0; i < width; i += 1) {
            result.add(generator.next());
        }
        return result;
    }
}
