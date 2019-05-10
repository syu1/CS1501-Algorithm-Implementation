import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.io.Writer;
import java.util.Scanner;
import java.util.Arrays;
/**
 * Write a description of class RsaSign here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class RsaSign
{
    // instance variables - replace the example below with your own
    private static LargeInteger n;
    private static LargeInteger e;
    private static LargeInteger d;

    /**
     * Constructor for objects of class RsaSign
     */
    public RsaSign()
    {
        try
        {
            File priv = new File("privkey.rsa");
            byte[] privKey = Files.readAllBytes(priv.toPath());
            File pub = new File("pubkey.rsa");

            byte[] pubKey = Files.readAllBytes(pub.toPath());

            File file = new File("n.txt");
            Scanner readFile = new Scanner(file);
            int nLen = readFile.nextInt();
            int privKeyLength = privKey.length- nLen ;
            int pubKeyLength = pubKey.length -nLen;

            byte[] nByte = new byte[nLen];
            byte[] ebyte = new byte[pubKey.length-nLen];
            byte[] dbyte = new byte[privKey.length-nLen];

            for(int i = 0;i<nLen;i++)
            {
                nByte[i] = privKey[i];
            }
            for(int i = 0;i<(pubKeyLength);i++)
            {
                ebyte[i] = pubKey[i+nLen];
            }
            for(int i = 0;i<(privKeyLength);i++)
            {
                dbyte[i] = privKey[i+nLen];
            }
            n = new LargeInteger(nByte);
            e = new LargeInteger(ebyte);
            d = new LargeInteger(dbyte);
            System.out.println(n.toString(n));
            System.out.println(e.toString(e));
            System.out.println(d.toString(d));
        }
        catch(Exception e)
        {
            System.out.println("public key not found");
        }

    }

    public static void main(String args[])
    {
        RsaSign myRsa = new RsaSign();
        if(args[0].equals("s"))
        {
            try
            {
                // read in the file to hash
                File infile = new File(args[1]);
                byte[] data = Files.readAllBytes(infile.toPath());

                // create class instance to create SHA-256 hash
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // process the file
                md.update(data);
                // generate a hash of the file
                byte[] digest = md.digest();
                LargeInteger hashed = new LargeInteger(digest);
                System.out.println("The first unsigned hash is "+hashed.toString(hashed));
                LargeInteger signed = hashed.modularExp(e,n);
                System.out.println("The first signed hash is "+signed.toString(signed));
                try 
                {
                    String extension = infile.toPath() +".sig";
                    File outfile = new File(extension);
                    FileOutputStream fOut = new FileOutputStream(outfile);

                    outfile.createNewFile();
                    // get the content in bytes

                    fOut.write(signed.getVal());

                    fOut.flush();
                    fOut.close();

                }

                catch(IOException e)
                {
                    System.out.println("file error");
                }

            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }
        else if(args[0].equals("v"))
        {
            try
            {
                // read in the file to hash
                File infile = new File(args[1]);
                byte[] data = Files.readAllBytes(infile.toPath());

                // create class instance to create SHA-256 hash
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // process the file
                md.update(data);
                // generate a hash of the file
                byte[] digest = md.digest();
                //generate a hash of the orginal file myfile.txt
                LargeInteger originalhashed = new LargeInteger(digest);
                System.out.println("The second unsigned hash of the file is "+originalhashed.toString(originalhashed));
                
                try
                {
                    // now read in the sig file
                    String extension = infile.toPath() +".sig";
                    File sigFile = new File(extension);
           
                    byte[] sigHash = Files.readAllBytes(sigFile.toPath());
                    //decrypt the sigfile
                    LargeInteger signed = new LargeInteger(sigHash);  
                    System.out.println("The recovered signed hash is "+signed.toString(signed));
                    LargeInteger unsigned = signed.modularExp(d,n);
                     System.out.println("The recovered decrypted hash is "+unsigned.toString(unsigned));
                    byte[] check1 = originalhashed.getVal();
                    byte[] check2 = unsigned.getVal();
                    if(Arrays.equals(check1,check2))
                    {
                        System.out.println("The signature was valid!");
                    }
                    else
                    {
                        System.out.println("The signature IS NOT VALID!");
                    }
                }

                catch(Exception e)
                {
                    System.out.println(e.toString());
                }

                
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }

    }
}
