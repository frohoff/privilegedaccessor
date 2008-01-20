package junit.extensions;

import java.lang.reflect.Array;
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
    public static Object instantiate(final Class<?> fromClass,
            final Class<?>[] argumentTypes, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return getConstructor(fromClass, argumentTypes).newInstance(args);
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
    public static Object instantiate(final Class<?> fromClass, final Object... args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return instantiate(fromClass, getParameterTypes(args), args);
    }

    /**
     * Calls a method on the given object instance with the given arguments.
     * Arguments can be object types or representations for primitives.
     * 
     * This method is called with 
     * arguments=o1, o2 if arguments was object[o1, o2] and with
     * arguments=[p1, p2] if arguments were indeed primitives[p1, p2].
     * This is due to the resolution of varargs in Java.
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
        Object[] correctedArguments;
               
        correctedArguments = correctVarargs(arguments);
        
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                getParameterTypes(methodSignature)).
                invoke(instanceOrClass, correctedArguments);
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
     * Gets the class with the given className.
     *
     * @param className the name of the class to get
     * @return the class for the given className
     * @throws ClassNotFoundException if the class could not be found
     */
    private static Class<?> getClassForName(final String className)
            throws ClassNotFoundException {

        if (className.indexOf('[') > -1) {
            Class<?> clazz = getClassForName(className.substring(0, className.indexOf('[')));
            return Array.newInstance(clazz, 0).getClass();
        }
        
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
    private static Constructor<?> getConstructor(final Class<?> type,
            final Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Constructor<?> constructor = type.getDeclaredConstructor(parameterTypes);
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

        Class<?> type = null;
        if (instanceOrClass instanceof Class) {
            type = (Class<?>) instanceOrClass;
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
    private static Method getMethod(final Class<?> type, final String methodName,
            final Class<?>... parameterTypes) throws NoSuchMethodException {
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
            final String methodName, final Class<?>... parameterTypes)
    throws NoSuchMethodException {
        Class<?> type;

        if (instanceOrClass instanceof Class) {
            type = (Class<?>) instanceOrClass;
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
    private static Class<?>[] getParameterTypes(final Object... parameters) {
        if (parameters == null) {
            return null;
        }

        Class<?>[] typesOfParameters = new Class[parameters.length];

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
     * @return the parameter types as class[]
     * @throws NoSuchMethodException if the method could not be found
     * @throws IllegalArgumentException if one of the given parameters
     *                                  doesn't math the given methodSignature
     */
    private static Class<?>[] getParameterTypes(final String methodSignature)
    throws NoSuchMethodException, IllegalArgumentException {
        String signature = getSignatureWithoutBraces(methodSignature);
        
        StringTokenizer tokenizer = new StringTokenizer(signature, ", *");
        Class<?>[] typesInSignature = new Class[tokenizer.countTokens()];
        
        for (int x = 0; tokenizer.hasMoreTokens(); x++) {
            String className = tokenizer.nextToken();
            try {
                typesInSignature[x] = getClassForName(className);
            } catch (ClassNotFoundException e) {
                throw new NoSuchMethodException("Method '" + methodSignature
                        + "' not found");
            }
        }
        return typesInSignature;
    }


    /**
     * Gets the parameter types as a string.
     *
     * @param classTypes the types to get as names
     * @return the parameter types as a string
     */
    private static String getParameterTypesAsString(final Class<?>... classTypes) {
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

}
