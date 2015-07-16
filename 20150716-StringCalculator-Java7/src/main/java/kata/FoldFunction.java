package kata;

public interface FoldFunction<S, T> {
    T execute(T result, S next);
}
