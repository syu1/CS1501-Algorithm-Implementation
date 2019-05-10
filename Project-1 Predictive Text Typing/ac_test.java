// Written by Sam Yu
import java.util.Scanner;
import java.io.*;
import java.util.Arrays;
public class ac_test
{
    Node head = null;
    static class Node
    {
        char data;
        Node down;
        Node right;
        Node(char character)
        {
            data = character;
            down = null;
            right = null;
        }
    }
    public ac_test()
    {

        String currentLine = "placeholder";
        try
        {
            File dictionary = new File("dictionary.txt");

            FileReader dictionaryRead = new FileReader(dictionary);
            BufferedReader buffRead = new BufferedReader(dictionaryRead);

            while(currentLine!= null)
            {
                currentLine = buffRead.readLine();
                if(currentLine == null)
                {
                    break;
                }
                currentLine = currentLine +"$";
                //System.out.println(currentLine);
                this.addPhrase(currentLine);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        catch(IOException e)
        {
            System.out.println("File could not be opened");
        }

    }
    // This is a really dumb way to do it. I should have 1 constructor that takes in different file types but I'm lazy soooo.
    public ac_test(int type2)
    {
        try
        {
            String currentLine = "placeholder";
            File dictionary = new File("user_history.txt");

            FileReader dictionaryRead = new FileReader(dictionary);
            BufferedReader buffRead = new BufferedReader(dictionaryRead);

            while(currentLine!= null)
            {
                currentLine = buffRead.readLine();
                if(currentLine == null)
                {
                    break;
                }
                currentLine = currentLine +"$";
                //System.out.println(currentLine);
                this.addPhrase(currentLine);
            }

        }
        catch (FileNotFoundException e)
        {
            System.out.println("No words from previous history detected");
        }
        catch(IOException e)
        {
            System.out.println("No words from previous history detected");
        }

    }
    // Checks for empty DLB
    public Node getHead()
    {
        return head;
    }

    public void addPhrase(String phrase)
    {
        // parse into new char array
        Node node = head;
        int length = phrase.length();
        char phraseCharArray[] = phrase.toCharArray();
        // keep track of where we are in the new word adding
        int phraseCounter = 0;
        // this should work for a somewhat populated dlb
        while(phraseCounter != length)
        {
            if(head == null)
            {
                head = new Node(phraseCharArray[phraseCounter]);

                phraseCounter++;
                node = head;
                //System.out.printf("%c",node.data);
            }
            else if(phraseCharArray[phraseCounter] == '$')
            {
                node.down = new Node('$');

                node = node.down;
                //System.out.printf("%c",node.data);
                return;
            }
            else if(node.down == null && node.right==null && node.data != '$')
            {

                node.down= new Node(phraseCharArray[phraseCounter]);
                phraseCounter++;
                node = node.down;
                // System.out.printf("%c",node.data);
            }

            else if(phraseCharArray[phraseCounter] == node.data )
            {
                phraseCounter++;
                if(node.down== null)
                {
                    node.down = new Node(phraseCharArray[phraseCounter]);

                }
                node = node.down;
                // System.out.printf("%c",node.data);
            }
            else if(phraseCharArray[phraseCounter] != node.data)
            {
                if(node.right ==null)
                {

                    node.right = new Node(phraseCharArray[phraseCounter]);

                    phraseCounter++;
                }
                node = node.right;
                // System.out.printf("%c",node.data);
            }
            else
            {
                System.out.println("This should never display something broke in the ac_test generator!");
            }

        }

    }

    public String[] getPredictions(char array[])
    {
        Node node = head;
        boolean remainFlag = true;
        char character = array[0];
        int charArrayIndexCounter = 0;
        int numberOfPredictionsMade = 0;
        String[] predictions = new String[5];
        String prediction ="";
        Scanner input = new Scanner(System.in);
        while(true)
        {
            try
            {
                if(node.down == null && node.right == null)
                {
                    predictions[numberOfPredictionsMade] = prediction;
                    numberOfPredictionsMade++;
                    return predictions;
                }
            }
            catch(NullPointerException e)
            {
                String stringArray = new String(array);
                System.out.println("Brand new word detected: "+stringArray);
                System.out.println("Add additional letters charcter by character then enter $ to finish or if your already done enter $ to finish");
                char newChar ='a';
                while(newChar !='$')
                {
                    newChar = input.next().charAt(0);
                    if(newChar =='$')
                    {
                        break;
                    }
                    prediction+=newChar;

                }
                if(array[array.length-1] !='$')
                {
                    prediction+=array[array.length-1];
                }
                System.out.println("New word added to dictionary: "+prediction);
                this.saveCommonWord(prediction);
                this.addPhrase((prediction+'$'));
                predictions[numberOfPredictionsMade] = prediction;
                numberOfPredictionsMade++;
                return null;
            }
            if(remainFlag == true)
            {
                if(node.data == character)
                {
                    prediction += node.data;
                    charArrayIndexCounter++;
                    node = node.down;
                    if(charArrayIndexCounter == array.length)
                    {
                        remainFlag = false;
                        continue;
                    }
                    character = array[charArrayIndexCounter];

                }
                else if(node.data != character)
                {
                    node = node.right;
                }
                else if(node.data == '$')
                {
                    predictions[numberOfPredictionsMade] = prediction;
                    numberOfPredictionsMade++;
                }
            }
            else
            {
                //System.out.printf("%c",node.data);
                if(node.down != null && node.data !='$')
                {
                    prediction +=node.data;
                    node = node.down;
                }
                else if(node.data == '$')
                {
                    predictions[numberOfPredictionsMade] = prediction;
                    numberOfPredictionsMade++;
                    if(node.right != null)
                    {
                        node = node.right;
                    }
                    else
                    {
                        return predictions;
                    }
                }
                else if(numberOfPredictionsMade == 5)
                {
                    return predictions;
                }
            }
        }

    }
    // Once again I have done the lazy thing. the type 2 does nothing except overload things
    public String[] getPredictions(char array[],int type2)
    {
        Node node = head;
        boolean remainFlag = true;
        char character = array[0];
        int charArrayIndexCounter = 0;
        int numberOfPredictionsMade = 0;
        String[] predictions = new String[5];
        String prediction ="";
        Scanner input = new Scanner(System.in);
        while(true)
        {
            try
            {
                if(node.down == null && node.right == null)
                {
                    predictions[numberOfPredictionsMade] = prediction;
                    numberOfPredictionsMade++;
                    return predictions;
                }
            }
            catch(NullPointerException e)
            {
                return null;
            }
            if(remainFlag == true)
            {
                if(node.data == character)
                {
                    prediction += node.data;
                    charArrayIndexCounter++;
                    node = node.down;
                    if(charArrayIndexCounter == array.length)
                    {
                        remainFlag = false;
                        continue;
                    }
                    character = array[charArrayIndexCounter];

                }
                else if(node.data != character)
                {
                    node = node.right;
                }
                else if(node.data == '$')
                {
                    predictions[numberOfPredictionsMade] = prediction;
                    numberOfPredictionsMade++;
                }
            }
            else
            {
                //System.out.printf("%c",node.data);
                if(node.down != null && node.data !='$')
                {
                    prediction +=node.data;
                    node = node.down;
                }
                else if(node.data == '$')
                {
                    predictions[numberOfPredictionsMade] = prediction;
                    numberOfPredictionsMade++;
                    if(node.right != null)
                    {
                        node = node.right;
                    }
                    else
                    {
                        return predictions;
                    }
                }
                else if(numberOfPredictionsMade == 5)
                {
                    return predictions;
                }
            }
        }

    }

    public boolean saveNewWord(String word)
    {
        word = "\n"+word;
        try
        {
            File saved = new File("new_user_history.txt");
            boolean firstTime = saved.exists();
            if(firstTime == false)
            {
                saved.createNewFile();
                FileWriter dictionaryWrite = new FileWriter(saved);
                BufferedWriter buffWriter = new BufferedWriter(dictionaryWrite);
                buffWriter.write(word);
                buffWriter.close();
            }
            else
            {
                FileWriter dictionaryWrite = new FileWriter(saved,true);
                BufferedWriter buffWriter = new BufferedWriter(dictionaryWrite);
                buffWriter.newLine();
                buffWriter.write(word);
                buffWriter.close();
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        catch(IOException e)
        {
            System.out.println("File could not be openned");
        }
        return true;
    }

    public boolean saveCommonWord(String word)
    {
        word = "\n"+word;
        try
        {
            File saved = new File("user_history.txt");
            boolean firstTime = saved.exists();
            if(firstTime == false)
            {
                saved.createNewFile();
                FileWriter dictionaryWrite = new FileWriter(saved);
                BufferedWriter buffWriter = new BufferedWriter(dictionaryWrite);
                buffWriter.write(word);
                buffWriter.close();
            }
            else
            {
                FileWriter dictionaryWrite = new FileWriter(saved,true);
                BufferedWriter buffWriter = new BufferedWriter(dictionaryWrite);
                buffWriter.newLine();
                buffWriter.write(word);
                buffWriter.close();
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        catch(IOException e)
        {
            System.out.println("File could not be openned");
        }
        return true;
    }

    public static int countNulls(String array[])
    {
        int nullCounter = 0;
        for(int i = 0; i < array.length;i++)
        {
            if(array[i]==null)
            {
                nullCounter++;
            }
        }
        return nullCounter;
    }

    public static void main(String[] args)
    {
        // I do realize I wrote this like an idiot I'm so sorry whoever has to grade this.
        ac_test dlb = new ac_test();
        ac_test user_history_dlb = new ac_test(2);
        int newRound = 0;
        char charArray[] = new char[1];
        int requestCounter = 0;
        int sizeCounter = 1;

        long totalTime = 0;
        Scanner reader = new Scanner(System.in);
        char input = 'a';
        while(input != '!')
        {
            if(newRound == 0)
            {
                // The 2 dosn't mean anything lol
                user_history_dlb = new ac_test(2);
                charArray = new char[1];
                requestCounter = 0;
                sizeCounter = 1;
                newRound = 1;
                System.out.print("Your first character: ");
                input = reader.next().charAt(0);
            }

            long startTime = System.nanoTime();
            charArray = java.util.Arrays.copyOf(charArray, sizeCounter);
            charArray[requestCounter] = input;
            requestCounter++;
            if(input == '!')
            {
                break;
            }
            String predictions[] = dlb.getPredictions(charArray);
            if(predictions == null)
            {
                newRound=0;
                continue;
            }
            if(user_history_dlb.getHead() != null)
            {
                String userHistoryPredictions[] = user_history_dlb.getPredictions(charArray,2);

                if(userHistoryPredictions !=null)
                {                    
                    String combinedPredictions[] = userHistoryPredictions;
                    int max0 = 0;
                    int max1 = 0;
                    int max2 = 0;
                    int max3 = 0;
                    
                    int counter = 0;
                    int userNulls = ac_test.countNulls(userHistoryPredictions);
                    int offSetNulls = 5-userNulls;
                    // just gonnna do this manually
                    for(int i = 0; i < userNulls;i++)
                    {
                        combinedPredictions[i+offSetNulls] = predictions[i];
                    }
                    for(int i = 0; i< 4; i++)
                    {
                        for(int j = 1; j <5; j++)
                        {
                            if(combinedPredictions[i] == combinedPredictions[j])
                            {
                                combinedPredictions[j] = null;
                                if(i == 0)
                                {
                                    max0++;
                                }
                                else if(i==1)
                                {
                                    max1++;
                                }
                                else if(i==2)
                                {
                                    max2++;
                                }
                                else if(i==3)
                                {
                                    max3++;
                                }                                            
                            }
                        }
                    }
                    
                    long estimate =System.nanoTime() - startTime;
                    totalTime +=estimate;
                    System.out.println("\n("+estimate+" ns)");
                    sizeCounter++;
                    System.out.println("Predictions: ");

                    String prediction1 = combinedPredictions[0];
                    String prediction2 = combinedPredictions[1];
                    String prediction3 = combinedPredictions[2];
                    String prediction4 = combinedPredictions[3];
                    String prediction5 = combinedPredictions[4];
                    System.out.printf("(1) %s,(2) %s,(3) %s,(4) %s,(5) %s\n",prediction1, prediction2,prediction3,prediction4,prediction5);
                    System.out.print("Your next character: ");
                    boolean selectBool = reader.hasNextInt();
                    input = reader.next().charAt(0);

                    if(input == '$')
                    {
                        String output = new String(charArray);
                        if(output.equals(prediction1))
                        {
                            System.out.println("WORD COMPLETED1: "+prediction1);
                            newRound = 0;
                        }
                        else if(output.equals(prediction2))
                        {
                            System.out.println("WORD COMPLETED2: "+prediction2);
                            newRound = 0;
                        }
                        else if(output.equals(prediction3))
                        {
                            System.out.println("WORD COMPLETED3: "+prediction3);
                            newRound = 0;
                        }
                        else if(output.equals(prediction4))
                        {
                            System.out.println("WORD COMPLETED4: "+prediction4);
                            newRound = 0;
                        }
                        else if(output.equals(prediction5))
                        {
                            System.out.println("WORD COMPLETED5: "+prediction5);
                            newRound = 0;
                        }
                        else
                        {
                            System.out.println("WORD COMPLETED6: "+ output);
                            // Weird but the way I wrote it. If you call this it adds a new word to the dict.
                            charArray = java.util.Arrays.copyOf(charArray, sizeCounter);
                            charArray[requestCounter] = input;
                            requestCounter++;
                            dlb.getPredictions(charArray);
                            newRound = 0;
                        }

                        continue;
                    }
                    if(selectBool == true)
                    {
                        int selection = Character.getNumericValue(input);
                        if(selection == 1)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction1);
                            System.out.println("WORD COMPLETED " + prediction1);
                        }
                        if(selection == 2)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction2);
                            System.out.println("WORD COMPLETED " + prediction2);
                        }
                        if(selection == 3)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction3);
                            System.out.println("WORD COMPLETED " + prediction3);
                        }
                        if(selection == 4)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction4);
                            System.out.println("WORD COMPLETED " + prediction4);
                        }
                        if(selection == 5)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction5);
                            System.out.println("WORD COMPLETED " + prediction5);
                        }
                    }
                }
                else
                {
                    long estimate =System.nanoTime() - startTime;
                    totalTime +=estimate;
                    System.out.println("\n("+estimate+" ns)");
                    sizeCounter++;
                    System.out.println("Predictions: ");

                    String prediction1 = predictions[0];
                    String prediction2 = predictions[1];
                    String prediction3 = predictions[2];
                    String prediction4 = predictions[3];
                    String prediction5 = predictions[4];

                    System.out.printf("(1) %s,(2) %s,(3) %s,(4) %s,(5) %s\n",prediction1, prediction2,prediction3,prediction4,prediction5);

                    System.out.print("Your next character: ");
                    boolean selectBool = reader.hasNextInt();
                    input = reader.next().charAt(0);

                    if(input == '$')
                    {
                        String output = new String(charArray);
                        if(output.equals(prediction1))
                        {
                            System.out.println("WORD COMPLETED1: "+prediction1);
                            newRound = 0;
                        }
                        else if(output.equals(prediction2))
                        {
                            System.out.println("WORD COMPLETED2: "+prediction2);
                            newRound = 0;
                        }
                        else if(output.equals(prediction3))
                        {
                            System.out.println("WORD COMPLETED3: "+prediction3);
                            newRound = 0;
                        }
                        else if(output.equals(prediction4))
                        {
                            System.out.println("WORD COMPLETED4: "+prediction4);
                            newRound = 0;
                        }
                        else if(output.equals(prediction5))
                        {
                            System.out.println("WORD COMPLETED5: "+prediction5);
                            newRound = 0;
                        }
                        else
                        {
                            System.out.println("WORD COMPLETED6: "+ output);
                            // Weird but the way I wrote it. If you call this it adds a new word to the dict.
                            charArray = java.util.Arrays.copyOf(charArray, sizeCounter);
                            charArray[requestCounter] = input;
                            requestCounter++;
                            dlb.getPredictions(charArray);
                            newRound = 0;
                        }

                        continue;
                    }
                    if(selectBool == true)
                    {
                        int selection = Character.getNumericValue(input);
                        if(selection == 1)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction1);
                            System.out.println("WORD COMPLETED " + prediction1);
                        }
                        if(selection == 2)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction2);
                            System.out.println("WORD COMPLETED " + prediction2);
                        }
                        if(selection == 3)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction3);
                            System.out.println("WORD COMPLETED " + prediction3);
                        }
                        if(selection == 4)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction4);
                            System.out.println("WORD COMPLETED " + prediction4);
                        }
                        if(selection == 5)
                        {
                            newRound = 0;
                            dlb.saveCommonWord(prediction5);
                            System.out.println("WORD COMPLETED " + prediction5);
                        }
                    }
                }
            }
            else
            {

                long estimate =System.nanoTime() - startTime;
                totalTime +=estimate;
                System.out.println("\n("+estimate+" ns)");
                sizeCounter++;
                System.out.println("Predictions: ");

                String prediction1 = predictions[0];
                String prediction2 = predictions[1];
                String prediction3 = predictions[2];
                String prediction4 = predictions[3];
                String prediction5 = predictions[4];

                System.out.printf("(1) %s,(2) %s,(3) %s,(4) %s,(5) %s\n",prediction1, prediction2,prediction3,prediction4,prediction5);

                System.out.print("Your next character: ");
                boolean selectBool = reader.hasNextInt();
                input = reader.next().charAt(0);

                if(input == '$')
                {
                    String output = new String(charArray);
                    if(output.equals(prediction1))
                    {
                        System.out.println("WORD COMPLETED1: "+prediction1);
                        newRound = 0;
                    }
                    else if(output.equals(prediction2))
                    {
                        System.out.println("WORD COMPLETED2: "+prediction2);
                        newRound = 0;
                    }
                    else if(output.equals(prediction3))
                    {
                        System.out.println("WORD COMPLETED3: "+prediction3);
                        newRound = 0;
                    }
                    else if(output.equals(prediction4))
                    {
                        System.out.println("WORD COMPLETED4: "+prediction4);
                        newRound = 0;
                    }
                    else if(output.equals(prediction5))
                    {
                        System.out.println("WORD COMPLETED5: "+prediction5);
                        newRound = 0;
                    }
                    else
                    {
                        System.out.println("WORD COMPLETED6: "+ output);
                        // Weird but the way I wrote it. If you call this it adds a new word to the dict.
                        charArray = java.util.Arrays.copyOf(charArray, sizeCounter);
                        charArray[requestCounter] = input;
                        requestCounter++;
                        dlb.getPredictions(charArray);
                        newRound = 0;
                    }

                    continue;
                }
                if(selectBool == true)
                {
                    int selection = Character.getNumericValue(input);
                    if(selection == 1)
                    {
                        newRound = 0;
                        dlb.saveCommonWord(prediction1);
                        System.out.println("WORD COMPLETED " + prediction1);
                    }
                    if(selection == 2)
                    {
                        newRound = 0;
                        dlb.saveCommonWord(prediction2);
                        System.out.println("WORD COMPLETED " + prediction2);
                    }
                    if(selection == 3)
                    {
                        newRound = 0;
                        dlb.saveCommonWord(prediction3);
                        System.out.println("WORD COMPLETED " + prediction3);
                    }
                    if(selection == 4)
                    {
                        newRound = 0;
                        dlb.saveCommonWord(prediction4);
                        System.out.println("WORD COMPLETED " + prediction4);
                    }
                    if(selection == 5)
                    {
                        newRound = 0;
                        dlb.saveCommonWord(prediction5);
                        System.out.println("WORD COMPLETED " + prediction5);
                    }
                }
            }
        }
        System.out.println("Your average time was " + totalTime/requestCounter +" ns");
    }

}