package kata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MyCollections {
    public static String join(Collection collection) {
        return join(collection, "");
    }

    public static String join(Collection collection, String separator) {
        StringBuilder sb = new StringBuilder();
        for (Object o : collection) {
            sb.append(separator).append(o.toString());
        }
        return sb.substring(separator.length());
    }

    public static <T> List<T> filter(List<T> items, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.evaluate(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static <T> boolean exists(List<T> items, Predicate<T> predicate) {
        for (T item : items) {
            if (predicate.evaluate(item)) {
                return true;
            }
        }
        return false;
    }

    public static <S> S reduce(List<S> items, FoldFunction<S, S> foldFunction) {
        Iterator<S> iterator = items.iterator();
        S first = iterator.next();
        return reduce(iterator, first, foldFunction);
    }

    public static <S, T> T reduce(List<S> items, T initial, FoldFunction<S, T> foldFunction) {
        return reduce(items.iterator(), initial, foldFunction);
    }

    private static <S, T> T reduce(Iterator<S> items, T initial, FoldFunction<S, T> foldFunction) {
        T result = initial;
        while (items.hasNext()) {
            result = foldFunction.execute(result, items.next());
        }
        return result;
    }
}
