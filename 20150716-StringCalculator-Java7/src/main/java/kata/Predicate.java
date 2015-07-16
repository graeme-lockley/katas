package kata;

public interface Predicate<T> {
    Predicate<Integer> IS_NEGATIVE = new Predicate<Integer>() {
        @Override
        public boolean evaluate(Integer item) {
            return item < 0;
        }
    };

    boolean evaluate(T item);
}
