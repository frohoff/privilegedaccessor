package junit.extensions;

/**
 * Test class with private methods to invoke via PrivilegedAccessor.
 */
public class TestChild extends TestParent {
    private int privateNumber;
    private long privateLong;
    private short privateShort;
    private byte privateByte;
    private char privateChar;
    private boolean privateBoolean;
    private float privateFloat;
    private double privateDouble;
    

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

    private void setNumber(int number) {
        this.privateNumber = number;
    }

    private boolean isPrivateBoolean() {
        return privateBoolean;
    }

    private void setPrivateBoolean(boolean privateBoolean) {
        this.privateBoolean = privateBoolean;
    }
    
    private boolean getPrivateBoolean() {
        return privateBoolean;
    }

    private byte getPrivateByte() {
        return privateByte;
    }

    private void setPrivateByte(byte privateByte) {
        this.privateByte = privateByte;
    }

    private char getPrivateChar() {
        return privateChar;
    }

    private void setPrivateChar(char privateChar) {
        this.privateChar = privateChar;
    }

    private long getPrivateLong() {
        return privateLong;
    }

    private void setPrivateLong(long privateLong) {
        this.privateLong = privateLong;
    }

    private short getPrivateShort() {
        return privateShort;
    }

    private void setPrivateShort(short privateShort) {
        this.privateShort = privateShort;
    }

    private double getPrivateDouble() {
        return privateDouble;
    }

    private void setPrivateDouble(double privateDouble) {
        this.privateDouble = privateDouble;
    }

    private float getPrivateFloat() {
        return privateFloat;
    }

    private void setPrivateFloat(float privateFloat) {
        this.privateFloat = privateFloat;
    }

    private void setSumOfTwoNumbers(int number1, int number2) {
        this.privateNumber = number1 + number2;
    }

    private void setData(String name, int number) {
        setName(name);
        this.privateNumber = number;
    }
}