package factorInput;

/**
 * Write a description of interface FactorGenerator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface FactorGenerator
{
    /**
     * Returns the next value from this FactorGenerator object
     */
    double getNextValue();
    double[] getValueList(int len);
    //double getMax();
    //double getMin();
    //double getRange();
}
