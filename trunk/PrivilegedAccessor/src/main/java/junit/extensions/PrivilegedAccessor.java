package junit.extensions;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * This class is used to access a method or field of an object no matter what the access modifier of the method or field. The syntax
 * for accessing fields and methods is out of the ordinary because this class uses reflection to peel away protection.
 * <p>
 * a.k.a. The "ObjectMolester"
 * <p>
 * Here is an example of using this to access a private member: <br>
 * <code>myObject</code> is an object of type <code>MyClass</code>. <code>setName(String)</code> is a private method of
 * <code>MyClass</code>.
 * 
 * <pre>
 * PrivilegedAccessor.invokeMethod(myObject, &quot;setName(java.lang.String)&quot;, &quot;newName&quot;);
 * </pre>
 * 
 * @author Charlie Hubbard (chubbard@iss.net)
 * @author Prashant Dhokte (pdhokte@iss.net)
 * @author Sebastian Dietrich (sebastian.dietrich@e-movimento.com)
 * 
 * @deprecated use PA instead. PA improves the functionality of PrivilegedAccessor by introducing support for varargs.
 */
public final class PrivilegedAccessor {
   /**
    * Private constructor to make it impossible to instantiate this class.
    */
   private PrivilegedAccessor() {
      assert false: "You mustn't instantiate PrivilegedAccessor, use its methods statically";
   }

   /**
    * Returns a string representation of the given object.
    * 
    * @param instanceOrClass the object or class to get a string representation of
    * @return a string representation of the given object
    */
   public static String toString(final Object instanceOrClass) {
      Collection<String> fields = getFieldNames(instanceOrClass);

      if (fields.size() == 0) {
         return getClass(instanceOrClass).getName();
      }

      StringBuffer sb = new StringBuffer();

      sb.append(getClass(instanceOrClass).getName() + " {");

      for (String fieldName : fields) {
         try {
            sb.append(fieldName + "=" + getValue(instanceOrClass, fieldName) + ", ");
         } catch (NoSuchFieldException e) {
            assert false: "It should always be possible to get a field that was just here";
         }
      }

      sb.replace(sb.lastIndexOf(", "), sb.length(), "}");
      return sb.toString();
   }

   /**
    * Gets the name of all fields (public, private, protected, default) of the given instance or class. This includes as well all
    * fields (public, private, protected, default) of all its super classes.
    * 
    * @param instanceOrClass the instance or class to get the fields of
    * @return the collection of field names of the given instance or class
    */
   public static Collection<String> getFieldNames(final Object instanceOrClass) {
      if (instanceOrClass == null) {
         return new ArrayList<String>();
      }

      Class<?> clazz = getClass(instanceOrClass);
      Field[] fields = clazz.getDeclaredFields();
      Collection<String> fieldNames = new ArrayList<String>(fields.length);

      for (Field field : fields) {
         fieldNames.add(field.getName());
      }
      fieldNames.addAll(getFieldNames(clazz.getSuperclass()));

      return fieldNames;
   }

   /**
    * Gets the signatures of all methods (public, private, protected, default) of the given instance or class. This includes as well
    * all methods (public, private, protected, default) of all its super classes. This does not include constructors.
    * 
    * @param instanceOrClass the instance or class to get the method signatures of
    * @return the collection of method signatures of the given instance or class
    */
   public static Collection<String> getMethodSignatures(final Object instanceOrClass) {
      if (instanceOrClass == null) {
         return new ArrayList<String>();
      }

      Class<?> clazz = getClass(instanceOrClass);
      Method[] methods = clazz.getDeclaredMethods();
      Collection<String> methodSignatures = new ArrayList<String>(methods.length + Object.class.getDeclaredMethods().length);

      for (Method method : methods) {
         methodSignatures.add(method.getName() + "(" + getParameterTypesAsString(method.getParameterTypes()) + ")");
      }
      methodSignatures.addAll(getMethodSignatures(clazz.getSuperclass()));

      return methodSignatures;
   }

   /**
    * Gets the value of the named field and returns it as an object. If instanceOrClass is a class then a static field is returned.
    * 
    * @param instanceOrClass the instance or class to get the field from
    * @param fieldName the name of the field
    * @return an object representing the value of the field
    * @throws NoSuchFieldException if the field does not exist
    */
   public static Object getValue(final Object instanceOrClass, final String fieldName) throws NoSuchFieldException {
      Field field = getField(instanceOrClass, fieldName);
      try {
         return field.get(instanceOrClass);
      } catch (IllegalAccessException e) {
         assert false: "getField() should have setAccessible(true), so an IllegalAccessException should not occur in this place";
         return null;
      }
   }

   /**
    * Instantiates an object of the given class with the given arguments and the given argument types. If you want to instantiate a
    * member class, you must provide the object it is a member of as first argument.
    * 
    * @param fromClass the class to instantiate an object from
    * @param args the arguments to pass to the constructor
    * @param argumentTypes the types of the arguments of the constructor
    * @return an object of the given type
    * @throws IllegalArgumentException if the number of actual and formal parameters differ; if an unwrapping conversion for primitive
    *            arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding formal
    *            parameter type by a method invocation conversion.
    * @throws IllegalAccessException if this Constructor object enforces Java language access control and the underlying constructor is
    *            inaccessible.
    * @throws InvocationTargetException if the underlying constructor throws an exception.
    * @throws NoSuchMethodException if the constructor could not be found
    * @throws InstantiationException if the class that declares the underlying constructor represents an abstract class.
    * 
    * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
    */
   public static <T> T instantiate(final Class<? extends T> fromClass, final Class<?>[] argumentTypes, final Object[] args)
      throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
      return getConstructor(fromClass, argumentTypes).newInstance(args);
   }

   /**
    * Instantiates an object of the given class with the given arguments. If you want to instantiate a member class, you must provide
    * the object it is a member of as first argument.
    * 
    * @param fromClass the class to instantiate an object from
    * @param args the arguments to pass to the constructor
    * @return an object of the given type
    * @throws IllegalArgumentException if the number of actual and formal parameters differ; if an unwrapping conversion for primitive
    *            arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding formal
    *            parameter type by a method invocation conversion.
    * @throws IllegalAccessException if this Constructor object enforces Java language access control and the underlying constructor is
    *            inaccessible.
    * @throws InvocationTargetException if the underlying constructor throws an exception.
    * @throws NoSuchMethodException if the constructor could not be found
    * @throws InstantiationException if the class that declares the underlying constructor represents an abstract class.
    * 
    * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
    */
   public static <T> T instantiate(final Class<? extends T> fromClass, final Object[] args) throws IllegalArgumentException,
      InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return instantiate(fromClass, getParameterTypes(args), args);
   }

   /**
    * Calls a method on the given object instance with the given arguments. Arguments can be object types or representations for
    * primitives.
    * 
    * @param instanceOrClass the instance or class to invoke the method on
    * @param methodSignature the name of the method and the parameters <br>
    *           (e.g. "myMethod(java.lang.String, com.company.project.MyObject)")
    * @param arguments an array of objects to pass as arguments
    * @return the return value of this method or null if void
    * @throws IllegalAccessException if the method is inaccessible
    * @throws InvocationTargetException if the underlying method throws an exception.
    * @throws NoSuchMethodException if no method with the given <code>methodSignature</code> could be found
    * @throws IllegalArgumentException if an argument couldn't be converted to match the expected type
    */
   public static Object invokeMethod(final Object instanceOrClass, final String methodSignature, final Object[] arguments)
      throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (methodSignature.indexOf('(') == -1 || methodSignature.indexOf('(') >= methodSignature.indexOf(')')) {
         throw new NoSuchMethodException(methodSignature);
      }
      return getMethod(instanceOrClass, getMethodName(methodSignature), getParameterTypes(methodSignature)).invoke(instanceOrClass,
         arguments);
   }

   /**
    * Sets the value of the named field. If instanceOrClass is a class then a static field is returned.
    * 
    * @param instanceOrClass the instance or class to set the field
    * @param fieldName the name of the field
    * @param value the new value of the field
    * @throws NoSuchFieldException if no field with the given <code>fieldName</code> can be found
    */
   public static void setValue(final Object instanceOrClass, final String fieldName, final Object value) throws NoSuchFieldException {
      Field field = getField(instanceOrClass, fieldName);
      try {
         field.set(instanceOrClass, value);
      } catch (IllegalAccessException e) {
         assert false: "getField() should have setAccessible(true), so an IllegalAccessException should not occur in this place";

      }
   }

   /**
    * Gets the class with the given className.
    * 
    * @param className the name of the class to get
    * @return the class for the given className
    * @throws ClassNotFoundException if the class could not be found
    */
   private static Class<?> getClassForName(final String className) throws ClassNotFoundException {

      if (className.indexOf('[') > -1) {
         Class<?> clazz = getClassForName(className.substring(0, className.indexOf('[')));
         return Array.newInstance(clazz, 0).getClass();
      }

      try {
         return Class.forName(className);
      } catch (ClassNotFoundException e) {
         return getSpecialClassForName(className);
      }
   }

   /**
    * Gets special classes for the given className. Special classes are primitives and "standard" Java types (like String)
    * 
    * @param className the name of the class to get
    * @return the class for the given className
    * @throws ClassNotFoundException if the class could not be found
    */
   private static Class<?> getSpecialClassForName(final String className) throws ClassNotFoundException {
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
      if (missesPackageName(className)) {
         return getStandardClassForName(className);
      }

      throw new ClassNotFoundException(className);
   }

   /**
    * Gets a 'standard' java class for the given className.
    * 
    * @param className the className
    * @return the class for the given className (if any)
    * @throws ClassNotFoundException of no 'standard' java class was found for the given className
    */
   private static Class<?> getStandardClassForName(String className) throws ClassNotFoundException {
      try {
         return Class.forName("java.lang." + className);
      } catch (ClassNotFoundException e) {
         try {
            return Class.forName("java.util." + className);
         } catch (ClassNotFoundException e1) {
            throw new ClassNotFoundException(className);
         }
      }
   }

   /**
    * Tests if the given className possibly misses its package name.
    * 
    * @param className the className
    * @return true if the className might miss its package name, otherwise false
    */
   private static boolean missesPackageName(String className) {
      if (className.contains(".")) {
         return false;
      }
      if (className.startsWith(className.substring(0, 1).toUpperCase())) {
         return true;
      }
      return false;
   }

   /**
    * Gets the constructor for a given class with the given parameters.
    * 
    * @param type the class to instantiate
    * @param parameterTypes the types of the parameters
    * @return the constructor
    * @throws NoSuchMethodException if the method could not be found
    */
   private static <T> Constructor<T> getConstructor(final Class<T> type, final Class<?>[] parameterTypes) throws NoSuchMethodException {
      Constructor<T> constructor = type.getDeclaredConstructor(parameterTypes);
      constructor.setAccessible(true);
      return constructor;
   }

   /**
    * Return the named field from the given instance or class. Returns a static field if instanceOrClass is a class.
    * 
    * @param instanceOrClass the instance or class to get the field from
    * @param fieldName the name of the field to get
    * @return the field
    * @throws NoSuchFieldException if no such field can be found
    * @throws InvalidParameterException if instanceOrClass was null
    */
   private static Field getField(final Object instanceOrClass, final String fieldName) throws NoSuchFieldException,
      InvalidParameterException {
      if (instanceOrClass == null) {
         throw new InvalidParameterException("Can't get field on null object/class");
      }

      Class<?> type = getClass(instanceOrClass);

      try {
         Field field = type.getDeclaredField(fieldName);
         field.setAccessible(true);
         return field;
      } catch (NoSuchFieldException e) {
         if (type.getSuperclass() == null) {
            throw new NoSuchFieldException(fieldName);
         }
         return getField(type.getSuperclass(), fieldName);
      }
   }

   /**
    * Gets the class of the given parameter. If the parameter is a class, it is returned, if it is an object, its class is returned
    * 
    * @param instanceOrClass the instance or class to get the class of
    * @return the class of the given parameter
    */
   private static Class<?> getClass(final Object instanceOrClass) {
      if (instanceOrClass instanceof Class) {
         return (Class<?>) instanceOrClass;
      }

      return instanceOrClass.getClass();
   }

   /**
    * Return the named method with a method signature matching classTypes from the given class.
    * 
    * @param type the class to get the method from
    * @param methodName the name of the method to get
    * @param parameterTypes the parameter-types of the method to get
    * @return the method
    * @throws NoSuchMethodException if the method could not be found
    */
   private static Method getMethod(final Class<?> type, final String methodName, final Class<?>[] parameterTypes)
      throws NoSuchMethodException {
      try {
         return type.getDeclaredMethod(methodName, parameterTypes);
      } catch (NoSuchMethodException e) {
         if (type.getSuperclass() == null) {
            throw new NoSuchMethodException(type.getName() + "." + methodName + "(" + getParameterTypesAsString(parameterTypes) + ")");
         }
         return getMethod(type.getSuperclass(), methodName, parameterTypes);
      }
   }

   /**
    * Gets the method with the given name and parameters from the given instance or class. If instanceOrClass is a class, then we get a
    * static method.
    * 
    * @param instanceOrClass the instance or class to get the method of
    * @param methodName the name of the method
    * @param parameterTypes the parameter-types of the method to get
    * @return the method
    * @throws NoSuchMethodException if the method could not be found
    */
   private static Method getMethod(final Object instanceOrClass, final String methodName, final Class<?>[] parameterTypes)
      throws NoSuchMethodException {
      Class<?> type;

      type = getClass(instanceOrClass);

      Method accessMethod = getMethod(type, methodName, parameterTypes);
      accessMethod.setAccessible(true);
      return accessMethod;
   }

   /**
    * Gets the name of a method.
    * 
    * @param methodSignature the signature of the method
    * @return the name of the method
    */
   private static String getMethodName(final String methodSignature) {
      try {
         return methodSignature.substring(0, methodSignature.indexOf('(')).trim();
      } catch (StringIndexOutOfBoundsException e) {
         assert false: "Signature must have been checked before this method was called";
         return null;
      }
   }

   /**
    * Gets the types of the parameters.
    * 
    * @param parameters the parameters
    * @return the class-types of the arguments
    */
   private static Class<?>[] getParameterTypes(final Object[] parameters) {
      if (parameters == null) {
         return new Class[0];
      }

      Class<?>[] typesOfParameters = new Class[parameters.length];

      for (int i = 0; i < parameters.length; i++) {
         typesOfParameters[i] = parameters[i].getClass();
      }
      return typesOfParameters;
   }

   /**
    * Gets the types of the given parameters. If the parameters don't match the given methodSignature an IllegalArgumentException is
    * thrown.
    * 
    * @param methodSignature the signature of the method
    * @return the parameter types as class[]
    * @throws NoSuchMethodException if the method could not be found
    * @throws IllegalArgumentException if one of the given parameters doesn't math the given methodSignature
    */
   private static Class<?>[] getParameterTypes(final String methodSignature) throws NoSuchMethodException, IllegalArgumentException {
      String signature = getSignatureWithoutBraces(methodSignature);

      StringTokenizer tokenizer = new StringTokenizer(signature, ", *");
      Class<?>[] typesInSignature = new Class[tokenizer.countTokens()];

      for (int x = 0; tokenizer.hasMoreTokens(); x++) {
         String className = tokenizer.nextToken();
         try {
            typesInSignature[x] = getClassForName(className);
         } catch (ClassNotFoundException e) {
            throw new NoSuchMethodException(methodSignature);
         }
      }
      return typesInSignature;
   }

   /**
    * Gets the parameter types as a string.
    * 
    * @param classTypes the types to get as names.
    * @return the parameter types as a string
    * 
    * @see java.lang.Class#argumentTypesAsString()
    */
   private static String getParameterTypesAsString(final Class<?>[] classTypes) {
      assert classTypes != null: "getParameterTypes() should have been called before this method and should have provided not-null classTypes";
      if (classTypes.length == 0) {
         return "";
      }

      StringBuilder parameterTypes = new StringBuilder();
      for (Class<?> clazz : classTypes) {
         assert clazz != null: "getParameterTypes() should have been called before this method and should have provided not-null classTypes";
         parameterTypes.append(clazz.getName()).append(", ");
      }

      return parameterTypes.substring(0, parameterTypes.length() - 2);
   }

   /**
    * Removes the braces around the methods signature.
    * 
    * @param methodSignature the signature with braces
    * @return the signature without braces
    */
   private static String getSignatureWithoutBraces(final String methodSignature) {
      try {
         return methodSignature.substring(methodSignature.indexOf('(') + 1, methodSignature.indexOf(')'));
      } catch (IndexOutOfBoundsException e) {
         assert false: "signature must have been checked before this method";
         return null;
      }
   }

}
