import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
/**
 * Write a description of class CS1501Stream here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CS1501Stream
{
    // instance variables - replace the example below with your own
    private String textFile;
    private int totalV;
    private int totalE = 0;
    private int v;
    private int w;
    private double capacity;
    private String wireType;
    private double cableLength;
    private double latency;
    private boolean allCopper = true;
    //cheesing the numbers a little so I dont have to deal with great precison
    //result remains the same 
    private final int copperSpeed =  230000000;
    private final int opticalSpeed = 200000000;
    private ArrayList<FlowEdge> readFlowEdges = new ArrayList<FlowEdge>();
    private ArrayList<Vertex> vArray = new ArrayList<Vertex>();
    /**
     * Constructor for objects of class CS1501Stream
     */
    public CS1501Stream(String textFile)
    {
        try
        {
            this.textFile = textFile;
            //System.out.println("hi");
            File myNetworkFile = new File(textFile);
            if(myNetworkFile.exists())
            {
                //reads from file line by line and constructs network object from saved information
                Scanner inputFile = new Scanner(myNetworkFile);//.useDelimiter("\\n");
                totalV = inputFile.nextInt();
                while(inputFile.hasNext())
                {
                    v = inputFile.nextInt();

                    w = inputFile.nextInt();

                    wireType = inputFile.next();

                    capacity = inputFile.nextDouble();
                    cableLength = inputFile.nextInt();
                    if(wireType.equals("optical"))
                    {                   
                        allCopper = false;
                        latency = cableLength/opticalSpeed;

                    }
                    if(wireType.equals("copper"))
                    {
                        latency = cableLength/copperSpeed;

                    }

                    FlowEdge myFlowEdge = new FlowEdge(v,w,capacity,wireType,cableLength,latency);
                    Vertex vertex = new Vertex(v,w);
                    vArray.add(vertex);
                    readFlowEdges.add(myFlowEdge);
                    totalE++;
                }
                inputFile.close();
            }
            //System.out.print(totalV);
            //System.out.print(totalE);
            //System.out.print(readFlowEdges.get(4).wireType());
        }
        catch(IOException e)
        {
            System.out.println("File not found");

        }

    }
    public CS1501Stream(String textFile, int removedVertex1, int removedVertex2)
    {
        try
        {
            this.textFile = textFile;
            //System.out.println("hi");
            File myNetworkFile = new File(textFile);
            if(myNetworkFile.exists())
            {
                //reads from file line by line and constructs network object from saved information
                Scanner inputFile = new Scanner(myNetworkFile);//.useDelimiter("\\n");
                totalV = inputFile.nextInt()-2;
                while(inputFile.hasNext())
                {
                    v = inputFile.nextInt();
                    w = inputFile.nextInt();
                    if(removedVertex1 == v || removedVertex2 == v)
                    {
                        inputFile.nextLine();

                    }
                    else
                    {   
                        if(v>=removedVertex2)
                        {
                            
                            v = v-1;
                            //System.out.println("v is now "+v);
                        }
                            
   
                        if(v>=removedVertex1)
                        {
                            v= v-1;
                            //System.out.println("v is now "+v);
                        }
                        
                        
                        //System.out.println("vertex number "+v);
                       
                        if(w>=removedVertex2)
                        {
                            
                            w = w-1;
                            //System.out.println("w is now "+w);
                        }
                            
   
                        if(w>=removedVertex1)
                        {
                            w-=1;
                            //System.out.println("w is now "+w);
                        }

                        wireType = inputFile.next();

                        capacity = inputFile.nextDouble();
                        cableLength = inputFile.nextInt();
                        if(wireType.equals("optical"))
                        {                   
                            allCopper = false;
                            latency = cableLength/opticalSpeed;

                        }
                        if(wireType.equals("copper"))
                        {
                            latency = cableLength/copperSpeed;

                        }

                        FlowEdge myFlowEdge = new FlowEdge(v,w,capacity,wireType,cableLength,latency);
                        Vertex vertex = new Vertex(v,w);
                        vArray.add(vertex);
                        readFlowEdges.add(myFlowEdge);
                        totalE++;
                    }
                }
                inputFile.close();
            }
            //System.out.print(totalV);
            //System.out.print(totalE);
            //System.out.print(readFlowEdges.get(4).wireType());
        }
        catch(IOException e)
        {
            System.out.println("File not found");

        }

    }
    public int totalV()
    {
        return totalV;
    }

    public int totalE()
    {
        return totalE;
    }

    public int getv()
    {
        return v;
    }

    public int getw()
    {
        return w;
    }

    public boolean allCopper()
    {
        return allCopper;
    }

    public FlowEdge currentFlowEdgeFromFile(int index)
    {
        return readFlowEdges.get(index);
    }
    public ArrayList<Vertex> getVArray()
    {
        return vArray;
    }
   

    public Vertex currentVertexFromFile(int index)
    {
        return vArray.get(index);
    }

}
