import java.util.Scanner;
import java.io.*;
/**
 * CarTracker implements an car tracking program that utilizes minMaxPQ
 * 
 * @author Sam Yu
 * @version 3/21/2019
 */
public class CarTracker
{    
    private static Scanner myScan = new Scanner(System.in).useDelimiter("\\n");
    private static BST<Car, Car> bstPrice = new BST<Car, Car>();
    private static BST<Car, Car> bstMileage = new BST<Car, Car>();
    private static BST<String, Car> bstPQPrice = new BST<String, Car>();
    private static BST<String, Car> bstPQMileage = new BST<String, Car>();

    // NOTE REDO THIS WITH TWO PQS!!! Genius idea Sam
    // One pq by mileage and one pq by price
    // Going to need 2 types of car objects though. Gotta overload that method lol
    /**
     * Constructor for objects of class CarTracker
     */
    public CarTracker()
    {
        //Driver dont really need nuffing

    }

    public static void main(String args[])
    {
        int selectionChoice = 0;

        CarTracker.populateHeap();
        System.out.println("Welcome to the CarTracker Program!" );
        while(selectionChoice !=8)
        {  
            System.out.println("\nWould you like to:");
            System.out.println("1. Add an car");
            System.out.println("2. Update an car");
            System.out.println("3. Remove an car from consideration");
            System.out.println("4. Retrieve the lowest priced car");
            System.out.println("5. Retrieve the lowest mileage car");
            System.out.println("6. Retrieve the lowest priced car by make and model");
            System.out.println("7. Retrieve the lowest mileage car by make and model");
            System.out.println("8. Exit the program: ");
            System.out.println("Please select a number: "); 
            selectionChoice = myScan.nextInt();
            if(selectionChoice == 1)
            {
                CarTracker.addACar();
            }
            else if(selectionChoice ==2)
            {
                CarTracker.updateACar();
            }
            else if(selectionChoice ==3)
            {
                CarTracker.removeCar();
            }
            else if(selectionChoice ==4)
            {
                CarTracker.lowestPriceCar();
            }
            else if(selectionChoice ==5)
            {
                CarTracker.lowestMileageCar();
            }
            else if(selectionChoice ==6)
            {
                CarTracker.lowestPriceByMakeModel();
            }
            else if(selectionChoice ==7)
            {
                CarTracker.lowestMileageByMakeModel ();
            }
            else if(selectionChoice == 8)
            {
                System.exit(0);
            }
            else
            {
                System.out.println("Wrong input sorry");
            }

        }

    }

    public static void populateHeap()
    {
        Car newCarPrice;
        Car newCarMileage;
        String vin;
        String make;
        String model;
        int mileage;
        double price;
        String color;

        try
        {
            File cars = new File("cars.txt");
            if(cars.exists())
            {
                Scanner inputFile = new Scanner(cars);
                inputFile.nextLine();
                inputFile.useDelimiter("[:\r\n]");

                while(inputFile.hasNext())
                {
                    vin = inputFile.next();
                    make = inputFile.next();
                    model = inputFile.next();
                    
                    price = inputFile.nextDouble();
                    mileage = inputFile.nextInt();
                    
                    color = inputFile.next();

                    newCarPrice = new Car(vin,make,model,mileage,price,color,0);
                    newCarMileage = new Car(vin,make,model,mileage,price,color,1);
                    bstPrice.put(newCarPrice,newCarPrice);
                    bstMileage.put(newCarMileage,newCarMileage);
                    bstPQPrice.put(vin,newCarPrice);
                    bstPQMileage.put(vin,newCarMileage);

                    if(inputFile.hasNext())
                        inputFile.nextLine();
                }
            }

        }
        catch(IOException e)
        {
            System.out.println("File not found");
        }

    }

    public static void addACar()
    {

        String vin;
        String make;
        String model;
        int mileage;
        double price;
        String color;

        System.out.println("\nChoice one selected!");

        System.out.println("What is the vin of the car: ");
        vin = myScan.next();

        System.out.println("What is the make of the car: ");
        make = myScan.next();

        System.out.println("What is the model of the car: ");
        model = myScan.next();

        System.out.println("What is the mileage of the car: ");
        mileage = myScan.nextInt();

        System.out.println("What is the price to rent the car: ");
        price = myScan.nextDouble();

        System.out.println("What is the color of the car: ");
        color = myScan.next();

        Car newCarPrice = new Car(vin,make,model,mileage,price,color,0);
        Car newCarMileage = new Car(vin,make,model,mileage,price,color,1);
        newCarPrice.toStrings();
        //Add the newCar to the maxheap priority queue!
        bstPrice.put(newCarPrice,newCarPrice);
        bstMileage.put(newCarMileage,newCarMileage);
        bstPQPrice.put(vin,newCarPrice);
        bstPQMileage.put(vin,newCarMileage);

    }

    public static void updateACar()
    {
        boolean found = false;
        String vin;
        double price;
        int mileage;
        String color;

        System.out.println("\nChoice Two selected!");

        System.out.println("What is the vin of the car to be updated?: ");
        vin = myScan.next();
        Car myCar1 = bstPQPrice.get(vin);
        Car myCar2 = bstPQMileage.get(vin);
        bstPrice.delete(myCar1);
        bstMileage.delete(myCar2);
        System.out.println("Select a choice (1.Change Price),(2.Change Mileage),(3.Color): ");
        int selectionChoice = myScan.nextInt();
        if(selectionChoice == 1)
        {
            System.out.println("What is the new price of the car?: ");
            price = myScan.nextDouble();
            myCar1.updatePrice(price);
            myCar2.updatePrice(price);

        }
        else if(selectionChoice ==2)
        {
            System.out.println("What is the mileage of the car to be updated?: ");
            mileage = myScan.nextInt();
            myCar1.updateMileage(mileage);
            myCar2.updateMileage(mileage);
        }
        else if(selectionChoice ==3)
        {
            System.out.println("What is the color of the car to be updated?: ");
            color = myScan.next();
            myCar1.updateColor(color);
            myCar2.updateColor(color);
        }
        
        
        bstPQPrice.put(vin,myCar1);
        bstPQMileage.put(vin,myCar2);
        
        bstPrice.put(myCar1,myCar1);
        // there may be some extreme weird case where references don't match up and i put in a different car here. Its really deep in the programmin
        // I hope i don't get off for this strange edge case. hopefully the double cars fixes this though.
        bstMileage.put(myCar2,myCar2);

    }

    public static void removeCar()
    {
        boolean found = false;
        String vin;

        System.out.println("\nChoice Three selected!");

        System.out.println("What is the vin of the car to be removed from listing?: ");
        vin = myScan.next();
        Car myCar1 = bstPQPrice.get(vin);
        Car myCar2 = bstPQMileage.get(vin);
        bstPQPrice.delete(vin);
        bstPQMileage.delete(vin);

        bstPrice.delete(myCar1);
        bstMileage.delete(myCar2);

    }

    public static void lowestPriceCar()
    {
        System.out.println("\nChoice Four selected!");

        System.out.println("The lowest price car in the listing is");
        if(bstPrice.isEmpty() ==true)
        {
            System.out.println("The list is empty!");
        }
        else
        {
            Car minCar = bstPrice.min();
            System.out.println("Vin is "+minCar.getVin());
            System.out.println("Car Make is" +minCar.getMake());
            System.out.println("Model is " +minCar.getModel());
            System.out.println("Mileage is "+ minCar.getMileage());
            System.out.println("Price is " + minCar.getPrice());
            System.out.println("Color is " +minCar.getColor());
        }
        //then jump to that index and unbox the car

    }

    // One way to solve this issue is two have two PQ running one by lowest mileage and one by lowest price!
    public static void lowestMileageCar()
    {
        System.out.println("\nChoice Five selected!");

        System.out.println("The lowest mileage car in the listing is");
        if(bstMileage.isEmpty() ==true)
        {
            System.out.println("The list is empty!");
        }
        else
        {
            Car minCar = bstMileage.min();
            System.out.println("Vin is "+minCar.getVin());
            System.out.println("Car Make is" +minCar.getMake());
            System.out.println("Model is " +minCar.getModel());
            System.out.println("Mileage is "+ minCar.getMileage());
            System.out.println("Price is " + minCar.getPrice());
            System.out.println("Color is " +minCar.getColor());
        }

    }

    public static void lowestPriceByMakeModel()
    {
        String make;
        String model;
        System.out.println("\nChoice Six selected!");
        //similar to lowestpricecar i guess
        System.out.println("What make are you looking at?: ");
        make = myScan.next();
        System.out.println("What model are you looking at?: ");
        model = myScan.next();
        int lowestKey = 0;
        boolean makeFound = false;
        boolean modelFound = false;
        double lowest = 10000000;
        if(bstPrice.isEmpty() == true)
        {
            System.out.println("The list is empty!");
            return;
        }
        for(int i =0; i < bstPrice.size();i++)
        {
            String makeCheck = bstPrice.select(i).getMake();
            if(makeCheck.equals(make))
            {
                makeFound = true;
                String modelCheck = bstPrice.select(i).getModel();
                //System.out.println("Make im looking for "+make+" " + " that is found" +makeCheck);
                if(modelCheck.equals(model))
                {
                    modelFound = true;
                    if((bstPrice.select(i).getPrice()) <lowest)
                    {
                        lowest = bstPrice.select(i).getPrice();
                        lowestKey = i;
                    }
                }
            }
        }
        if(makeFound ==  false)
        {
            System.out.println("Make not Found!");
            return;
        }
        if(modelFound ==  false)
        {
            System.out.println("Model not Found!");
            return;
        }

        System.out.println("The lowest price car of make " + make +" and model "+ model); 
        System.out.println("Vin is "+bstPrice.select(lowestKey).getVin());
        System.out.println("Car Make is "+bstPrice.select(lowestKey).getMake());
        System.out.println("Model is "+bstPrice.select(lowestKey).getModel());
        System.out.println("Mileage is "+bstPrice.select(lowestKey).getMileage());
        System.out.println("Price is "+bstPrice.select(lowestKey).getPrice());
        System.out.println("Color is "+bstPrice.select(lowestKey).getColor());

    }

    public static void lowestMileageByMakeModel()
    {
        String make;
        String model;
        System.out.println("\nChoice Seven selected!");
        //similar to lowestpricecar i guess
        System.out.println("What make are you looking at?: ");
        make = myScan.next();
        System.out.println("What model are you looking at?: ");
        model = myScan.next();
        int lowestKey = 0;
        boolean makeFound = false;
        boolean modelFound = false;
        double lowest = 10000000;
        if(bstMileage.isEmpty() == true)
        {
            System.out.println("The list is empty!");
            return;
        }
        for(int i =0; i < bstMileage.size();i++)
        {
            String makeCheck = bstMileage.select(i).getMake();
            if(makeCheck.equals(make))
            {
                makeFound = true;
                String modelCheck = bstMileage.select(i).getModel();
                //System.out.println("Make im looking for "+make+" " + " that is found" +makeCheck);
                if(modelCheck.equals(model))
                {
                    modelFound = true;
                    if((bstMileage.select(i).getMileage()) <lowest)
                    {
                        lowest = bstMileage.select(i).getMileage();
                        lowestKey = i;
                    }
                }
            }
        }
        if(makeFound ==  false)
        {
            System.out.println("Make not Found!");
            return;
        }
        if(modelFound ==  false)
        {
            System.out.println("Model not Found!");
            return;
        }

        System.out.println("The lowest mileage car of make " + make +" and model "+ model); 
        System.out.println("Vin is "+bstMileage.select(lowestKey).getVin());
        System.out.println("Car Make is "+bstMileage.select(lowestKey).getMake());
        System.out.println("Model is "+bstMileage.select(lowestKey).getModel());
        System.out.println("Mileage is "+bstMileage.select(lowestKey).getMileage());
        System.out.println("Price is "+bstMileage.select(lowestKey).getPrice());
        System.out.println("Color is "+bstMileage.select(lowestKey).getColor());

    }

}