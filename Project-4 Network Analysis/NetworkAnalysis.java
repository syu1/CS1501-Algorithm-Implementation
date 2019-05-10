import java.util.Scanner;
/**
 * Write a description of class NetWorkFlow here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class NetworkAnalysis
{
    // instance variables - replace the example below with your own
    private static Scanner myScan = new Scanner(System.in);
    private static final String NEWLINE = System.getProperty("line.separator");
    private static int numVertexs;
    private static int numEdges;

    private static CS1501Stream myStream;
    /**
     * Constructor for objects of class NetWorkFlow
     */
    public NetworkAnalysis()
    {
    }

    public static void main(String args[])
    {
        myStream = new CS1501Stream(args[0]);
        FlowNetwork G = new FlowNetwork(myStream);
        numVertexs= G.V();
        numEdges = G.E();

        //Iterable<FlowEdge> iterable;

        //In in = new In(args[0]);
        //FlowNetwork G = new FlowNetwork(in);
        //StdOut.println(G);
        System.out.println("Welcome to the NetWorkFlow Program!" );
        int selectionChoice = 0;

        while(true)
        {
            System.out.println("Would you like to:");
            System.out.println("1. Find the lowest Latency Path");
            System.out.println("2. See if this network is only copper connected");
            System.out.println("3. Find the lowest average latency spanning tree for the network");
            System.out.println("4. Determine if any two severed vertices would cause the network to fail");
            System.out.println("5. Exit the program: ");
            System.out.println("Please select a number: ");
            selectionChoice = myScan.nextInt();
            if(selectionChoice == 1)
            {
                int s;
                int t;
                double distance;
                // bugs if the bandwith is like a quintillion gigabits lol
                double minCapacity=10000000000000000.0;
                Iterable<FlowEdge> shortEdges;
                System.out.println("What is your first vertex?: ");
                s = myScan.nextInt();
                System.out.println("What is your second vertex?: ");
                t = myScan.nextInt();
                DijkstraAllPairsSP shortPath = new DijkstraAllPairsSP(G);

                shortEdges = shortPath.path(s,t);
                StringBuilder str = new StringBuilder();

                int v = 0;
                str.append(v + ":  ");
                for (FlowEdge e : shortEdges) {
                    //System.out.println(e.capacity());
                    if(minCapacity > e.capacity())
                    {
                        minCapacity = e.capacity();
                    }

                    if(e.to() != v)
                    {
                        str.append(e + "  ");
                    }
                }
                str.append(NEWLINE);
                //}
                System.out.println(str.toString());
                System.out.println("Minimum bandwith along this path: " + minCapacity);

                //System.out.println("The bandwith available along this path is: "+ distance);
                // for (int v = 0; v < G.V(); v++) {
                // for (FlowEdge e : G.adj(v)) {
                // if ((v == e.from()) && e.flow() > 0)
                // StdOut.println("   " + e);
                // }
                // }

            }
            else if(selectionChoice ==2)
            {
                if(myStream.allCopper() == true)
                {
                    System.out.println("The network is all copper connected\n");
                }
                else
                {
                    System.out.println("The network is not all copper connected\n");
                }
            }
            
            else if(selectionChoice ==3)
            {
                int s = 0;
                DijkstraSP sp = new DijkstraSP(G, s);

                // print shortest path
                for (int t = 0; t < G.V(); t++) {
                    if (sp.hasPathTo(t)) {
                        StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
                        for (FlowEdge e : sp.pathTo(t)) {
                            StdOut.print(e + "   ");
                        }
                        StdOut.println();
                    }
                    else {
                        StdOut.printf("%d to %d         no path\n", s, t);
                    }
                }

            }
            else if(selectionChoice ==4)
            {
                // Here is a slow an extremley slow implementation lol
                boolean networkFailureDetected = false;
                for(int i = 0; i<numVertexs;i++)
                {
                    for(int j = 1; j<numVertexs;j++)
                    {
                        if(i == j)
                        {
                            continue;
                        }
                        //System.out.println("The severed nodes are " + i + " and " + j);
                        FlowNetwork testFail = new FlowNetwork(myStream,i,j);
                        WeightedQuickUnionUF testFailUF = testFail.getUF();

                        //System.out.println("The number of components in this graph");
                        //System.out.println(testFailUF.count());
                        if(testFailUF.count()>1)
                        {
                            //System.out.println(testFailUF.count());
                            networkFailureDetected = true;
                            System.out.println("Removing node "+i+" and node "+j+" will cause network failure");
                        }
                    }
                }
                if(networkFailureDetected == false)
                {
                    System.out.println("No network failures by severing any set of two nodes.");
                }
                else
                {
                    System.out.println("There has been Network Failure Detected");
                }


            }
            else if(selectionChoice == 5)
            {
                System.exit(0);
            }
            else
            {
                System.out.println("Wrong input sorry");
            }

        }
    }
}
