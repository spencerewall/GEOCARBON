import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public abstract class CO2Run
{
    protected ArrayList<Double> CO2;
    
    /**
     * Default constructor for the CO2Run class.  This creates a new object that contains no CO2 values.
     */
    public CO2Run() 
    {
        CO2 = new ArrayList<Double>();
    }
    /**
     * Overloaded constructor for the CO2Run class.  This constructor takes all CO2 values in the given list
     * and creates a new instance of CO2Run with these initial CO2 values.
     * 
     * @param CO2Vals the values to be included in this instance of CO2Run
     */
    public CO2Run(ArrayList<Double> CO2Vals)
    {
        CO2 = CO2Vals;
    }
    /**
     * Gets the CO2 concentration (ppm) held at the given index.
     * 
     * @param i the index for which to obtain the value of CO2
     * @return the CO2 ppm at the specified index
     */
    public double getCO2(int i)
    {
        return CO2.get(i);
    }
    /**
     * Appends the specified CO2 value to the end of the run.  The CO2 value is taken as a double.
     * 
     * @param d CO2 value to be appended to the run
     */
    public void addCO2(double d)
    {
        CO2.add(d);
    }
    /**
     * Returns the size of this run.  The size of the run is defined by the number of CO2 values that it contains.
     * @return the number of CO2 values in this run
     */
    public int size()
    {
        return CO2.size();
    }
    /**
     * returns the array containing all CO2 values generated since the last call of
     * calculateCO2s()
     * 
     * @return the array containing CO2 values in ppm
     */
    public ArrayList<Double> getAllCO2()
    {
        return CO2;
    }
    /**
     * Compares the specified object with this CO2Run for equality.  Returns true if and only if the specified object
     * is also a CO2Run, has the same number of CO2 values, and if the lists of the CO2 values from both runs are equal.
     * List equality is defined by the equals method of java.util.ArrayList.
     * 
     * @param other the Object to be compared for equality with this CO2Run
     * @return true if the specified object is equal to this CO2Run
     */
    public boolean equals(Object other)
    {
        if (other instanceof CO2Run)
        {
            if (getAllCO2().equals(((CO2Run)other).getAllCO2()))
                return true;
        }
        return false;
    }
    /**
     * Returns a hash code for this <code>CO2Run</code> object.
     * 
     * @returns a hash code value for this object.
     */
    public abstract int hashCode();
}