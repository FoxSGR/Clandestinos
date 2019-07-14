package foxsgr.clandestinos.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Contains useful validation methods.
 */
@SuppressWarnings({"unused"})
public final class Preconditions {

    /**
     * Private constructor to hide the implicit public one.
     */
    private Preconditions() {
        // Should be empty.
    }

    public static void ensure(boolean expression, String message) {
        if (!expression) {
            exception(message);
        }
    }

    public static <T extends Throwable> void ensure(boolean expression, Class<T> clazz) throws T {
        if (!expression) {
            exception(clazz);
        }
    }

    public static <T extends Throwable> void ensureBetween(double value, double lowerLimit, double upperLimit, Class<T> clazz) throws T {
        if (value < lowerLimit || value > upperLimit) {
            exception(clazz);
        }
    }

    public static <T extends Throwable> void ensureNotEmpty(String string, Class<T> clazz) throws T {
        if (string == null || string.isEmpty()) {
            exception(clazz);
        }
    }

    public static void ensureNotEmpty(String string, String message) {
        if (string == null || string.isEmpty()) {
            exception(message);
        }
    }

    public static void ensureNotNull(Object value, String message) {
        if (value == null) {
            exception(message);
        }
    }

    private static <T extends Throwable> void exception(Class<T> clazz) throws T {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            throw constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void exception(String message) {
        throw new IllegalArgumentException(message);
    }
}
