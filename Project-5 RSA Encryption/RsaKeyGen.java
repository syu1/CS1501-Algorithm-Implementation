import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.io.Writer;
import java.math.BigInteger;
import java.util.Arrays;
/**
 * Write a description of class RsaKeyGen here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class RsaKeyGen
{
    private LargeInteger n;
    private LargeInteger p;
    private LargeInteger q;
    private LargeInteger phiN;
    private LargeInteger e;
    private LargeInteger d;
    private final byte[] ONE = {(byte) 1};

    /**
     * Constructor for objects of class RsaKeyGen
     */
    public RsaKeyGen(LargeInteger p,LargeInteger q)
    {
        this.p = p;
        this.q = q;
        System.out.println("The randomly generated p value "+p.toString(p));
        System.out.println("The randomly generated q value "+q.toString(q));
        //byte[] largeOne = {0b00000001};

        LargeInteger largeIntOne = new LargeInteger(ONE);
        n = p.multiply(q);

        LargeInteger phiP = p.subtract(largeIntOne);
        LargeInteger phiQ = q.subtract(largeIntOne);
        phiN = phiP.multiply(phiQ);
        //just hardcode it to 3 guranteed to have gcd of 1 and guarnteed to less
        // than phiN
        byte[] hardCodeThree = {0b00000000,0b00000011};

        e = new LargeInteger(hardCodeThree);

        //should do a XGCD check anyway
        //XGCD is good 
        boolean sentry = true;
        while(sentry)
        {
            LargeInteger gCheck = p.XGCD(q)[0];
            byte[] gcheck1= gCheck.getVal();
            byte[] gcheck2 = largeIntOne.getVal();
            if (Arrays.equals(gcheck1,gcheck2))
            {
                d = e.modularInverse(phiN);
                sentry = false;
            }
        }
        //hardCode e some more
        //calculate d = e^-1 mod phiN

        System.out.println("The generated n " +n.toString(n));
        System.out.println("The generated phi " +phiN.toString(phiN));

        System.out.println("The generated d "+d.toString(d));
        System.out.println("The generated e "+e.toString(e));
        // System.out.println("n length generated"+n.length());
    }

    public void exportPublicKey()
    {
        try 
        {
            File file = new File("pubkey.rsa");
            FileOutputStream fOut = new FileOutputStream(file);

            file.createNewFile();
            // get the content in bytes

            fOut.write(n.getVal());
            fOut.write(e.getVal());
            fOut.flush();
            fOut.close();

        }
        catch(IOException e)
        {
            System.out.println("file error");
        }
    }

    public void exportPrivateKey()
    {

        try {
            File file = new File("privkey.rsa");
            FileOutputStream fOut = new FileOutputStream(file);

            // if file doesnt exists, then create it

            file.createNewFile();

            // get the content in bytes

            fOut.write(n.getVal());
            fOut.write(d.getVal());
            fOut.flush();
            fOut.close();

        }
        catch(IOException e)
        {
            System.out.println("file error");
        }

    }

    public void exportNLength()
    {
        try
        {   
            String nlength =""+ n.length();
            Writer wr = new FileWriter("n.txt");
            wr.write(nlength);
            wr.close();
        }
        catch(IOException e)
        {
            System.out.println("file error");
        }

    }

    public int exportPrivateKeyELength()
    {
        return e.length();
    }

    public int exportPrivateKeyDLength()
    {
        return d.length();
    }

    public LargeInteger encrypt(LargeInteger msg)
    {
        msg = msg.modularExp(e,n);
        return msg;
    }

    public LargeInteger decrypt(LargeInteger msg)
    {
        msg = msg.modularExp(d,n);
        return msg;
    }

    public static void main(String args[])
    {
        Random ran1 = new Random();
        Random ran2 = new Random();
        LargeInteger firstPrime = new LargeInteger(256,ran1);
        LargeInteger secondPrime = new LargeInteger(256,ran2);
        RsaKeyGen generator = new RsaKeyGen(firstPrime,secondPrime);

        generator.exportNLength();
        generator.exportPublicKey();
        generator.exportPrivateKey();
        BigInteger bmsg = new BigInteger("15");
        LargeInteger msg = new LargeInteger(bmsg.toByteArray());
        LargeInteger ecy = generator.encrypt(msg);
        LargeInteger decy = generator.decrypt(ecy);
        System.out.println("The test msg "+msg.toString(msg));
        System.out.println("The encyrption "+ecy.toString(ecy));
        System.out.println("The decryption "+decy.toString(decy));
    }

}

