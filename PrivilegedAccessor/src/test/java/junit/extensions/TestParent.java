package junit.extensions;

/**
 * Test class with private methods to invoke via PrivilegedAccessor
 */
public class TestParent {
    private String privateName;
    private Object privateObject;

    private static int privateStaticNumber;

    public TestParent(String name) {
        this.privateName = name;
        this.privateObject = "Brown";
        privateStaticNumber = 1;
    }

    private TestParent() {
        this.privateName = "Charlie";
        this.privateObject = "Brown";
        privateStaticNumber = 0;
    }

    private String getName() {
        return this.privateName;
    }

    protected void setName(String newName) {
        this.privateName = newName;
    }
    
    /** overloading setName(String) **/
    private void setName() {
        this.privateName = "Chaplin";
    }

    private Object getObject() {
        return this.privateObject;
    }

    private void setObject(Object newObject) {
        this.privateObject = newObject;
    }
    
    private static void setStaticNumber(int number) {
        TestParent.privateStaticNumber = number;
    }
    
    private static int getStaticNumber() {
        return TestParent.privateStaticNumber;
    }

    public boolean equals(Object other) {
        if (!(other instanceof TestParent)) {
            return false;
        }

        TestParent otherTestParent = (TestParent) other;

        if (this.privateName.equals(otherTestParent.privateName) && this.privateObject.equals(otherTestParent.privateObject)) {
            return true;
        }

        return false;
    }
    
    public String toString() {
        return this.getClass().getName() + " {privateName=" + getName() + ", privateObject=" + getObject() + ", privateStaticNumber=" + TestParent.getStaticNumber() + "}";
    }
}
