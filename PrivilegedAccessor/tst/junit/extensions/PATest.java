package junit.extensions;

import junit.framework.TestCase;

/**
 * Tests for the class <code>PA</code>.
 */
public class PATest extends TestCase {

    /**
     * An instance of a test-subclass.
     */
    private TestChild child;

    /**
     * An instance of a test-subclass in a variable of type superclass.
     */
    private TestParent childInParent;
    /**
     * an instance of a test-superclass.
     */
    private TestParent parent;

    /**
     * Constructor for this test.
     *
     * @param name
     * @deprecated only necessary for junit prior to 3.8.1
     */
    public PATest(String name) {
        super(name);
    }

    /**
     * The main test-method.
     *
     * @param unused not used
     */
    public static void main(final String[] unused) {
        junit.textui.TestRunner.run(PrivilegedAccessorTest.class);
    }

    /**
     * Sets up the test-environment by instantiating the test-instances.
     *
     * @see junit.framework.TestCase#setUp()
     */
    public final void setUp() {
        this.parent = new TestParent("Charlie");
        this.child = new TestChild("Charlie");
        this.childInParent = new TestChild("Charlie");
    }

    /**
     * Tears down the test-environment by deleting the test-instances.
     *
     * @see junit.framework.TestCase#tearDown()
     */
    public final void tearDown() {
        this.parent = null;
        this.child = null;
        this.childInParent = null;
    }

    /**
     * Tests the method <code>getValue</code>.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#getValue(Object, String)
     */
    public final void testGetValue() throws Exception {
        assertEquals("Charlie", PA.getValue(this.parent, "privateName"));

        assertEquals("Charlie", PA.getValue(this.child, "privateName"));
        assertEquals(new Integer(8), PA.getValue(this.child, "privateNumber"));

        assertEquals("Charlie", PA.getValue(this.childInParent, "privateName"));
        assertEquals(new Integer(8), PA.getValue(this.childInParent,
                "privateNumber"));
    }

    /**
     * Tests the method <code>getValue</code> with a static field.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#getValue(Object, String)
     */
    public void testGetValueOfStaticField() throws Exception {
        assertEquals(new Integer(1), PA.getValue(this.parent,
                "privateStaticNumber"));
        assertEquals(new Integer(1), PA.getValue(TestParent.class,
                "privateStaticNumber"));
    }

    /**
     * Tests the method <code>getValue</code> with a non-existing field.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#getValue(Object, String)
     */
    public void testGetValueOnInvalidField() throws Exception {
        try {
            PA.getValue(this.parent, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.getValue(this.child, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.getValue(this.childInParent, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.getValue(TestParent.class, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }
    }

    /**
     * Tests the method <code>instantiate</code>.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#instantiate(Class)
     */
    public void testInstantiate() throws Exception {
        assertEquals(this.parent, PA.instantiate(TestParent.class));
        assertEquals(this.parent, PA.instantiate(TestParent.class, "Charlie"));
        assertEquals(this.child, PA.instantiate(TestChild.class,
                "Charlie", 8));
        assertEquals(this.childInParent, PA.instantiate(TestChild.class,
                "Charlie", 8));
        assertEquals(this.childInParent, PA.instantiate(TestChild.class, 
                new Class[] {String.class, Integer.class},
                "Charlie", 8));
    }
    
    /**
     * Tests the method <code>instantiate</code>.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#instantiate(Class)
     */
    public void testInstantiateOnInvalidParameters() throws Exception {
        try {
            PA.instantiate(TestParent.class, 21);
            fail ("instantiating with wrong parameter type should throw Exception");
        } catch (Exception e) {
            //this is what we expect
        }
        
        try {
            PA.instantiate(TestChild.class, "Charlie", "Brown");
            fail ("instantiating with wrong second parameter type should throw Exception");
        } catch (Exception e) {
            //this is what we expect
        }        
        
        try {
            PA.instantiate(TestChild.class, new Class[] {String.class, String.class},
                    "Charlie", 8);
            fail ("instantiating with unmatching parameter types should throw Exception");
            PA.instantiate(TestChild.class, new Class[] {String.class, Integer.class},
                    "Charlie", "Brown");
            fail ("instantiating with unmatching parameter types should throw Exception");
        } catch (Exception e) {
            //this is what we expect
        }
        
        try {
            PA.instantiate(TestChild.class, new Class[] {String.class, Integer.class, String.class},
                    "Charlie", 8, "Brown");
            fail ("instantiating with wrong parameter count should throw Exception");
        } catch (Exception e) {
            //this is what we expect
        }               
    }

    /**
     * Tests the constructor of PA and PrivilegedAccessor.
     *
     * @throws Exception if something went wrong
     */
    public final void testInstantiationThrowsException() throws Exception {
        try {
            PA.instantiate(PA.class);
            fail("Instantiating PA should throw Exception");
        } catch (Exception e) {
            //thats what we expect
        }

        try {
            PA.instantiate(PrivilegedAccessor.class);
            fail("Instantiating PrivilegedAccessor should throw Exception");
        } catch (Exception e) {
            //thats what we expect
        }
    }

    /**
     * Tests the method <code>invokeMethod</code>.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethod() throws Exception {
        assertEquals("Charlie", PA.invokeMethod(this.parent, "getName()"));
        
        PA.invokeMethod(this.parent, "setName(java.lang.String)", "Herbert");
        assertEquals("Herbert", PA.getValue(this.parent, "privateName"));

        PA.invokeMethod(this.parent, "setName(java.lang.String)", (Object[])null);
        assertEquals(null, PA.getValue(this.parent, "privateName"));
        
        PA.invokeMethod(this.parent, "setName()");
        assertEquals("Chaplin", PA.getValue(this.parent, "privateName"));
        
        PA.invokeMethod(this.child, "setName(java.lang.String)", "Hubert");
        assertEquals("Hubert", PA.getValue(this.child, "privateName"));

        PA.invokeMethod(this.child, "setNumber(int)", 3);
        assertEquals(3, PA.invokeMethod(this.child, "getNumber()"));

        PA.invokeMethod(this.childInParent, "setName(java.lang.String)",
                "Norbert");
        PA.invokeMethod(this.childInParent, "setNumber(int)", 3);
        assertEquals("Norbert", PA.getValue(this.childInParent, "privateName"));
        assertEquals(3, PA.getValue(this.childInParent, "privateNumber"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with different primitives.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithPrimitives() throws Exception {
        PA.invokeMethod(this.child, "setNumber(int)", 3);
        assertEquals(3, PA.invokeMethod(this.child, "getNumber()"));

        PA.invokeMethod(this.child, "setPrivateLong(long)", 3L);
        assertEquals(3L, PA.invokeMethod(this.child, "getPrivateLong()"));

        PA.invokeMethod(this.child, "setPrivateShort(short)", (short)3);
        assertEquals((short)3, PA.invokeMethod(this.child, "getPrivateShort()"));

        PA.invokeMethod(this.child, "setPrivateByte(byte)", (byte)3);
        assertEquals((byte)3, PA.invokeMethod(this.child, "getPrivateByte()"));

        PA.invokeMethod(this.child, "setPrivateBoolean(boolean)", true);
        assertEquals(true, PA.invokeMethod(this.child, "isPrivateBoolean()"));

        PA.invokeMethod(this.child, "setPrivateChar(char)", 'A');
        assertEquals('A', PA.invokeMethod(this.child, "getPrivateChar()"));

        PA.invokeMethod(this.child, "setPrivateFloat(float)", 3.1f);
        assertEquals(3.1f, PA.invokeMethod(this.child, "getPrivateFloat()"));

        PA.invokeMethod(this.child, "setPrivateDouble(double)", 3.1);
        assertEquals(3.1, PA.invokeMethod(this.child, "getPrivateDouble()"));
    }

    /**
     * Tests the method <code>invokeMethod</code> on a non-existing method.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodOnInvalidMethodName() throws Exception {
        try {
            PA.invokeMethod(this.child, "getNumber");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.child, "setNumber)(", 5);
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.child, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.parent, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.childInParent, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(TestParent.class, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }
    }

    public void testInvokeMethodWithArray() throws Exception {
        Object[] args = { 5 };
        PA.invokeMethod(this.childInParent, "setNumber(int)", args);
        assertEquals(5, PA.getValue(this.childInParent,
                "privateNumber"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with invalid arguments.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithInvalidSignature() throws Exception {
        try {
            PA.invokeMethod(this.child, "setName", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect - since the signature is missing
        }

        try {
            PA.invokeMethod(this.child, "setName java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect - since the first brace is missing
        }

        try {
            PA.invokeMethod(this.child, "setName(java.lang.String", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect - since the last brace is missing
        }

        try {
            PA.invokeMethod(this.child, "setName(java.lang.SString)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect - since the signature denotes a non-existing class
        }

    }
        
    /**
     * Tests the method <code>invokeMethod</code> with invalid arguments.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithInvalidArguments() throws Exception {
        try {
            PA.invokeMethod(this.child, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.child, "setData(non.existing.package.NonExistingClass)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.child, "setData()");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.parent, "setData(java.lang.String)",
                            "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.childInParent, "setData(java.lang.String)",
                    "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.child, "setName(java.lang.String)", 2);
            fail("should throw NoSuchMethodException");
        } catch (IllegalArgumentException e) {
            // that is what we expect
        }
        
        try {
            PA.invokeMethod(this.child, "setNumber(java.lang.String)", (Object[])null);
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.child, "setNumber(int)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (IllegalArgumentException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(TestParent.class,
                    "setStaticNumber(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }
        
        try {
            PA.invokeMethod(this.child, "setNumber(int)", (Object[])null);
            fail("should throw NoSuchMethodException");
        } catch (IllegalArgumentException e) {
            // that is what we expect
        }
    }

    /**
     * Tests the method <code>invokeMethod</code> with several primitive arguments.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithMoreThanOnePrimitive() throws Exception {
        PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)",
                5, 3);
        assertEquals(8, PA.getValue(this.child, "privateNumber"));
        
        PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)",
                new Integer(5), new Integer(4));
        assertEquals(9, PA.getValue(this.child, "privateNumber"));
    }
    
    /**
     * Tests the method <code>invokeMethod</code> with arrays as arguments.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithArrays() throws Exception {
        int[] numbers = new int[] { 5, 3 };
        PA.invokeMethod(this.child, "setPrivateNumbers(int[])", numbers);
        assertEquals(numbers, PA.getValue(this.child, "privateNumbers"));

        numbers = new int[] { 5 };
        PA.invokeMethod(this.child, "setPrivateNumbers(int[])", numbers);
        assertEquals(numbers, PA.getValue(this.child, "privateNumbers"));
        
        String[] strings = new String[] { "Hello", "Dolly"};
        PA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object[])strings);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));

        strings = new String[] { "Hello"};
        PA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object[])strings);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));
    }
    
    /**
     * Tests the bugs in invoke method (invoke method does not work on methods that require object arrays as parameters)
     * 
     * @throws Exception
     */
    public void testInvokeMethodThatRequireArrays() throws Exception {
        //TODO this is a bug
        try {
            PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] { new Integer(1)});
            fail("invoking method which require object arrays as parameters currently fails");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
        
        //TODO this is a bug
        try {
            PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] { "Dolly" });
            fail("invoking method which require object arrays as parameters currently fails");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }

        //TODO this is a bug
        try {
            PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] { "Hello", new Integer(1)});
            fail("invoking method which require object arrays as parameters currently fails");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
    }
    
    /**
     * Tests the method <code>invokeMethod</code> with array of non primitives instead of several arguments.
     * This is ok for several reasons:
     * a) downward compatibility - since this was the only way prior to varargs
     * b) using varargs there is no possibility to distinquish arrays from several arguments.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithArrayInsteadOfSingleValues() throws Exception {
        Object[] onumbers = new Object[] { 3, 3 };
        PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)", onumbers);
        assertEquals(6, PA.getValue(this.child, "privateNumber"));
    }
    
    /**
     * Tests the method <code>invokeMethod</code> with array of primitives instead of several arguments.
     * This is not ok for several reasons:
     * a) downward compatibility - was not ok in the past (one had to use Object[])
     * b) this is the typical behaviour when using varargs (Java doesn't autoconvert primitive arrays)
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithPrimitiveArrayInsteadOfSingleValues() throws Exception {
        try {
            PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)", new int[] { 5, 3 });
            fail("invoking method with an array of primitives instead of single primitives should raise exception");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
        
        try {
            PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)", new Integer[] { 4, 3 });
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
    }
    
    /**
     * Tests the method <code>invokeMethod</code> with arrays of wrong length instead of several arguments.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithArraysOfWrongLengthInsteadOfSingleValues() throws Exception {
        try {
            PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)",
                    new int[] { 1 });
            fail("invoking method with array of wrong size should raise exception");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
        
        try {
            PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)",
                    new Integer[] { 2 });
            fail("invoking method with array of wrong size should raise exception");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
        
        try {
            PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)",
                    new Object[] { 3 });
            fail("invoking method with array of wrong size should raise exception");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
    }
    
    /**
     * Tests the method <code>invokeMethod</code> with single values instead of an array.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithSingleValuesInsteadOfArray() throws Exception {
        try {
            PA.invokeMethod(this.child, "setPrivateNumbers(int[])", 1, 2);
            fail("invoking method with single values instead of array as parameters should raise exception");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
        
        try {
            PA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", "Hello", "Bruno");
            fail("invoking method with single values instead of array as parameters should raise exception");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
        
        try {
            PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", "Hello", new Integer(3));
            fail("invoking method with single values instead of array as parameters should raise exception");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }
    }

    /**
     * Tests the method <code>invokeMethod</code> with arguments of type object and primitive.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithObjectAndPrimitive() throws Exception {
        Object[] args = { "Marcus", 5 };
        PA.invokeMethod(this.child,
                "setData(java.lang.String, int)", args);
        assertEquals("Marcus", PA.getValue(this.child, "privateName"));
        assertEquals(5, PA.getValue(this.child, "privateNumber"));

        PA.invokeMethod(this.childInParent,
                "setData(java.lang.String, int)", args);
        assertEquals("Marcus", PA.getValue(this.childInParent, "privateName"));
        assertEquals(5, PA.getValue(this.childInParent,
                "privateNumber"));
    }

    /**
     * Tests the method <code>invokeMethod</code> on a static method.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeStaticMethod() throws Exception {
        PA.invokeMethod(TestParent.class, "setStaticNumber(int)", 3);
        assertEquals(3, PA.getValue(TestParent.class,
                "privateStaticNumber"));
    }

    /**
     * Tests the method <code>setValue</code>.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetGetValueWithPrimitives() throws Exception {
        PA.setValue(this.child, "privateNumber", 6);
        assertEquals(6, PA.getValue(this.child, "privateNumber"));

        PA.setValue(this.childInParent, "privateNumber", 6);
        assertEquals(6, PA.getValue(this.childInParent, "privateNumber"));
        
        PA.setValue(this.child, "privateLong", 8L);
        assertEquals(8L, PA.getValue(this.child, "privateLong"));

        PA.setValue(this.child, "privateShort", (short)6);
        assertEquals((short)6, PA.getValue(this.child, "privateShort"));
        
        PA.setValue(this.child, "privateByte", (byte)2);
        assertEquals((byte)2, PA.getValue(this.child, "privateByte"));
        
        PA.setValue(this.child, "privateChar", 'F');
        assertEquals('F', PA.getValue(this.child, "privateChar"));
        
        PA.setValue(this.child, "privateBoolean", true);
        assertEquals(true, PA.getValue(this.child, "privateBoolean"));
        
        PA.setValue(this.child, "privateFloat", 1.5f);
        assertEquals(1.5f, PA.getValue(this.child, "privateFloat"));
        
        PA.setValue(this.child, "privateDouble", 1.175);
        assertEquals(1.175, PA.getValue(this.child, "privateDouble"));
    }
    
    /**
     * Tests the method <code>setValue</code>.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetGetValueWithObjectsAndArrays() throws Exception {
        PA.setValue(this.parent, "privateName", "Hubert");
        assertEquals("Hubert", PA.getValue(this.parent, "privateName"));

        PA.setValue(this.child, "privateName", "Hubert");
        assertEquals("Hubert", PA.getValue(this.child, "privateName"));

        PA.setValue(this.childInParent, "privateName", "Hubert");
        assertEquals("Hubert", PA.getValue(this.childInParent, "privateName"));
        
        int[] numbers = new int[] {1, 2, 3};
        PA.setValue(this.child, "privateNumbers", numbers);
        assertEquals(numbers, PA.getValue(this.child, "privateNumbers"));
        
        String[] strings = new String[] {"Happy", "Birthday"};
        PA.setValue(this.child, "privateStrings", strings);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));
    }

    /**
     * Tests the method <code>setValue</code> with a static field.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetValueOfStaticField() throws Exception {
        PA.setValue(this.parent, "privateStaticNumber", 6);
        assertEquals(6, PA.getValue(this.parent,
                "privateStaticNumber"));

        PA.setValue(TestParent.class, "privateStaticNumber", 7);
        assertEquals(7, PA.getValue(this.parent,
                "privateStaticNumber"));
    }

    /**
     * Tests the method <code>setValue</code> with a non-existing field.
     *
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetValueOnInvalidField() throws Exception {
        try {
            PA.setValue(this.parent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.setValue(this.child, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.setValue(this.childInParent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.setValue(TestParent.class, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }
    }
}