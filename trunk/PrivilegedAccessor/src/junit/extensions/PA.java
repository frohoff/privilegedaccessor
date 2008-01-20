package junit.extensions;

import java.lang.reflect.InvocationTargetException;

/**
 * This is the short named interface to the PrivilegedAccessor class.
 * Use this if the name "PrivilegedAccessor" is too long for your application
 *
 * @author Sebastian Dietrich (sebastian.dietrich@anecon.com)
 */
public final class PA {
    /**
     * Private constructor to make it impossible to instantiate this class.
     */
    private PA() {
        throw new Error("Assertion failed"); //should never be called
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.getValue(Object, String)
     */
    public static Object getValue(final Object instanceOrClass,
            final String fieldName) throws NoSuchFieldException {
        return PrivilegedAccessor.getValue(instanceOrClass, fieldName);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.instantiate(Class, Class[], Object...)
     */
    public static Object instantiate(final Class<?> fromClass,
            final Class<?>[] argumentTypes, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, argumentTypes, args);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.instantiate(Class, Object...)
     */
    public static Object instantiate(final Class<?> fromClass, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, args);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, Object...)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final Object... arguments)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arguments);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, Object)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final Object value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }
}