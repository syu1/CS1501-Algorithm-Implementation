import java.util.Random;
import java.math.BigInteger;
public class LargeInteger {

    private final byte[] ONE = {(byte) 1};

    private byte[] val;
    private int signum = 10;
    /**
     * Construct the LargeInteger from a given byte array
     * @param b the byte array that this LargeInteger should represent
     */
    public LargeInteger(byte[] b) {
        val = b;

    }

    /**
     * Construct the LargeInteger by generatin a random n-bit number that is
     * probably prime (2^-100 chance of being composite).
     * @param n the bitlength of the requested integer
     * @param rnd instance of java.util.Random to use in prime generation
     */
    public LargeInteger(int n, Random rnd) {
        val = BigInteger.probablePrime(n, rnd).toByteArray();
    }

    /**
     * Return this LargeInteger's val
     * @return val
     */
    public byte[] getVal() {
        return val;
    }

    /**
     * Return the number of bytes in val
     * @return length of the val byte array
     */
    public int length() {
        return val.length;
    }

    /** 
     * Add a new byte as the most significant in this
     * @param extension the byte to place as most significant
     */
    public void extend(byte extension) {
        byte[] newv = new byte[val.length + 1];
        newv[0] = extension;
        for (int i = 0; i < val.length; i++) {
            newv[i + 1] = val[i];
        }
        val = newv;
    }

    /**
     * If this is negative, most significant bit will be 1 meaning most 
     * significant byte will be a negative signed number
     * @return true if this is negative, false if positive
     */
    public boolean isNegative() {
        return (val[0] < 0);
    }

    /**
     * Computes the sum of this and other
     * @param other the other LargeInteger to sum with this
     */
    public LargeInteger add(LargeInteger other) {
        byte[] a, b;
        // If operands are of different sizes, put larger first ...
        if (val.length < other.length()) {
            a = other.getVal();
            b = val;
        }
        else {
            a = val;
            b = other.getVal();
        }

        // ... and normalize size for convenience
        if (b.length < a.length) {
            int diff = a.length - b.length;

            byte pad = (byte) 0;
            if (b[0] < 0) {
                pad = (byte) 0xFF;
            }

            byte[] newb = new byte[a.length];
            for (int i = 0; i < diff; i++) {
                newb[i] = pad;
            }

            for (int i = 0; i < b.length; i++) {
                newb[i + diff] = b[i];
            }

            b = newb;
        }

        // Actually compute the add
        int carry = 0;
        byte[] res = new byte[a.length];
        for (int i = a.length - 1; i >= 0; i--) {
            // Be sure to bitmask so that cast of negative bytes does not
            //  introduce spurious 1 bits into result of cast
            carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

            // Assign to next byte
            res[i] = (byte) (carry & 0xFF);

            // Carry remainder over to next byte (always want to shift in 0s)
            carry = carry >>> 8;
        }

        LargeInteger res_li = new LargeInteger(res);

        // If both operands are positive, magnitude could increase as a result
        //  of addition
        if (!this.isNegative() && !other.isNegative()) {
            // If we have either a leftover carry value or we used the last
            //  bit in the most significant byte, we need to extend the result
            if (res_li.isNegative()) {
                res_li.extend((byte) carry);
            }
        }
        // Magnitude could also increase if both operands are negative
        else if (this.isNegative() && other.isNegative()) {
            if (!res_li.isNegative()) {
                res_li.extend((byte) 0xFF);
            }
        }

        // Note that result will always be the same size as biggest input
        //  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
        return res_li;
    }

    /**
     * Negate val using two's complement representation
     * @return negation of this
     */
    public LargeInteger negate() {
        byte[] neg = new byte[val.length];
        int offset = 0;

        // Check to ensure we can represent negation in same length
        //  (e.g., -128 can be represented in 8 bits using two's 
        //  complement, +128 requires 9)
        if (val[0] == (byte) 0x80) { // 0x80 is 10000000
            boolean needs_ex = true;
            for (int i = 1; i < val.length; i++) {
                if (val[i] != (byte) 0) {
                    needs_ex = false;
                    break;
                }
            }
            // if first byte is 0x80 and all others are 0, must extend
            if (needs_ex) {
                neg = new byte[val.length + 1];
                neg[0] = (byte) 0;
                offset = 1;
            }
        }

        // flip all bits
        for (int i  = 0; i < val.length; i++) {
            neg[i + offset] = (byte) ~val[i];
        }

        LargeInteger neg_li = new LargeInteger(neg);

        // add 1 to complete two's complement negation
        return neg_li.add(new LargeInteger(ONE));
    }

    /**
     * Implement subtraction as simply negation and addition
     * @param other LargeInteger to subtract from this
     * @return difference of this and other
     */
    public LargeInteger subtract(LargeInteger other) {
        return this.add(other.negate());
    }

    public LargeInteger shiftRight(int n)
    {
        // ill swap with n if this ends up being too slow lol
        byte[] bytes = { (byte) 0b11111111, 0x00, 0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55, 0x66, 0x66, 0x77, 0x77 };
        String complete = "";
        for(int i = 0; i < bytes.length;i++)
        {
            byte b1 = bytes[i];
            String s1 =Integer.toBinaryString(b1);
            complete +=s1;

        }
        StringBuilder myComplete = new StringBuilder(complete);
        char firstBit = myComplete.charAt(0);

        for(int i = 0; i < n; i++)
        {

            for(int j = myComplete.length()-1 ; j>0; j--)
            {
                if(j<n-1)
                {
                    if(firstBit =='0')
                    {
                        myComplete.setCharAt(j,'0');
                    }
                    else
                    {
                        myComplete.setCharAt(j,'1');
                    }
                }
                else{
                    char temp = myComplete.charAt(j);
                    myComplete.setCharAt(j,myComplete.charAt(j-1));
                    myComplete.setCharAt(j-1,temp);
                }

            }
        }
        
        byte[] byteString = myComplete.toString().getBytes();

        return new LargeInteger(byteString);
    }

    public int LargeIntToInt()
    {
        byte[] bytes = this.getVal();
        String complete = "";
        for(int i = 0; i < bytes.length;i++)
        {
            byte b1 = bytes[i];
            String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
            complete +=s1;

        }
        return  Integer.parseInt(complete, 2);
    }

    public LargeInteger shiftLeft(int n)
    {
        // ill swap with n if this ends up being too slow lol
        byte[] bytes = { (byte) 0b11111111, 0x00, 0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55, 0x66, 0x66, 0x77, 0x77 };
        String complete = "";
        for(int i = 0; i < bytes.length;i++)
        {
            byte b1 = bytes[i];
            String s1 =  Integer.toBinaryString(b1);
            complete +=s1;

        }

        for(int i = 0; i < n; i++)
        {
            complete +='0';
        }

        byte[] byteString = complete.getBytes();
        
        return new LargeInteger( byteString);
    }

    /**
     * Compute the product of this and other
     * @param other LargeInteger to multiply by this
     * @return product of this and other
     */
    //public LargeInteger multiply(LargeInteger other) {

    //    return kratsuba(this, other);
    //}
    public LargeInteger multiply(LargeInteger other) {
        BigInteger x = new BigInteger(this.getVal());
        BigInteger y = new BigInteger(other.getVal());
        BigInteger resultBig = x.multiply(y);
        LargeInteger result = new LargeInteger(resultBig.toByteArray());
        return result;
    }

    public static LargeInteger kratsuba(LargeInteger x,LargeInteger y)
    {
        // cutoff to brute force
        //System.out.println(x.length());
        //System.out.println(y.length());
        int N = Math.max(x.length(), y.length());
        // 12 bits times 12 bits is 24 bits so i should be safe here
        if (N <= 8){
            int intx = x.LargeIntToInt();
            int inty = y.LargeIntToInt();
            int result = intx*inty;
            String sResult = Integer.toString(result);
            //System.out.println(result);
            System.out.println(sResult);
            return x;
        }

        // number of bits divided by 2, rounded up
        N = (N / 2) + (N % 2);

        // x = a + 2^N b,   y = c + 2^N d
        LargeInteger b = x.shiftRight(N);
        LargeInteger a = x.subtract(b.shiftLeft(N));
        LargeInteger d = y.shiftRight(N);
        LargeInteger c = y.subtract(d.shiftLeft(N));

        // compute sub-expressions
        LargeInteger ac    = kratsuba(a, c);
        LargeInteger bd    = kratsuba(b, d);
        LargeInteger abcd  = kratsuba(a.add(b), c.add(d));

        return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2*N));
    }

    /**
     * Run the extended Euclidean algorithm on this and other
     * @param other another LargeInteger
     * @return an array structured as follows:
     *   0:  the GCD of this and other
     *   1:  a valid x value
     *   2:  a valid y value
     * such that this * x + other * y == GCD in index 0
     */
    public LargeInteger[] XGCD(LargeInteger other) {
        LargeInteger[] resultArray = new LargeInteger[3];
        BigInteger x = new BigInteger(this.getVal());
        BigInteger y = new BigInteger(other.getVal());
        BigInteger z = x.gcd(y);
        LargeInteger result1 = new LargeInteger(z.toByteArray());
        LargeInteger result2= null;
        LargeInteger result3= null;
        resultArray[0] = result1;
        //We'll see how this works out later
        resultArray[1] = result2;
        resultArray[2] = result3;
        return resultArray;
    }

    /**
     * Compute the result of raising this to the power of y mod n
     * @param y exponent to raise this to
     * @param n modulus value to use
     * @return this^y mod n
     */
    public LargeInteger modularExp(LargeInteger y, LargeInteger n) {
        // YOUR CODE HERE (replace the return, too...)

        BigInteger base = new BigInteger(this.getVal());
        BigInteger power = new BigInteger(y.getVal());
        BigInteger mod = new BigInteger(n.getVal());
        BigInteger resultBig = base.modPow(power,mod);

        LargeInteger result = new LargeInteger(resultBig.toByteArray()); 
        return result;
    }

    /**
     * Compute the result of raising this to the power of -1 mod n
     * @param y exponent to raise this to
     * @param n modulus value to use
     * @return this^y mod n
     */
    public LargeInteger modularInverse(LargeInteger n) {
        // YOUR CODE HERE (replace the return, too...)

        BigInteger base = new BigInteger(this.getVal());

        BigInteger mod = new BigInteger(n.getVal());
        LargeInteger result = null;
        try{
            BigInteger resultBig = base.modInverse(mod);
            result = new LargeInteger(resultBig.toByteArray()); 
        }
        catch(Exception e)
        {
            System.out.println("UNSOLVEABLE BASE DETECTED FOR MOD INVERSE RESTART PROGRAM");
            System.exit(0);
        }

        return result;
    }

    public String toString(LargeInteger num)
    {
        BigInteger myNum = new BigInteger(num.getVal());
        String stringify = myNum.doubleValue()+"";
        return stringify;

    }

    private static byte[] bigIntToByteArray( final int i ) {
        BigInteger bigInt = BigInteger.valueOf(i);      
        return bigInt.toByteArray();
    }

    public static void main(String args[])
    {
        //testing.shiftRight(3);
        long start, stop, elapsed;
        byte[] a = LargeInteger.bigIntToByteArray(200);
        byte[] b = LargeInteger.bigIntToByteArray(200);
        LargeInteger seven = new LargeInteger(a);
        LargeInteger four = new LargeInteger(b);
        LargeInteger result = LargeInteger.kratsuba(seven, four);


    }
}