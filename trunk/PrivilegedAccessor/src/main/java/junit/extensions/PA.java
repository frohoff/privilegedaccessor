/*
 * Copyright 2004-2012 Sebastian Dietrich (Sebastian.Dietrich@e-movimento.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package junit.extensions;

import java.util.Collection;

/**
 * This class is used to access a method or field of an object no matter what the access modifier of the method or field. The syntax
 * for accessing fields and methods is out of the ordinary because this class uses reflection to peel away protection.
 * <p>
 * a.k.a. The "ObjectMolester"
 * <p>
 * Here is an example of using this to access a private member: <br>
 * Given the following class <code>MyClass</code>: <br>
 * 
 * <pre>
 * public class MyClass {
 *    private String name; // private attribute
 * 
 *    // private constructor
 *    private MyClass() {
 *       super();
 *    }
 * 
 *    // private method
 *    private void setName(String newName) {
 *       this.name = newName;
 *    }
 * }
 * </pre>
 * 
 * We now want to access the class: <br>
 * 
 * <pre>
 * MyClass myObj = PA.instantiate(MyClass.class);
 * PA.invokeMethod(myObj, &quot;setName(java.lang.String)&quot;, &quot;myNewName&quot;);
 * String name = PA.getValue(myObj, &quot;name&quot;);
 * </pre>
 * 
 * This class extends {@link PrivilegedAccessor} by re-throwing checked {@link Exception}s as {@link RuntimeException}s.
 * 
 * 
 * @see PrivilegedAccessor
 * 
 * @author Sebastian Dietrich (sebastian.dietrich@e-movimento.com)
 * @author Lubos Bistak (lubos@bistak.sk)
 */
public class PA {
   /**
    * Private constructor to make it impossible to instantiate this class.
    */
   private PA() {
      assert false : "You mustn't instantiate PA, use its methods statically";
   }

   /**
    * @see PrivilegedAccessor#toString(Object)
    */
   public static String toString(final Object instanceOrClass) {
      return PrivilegedAccessor.toString(instanceOrClass);
   }

   /**
    * @see PrivilegedAccessor#getFieldNames(Object)
    */
   public static Collection<String> getFieldNames(final Object instanceOrClass) {
      return PrivilegedAccessor.getFieldNames(instanceOrClass);
   }

   /**
    * @see PrivilegedAccessor#getMethodSignatures(Object)
    */
   public static Collection<String> getMethodSignatures(final Object instanceOrClass) {
      return PrivilegedAccessor.getMethodSignatures(instanceOrClass);
   }

   /**
    * @see PrivilegedAccessor#getValue(Object, String)
    */
   public static Object getValue(final Object instanceOrClass, final String fieldName) {
      try {
         return PrivilegedAccessor.getValue(instanceOrClass, fieldName);
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   /**
    * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
    */
   public static <T> T instantiate(final Class<? extends T> fromClass, final Class<?>[] argumentTypes, final Object... args) {
      try {
         return PrivilegedAccessor.instantiate(fromClass, argumentTypes, args);
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   /**
    * @see PrivilegedAccessor#instantiate(Class, Class[], Object...)
    */
   public static <T> T instantiate(final Class<? extends T> fromClass, final Object... args) {
      try {
         return PrivilegedAccessor.instantiate(fromClass, args);
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   /**
    * @see PrivilegedAccessor#invokeMethod(Object, String, Object...)
    */
   public static Object invokeMethod(final Object instanceOrClass, final String methodSignature, final Object... arguments) {
      try {
         return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, correctVarargs(arguments));
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   /**
    * Corrects varargs to their initial form. If you call a method with an object-array as last argument the Java varargs mechanism
    * converts this array in single arguments. This method returns an object array if the arguments are all of the same type.
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
    * Tests if the arguments were changed by vararg. Arguments are changed by vararg if they are of a non primitive array type. E.g.
    * arguments[] = Object[String[]] is converted to String[] while e.g. arguments[] = Object[int[]] is not converted and stays
    * Object[int[]]
    * 
    * Unfortunately we can't detect the difference for arg = Object[primitive] since arguments[] = Object[Object[primitive]] which is
    * converted to Object[primitive] and arguments[] = Object[primitive] which stays Object[primitive]
    * 
    * and we can't detect the difference for arg = Object[non primitive] since arguments[] = Object[Object[non primitive]] is converted
    * to Object[non primitive] and arguments[] = Object[non primitive] stays Object[non primitive]
    * 
    * @param parameters the parameters
    * @return true if parameters were changes by varargs, false otherwise
    */
   private static boolean changedByVararg(final Object[] parameters) {
      if (parameters.length == 0 || parameters[0] == null) {
         return false;
      }

      if (parameters.getClass() == Object[].class) {
         return false;
      }

      return true;
   }

   /**
    * @see PrivilegedAccessor#setValue(Object, String, Object)
    */
   public static void setValue(final Object instanceOrClass, final String fieldName, final Object value) {
      try {
         PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }
}
