import java.util.LinkedList;
//import java.util.Collection;
import java.util.Arrays;

public class MeanRun implements CO2Run
{
    private LinkedList<CO2Run> includeLst;
    private double[] meanData;
    private double[] sum;
    private boolean change;
    
    public MeanRun()
    {
        includeLst = new LinkedList<CO2Run>();
        meanData = new double[0];
        sum = new double[0];
        change = false;
    }
    /**
     * Fail if
     *  (a) run is null
     */
    public boolean include(CO2Run r)
    {
        if (!includeLst.isEmpty()) //tests if size is compatible
            if (this.size() != r.size())
                return false;
        
        boolean addbool = includeLst.add(r);
        if (addbool == false) // tests if the item can be added
            return false;
        
        if (includeLst.size()==1)   //first time a run is being added
        {
            sum=new double[r.size()];
            Arrays.fill(sum, 0);
        }
        for (int i=0; i<r.size(); i++)
        {
            sum[i]=sum[i]+r.getCO2(i);
        }
        change = true;
        return true;
    }
    public int size()
    {
        return sum.length;
    }
    public void calculateMean()
    {
        for (int i=0; i<sum.length; i++)
        {
            meanData[i]=sum[i]/includeLst.size();
        }
        change=false;
    }
    public double getCO2(int i)
    {
        if (change)
            calculateMean();
        return meanData[i];
    }
    public double[] getAllCO2()
    {
        if (change)
            calculateMean();
        return meanData;
    }
    /*
    public void addAllRuns(Collection<CO2Run> initialRuns)
    public void removeRun(CO2Run run)
    public ArrayList<Double> getAllStandardDev()
    public Double getStandardDev(int i)
    */

   
    /**
     * Compares this object against the specified object for equality. The result is <code>true</code> if and only
     * if (a) the argument is not null and (b) all members of its list containing CO2 values are equal to the
     * cooresponding members of this object's list containing CO2 values.  This definition also extends to require
     * that the CO2 value lists for the two MeanRun objects are the same size.  Specifically, it will return the
     * value given by:
     * <p><p>*<code>this.getAllCO2().equals(other.getAllCO2())</code>
     * 
     * @param other the object to compare with
     * @return <code>true</code> if the objects are the same; <code>false</code> otherwise
     */
    public boolean equals(Object other)
    {
        if (this==other)
            return true;
        if (!(other instanceof MeanRun))
            return false;
        MeanRun o = (MeanRun) other;
        return (this.getAllCO2().equals(o.getAllCO2()));
    }
    /**
     * Returns a hash code for this <code>MeanRun</code> object.  The hashcode returned is the same as the
     * hashcode of the ArrayList<Double> object given by getAllCO2().
     * 
     * @returns a hash code value for this object
     */
    public int hashCode()
    {
        return getAllCO2().hashCode();
    }
}