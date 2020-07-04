package foxsgr.clandestinos.util;

import java.util.Collection;
import java.util.function.Consumer;

public final class UtilUtil {

    public static <T> void addIfNotNull(T value, Collection<T> collection) {
        if (value != null) {
            collection.add(value);
        }
    }

    public static <T> void applyIfNotNull(T value, Consumer<T> func) {
        if (value != null) {
            func.accept(value);
        }
    }
}
