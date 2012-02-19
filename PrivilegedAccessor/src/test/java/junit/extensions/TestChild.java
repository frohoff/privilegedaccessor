package junit.extensions;

/**
 * Test class with private methods to invoke via PrivilegedAccessor.
 */
public class TestChild extends TestParent {
   private int                          privateNumber;
   private long                         privateLong;
   private short                        privateShort;
   private byte                         privateByte;
   private char                         privateChar;
   private boolean                      privateBoolean;
   private float                        privateFloat;
   private double                       privateDouble;
   private int[]                        privateNumbers;
   private String[]                     privateStrings;
   private Object[]                     privateObjects;
   private java.util.Collection<String> privateCollection;

   @SuppressWarnings("unused")
   private class TestInnerChild {
      private int privateInnerNumber;

      private void setPrivateInnerNumber(int privateInnerNumber) {
         this.privateInnerNumber = privateInnerNumber;
      }

      private int getPrivateInnerNumber() {
         return this.privateInnerNumber;
      }
   }

   private TestChild(String name, Integer number) {
      super(name);
      this.privateNumber = number.intValue();
   }

   public TestChild(String name) {
      this(name, new Integer(8));
   }

   private int getNumber() {
      return this.privateNumber;
   }

   @SuppressWarnings("unused")
   private void setNumber(int number) {
      this.privateNumber = number;
   }

   private boolean isPrivateBoolean() {
      return this.privateBoolean;
   }

   @SuppressWarnings("unused")
   private void setPrivateBoolean(boolean privateBoolean) {
      this.privateBoolean = privateBoolean;
   }

   private byte getPrivateByte() {
      return this.privateByte;
   }

   @SuppressWarnings("unused")
   private void setPrivateByte(byte privateByte) {
      this.privateByte = privateByte;
   }

   private char getPrivateChar() {
      return this.privateChar;
   }

   @SuppressWarnings("unused")
   private void setPrivateChar(char privateChar) {
      this.privateChar = privateChar;
   }

   private long getPrivateLong() {
      return this.privateLong;
   }

   @SuppressWarnings("unused")
   private void setPrivateLong(long privateLong) {
      this.privateLong = privateLong;
   }

   private short getPrivateShort() {
      return this.privateShort;
   }

   @SuppressWarnings("unused")
   private void setPrivateShort(short privateShort) {
      this.privateShort = privateShort;
   }

   private double getPrivateDouble() {
      return this.privateDouble;
   }

   @SuppressWarnings("unused")
   private void setPrivateDouble(double privateDouble) {
      this.privateDouble = privateDouble;
   }

   private float getPrivateFloat() {
      return this.privateFloat;
   }

   @SuppressWarnings("unused")
   private void setPrivateFloat(float privateFloat) {
      this.privateFloat = privateFloat;
   }

   private int[] getPrivateNumbers() {
      return this.privateNumbers;
   }

   @SuppressWarnings("unused")
   private void setPrivateNumbers(int[] privateNumbers) {
      this.privateNumbers = privateNumbers;
   }

   private String[] getPrivateStrings() {
      return this.privateStrings;
   }

   @SuppressWarnings("unused")
   private void setPrivateStrings(String[] privateStrings) {
      this.privateStrings = privateStrings;
   }

   private Object[] getPrivateObjects() {
      return this.privateObjects;
   }

   @SuppressWarnings("unused")
   private void setPrivateObjects(Object[] privateObjects) {
      this.privateObjects = privateObjects;
   }

   private java.util.Collection<String> getPrivateCollection() {
      return privateCollection;
   }

   @SuppressWarnings("unused")
   private void setPrivateCollection(java.util.Collection<String> privateCollection) {
      this.privateCollection = privateCollection;
   }

   @SuppressWarnings("unused")
   private void setSumOfTwoNumbers(int number1, int number2) {
      this.privateNumber = number1 + number2;
   }

   @SuppressWarnings("unused")
   private void setData(String name, int number) {
      setName(name);
      this.privateNumber = number;
   }

   public String toString() {
      return this.getClass().getName() + " {privateNumber=" + getNumber() + ", privateLong=" + getPrivateLong() + ", privateShort="
         + getPrivateShort() + ", privateByte=" + getPrivateByte() + ", privateChar=" + getPrivateChar() + ", privateBoolean="
         + isPrivateBoolean() + ", privateFloat=" + getPrivateFloat() + ", privateDouble=" + getPrivateDouble() + ", privateNumbers="
         + getPrivateNumbers() + ", privateStrings=" + getPrivateStrings() + ", privateObjects=" + getPrivateObjects()
         + ", privateCollection=" + getPrivateCollection() + ", " + super.toString().substring(super.toString().indexOf('{') + 1);
   }
}