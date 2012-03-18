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
 * This class extends {@link StrictPA} by re-throwing checked {@link Exception}s as {@link RuntimeException}s.
 * 
 * 
 * @see StrictPA
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
    * @see StrictPA#toString(Object)
    */
   public static String toString(final Object instanceOrClass) {
      return StrictPA.toString(instanceOrClass);
   }

   /**
    * @see StrictPA#getFieldNames(Object)
    */
   public static Collection<String> getFieldNames(final Object instanceOrClass) {
      return StrictPA.getFieldNames(instanceOrClass);
   }

   /**
    * @see StrictPA#getMethodSignatures(Object)
    */
   public static Collection<String> getMethodSignatures(final Object instanceOrClass) {
      return StrictPA.getMethodSignatures(instanceOrClass);
   }

   /**
    * @see StrictPA#getValue(Object, String)
    */
   public static Object getValue(final Object instanceOrClass, final String fieldName) {
      try {
         return StrictPA.getValue(instanceOrClass, fieldName);
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   /**
    * @see StrictPA#invokeMethod(Object,String,Object)
    */
   public static <T> T instantiate(final Class<? extends T> fromClass, final Class<?>[] argumentTypes, final Object... args) {
      try {
         return StrictPA.instantiate(fromClass, argumentTypes, args);
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   /**
    * @see StrictPA#instantiate(Class, Class[], Object...)
    */
   public static <T> T instantiate(final Class<? extends T> fromClass, final Object... args) {
      try {
         return StrictPA.instantiate(fromClass, args);
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   /**
    * @see StrictPA#invokeMethod(Object, String, Object...)
    */
   public static Object invokeMethod(final Object instanceOrClass, final String methodSignature, final Object... arguments) {
      try {
         return StrictPA.invokeMethod(instanceOrClass, methodSignature, arguments);
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   /**
    * @see StrictPA#setValue(Object, String, Object)
    */
   public static void setValue(final Object instanceOrClass, final String fieldName, final Object value) {
      try {
         StrictPA.setValue(instanceOrClass, fieldName, value);
      } catch (Exception e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }
}
