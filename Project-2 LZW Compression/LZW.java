
/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class LZW {
    private static final int R = 256;        // number of input chars
    private static final int L = 4096;       // number of codewords = 2^W
    private static final int W = 12;         // codeword width

    public static void compress()
    { 
        //what is the length of this string actually hmm
        String input = BinaryStdIn.readString();
        //ternary search trie prolly?
        TST<Integer> st = new TST<Integer>();
        //populate the dictonary with ASCII char 0 through 256
        for (int i = 0; i < R; i++)
        {
            st.put("" + (char) i, i);
        }
        int code = R+1;  // jk the entird of file is when input.length() == 0
        //actual compression algorithm
        while (input.length() > 0) 
        {
            //System.err.println("input length at the begging of the loop"+input.length());//i need to know if its reading line by line or input cointains the entire file
            // so it definitley reads the entire file as a string at first
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            //not exactly print more like write to file
            System.err.println("Integer value of get(s) " + st.get(s)); 
            BinaryStdOut.write(st.get(s), W);      // i assume st converts s to encoding with gets then W is the length to be written
            //What is t doing here. its bascially the length of the longest prefix so if longest prefix is sil then t  =3
            int t = s.length();
            //if the longest prefix found is less than the length of the ENITRE input
            // if the code is < L well thats simply its just checking that we have space in our dictionary
            if (t < input.length() && code < L)
            // Add s to symbol table.
            {
                //adds a try to st. so it substrings the input
                //addes a new codeword
                st.put(input.substring(0, t + 1), code++);
            }
                //new input length huh
            input = input.substring(t);            // Scan past s in input.
            
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();

    }

    public static void expand()
    {
        String[] st = new String[L];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
        {
            st[i] = "" + (char) i;
        }
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R)
        {
            return;
        }// expanded message is empty string
        String val = st[codeword];

        while (true) 
        {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R)
            {
                break;
            }
            String s = st[codeword];
            if (i == codeword)
            {
                s = val + val.charAt(0);
            }// special case hack
            if (i < L) 
            {   
                st[i++] = val + s.charAt(0);
            }
            val = s;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args)
    {
        if (args[0].equals("-"))
        {
            compress();
        }
        else if (args[0].equals("+"))
        {
            expand();
        }
        else
        {
            throw new IllegalArgumentException("Illegal command line argument");

        }
    }
}