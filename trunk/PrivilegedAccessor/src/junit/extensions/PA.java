package junit.extensions;

import java.lang.reflect.InvocationTargetException;

/**
 * This is the short named interface to the PrivilegedAccessor class.
 * Use this if the name "PrivilegedAccessor" is too long for your application.
 * The only difference to PrivilegedAccessor is that PA uses varargs.
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
     * @see junit.extensions.PrivilegedAccessor.instantiate(Class, Class[], Object[])
     */
    public static Object instantiate(final Class<?> fromClass,
            final Class<?>[] argumentTypes, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, argumentTypes, args);
    }

    /**
     * @see junit.extensions.PrivilegedAccessor.instantiate(Class, Object[])
     */
    public static Object instantiate(final Class<?> fromClass, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, args);
    }

    /**
     * Calls a method on the given object instance with the given arguments.
     * Arguments can be object types or representations for primitives.
     * 
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters <br>
     *        (e.g. "myMethod(java.lang.String, com.company.project.MyObject)")
     * @param arguments an array of objects to pass as arguments
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final Object... arguments)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, correctVarargs(arguments));
    }
    
    /**
     * Corrects varargs to their initial form.
     * If you call a method with an object-array as last argument the Java varargs
     * mechanism converts this array in single arguments.
     * This method returns an object array if the arguments are all of the same type.
     * 
     * @param arguments the possibly converted arguments of a vararg method
     * @return arguments possibly converted
     */
    private static Object[] correctVarargs(final Object... arguments) {
        if (arguments == null || changedByVararg(arguments)) {
            return new Object[] {arguments};
        }
        return arguments;
    }
    
    /**
     * Tests if the arguments were changed by vararg.
     * Arguments are changed by vararg if they are of a non primitive array type.
     * E.g. arguments[] = Object[String[]] is converted to String[] while
     * e.g. arguments[] = Object[int[]] is not converted and stays Object[int[]]
     * 
     * Unfortunately we can't detect the difference for arg = Object[primitive] since 
     * arguments[] = Object[Object[primitive]] which is converted to Object[primitive] and
     * arguments[] = Object[primitive] which stays Object[primitive]
     * 
     * and we can't detect the difference for arg = Object[non primitive] since
     * arguments[] = Object[Object[non primitive]] is converted to Object[non primitive] and
     * arguments[] = Object[non primitive] stays Object[non primitive]
     * 
     *  
     * @param objects
     * @return
     */
    private static boolean changedByVararg(final Object[] objects) {
        if (objects.length == 0 || objects[0] == null) {
            return false;
        }
        
        if (objects.getClass() == Object[].class) {
            return false;
        }
        
        return true;
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