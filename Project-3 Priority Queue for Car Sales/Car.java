
/**
 * Write a description of class apartments here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Car implements Comparable<Car>
{
    // instance variables - replace the example below with your own
    // unique vin no i, o, or q chars, 0 and 1 numerals allowed
    private String vin;
    private String make;
    private String model;
    private int mileage;
    private double price;
    private String color;
    private int choice;
    /**
     * Constructor for objects of class apartments
     */
    public Car(String myVin, String myMake, String myModel, int myMileage,double myPrice,String myColor, int myChoice) // carType 0 is price, // carType 1 is mileage 
    {
        vin = myVin;
        make = myMake;
        model = myModel;
        mileage = myMileage;
        price = myPrice;
        color = myColor;
        choice = myChoice;

    }

    public String getVin()
    {
        return vin;
    }

    public void updateVin(String newVin)
    {
        vin = newVin;
    }

    public String getMake()
    {
        return make;
    }

    public void updateMake(String newMake)
    {
        make = newMake;
    }

    public String getModel()
    {
        return model;
    }

    public void updateModel(String newModel)
    {
        model = newModel;
    }

    public int getMileage()
    {
        return mileage;
    }

    public void updateMileage(int newMileage)
    {
        mileage = newMileage;
    }
    //this is the one I actually need via rubic wise
    //can delete the rest once testing is done
    public double getPrice()
    {
        return price;
    }

    public void updatePrice(double newPrice)
    {
        price = newPrice;
    }

    public String getColor()
    {
        return color;
    }

    public void updateColor(String newColor)
    {
        color = newColor;
    }

    public void toStrings()
    {
        System.out.println(vin+" "+ make+" "+ model+" "+ mileage+" "+ price+" "+ color);
    }
    //

    public int compareTo(Car aCar) 
    {
        if(choice==0){
            if(price == aCar.getPrice())
            {
                return 0; 
            }
            else if(price > aCar.getPrice())
            {
                return 1;
            }
            else if(price < aCar.getPrice())
            {
                return -1;
            }
            else
            {
                System.out.println("Error 'price' comparable crash");
                return 0;
            }
        }
        else{
            if(mileage == aCar.getMileage())
            {
                return 0; 
            }
            else if(mileage > aCar.getMileage())
            {
                return 1;
            }
            else if(mileage < aCar.getMileage())
            {
                return -1;
            }
            else
            {
                System.out.println("Error 'mileage' comparable crash");
                return 0;
            }
        }

    }
}
