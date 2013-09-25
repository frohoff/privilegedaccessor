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

/**
 * Test class with private methods to invoke via PrivilegedAccessor.
 */
public class Child extends Parent {
   private int                          privateInt;
   private long                         privateLong;
   private short                        privateShort;
   private byte                         privateByte;
   private char                         privateChar;
   private boolean                      privateBoolean;
   private float                        privateFloat;
   private double                       privateDouble;
   private int[]                        privateInts;
   private String[]                     privateStrings;
   private Object[]                     privateObjects;
   private java.util.Collection<String> privateCollection;

   @SuppressWarnings("unused")
   private class InnerChild {
      private int privateInnerInt;

      private void setPrivateInnerInt(int privateInnerInt) {
         this.privateInnerInt = privateInnerInt;
      }

      private int getPrivateInnerInt() {
         return this.privateInnerInt;
      }
   }

   private Child(String name, Integer number) {
      super(name);
      this.privateInt = number.intValue();
   }

   @SuppressWarnings("unused")
   private Child(String[] strings) {
      this(strings[0]);
      this.privateStrings = strings;
   }

   public Child(String name) {
      this(name, new Integer(8));
   }

   private Child(int privateInt, float privateFloat) {
      super("Charlie");
      this.privateInt = privateInt;
      this.privateFloat = privateFloat;
   }

   private int getInt() {
      return this.privateInt;
   }

   @SuppressWarnings("unused")
   private void setInt(int number) {
      this.privateInt = number;
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

   private int[] getPrivateInts() {
      return this.privateInts;
   }

   @SuppressWarnings("unused")
   private void setPrivateInts(int[] privateInts) {
      this.privateInts = privateInts;
   }

   private String[] getPrivateStrings() {
      return this.privateStrings;
   }

   @SuppressWarnings("unused")
   private void setPrivateStrings(String[] privateStrings) {
      this.privateStrings = privateStrings;
   }

   @SuppressWarnings("unused")
   private void setPrivateStringsAndInt(String[] privateStrings, int privateInt) {
      this.privateStrings = privateStrings;
      this.privateInt = privateInt;
   }

   @SuppressWarnings("unused")
   private void setPrivateObjectsAndInt(Object[] privateObjects, int privateInt) {
      this.privateObjects = privateObjects;
      this.privateInt = privateInt;
   }

   @SuppressWarnings("unused")
   private void setPrivateIntAndStrings(int privateInt, String[] privateStrings) {
      this.privateStrings = privateStrings;
      this.privateInt = privateInt;
   }

   @SuppressWarnings("unused")
   private void setPrivateIntAndObjects(int privateInt, Object[] privateObjects) {
      this.privateObjects = privateObjects;
      this.privateInt = privateInt;
   }

   @SuppressWarnings("unused")
   private void setPrivateStringsAndObjects(String[] privateStrings, Object[] privateObjects) {
      this.privateStrings = privateStrings;
      this.privateObjects = privateObjects;
   }

   @SuppressWarnings("unused")
   private void setPrivateObjectsAndStrings(Object[] privateObjects, String[] privateStrings) {
      this.privateStrings = privateStrings;
      this.privateObjects = privateObjects;
   }

   @SuppressWarnings("unused")
   private void setPrivateObjectsAndObjects(Object[] privateObjects1, Object[] privateObjects2) {
      this.privateObjects = privateObjects1;
      this.privateObjects = privateObjects2;
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
   private void setSumOfTwoInts(int int1, int int2) {
      this.privateInt = int1 + int2;
   }

   @SuppressWarnings("unused")
   private void setData(String name, int number) {
      setName(name);
      this.privateInt = number;
   }

   @Override
   public String toString() {
      return this.getClass().getName() + " {privateInt=" + getInt() + ", privateLong=" + getPrivateLong() + ", privateShort="
         + getPrivateShort() + ", privateByte=" + getPrivateByte() + ", privateChar=" + getPrivateChar() + ", privateBoolean="
         + isPrivateBoolean() + ", privateFloat=" + getPrivateFloat() + ", privateDouble=" + getPrivateDouble() + ", privateInts="
         + getPrivateInts() + ", privateStrings=" + getPrivateStrings() + ", privateObjects=" + getPrivateObjects()
         + ", privateCollection=" + getPrivateCollection() + ", " + super.toString().substring(super.toString().indexOf('{') + 1);
   }
}