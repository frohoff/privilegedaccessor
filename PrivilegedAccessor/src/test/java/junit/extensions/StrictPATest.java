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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the class <code>StrictPA</code>.
 */
public class StrictPATest {

   /**
    * An instance of a test-subclass.
    */
   private Child  child;

   /**
    * An instance of a test-subclass in a variable of type superclass.
    */
   private Parent childInParent;
   /**
    * an instance of a test-superclass.
    */
   private Parent parent;

   /**
    * Sets up the test-environment by instantiating the test-instances.
    * 
    * @see junit.framework.TestCase#setUp()
    */
   @Before
   public final void setUp() {
      this.parent = new Parent("Charlie");
      this.child = new Child("Charlie");
      this.childInParent = new Child("Charlie");
   }

   /**
    * Tears down the test-environment by deleting the test-instances.
    * 
    * @see junit.framework.TestCase#tearDown()
    */
   @After
   public final void tearDown() {
      this.parent = null;
      this.child = null;
      this.childInParent = null;
   }

   /**
    * Tests the method <code>toString</code>
    */
   @Test
   public final void testToString() throws Exception {
      assertEquals("java.lang.Object", StrictPA.toString(new Object()));
      assertEquals(this.parent.toString(), StrictPA.toString(this.parent));
      assertEquals(this.child.toString(), StrictPA.toString(this.child));
      assertEquals(this.childInParent.toString(), StrictPA.toString(this.childInParent));
   }

   /**
    * Tests the method <code>getFieldNames</code>.
    * 
    * @see junit.extensions.StrictPA#getFieldNames(java.lang.Object)
    */
   @Test
   public final void testGetFieldNames() throws Exception {
      Collection<String> testFieldNames = new ArrayList<String>();

      assertEquals(testFieldNames, StrictPA.getFieldNames(null));

      assertEquals(testFieldNames, StrictPA.getFieldNames(Object.class));

      testFieldNames.add("privateName");
      testFieldNames.add("privateObject");
      testFieldNames.add("privateStaticInt");
      testFieldNames.add("privateFinalInt");
      testFieldNames.add("privateFinalString");
      testFieldNames.add("privateStaticFinalInt");
      testFieldNames.add("privateStaticFinalString");
      assertEquals(testFieldNames, StrictPA.getFieldNames(this.parent));

      testFieldNames = Arrays.asList(new String[] {"privateInt", "privateObject", "privateLong", "privateShort", "privateByte",
         "privateChar", "privateBoolean", "privateFloat", "privateDouble", "privateInts", "privateStrings", "privateObjects",
         "privateName", "privateStaticInt"});

      assertTrue("getFieldNames didn't return all field names", StrictPA.getFieldNames(this.child).containsAll(testFieldNames));
      assertTrue("getFieldNames didn't return all field names", StrictPA.getFieldNames(this.childInParent).containsAll(testFieldNames));
   }

   /**
    * Tests the method <code>getMethodSignatures</code>.
    * 
    * @see junit.StrictPA.PA#getMethodSigantures(java.lang.Object)
    */
   @Test
   public final void testGetMethodsignatures() throws Exception {
      Collection<String> testMethodSignatures = new ArrayList<String>();

      assertEquals(testMethodSignatures, StrictPA.getMethodSignatures(null));

      testMethodSignatures = Arrays.asList(new String[] {"hashCode()", "getClass()", "finalize()", "clone()", "wait(long, int)",
         "wait()", "wait(long)", "registerNatives()", "equals(java.lang.Object)", "toString()", "notify()", "notifyAll()"});
      assertTrue("getMethodSignatures didn't return correct signatures",
         StrictPA.getMethodSignatures(Object.class).containsAll(testMethodSignatures));

      testMethodSignatures = Arrays.asList(new String[] {"equals(java.lang.Object)", "getName()", "setName(java.lang.String)",
         "setName()", "setStaticInt(int)", "hashCode()", "getClass()", "finalize()", "clone()", "wait(long, int)", "wait()",
         "wait(long)", "registerNatives()", "equals(java.lang.Object)", "toString()", "notify()", "notifyAll()"});
      assertTrue("getMethodSignatures didn't return correct signatures",
         StrictPA.getMethodSignatures(this.parent).containsAll(testMethodSignatures));
   }

   /**
    * Tests the method <code>getValue</code>.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#getValue(java.lang.Object, java.lang.String)
    */
   @Test
   public final void testGetValue() throws Exception {
      assertEquals("Charlie", StrictPA.getValue(this.parent, "privateName"));

      assertEquals("Charlie", StrictPA.getValue(this.child, "privateName"));
      assertEquals(new Integer(8), StrictPA.getValue(this.child, "privateInt"));

      assertEquals("Charlie", StrictPA.getValue(this.childInParent, "privateName"));
      assertEquals(new Integer(8), StrictPA.getValue(this.childInParent, "privateInt"));
   }

   /**
    * Tests the method <code>getValue</code> with a static field.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#getValue(java.lang.Object, java.lang.String)
    */
   @Test
   public void testGetValueOfStaticField() throws Exception {
      assertEquals(new Integer(1), StrictPA.getValue(this.parent, "privateStaticInt"));
      assertEquals(new Integer(1), StrictPA.getValue(Parent.class, "privateStaticInt"));
   }

   /**
    * Tests the method <code>getValue</code> with a non-existing field.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#getValue(java.lang.Object, java.lang.String)
    */
   @Test
   public void testGetValueOnInvalidField() throws Exception {
      try {
         StrictPA.getValue(this.parent, "noSuchField");
         fail("should throw NoSuchFieldException");
      } catch (NoSuchFieldException e) {
         // that is what we expect
      }

      try {
         StrictPA.getValue(this.child, "noSuchField");
         fail("should throw NoSuchFieldException");
      } catch (NoSuchFieldException e) {
         // that is what we expect
      }

      try {
         StrictPA.getValue(this.childInParent, "noSuchField");
         fail("should throw NoSuchFieldException");
      } catch (NoSuchFieldException e) {
         // that is what we expect
      }

      try {
         StrictPA.getValue(Parent.class, "noSuchField");
         fail("should throw NoSuchFieldException");
      } catch (NoSuchFieldException e) {
         // that is what we expect
      }

      try {
         StrictPA.getValue(null, "noSuchField");
         fail("should throw InvalidParameterException");
      } catch (InvalidParameterException e) {
         // that is what we expect
      }
   }

   /**
    * Tests the method <code>instantiate</code>.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#instantiate(java.lang.Class)
    */
   @Test
   public void testInstantiate() throws Exception {
      Parent tp = StrictPA.instantiate(Parent.class);
      assertEquals(this.parent, tp);
      assertNotSame(this.parent, tp);
      assertEquals(this.parent, StrictPA.instantiate(Parent.class));
      assertEquals(this.parent, StrictPA.instantiate(Parent.class, "Charlie"));
      assertEquals(this.child, StrictPA.instantiate(Child.class, "Charlie", 8));
      assertEquals(this.childInParent, StrictPA.instantiate(Child.class, "Charlie", 8));
      assertEquals(this.childInParent, StrictPA.instantiate(Child.class, new Class[] {String.class, Integer.class}, "Charlie", 8));
   }

   /**
    * Tests the method <code>instantiate</code>.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#instantiate(java.lang.Class)
    */
   @Test
   public void testInstantiateOnInvalidParameters() throws Exception {
      try {
         StrictPA.instantiate(Parent.class, 21);
         fail("instantiating with wrong parameter type should throw Exception");
      } catch (Exception e) {
         // this is what we expect
      }

      try {
         StrictPA.instantiate(Child.class, "Charlie", "Brown");
         fail("instantiating with wrong second parameter type should throw Exception");
      } catch (Exception e) {
         // this is what we expect
      }

      try {
         StrictPA.instantiate(Child.class, new Class[] {String.class, String.class}, "Charlie", 8);
         fail("instantiating with unmatching parameter types should throw Exception");
         StrictPA.instantiate(Child.class, new Class[] {String.class, Integer.class}, "Charlie", "Brown");
         fail("instantiating with unmatching parameter types should throw Exception");
      } catch (Exception e) {
         // this is what we expect
      }

      try {
         StrictPA.instantiate(Child.class, new Class[] {String.class, Integer.class, String.class}, "Charlie", 8, "Brown");
         fail("instantiating with wrong parameter count should throw Exception");
      } catch (Exception e) {
         // this is what we expect
      }
   }

   /**
    * Tests the constructor of StrictPA.
    * 
    * @throws Exception if something went wrong
    * @see junit.extensions.StrictPA#instantiate(java.lang.Class)
    */
   @Test
   public final void testInstantiationThrowsException() throws Exception {
      try {
         StrictPA.instantiate(StrictPA.class);
         fail("Instantiating StrictPA should throw Exception - you must have enabled assertions to run unit-tests");
      } catch (InvocationTargetException e) {
         // thats what we expect
      }
   }

   /**
    * Tests the method <code>invokeMethod</code>.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethod() throws Exception {
      assertEquals("Charlie", StrictPA.invokeMethod(this.parent, "getName()"));

      StrictPA.invokeMethod(this.parent, "setName(java.lang.String)", "Herbert");
      assertEquals("Herbert", StrictPA.getValue(this.parent, "privateName"));

      StrictPA.invokeMethod(this.parent, "setName(java.lang.String)", (Object[]) null);
      assertEquals(null, StrictPA.getValue(this.parent, "privateName"));

      StrictPA.invokeMethod(this.parent, "setName()");
      assertEquals("Chaplin", StrictPA.getValue(this.parent, "privateName"));

      StrictPA.invokeMethod(this.child, "setName(java.lang.String)", "Hubert");
      assertEquals("Hubert", StrictPA.getValue(this.child, "privateName"));

      StrictPA.invokeMethod(this.child, "setInt(int)", 3);
      assertEquals(3, StrictPA.invokeMethod(this.child, "getInt()"));

      StrictPA.invokeMethod(this.childInParent, "setName(java.lang.String)", "Norbert");
      StrictPA.invokeMethod(this.childInParent, "setInt(int)", 3);
      assertEquals("Norbert", StrictPA.getValue(this.childInParent, "privateName"));
      assertEquals(3, StrictPA.getValue(this.childInParent, "privateInt"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with not fully declared types.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithNotFullyDeclaredTypes() throws Exception {
      StrictPA.invokeMethod(this.parent, "setName(String)", "Hubert");
      assertEquals("Hubert", StrictPA.getValue(this.parent, "privateName"));

      StrictPA.invokeMethod(this.parent, "setObject(Object)", "Heribert");
      assertEquals("Heribert", StrictPA.getValue(this.parent, "privateObject"));

      Collection<String> testCollection = new ArrayList<String>();
      StrictPA.invokeMethod(this.child, "setPrivateCollection(Collection)", testCollection);
      assertEquals(testCollection, StrictPA.getValue(this.child, "privateCollection"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with different primitives.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithPrimitives() throws Exception {
      StrictPA.invokeMethod(this.child, "setInt(int)", 3);
      assertEquals(3, StrictPA.invokeMethod(this.child, "getInt()"));

      StrictPA.invokeMethod(this.child, "setPrivateLong(long)", 3L);
      assertEquals(3L, StrictPA.invokeMethod(this.child, "getPrivateLong()"));

      StrictPA.invokeMethod(this.child, "setPrivateShort(short)", (short) 3);
      assertEquals((short) 3, StrictPA.invokeMethod(this.child, "getPrivateShort()"));

      StrictPA.invokeMethod(this.child, "setPrivateByte(byte)", (byte) 3);
      assertEquals((byte) 3, StrictPA.invokeMethod(this.child, "getPrivateByte()"));

      StrictPA.invokeMethod(this.child, "setPrivateBoolean(boolean)", true);
      assertEquals(true, StrictPA.invokeMethod(this.child, "isPrivateBoolean()"));

      StrictPA.invokeMethod(this.child, "setPrivateChar(char)", 'A');
      assertEquals('A', StrictPA.invokeMethod(this.child, "getPrivateChar()"));

      StrictPA.invokeMethod(this.child, "setPrivateFloat(float)", 3.1f);
      assertEquals(3.1f, StrictPA.invokeMethod(this.child, "getPrivateFloat()"));

      StrictPA.invokeMethod(this.child, "setPrivateDouble(double)", 3.1);
      assertEquals(3.1, StrictPA.invokeMethod(this.child, "getPrivateDouble()"));
   }

   /**
    * Tests the method <code>invokeMethod</code> on a non-existing method.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodOnInvalidMethodName() throws Exception {
      try {
         StrictPA.invokeMethod(this.child, "getInt");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setInt)(", 5);
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "getInt)");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "noSuchMethod()", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.parent, "noSuchMethod()", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.childInParent, "noSuchMethod()", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(Parent.class, "noSuchMethod()", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }
   }

   /**
    * Tests the method <code>invokeMethod</code> with an array.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithArray() throws Exception {
      Object[] args = {5};
      StrictPA.invokeMethod(this.childInParent, "setInt(int)", args);
      assertEquals(5, StrictPA.getValue(this.childInParent, "privateInt"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with a typed collection.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithTypedCollection() throws Exception {
      Collection<String> testCollection = new ArrayList<String>();
      StrictPA.invokeMethod(this.child, "setPrivateCollection(java.util.Collection)", testCollection);
      assertEquals(testCollection, StrictPA.getValue(this.child, "privateCollection"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with invalid arguments.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithInvalidSignature() throws Exception {
      try {
         StrictPA.invokeMethod(this.child, "setName", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect - since the signature is missing
      }

      try {
         StrictPA.invokeMethod(this.child, "setName java.lang.String)", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect - since the first brace is missing
      }

      try {
         StrictPA.invokeMethod(this.child, "setName(java.lang.String", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect - since the last brace is missing
      }

      try {
         StrictPA.invokeMethod(this.child, "setName(java.lang.SString)", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect - since the signature denotes a non-existing class
      }

   }

   /**
    * Tests the method <code>invokeMethod</code> with invalid arguments.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithInvalidArguments() throws Exception {
      try {
         StrictPA.invokeMethod(this.child, "setData(java.lang.String)", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setData(non.existing.package.NonExistingClass)", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setData()");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.parent, "setData(java.lang.String)", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.childInParent, "setData(java.lang.String)", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setName(java.lang.String)", 2);
         fail("should throw IllegalArgumentException");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setName(.String)", "Heribert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setName(string)", "Heribert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setName(NotAString)", "Heribert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setData(Integer)", 2);
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setInt(java.lang.String)", (Object[]) null);
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setInt(int)", "Herbert");
         fail("should throw IllegalArgumentException");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(Parent.class, "setStaticInt(java.lang.String)", "Herbert");
         fail("should throw NoSuchMethodException");
      } catch (NoSuchMethodException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setInt(int)", (Object[]) null);
         fail("should throw IllegalArgumentException");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }
   }

   /**
    * Tests the method <code>invokeMethod</code> with several primitive arguments.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithMoreThanOnePrimitive() throws Exception {
      StrictPA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", 5, 3);
      assertEquals(8, StrictPA.getValue(this.child, "privateInt"));

      StrictPA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Integer(5), new Integer(4));
      assertEquals(9, StrictPA.getValue(this.child, "privateInt"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with arrays as arguments.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithArrays() throws Exception {
      int[] ints = new int[] {5, 3};
      StrictPA.invokeMethod(this.child, "setPrivateInts(int[])", ints);
      assertEquals(ints, StrictPA.getValue(this.child, "privateInts"));

      ints = new int[] {5};
      StrictPA.invokeMethod(this.child, "setPrivateInts(int[])", ints);
      assertEquals(ints, StrictPA.getValue(this.child, "privateInts"));

      String[] strings = new String[] {"Hello", "Dolly"};
      StrictPA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object[]) strings);
      assertEquals(strings, StrictPA.getValue(this.child, "privateStrings"));

      strings = new String[] {"Hello"};
      StrictPA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object[]) strings);
      assertEquals(strings, StrictPA.getValue(this.child, "privateStrings"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with empty arrays as arguments.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithEmptyArrays() throws Exception {
      StrictPA.invokeMethod(this.child, "setPrivateInts(int[])", (int[]) null);
      assertEquals(null, StrictPA.getValue(this.child, "privateInts"));

      int[] ints = new int[0];
      StrictPA.invokeMethod(this.child, "setPrivateInts(int[])", ints);
      assertEquals(ints, StrictPA.getValue(this.child, "privateInts"));

      StrictPA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object) null);
      assertEquals(null, StrictPA.getValue(this.child, "privateStrings"));

      String[] strings = new String[0];
      StrictPA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object) strings);
      assertEquals(strings, StrictPA.getValue(this.child, "privateStrings"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with filled arrays as arguments.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodThatRequireArrays() throws Exception {
      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", (Object) new Object[] {new Integer(1)});
      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {new Integer(1)});

      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", (Object) new Object[] {"Dolly"});
      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {"Dolly"});

      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", (Object) new Object[] {"Hello", new Integer(1)});
      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {"Hello", new Integer(1)});

      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", (Object) new Object[] {"Hello", new Integer(1)});
      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {"Hello", new Integer(1)});
   }

   /**
    * Tests the method <code>invokeMethod</code> with filled arrays and something else as arguments.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodThatRequireArraysAndSomethingElse() throws Exception {
      String[] strings = new String[] {"Mike"};
      String[] emptyStrings = new String[0];

      StrictPA.invokeMethod(this.child, "setPrivateStringsAndInt(java.lang.String[], int)", strings, 3);
      assertEquals(strings, StrictPA.getValue(this.child, "privateStrings"));
      assertEquals(3, StrictPA.getValue(this.child, "privateInt"));
      StrictPA.invokeMethod(this.child, "setPrivateStringsAndInt(java.lang.String[], int)", emptyStrings, 0);
      assertEquals(emptyStrings, StrictPA.getValue(this.child, "privateStrings"));
      assertEquals(0, StrictPA.getValue(this.child, "privateInt"));

      Object[] objects = new Object[] {"John"};
      Object[] emptyObjects = new Object[0];

      StrictPA.invokeMethod(this.child, "setPrivateObjectsAndInt(java.lang.Object[], int)", objects, 4);
      assertEquals(objects, StrictPA.getValue(this.child, "privateObjects"));
      assertEquals(4, StrictPA.getValue(this.child, "privateInt"));
      StrictPA.invokeMethod(this.child, "setPrivateObjectsAndInt(java.lang.Object[], int)", emptyObjects, 0);
      assertEquals(emptyObjects, StrictPA.getValue(this.child, "privateObjects"));
      assertEquals(0, StrictPA.getValue(this.child, "privateInt"));

      StrictPA.invokeMethod(this.child, "setPrivateIntAndStrings(int, java.lang.String[])", 5, strings);
      assertEquals(strings, StrictPA.getValue(this.child, "privateStrings"));
      assertEquals(5, StrictPA.getValue(this.child, "privateInt"));
      StrictPA.invokeMethod(this.child, "setPrivateIntAndStrings(int, java.lang.String[])", 0, emptyStrings);
      assertEquals(emptyStrings, StrictPA.getValue(this.child, "privateStrings"));
      assertEquals(0, StrictPA.getValue(this.child, "privateInt"));

      StrictPA.invokeMethod(this.child, "setPrivateIntAndObjects(int, java.lang.Object[])", 6, objects);
      assertEquals(objects, StrictPA.getValue(this.child, "privateObjects"));
      assertEquals(6, StrictPA.getValue(this.child, "privateInt"));
      StrictPA.invokeMethod(this.child, "setPrivateIntAndObjects(int, java.lang.Object[])", 0, emptyObjects);
      assertEquals(emptyObjects, StrictPA.getValue(this.child, "privateObjects"));
      assertEquals(0, StrictPA.getValue(this.child, "privateInt"));

      StrictPA.invokeMethod(this.child, "setPrivateStringsAndObjects(java.lang.String[],java.lang.Object[])", strings, objects);
      assertEquals(strings, StrictPA.getValue(this.child, "privateStrings"));
      assertEquals(objects, StrictPA.getValue(this.child, "privateObjects"));
      StrictPA.invokeMethod(this.child, "setPrivateStringsAndObjects(java.lang.String[],java.lang.Object[])", emptyStrings,
         emptyObjects);
      assertEquals(emptyStrings, StrictPA.getValue(this.child, "privateStrings"));
      assertEquals(emptyObjects, StrictPA.getValue(this.child, "privateObjects"));

      StrictPA.invokeMethod(this.child, "setPrivateObjectsAndStrings(java.lang.Object[],java.lang.String[])", objects, strings);
      assertEquals(strings, StrictPA.getValue(this.child, "privateStrings"));
      assertEquals(objects, StrictPA.getValue(this.child, "privateObjects"));
      StrictPA.invokeMethod(this.child, "setPrivateObjectsAndStrings(java.lang.Object[],java.lang.String[])", emptyObjects,
         emptyStrings);
      assertEquals(emptyStrings, StrictPA.getValue(this.child, "privateStrings"));
      assertEquals(emptyObjects, StrictPA.getValue(this.child, "privateObjects"));

      StrictPA.invokeMethod(this.child, "setPrivateObjectsAndObjects(java.lang.Object[],java.lang.Object[])", objects, objects);
      assertEquals(objects, StrictPA.getValue(this.child, "privateObjects"));
      StrictPA.invokeMethod(this.child, "setPrivateObjectsAndObjects(java.lang.Object[],java.lang.Object[])", emptyObjects,
         emptyObjects);
      assertEquals(emptyObjects, StrictPA.getValue(this.child, "privateObjects"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with array of non primitives instead of several arguments. This is ok for several
    * reasons: a) downward compatibility - since this was the only way prior to varargs b) using varargs there is no possibility to
    * distinquish arrays from several arguments.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithArrayInsteadOfSingleValues() throws Exception {
      Object[] oInts = new Object[] {3, 3};
      StrictPA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", oInts);
      assertEquals(6, StrictPA.getValue(this.child, "privateInt"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with array of primitives instead of several arguments. This is not ok for several
    * reasons: a) downward compatibility - was not ok in the past (one had to use Object[]) b) this is the typical behaviour when using
    * varargs (Java doesn't autoconvert primitive arrays)
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithPrimitiveArrayInsteadOfSingleValues() throws Exception {
      try {
         StrictPA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new int[] {5, 3});
         fail("invoking method with an array of primitives instead of single primitives should raise exception");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Integer[] {4, 3});
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }
   }

   /**
    * Tests the method <code>invokeMethod</code> with arrays of wrong length instead of several arguments.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithArraysOfWrongLengthInsteadOfSingleValues() throws Exception {
      try {
         StrictPA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new int[] {1});
         fail("invoking method with array of wrong size should raise exception");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Integer[] {2});
         fail("invoking method with array of wrong size should raise exception");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Object[] {3});
         fail("invoking method with array of wrong size should raise exception");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }
   }

   /**
    * Tests Autoboxing not supported
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testAutoboxingNotSupported() throws Exception {
      try {
         StrictPA.invokeMethod(this.child, "setPrivateInts(int[])", new Integer[] {1, 2});
         fail("invoking method with single values instead of array as parameters should raise exception");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }
   }

   /**
    * Tests the method <code>invokeMethod</code> with single values instead of an array.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithSingleValuesInsteadOfArray() throws Exception {
      try {
         StrictPA.invokeMethod(this.child, "setPrivateInts(int[])", 1, 2);
         fail("invoking method with single values instead of array as parameters should raise exception");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }

      try {
         StrictPA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", "Hello", "Bruno");
         fail("invoking method with single values instead of array as parameters should raise exception");
      } catch (IllegalArgumentException e) {
         // that is what we expect
      }
   }

   /**
    * Tests varargs and Object[] can't be distinquished. See http://code.google.com/p/privilegedaccessor/issues/detail?id=11
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testVarargsCantBeDistinquishedFromObjectArray() throws Exception {
      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {"Hello", new Integer(3)});

      // [Issue 11] the next method should fail with IllegalArgumentException, but since we can't distinquish varargs from Object[] it
      // works.
      StrictPA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", "Hello", new Integer(3));
   }

   /**
    * Tests the method <code>invokeMethod</code> with arguments of type object and primitive.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeMethodWithObjectAndPrimitive() throws Exception {
      Object[] args = {"Marcus", 5};
      StrictPA.invokeMethod(this.child, "setData(java.lang.String, int)", args);
      assertEquals("Marcus", StrictPA.getValue(this.child, "privateName"));
      assertEquals(5, StrictPA.getValue(this.child, "privateInt"));

      StrictPA.invokeMethod(this.childInParent, "setData(java.lang.String, int)", args);
      assertEquals("Marcus", StrictPA.getValue(this.childInParent, "privateName"));
      assertEquals(5, StrictPA.getValue(this.childInParent, "privateInt"));
   }

   /**
    * Tests the method <code>invokeMethod</code> on a static method.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testInvokeStaticMethod() throws Exception {
      StrictPA.invokeMethod(Parent.class, "setStaticInt(int)", 3);
      assertEquals(3, StrictPA.getValue(Parent.class, "privateStaticInt"));
   }

   /**
    * Tests the example given in javadoc of <code>setValue</code>.
    * 
    * This test could fail under some JVMs, since setting the value of final fields at other times than instantiation can have
    * unpredictable effects.
    * 
    * @see java.lang.reflect.Field.set(java.lang.Object, java.lang.Object)
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
   @Test
   public void testSetGetValueExample() throws Exception {
      String myString = "Test";
      StrictPA.setValue(myString, "value", new char[] {'T', 'e', 's', 't'}); // sets the final field value
      StrictPA.setValue(myString.getClass(), "serialVersionUID", 1); // sets the static final field serialVersionUID

      assertTrue(Arrays.equals(new char[] {'T', 'e', 's', 't'}, (char[]) StrictPA.getValue(myString, "value")));
      assertEquals(1L, StrictPA.getValue(String.class, "serialVersionUID"));
   }

   /**
    * Tests the method <code>setValue</code>.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
   @Test
   public void testSetGetValueWithPrimitives() throws Exception {
      StrictPA.setValue(this.child, "privateInt", 6);
      assertEquals(6, StrictPA.getValue(this.child, "privateInt"));

      StrictPA.setValue(this.childInParent, "privateInt", 6);
      assertEquals(6, StrictPA.getValue(this.childInParent, "privateInt"));

      StrictPA.setValue(this.child, "privateLong", 8L);
      assertEquals(8L, StrictPA.getValue(this.child, "privateLong"));

      StrictPA.setValue(this.child, "privateShort", (short) 6);
      assertEquals((short) 6, StrictPA.getValue(this.child, "privateShort"));

      StrictPA.setValue(this.child, "privateByte", (byte) 2);
      assertEquals((byte) 2, StrictPA.getValue(this.child, "privateByte"));

      StrictPA.setValue(this.child, "privateChar", 'F');
      assertEquals('F', StrictPA.getValue(this.child, "privateChar"));

      StrictPA.setValue(this.child, "privateBoolean", true);
      assertEquals(true, StrictPA.getValue(this.child, "privateBoolean"));

      StrictPA.setValue(this.child, "privateFloat", 1.5f);
      assertEquals(1.5f, StrictPA.getValue(this.child, "privateFloat"));

      StrictPA.setValue(this.child, "privateDouble", 1.175);
      assertEquals(1.175, StrictPA.getValue(this.child, "privateDouble"));
   }

   /**
    * Tests the method <code>setValue</code>.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
   @Test
   public void testSetGetValueWithObjectsAndArrays() throws Exception {
      StrictPA.setValue(this.parent, "privateName", "Hubert");
      assertEquals("Hubert", StrictPA.getValue(this.parent, "privateName"));

      StrictPA.setValue(this.child, "privateName", "Hubert");
      assertEquals("Hubert", StrictPA.getValue(this.child, "privateName"));

      StrictPA.setValue(this.childInParent, "privateName", "Hubert");
      assertEquals("Hubert", StrictPA.getValue(this.childInParent, "privateName"));

      int[] Ints = new int[] {1, 2, 3};
      StrictPA.setValue(this.child, "privateInts", Ints);
      assertEquals(Ints, StrictPA.getValue(this.child, "privateInts"));

      String[] strings = new String[] {"Happy", "Birthday"};
      StrictPA.setValue(this.child, "privateStrings", strings);
      assertEquals(strings, StrictPA.getValue(this.child, "privateStrings"));
   }

   /**
    * Tests the method <code>setValue</code> with a static field.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
   @Test
   public void testSetValueOfStaticField() throws Exception {
      int previousValue = (Integer) StrictPA.getValue(this.parent, "privateStaticInt");
      assertTrue(previousValue != -1);

      StrictPA.setValue(this.parent, "privateStaticInt", -1);
      assertEquals(-1, StrictPA.getValue(this.parent, "privateStaticInt"));

      StrictPA.setValue(Parent.class, "privateStaticInt", previousValue);
      assertEquals(previousValue, StrictPA.getValue(this.parent, "privateStaticInt"));
   }

   /**
    * Tests the method <code>setValue</code> with a final field.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
   @Test
   public void testSetValueOfFinalField() throws Exception {
      int previousValue = (Integer) StrictPA.getValue(this.parent, "privateFinalInt");
      assertTrue(previousValue != -2);

      StrictPA.setValue(this.parent, "privateFinalInt", -2);
      assertEquals(-2, StrictPA.getValue(this.parent, "privateFinalInt"));

      StrictPA.setValue(this.parent, "privateFinalInt", previousValue);
      assertEquals(previousValue, StrictPA.getValue(this.parent, "privateFinalInt"));
   }

   /**
    * Tests the method <code>setValue</code> with a final field.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
   @Test
   public void testSetValueOfFinalStringField() throws Exception {
      String previousValue = (String) StrictPA.getValue(this.parent, "privateFinalString");
      assertTrue(previousValue != "Test");

      StrictPA.setValue(this.parent, "privateFinalString", "Test");
      assertEquals("Test", StrictPA.getValue(this.parent, "privateFinalString"));

      StrictPA.setValue(this.parent, "privateFinalString", previousValue);
      assertEquals(previousValue, StrictPA.getValue(this.parent, "privateFinalString"));
   }

   /**
    * Tests the method <code>setValue</code> with a static final field.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
   @Test
   public void testSetValueOfStaticFinalField() throws Exception {
      int previousValue = (Integer) StrictPA.getValue(this.parent, "privateStaticFinalInt");
      assertTrue(previousValue != -3);

      try {
         StrictPA.setValue(this.parent, "privateStaticFinalInt", -3);
         assertEquals(-3, StrictPA.getValue(this.parent, "privateStaticFinalInt"));
         fail();
      } catch (IllegalAccessException e) {
         // this is what we expect when accessing private static final fields of non java.lang classes
      }

      try {
         StrictPA.setValue(this.parent, "privateStaticFinalInt", previousValue);
         assertEquals(previousValue, StrictPA.getValue(this.parent, "privateStaticFinalInt"));
         fail();
      } catch (IllegalAccessException e) {
         // this is what we expect when accessing private static final fields of non java.lang classes
      }
   }

   /**
    * Tests the method <code>setValue</code> with a static final object field.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
   @Test
   public void testSetValueOfStaticFinalObjectField() throws Exception {
      String previousValue = (String) StrictPA.getValue(this.parent, "privateStaticFinalString");
      assertTrue(previousValue != "Herbert");

      try {
         StrictPA.setValue(this.parent, "privateStaticFinalString", "Herbert");
         assertEquals("Herbert", StrictPA.getValue(this.parent, "privateStaticFinalString"));
         fail();
      } catch (IllegalAccessException e) {
         // this is what we expect when accessing private static final fields of non java.lang classes
      }

      try {
         StrictPA.setValue(this.parent, "privateStaticFinalString", previousValue);
         assertEquals(previousValue, StrictPA.getValue(this.parent, "privateStaticFinalString"));
         fail();
      } catch (IllegalAccessException e) {
         // this is what we expect when accessing private static final fields of non java.lang classes
      }
   }

   /**
    * Tests the method <code>setValue</code> with a non-existing field.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#setValue(java.lang.Object, java.lang.String, java.lang.String)
    */
   @Test
   public void testSetValueOnInvalidField() throws Exception {
      try {
         StrictPA.setValue(this.parent, "noSuchField", "value");
         fail("should throw NoSuchFieldException");
      } catch (NoSuchFieldException e) {
         // that is what we expect
      }

      try {
         StrictPA.setValue(this.child, "noSuchField", "value");
         fail("should throw NoSuchFieldException");
      } catch (NoSuchFieldException e) {
         // that is what we expect
      }

      try {
         StrictPA.setValue(this.childInParent, "noSuchField", "value");
         fail("should throw NoSuchFieldException");
      } catch (NoSuchFieldException e) {
         // that is what we expect
      }

      try {
         StrictPA.setValue(Parent.class, "noSuchField", "value");
         fail("should throw NoSuchFieldException");
      } catch (NoSuchFieldException e) {
         // that is what we expect
      }
   }

   /**
    * Tests the method <code>instantiate</code> on an inner class.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#instantiate(java.lang.Class)
    */
   @Test
   public void testInstantiateInnerClass() throws Exception {
      Object tic = StrictPA.instantiate(Class.forName("junit.extensions.Child$InnerChild"), this.child);
      assertEquals(Class.forName("junit.extensions.Child$InnerChild"), tic.getClass());
   }

   /**
    * Tests the method <code>getValue</code> with a field of an inner class.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#getValue(java.lang.Object, java.lang.String)
    */
   @Test
   public void testAccessInnerClass() throws Exception {
      Object tic = StrictPA.instantiate(Class.forName("junit.extensions.Child$InnerChild"), this.child);
      StrictPA.setValue(tic, "privateInnerInt", 5);
      assertEquals(5, StrictPA.getValue(tic, "privateInnerInt"));
   }

   /**
    * Tests the method <code>invokeMethod</code> with a method of an inner class.
    * 
    * @throws Exception
    * @see junit.extensions.StrictPA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
    */
   @Test
   public void testAccessInnerMethod() throws Exception {
      Object tic = StrictPA.instantiate(Class.forName("junit.extensions.Child$InnerChild"), this.child);
      StrictPA.invokeMethod(tic, "setPrivateInnerInt(int)", 7);
      assertEquals(7, StrictPA.invokeMethod(tic, "getPrivateInnerInt()"));
   }
}
