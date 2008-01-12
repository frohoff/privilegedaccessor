package junit.extensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 * This class is used to access a method or field of an object no matter what
 * the access modifier of the method or field. The syntax for accessing fields
 * and methods is out of the ordinary because this class uses reflection to
 * peel away protection.
 * <p>
 * a.k.a. The "ObjectMolester"
 * <p>
 * Here is an example of using this to access a private member: <br>
 * <code>myObject</code> is an object of type <code>MyClass</code>.
 * <code>setName(String)</code> is a private method of <code>MyClass</code>.
 *
 * <pre>
 * PrivilegedAccessor.invokeMethod(myObject,
 *         &quot;setName(java.lang.String)&quot;, &quot;newName&quot;);
 * </pre>
 *
 * @author Charlie Hubbard (chubbard@iss.net)
 * @author Prashant Dhokte (pdhokte@iss.net)
 * @author Sebastian Dietrich (sebastian.dietrich@anecon.com)
 */
public final class PrivilegedAccessor {
    /**
     * Private constructor to make it impossible to instantiate this class.
     */
    private PrivilegedAccessor() {
        throw new Error("Assertion failed"); //should never be called
    }

    /**
     * Gets the value of the named field and returns it as an object.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to get the field from
     * @param fieldName the name of the field
     * @return an object representing the value of the field
     * @throws NoSuchFieldException if the field does not exist
     */
    public static Object getValue(final Object instanceOrClass,
            final String fieldName) throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        try {
            return field.get(instanceOrClass);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); // would mean that setAccessible(true) didn't work
        }
    }

    /**
     * Instantiates an object of the given class without parameters.
     * e.g. instantiate(String.class);
     *
     * @param fromClass the class to instantiate an object from
     * @return the newly created null
     * @throws IllegalArgumentException if the number of actual and formal
     *         parameters differ; if an unwrapping conversion for primitive
     *         arguments fails; or if, after possible unwrapping, a parameter
     *         value cannot be converted to the corresponding formal parameter
     *         type by a method invocation conversion.
     * @throws IllegalAccessException if this Constructor object enforces Java
     *         language access control and the underlying constructor is
     *         inaccessible.
     * @throws InvocationTargetException if the underlying constructor throws
     *         an exception.
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws InstantiationException if the class that declares the underlying
     *         constructor represents an abstract class.
     *
     * @see PrivilegedAccessor#instantiate(Class,Object)
     */
    public static Object instantiate(final Class fromClass)
    throws IllegalArgumentException, InstantiationException,
    IllegalAccessException, InvocationTargetException,
    NoSuchMethodException {
        return instantiate(fromClass, null);
    }

    /**
     * Instantiates an object of the given class with the given arguments and
     * the given argument types.
     *
     * @param fromClass the class to instantiate an object from
     * @param args the arguments to pass to the constructor
     * @param argumentTypes the types of the arguments of the constructor
     * @return an null of the given type
     * @throws IllegalArgumentException if the number of actual and formal
     *         parameters differ; if an unwrapping conversion for primitive
     *         arguments fails; or if, after possible unwrapping, a parameter
     *         value cannot be converted to the corresponding formal parameter
     *         type by a method invocation conversion.
     * @throws IllegalAccessException if this Constructor object enforces Java
     *         language access control and the underlying constructor is
     *         inaccessible.
     * @throws InvocationTargetException if the underlying constructor throws
     *         an exception.
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws InstantiationException if the class that declares the underlying
     *         constructor represents an abstract class.
     *
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object instantiate(final Class fromClass,
            final Class[] argumentTypes, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return getConstructor(fromClass, argumentTypes).newInstance(args);
    }

    /**
     * Instantiates an object of the given class with the given argument. e.g.
     * instantiate(String.class, "string to copy");
     *
     * @param fromClass the class to instantiate an object from
     * @param argument the argument to pass to the constructor
     * @return an null of the given type
     * @throws IllegalArgumentException if the number of actual and formal
     *         parameters differ; if an unwrapping conversion for primitive
     *         arguments fails; or if, after possible unwrapping, a parameter
     *         value cannot be converted to the corresponding formal parameter
     *         type by a method invocation conversion.
     * @throws IllegalAccessException if this Constructor object enforces Java
     *         language access control and the underlying constructor is
     *         inaccessible.
     * @throws InvocationTargetException if the underlying constructor throws
     *         an exception.
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws InstantiationException if the class that declares the underlying
     *         constructor represents an abstract class.
     *
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object...)
     */
    public static Object instantiate(final Class fromClass,
            final Object argument)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        Object[] args = new Object[1];
        args[0] = argument;
        return instantiate(fromClass, args);
    }

    /**
     * Instantiates an object of the given class with the given arguments.
     *
     * @param fromClass the class to instantiate an object from
     * @param args the arguments to pass to the constructor
     * @return an null of the given type
     * @throws IllegalArgumentException if the number of actual and formal
     *         parameters differ; if an unwrapping conversion for primitive
     *         arguments fails; or if, after possible unwrapping, a parameter
     *         value cannot be converted to the corresponding formal parameter
     *         type by a method invocation conversion.
     * @throws IllegalAccessException if this Constructor object enforces Java
     *         language access control and the underlying constructor is
     *         inaccessible.
     * @throws InvocationTargetException if the underlying constructor throws
     *         an exception.
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws InstantiationException if the class that declares the underlying
     *         constructor represents an abstract class.
     *
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object instantiate(final Class fromClass, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return instantiate(fromClass, getParameterTypes(args), args);
    }

    /**
     * Calls a method without parameters on the given object instance or class.
     * If the given instanceOrClass is a class then a static method is called.
     *
     * @param instanceOrClass the instance or class to call the method on
     * @param methodSignature the name of the method with brackets
     *        (e.g. "test (java.lang.String, com.company.project.MyObject)")
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature)
    throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        if (methodSignature.indexOf('(') != methodSignature.indexOf(')') - 1) {
            throw new NoSuchMethodException("Method '" + methodSignature
                    + "' must have no parameters");
        }
        return getMethod(instanceOrClass, getMethodName(methodSignature), null)
            .invoke(instanceOrClass, null);
    }

    /**
     * Calls a method on the given object instance with the given boolean
     * argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(boolean)")
     * @param arg the boolean argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final boolean arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Boolean.TYPE}).invoke(instanceOrClass,
                new Object[] {new Boolean(arg)});
    }

    /**
     * Calls a method on the given object instance with the given byte argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(byte)")
     * @param arg the byte argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final byte arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Byte.TYPE}).invoke(instanceOrClass,
                new Object[] {new Byte(arg)});
    }

    /**
     * Calls a method on the given object instance with the given char argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(char)")
     * @param arg the char argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final char arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Character.TYPE}).invoke(instanceOrClass,
                new Object[] {new Character(arg)});
    }

    /**
     * Calls a method on the given object instance with the given double
     * argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(double)")
     * @param arg the double argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final double arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Double.TYPE}).invoke(instanceOrClass,
                new Object[] {new Double(arg)});
    }

    /**
     * Calls a method on the given object instance with the given float
     * argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(flaot)")
     * @param arg the float argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final float arg)
    throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Float.TYPE}).invoke(instanceOrClass,
                new Object[] {new Float(arg)});
    }

    /**
     * Calls a method on the given object instance with the given int argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(int)")
     * @param arg the integer argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final int arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
         return getMethod(instanceOrClass, getMethodName(methodSignature),
                    new Class[] {Integer.TYPE}).invoke(instanceOrClass,
                            new Object[] {new Integer(arg)});
    }

    /**
     * Calls a method on the given object instance with the given long argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(long)")
     * @param arg the long argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final long arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Long.TYPE}).invoke(instanceOrClass,
                new Object[] {new Long(arg)});
    }

    /**
     * Calls a method on the given object instance with the given argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "test (java.lang.String, com.company.project.MyObject)")
     * @param arg the argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object...)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final Object arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return invokeMethod(instanceOrClass, methodSignature,
                new Object[] {arg});
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
     * @see PrivilegedAccessor#invokeMethod(Class,String,Object...)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final Object... arguments)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        if (arguments == null) {
            return invokeMethod(instanceOrClass, methodSignature,
                    new Object[] {null});
        }

        return getMethod(instanceOrClass, getMethodName(methodSignature),
                getParameterTypes(methodSignature, arguments)).
                invoke(instanceOrClass, arguments);
    }

    /**
     * Calls a method on the given object instance with the given short
     * argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(short)")
     * @param arg the short argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the underlying method throws an
     *                                   exception.
     * @throws NoSuchMethodException if no method with the given
     *                               <code>methodSignature</code> could be
     *                               found
     * @throws IllegalArgumentException if an argument couldn't be converted to
     *                                  match the expected type
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final short arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Short.TYPE}).invoke(instanceOrClass,
                new Object[] {new Short(arg)});
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws NoSuchFieldException if no field with the given
     *                              <code>fieldName</code> can be found
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final boolean value)
    throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        try {
            field.setBoolean(instanceOrClass, value);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); //would mean that setAccessible didn't work
        }
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws NoSuchFieldException if no field with the given
     *                              <code>fieldName</code> can be found
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final byte value)
    throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        try {
            field.setByte(instanceOrClass, value);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); //would mean that setAccessible didn't work
        }
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws NoSuchFieldException if no field with the given
     *                              <code>fieldName</code> can be found
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final char value)
    throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        try {
            field.setChar(instanceOrClass, value);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); //would mean that setAccessible didn't work
        }
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws NoSuchFieldException if no field with the given
     *                              <code>fieldName</code> can be found
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final double value)
    throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        try {
            field.setDouble(instanceOrClass, value);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); //would mean that setAccessible didn't work
        }
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws NoSuchFieldException if no field with the given
     *                              <code>fieldName</code> can be found
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final float value)
    throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        try {
            field.setFloat(instanceOrClass, value);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); //would mean that setAccessible didn't work
        }
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws NoSuchFieldException if no field with the given
     *                              <code>fieldName</code> can be found
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final int value)
    throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        try {
            field.setInt(instanceOrClass, value);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); //would mean that setAccessible didn't work
        }
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws NoSuchFieldException if no field with the given
     *                              <code>fieldName</code> can be found
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final long value)
    throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);

        try {
            field.setLong(instanceOrClass, value);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); //would mean that setAccessible didn't work
        }
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws NoSuchFieldException if no field with the given
     *                              <code>fieldName</code> can be found
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final Object value)
    throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        try {
            field.set(instanceOrClass, value);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); //would mean that setAccessible didn't work
        }
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws NoSuchFieldException if no field with the given
     *                              <code>fieldName</code> can be found
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final short value)
    throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        try {
            field.setShort(instanceOrClass, value);
        } catch (IllegalAccessException e) {
            throw new Error("Assertion failed"); //would mean that setAccessible didn't work
        }
    }

    /**
     * Gets the class with the given className.
     *
     * @param className the name of the class to get
     * @return the class for the given className
     * @throws ClassNotFoundException if the class could not be found
     */
    private static Class getClassForName(final String className)
            throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            if (className.equals("int")) {
                return Integer.TYPE;
            }
            if (className.equals("float")) {
                return Float.TYPE;
            }
            if (className.equals("double")) {
                return Double.TYPE;
            }
            if (className.equals("short")) {
                return Short.TYPE;
            }
            if (className.equals("long")) {
                return Long.TYPE;
            }
            if (className.equals("byte")) {
                return Byte.TYPE;
            }
            if (className.equals("char")) {
                return Character.TYPE;
            }
            if (className.equals("boolean")) {
                return Boolean.TYPE;
            }
            throw e;
        }
    }

    /**
     * Gets the constructor for a given class with the given parameters.
     *
     * @param type the class to instantiate
     * @param parameterTypes the types of the parameters
     * @return the constructor
     * @throws NoSuchMethodException if the method could not be found
     */
    private static Constructor getConstructor(final Class type,
            final Class... parameterTypes)
            throws NoSuchMethodException {
        Constructor constructor = type.getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        return constructor;
    }

    /**
     * Return the named field from the given instance or class.
     * Returns a static field if instanceOrClass is a class.
     *
     * @param instanceOrClass the instance or class to get the field from
     * @param fieldName the name of the field to get
     * @return the field
     * @throws NoSuchFieldException if no such field can be found
     */
    private static Field getField(final Object instanceOrClass,
            final String fieldName)
    throws NoSuchFieldException {
        if (instanceOrClass == null) {
            throw new NoSuchFieldException("Invalid field : " + fieldName);
        }

        Class type = null;
        if (instanceOrClass instanceof Class) {
            type = (Class) instanceOrClass;
        } else {
            type = instanceOrClass.getClass();
        }

        try {
            Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            return getField(type.getSuperclass(), fieldName);
        }
    }

    /**
     * Return the named method with a method signature matching classTypes
     * from the given class.
     *
     * @param type the class to get the method from
     * @param methodName the name of the method to get
     * @param parameterTypes the parameter-types of the method to get
     * @return the method
     * @throws NoSuchMethodException if the method could not be found
     */
    private static Method getMethod(final Class type, final String methodName,
            final Class... parameterTypes) throws NoSuchMethodException {
        try {
            return type.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            if (type.getSuperclass() == null) {
                throw new NoSuchMethodException("Invalid method : "
                        + type.getName() + "." + methodName + "("
                        + getParameterTypesAsString(parameterTypes) + ")");
            }
            return getMethod(type.getSuperclass(), methodName, parameterTypes);
        }
    }

    /**
     * Gets the method with the given name and parameters from the given
     * instance or class. If instanceOrClass is a class, then we get
     * a static method.
     *
     * @param instanceOrClass the instance or class to get the method of
     * @param methodName the name of the method
     * @param parameterTypes the parameter-types of the method to get
     * @return the method
     * @throws NoSuchMethodException if the method could not be found
     */
    private static Method getMethod(final Object instanceOrClass,
            final String methodName, final Class... parameterTypes)
    throws NoSuchMethodException {
        Class type;

        if (instanceOrClass instanceof Class) {
            type = (Class) instanceOrClass;
        } else {
            type = instanceOrClass.getClass();
        }

        Method accessMethod = getMethod(type, methodName, parameterTypes);
        accessMethod.setAccessible(true);
        return accessMethod;
    }

    /**
     * Gets the name of a method.
     *
     * @param methodSignature the signature of the method
     * @return the name of the method
     * @throws NoSuchMethodException if no method with the given
     *         <code>methodSignature</code> exists.
     */
    private static String getMethodName(final String methodSignature)
    throws NoSuchMethodException {
        if (methodSignature.indexOf('(') >= methodSignature.indexOf(')')) {
            throw new NoSuchMethodException("Method '" + methodSignature
                    + "' must have brackets");
        }

        try {
            return methodSignature.substring(0,
                    methodSignature.indexOf('(')).trim();
        } catch (StringIndexOutOfBoundsException e) {
            throw new NoSuchMethodException("Method '" + methodSignature
                    + "' must have brackets");
        }
    }

    /**
     * Gets the types of the parameters.
     *
     * @param parameters the parameters
     * @return the class-types of the arguments
     */
    private static Class[] getParameterTypes(final Object... parameters) {
        if (parameters == null) {
            return null;
        }

        Class[] typesOfParameters = new Class[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            typesOfParameters[i] = parameters[i].getClass();
        }
        return typesOfParameters;
    }

    /**
     * Gets the types of the given parameters. If the parameters
     * don't match the given methodSignature an IllegalArgumentException
     * is thrown.
     *
     * @param methodSignature the signature of the method
     * @param parameters the arguments of the method
     * @return the parameter types as class[]
     * @throws NoSuchMethodException if the method could not be found
     * @throws IllegalArgumentException if one of the given parameters
     *                                  doesn't math the given methodSignature
     */
    private static Class[] getParameterTypes(final String methodSignature,
            final Object... parameters)
    throws NoSuchMethodException, IllegalArgumentException {
        Class[] typesOfParameters = getTypesInSignature(methodSignature);

        for (int i = 0; i < typesOfParameters.length; i++) {
            if (parameters[i] == null
                || typesOfParameters[i].isAssignableFrom(parameters[i].getClass())
                || isPrimitiveForm(typesOfParameters[i], parameters[i].getClass())) {
                continue;
            }
            throw new IllegalArgumentException("Method '" + methodSignature
                    + "'s parameter nr" + i
                    + " (" + typesOfParameters[i].getName()
                    + ") does not math argument type ("
                    + parameters.getClass().getName() + ")");
        }
        return typesOfParameters;
    }

    /**
     * Gets the parameter types as a string.
     *
     * @param classTypes the types to get as names
     * @return the parameter types as a string
     */
    private static String getParameterTypesAsString(final Class... classTypes) {
        if (classTypes == null || classTypes.length == 0) {
            return "";
        }

        String parameterTypes = "";
        for (int x = 0; x < classTypes.length; x++) {
            if (classTypes[x] == null) {
                parameterTypes += "null";
            } else {
                parameterTypes += classTypes[x].getName();
            }
            parameterTypes += ", ";
        }
        parameterTypes = parameterTypes.substring(0,
                parameterTypes.length() - 2);
        return parameterTypes;
    }

    /**
     * Removes the braces around the methods signature.
     *
     * @param methodSignature the signature with braces
     * @return the signature without braces
     * @throws NoSuchMethodException if the method has no braces defined
     */
    private static String getSignatureWithoutBraces(final String methodSignature)
    throws NoSuchMethodException {
        try {
            return methodSignature.substring(methodSignature.indexOf('(') + 1,
                    methodSignature.indexOf(')'));
        } catch (StringIndexOutOfBoundsException e) {
            throw new NoSuchMethodException("Method '" + methodSignature
                    + "' has no brackets");
        }
    }

    /**
     * Gets the types in the method signature. The methodSignature
     * is a comma separated string with fully qualified types
     * e.g. java.lang.String, java.lang.Integer
     *
     * @param methodSignature the methodSignature to get the types from
     * @return the types of the signature
     * @throws NoSuchMethodException if the signature is not correct
     */
    private static Class[] getTypesInSignature(final String methodSignature)
    throws NoSuchMethodException {
        String signature = getSignatureWithoutBraces(methodSignature);

        StringTokenizer tokenizer = new StringTokenizer(signature, ", *");
        Class[] typesInSignature = new Class[tokenizer.countTokens()];

        for (int x = 0; tokenizer.hasMoreTokens(); x++) {
            String className = tokenizer.nextToken();
            try {
                typesInSignature[x] = getClassForName(className);
            } catch (ClassNotFoundException e) {
                throw new NoSuchMethodException("Method '" + methodSignature
                        + "'s parameter nr" + (x + 1) + " (" + className
                        + ") not found");
            }
        }

        return typesInSignature;
    }

    /**
     * Tests if the given primitive is the primitive form for the given class.
     *
     * @param primitive the primitive to test
     * @param type the type to check if the primitive corresponds to
     *
     * @return true if the primitive matches the given type, otherwise false
     * TODO check if this is longer necessary for java 1.5 upwards (autoboxing)
     */
    private static boolean isPrimitiveForm(final Class primitive, final Class type) {
        if (primitive  == Integer.TYPE && type == Integer.class) {
            return true;
        }
        if (primitive  == Float.TYPE && type == Float.class) {
            return true;
        }
        if (primitive  == Double.TYPE && type == Double.class) {
            return true;
        }
        if (primitive  == Short.TYPE && type == Short.class) {
            return true;
        }
        if (primitive  == Long.TYPE && type == Long.class) {
            return true;
        }
        if (primitive  == Byte.TYPE && type == Byte.class) {
            return true;
        }
        if (primitive  == Character.TYPE && type == Character.class) {
            return true;
        }
        if (primitive  == Boolean.TYPE && type == Boolean.class) {
            return true;
        }
        return false;
    }
}
