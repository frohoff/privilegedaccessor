package junit.extensions;

/**
 * Test class with private methods to invoke via PrivilegedAccessor
 */
public class TestParent {
   private String     privateName;
   private Object     privateObject;

   private static int privateStaticInt;

   public TestParent(String name) {
      this.privateName = name;
      this.privateObject = "Brown";
      privateStaticInt = 1;
   }

   @SuppressWarnings("unused")
   private TestParent() {
      this.privateName = "Charlie";
      this.privateObject = "Brown";
      privateStaticInt = 0;
   }

   private String getName() {
      return this.privateName;
   }

   protected void setName(String newName) {
      this.privateName = newName;
   }

   /** overloading setName(String) **/
   @SuppressWarnings("unused")
   private void setName() {
      this.privateName = "Chaplin";
   }

   private Object getObject() {
      return this.privateObject;
   }

   @SuppressWarnings("unused")
   private void setObject(Object newObject) {
      this.privateObject = newObject;
   }

   @SuppressWarnings("unused")
   private static void setStaticInt(int number) {
      TestParent.privateStaticInt = number;
   }

   private static int getStaticInt() {
      return TestParent.privateStaticInt;
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
      return this.getClass().getName() + " {privateName=" + getName() + ", privateObject=" + getObject() + ", privateStaticInt="
         + TestParent.getStaticInt() + "}";
   }
}
