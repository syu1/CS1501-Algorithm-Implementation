import java.util.Arrays;
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

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static int W = 9;         // codeword width
    private static int L = 512;       // number of codewords = 2^W

    public static void increaseWordWidth()
    {
        W = W+1;
    }

    public static void increaseCodeWords()
    {  
        L = (int)Math.pow(2,W);

    }

    public static void compress(String mode) {
        if(mode.equals("n"))
        {

            String input = BinaryStdIn.readString(); // reads in text file using < thingys
            TST<Integer> st = new TST<Integer>(); // makes tst of type integer
            for (int i = 0; i < R; i++)// puts the alphabet with key same as value
            {
                st.put("" + (char) i, i); // 0 through 255
            }
            int endCodeWord = R+1;  // R is codeword for EOF, so 256, remember that its 0 to 255! or 8 bits
            BinaryStdOut.write('n'); //write the very first character of out output.lzw with a n!
            while (input.length() > 0)  // start compressing the provided file
            {

                String currentCodeWordMatch = st.longestPrefixOf(input);// Find max prefix in our codebook, this is part of lzw algorthim of compression using longest codeword
                BinaryStdOut.write(st.get(currentCodeWordMatch), W);      // Write s's encoding to the output file using W the width of a codeword.
                int currentCodeWordMatchLength = currentCodeWordMatch.length();      
                // Checking if there is room left in the symbol table
                // Checking to make sure there is room in the symbol table and that currentCodeWordMatchLength IS NOT greater than whatever words are left in the file.
                // Remember that input.length will decrease as we process the text file.
                if (currentCodeWordMatchLength < input.length() && endCodeWord < L)// Add s to symbol table.
                {
                    // Placing currentCodwWordMatch in the symbol table
                    // First we take the the remaining input from the beginning to the length of our codeword, then add 1 additional symbol.
                    // This represents adding an additional part to the codeword according to the algorthim
                    // Then we place it into the symbol table in the next available spot using endCodeWord++
                    //System.err.print(input.substring(0, currentCodeWordMatchLength + 1)+"\n");
                    st.put(input.substring(0, currentCodeWordMatchLength + 1), endCodeWord++);
                }
                // if endCodeWord > L
                // We need to resize the symbol table
                else if(currentCodeWordMatchLength< input.length() && W!=16)
                {
                    //System.err.println("the word width before it increased " + W);
                    //System.err.println("the current available number of codewords ");
                    //System.err.println("this number should match codewords as it is the current number of codewords in codebook " + code); 
                    increaseWordWidth();
                    increaseCodeWords();
                    st.put(input.substring(0, currentCodeWordMatchLength + 1), endCodeWord++);
                }

                input = input.substring(currentCodeWordMatchLength);            // Scan past s in input.
            }
            // We are writing to the end of file the R and W for expansion here
            BinaryStdOut.write(R, W);
            BinaryStdOut.close();
        }
        else if(mode.equals("r"))
        {
            String input = BinaryStdIn.readString();
            TST<Integer> st = new TST<Integer>();
            for (int i = 0; i < R; i++)
            {
                st.put("" + (char) i, i);
            }
            int code = R+1;  // R is codeword for EOF
            BinaryStdOut.write('r');
            while (input.length() > 0) 
            {
                String s = st.longestPrefixOf(input);  // Find max prefix match s.
                //problem here is the length W may be wrong I think. Write the wrong W it may be a problem.

                BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
                int t = s.length();
                //System.err.print(code+"\n");
                if (t < input.length() && code < L)// Add s to symbol table.
                {
                    st.put(input.substring(0, t + 1), code++);
                }
                // if code > L
                else if(t< input.length() && W!=16)
                {
                    //System.err.println("the word width before it increased " + W);
                    //System.err.println("the current available number of codewords ");
                    //System.err.println("this number should match codewords as it is the current number of codewords in codebook " + code); 
                    increaseWordWidth();
                    increaseCodeWords();
                    st.put(input.substring(0, t + 1), code++);
                }
                else if(W == 16 && code == 65536)
                {   
                    //System.err.print("hello");
                    W = 9;
                    L = 512;
                    st = new TST<Integer>();
                    code = R+1;
                    // repopulate the dictionary
                    for (int i = 0; i < R; i++)
                    {
                        st.put("" + (char) i, i);
                    }
                    st.put(input.substring(0, t + 1), code++);
                }
                input = input.substring(t);            // Scan past s in input.
            }
            BinaryStdOut.write(R, W);
            BinaryStdOut.close();
        }
        else if(mode.equals("m"))
        {
            double compressionRatio;
            double oldRatio = 1.0;
            double currentRatio = 1.0;
            double totalUncompressedBitsProcessed = 0;
            double totalCompressedBitsProcessed = 0;
            String input = BinaryStdIn.readString();
            compressionRatio = oldRatio/currentRatio;
            int counter =0;
            TST<Integer> st = new TST<Integer>();
            for (int i = 0; i < R; i++)
            {
                st.put("" + (char) i, i);
            }
            int code = R+1;  // R is codeword for EOF
            BinaryStdOut.write('m');
            while (input.length() > 0) 
            {
                String s = st.longestPrefixOf(input);  // Find max prefix match s.
                //problem here is the length W may be wrong I think. Write the wrong W it may be a problem.
                BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
                int t = s.length();
                totalUncompressedBitsProcessed += (t * 16);// for some reason a char is 16 bits in java that took me a while; 
                totalCompressedBitsProcessed += W;// the bits; 
                currentRatio = totalUncompressedBitsProcessed/totalCompressedBitsProcessed;
                //System.err.print(W+"\n");
                //System.err.print(code+"\n");
                //System.err.print(L+"\n");}
                if (t < input.length() && code < L)// Add s to symbol table.
                {
                    st.put(input.substring(0, t + 1), code++);
                }
                // if code > L
                else if(t< input.length() && W!=16)
                {
                    //System.err.println("the word width before it increased " + W);
                    //System.err.println("the current available number of codewords ");
                    //System.err.println("this number should match codewords as it is the current number of codewords in codebook " + code); 
                    increaseWordWidth();
                    increaseCodeWords();
                    st.put(input.substring(0, t + 1), code++);
                }
                else if(W== 16 && code==65536)
                {

                    if(counter == 0)
                    {
                        oldRatio = totalUncompressedBitsProcessed/totalCompressedBitsProcessed;
                        counter = 1;
                    }

                    compressionRatio = oldRatio/currentRatio;
                    //System.err.print(compressionRatio+"\n");
                    if(compressionRatio >= 1.1)
                    {
                        W = 9;
                        L = 512;
                        st = new TST<Integer>();
                        code = R+1;
                        // repopulate the dictionary
                        for (int i = 0; i < R; i++)
                        {
                            st.put("" + (char) i, i);
                        }
                        st.put(input.substring(0, t + 1), code++);
                        counter = 0;
                    }
                    //otherwise do nothing
                }
                input = input.substring(t);            // Scan past s in input.
            }

            BinaryStdOut.write(R, W);
            BinaryStdOut.close();
        }
    }

    public static void expand()
    {
        // i need a way to decompress using the variable length of the codeword?????
        // Since there is a chance that the dictionarly will be maxed at W = 16 my st needs to be
        // large enough to accomadtae everything

        // Consume the first character in the file for checking the mode
        char selectedMode = BinaryStdIn.readChar();
        if(selectedMode == 'n')
        {
            String[] st = new String[65536];
            int currentMaxLength = L-1;
            System.err.print(currentMaxLength); // 0 through 511 cause we count at 0
            int i; // next available codeword value

            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
            {   
                st[i] = "" + (char) i;
            }
            st[i++] = "";                        // (unused) lookahead for EOF

            int codeword = BinaryStdIn.readInt(W); // Consume a codeword 
            if (codeword == R)
            {
                //ohhhh here it means that R is the largest value 256 so there are no codewords therefore empty string
                return; // expanded message is empty string
            }

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
                //this is like checking if t < inputlength() kind of
                if (i < currentMaxLength)
                {
                    st[i++] = val + s.charAt(0);
                }
                else if(W != 16)
                {
                    increaseWordWidth();
                    increaseCodeWords();
                    currentMaxLength = L-1;
                    st[i++] = val + s.charAt(0);
                }
                val = s;
            }
            BinaryStdOut.close();
        }
        else if(selectedMode == 'r')
        {
            String[] st = new String[65536];
            int currentMaxLength = L-1;
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
                //ohhhh here it means that R is the largest value 256 so there are no codewords therefore empty string
                return; // expanded message is empty string
            }

            String val = st[codeword];

            while (true) 
            {
                //reset the values
                
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
                //this is like checking if t < inputlength() kind of
                if(W== 16 && i == currentMaxLength)
                {
                        W = 9;
                        L = 512;
                        st = new String[65536];
                        currentMaxLength = L-1;
                        
                        // repopulate the dictionary

                        for (i = 0; i < R; i++)
                        {
                            st[i] = "" + (char) i;
                        }
                        st[i++] = ""; 
                        i =R +1;
                }
                if (i < currentMaxLength)
                {
                     
                         try{
                       st[i++] = val + s.charAt(0);
                    }
                    catch(Exception E)
                    {
                        break;
                    }
                    
                   
                }
                else if(W != 16)
                {
                    increaseWordWidth();
                    increaseCodeWords();
                    currentMaxLength = L-1;
                    st[i++] = val + s.charAt(0);
                }
                val = s;

                //once the dictionarry is maxed out change stuff

            }
            BinaryStdOut.close();
        }
        else if(selectedMode == 'm')
        {
            String[] st = new String[65536];
            double compressionRatio = 1.0;
            double oldRatio = 1.0;
            double currentRatio = 1.0;
            double totalUncompressedBitsProcessed = 0;
            double totalCompressedBitsProcessed = 0;
            int currentMaxLength = L-1;
            int counter = 0;
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
                //ohhhh here it means that R is the largest value 256 so there are no codewords therefore empty string
                return; // expanded message is empty string
            }

            String val = st[codeword];

            while (true) 
            {
                //reset the values
                totalUncompressedBitsProcessed += (val.length() * 16);// for some reason a char is 16 bits in java that took me a while; 
                totalCompressedBitsProcessed += W;// the bits;
                currentRatio = totalUncompressedBitsProcessed/totalCompressedBitsProcessed;
                BinaryStdOut.write(val);
                if(W == 16 && i ==currentMaxLength)
                {
                    if(counter == 0)
                    {
                        oldRatio = totalUncompressedBitsProcessed/totalCompressedBitsProcessed;
                        counter = 1;
                    }
                    compressionRatio = oldRatio/currentRatio;
                    //System.err.print(compressionRatio+"\n");
                    if(compressionRatio >= 1.1)
                    {
                        W = 9;
                        L = 512;
                        st = new String[65536];
                        currentMaxLength = L-1;
                        
                        // repopulate the dictionary

                        for (i = 0; i < R; i++)
                        {
                            st[i] = "" + (char) i;
                        }
                        st[i++] = ""; 
                        i = R +1;
                        counter = 0;
                    }
                }
                codeword = BinaryStdIn.readInt(W);
                
                if (codeword == R)
                {
                    //System.err.print("hello");
                    break;
                }
                String s = st[codeword];
                if (i == codeword)
                {
                    // System.err.print("hello");
                    s = val + val.charAt(0);
                }// special case hack
                //this is like checking if t < inputlength() kind of
                if (i < currentMaxLength)
                {
                    //System.err.print(i+"\n");
                    //System.err.print(val+"\n");
                    //System.err.print(s+"\n");
                      try{
                       st[i++] = val + s.charAt(0);
                    }
                    catch(Exception E)
                    {
                        break;
                    }
                    
                }
                else if(W != 16)
                {
                    increaseWordWidth();
                    increaseCodeWords();
                    currentMaxLength = L-1;

                    st[i++] = val + s.charAt(0);
                }
                
                val = s;

                //once the dictionarry is maxed out change stuff

            }
            BinaryStdOut.close();
        }
    }

    public static void main(String[] args) {
        if(args[0].equals("-") && args.length == 1) 
        {
            compress("n");
        }
        else if (args[0].equals("+") && args.length ==1)
        {
            expand();
        }
        else if(args[0].equals("-") && args[1].equals("n"))
        {
            compress("n");
        }
        else if(args[0].equals("-") && args[1].equals("r"))
        {
            compress("r");
        }
        else if(args[0].equals("-") && args[1].equals("m"))
        {
            compress("m");
        }
        else 
        {
            throw new IllegalArgumentException("Illegal command line argument" );
        }
    }
}
