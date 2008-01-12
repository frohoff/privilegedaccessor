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
     * @see junit.extensions.PrivilegedAccessor.instantiate(Class)
     */
    public static Object instantiate(final Class fromClass)
    throws IllegalArgumentException, InstantiationException,
    IllegalAccessException, InvocationTargetException,
    NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.instantiate(Class, Class[], Object...)
     */
    public static Object instantiate(final Class fromClass,
            final Class[] argumentTypes, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, argumentTypes, args);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.instantiate(Class, Object)
     */
    public static Object instantiate(final Class fromClass,
            final Object argument)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, argument);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.instantiate(Class, Object...)
     */
    public static Object instantiate(final Class fromClass, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, args);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature)
    throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, boolean)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final boolean arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, byte)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final byte arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, char)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final char arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, double)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final double arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, float)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final float arg)
    throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, int)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final int arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, long)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final long arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final Object arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
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
     * @see junit.extensions.PrivilegedAccessor.invokeMethod(Object, String, short)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final short arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, boolean)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final boolean value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, byte)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final byte value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, char)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final char value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, double)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final double value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, float)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final float value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, int)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final int value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, long)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final long value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, Object)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final Object value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.setValue(Object, String, short)
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final short value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }
}