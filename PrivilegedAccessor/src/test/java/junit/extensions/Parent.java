package junit.extensions;

/**
 * Test class with private methods to invoke via PrivilegedAccessor
 */
public class Parent {
   private String           privateName;
   private Object           privateObject;
   private static int       privateStaticInt;
   private final int        privateFinalInt;
   private final String     privateFinalString;
   private static final int privateStaticFinalInt;
   private static final String privateStaticFinalString;
   
   static {
      privateStaticFinalInt = 3;
      privateStaticFinalString = "Tester";
   }

   public Parent(String name) {
      this(name, "Brown");
   }

   private Parent(String name, Object object) {
      this.privateName = name;
      this.privateObject = object;
      privateStaticInt = 1;
      privateFinalInt = 2;
      privateFinalString = "Tom";
   }

   @SuppressWarnings("unused")
   private Parent() {
      this("Charlie", "Brown");
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
      Parent.privateStaticInt = number;
   }

   private static int getStaticInt() {
      return Parent.privateStaticInt;
   }
   
   private int getFinalInt() {
      return privateFinalInt;
   }
   
   private String getFinalString() {
      return privateFinalString;
   }
   
   private static int getStaticFinalInt() {
      return Parent.privateStaticFinalInt;
   }
   
   private static String getStaticFinalString() {
      return Parent.privateStaticFinalString;
   }

   public boolean equals(Object other) {
      if (!(other instanceof Parent)) {
         return false;
      }

      Parent otherParent = (Parent) other;

      if (this.privateName.equals(otherParent.privateName) && this.privateObject.equals(otherParent.privateObject)) {
         return true;
      }

      return false;
   }

   public String toString() {
      return this.getClass().getName() + " {privateName=" + getName() + ", privateObject=" + getObject() + ", privateStaticInt="
         + Parent.getStaticInt() + ", privateFinalInt=" + getFinalInt() + ", privateFinalString=" + getFinalString() + ", privateStaticFinalInt=" + Parent.getStaticFinalInt() + ", privateStaticFinalString=" + Parent.getStaticFinalString() + "}";
   }
}
