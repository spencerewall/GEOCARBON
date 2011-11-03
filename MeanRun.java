import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
/** 
 * 
 * @author (Spencer Ewall) 
 */
public class MeanRun extends CO2Run
{
    private ArrayList<CO2Run> runs;
    private double[] sum;
    //private double[] stDev;
    
    /**
     * Constructs a new list of CO2 values over a time interval.  CO2 values are backed 
     */
    public MeanRun()
    {
        super();
        runs = new ArrayList<CO2Run>();
        sum=new double[0];
        //stDev=new double[0];
    }
    public MeanRun(int numRuns)
    {
        super();
        runs = new ArrayList<CO2Run>(numRuns);
        sum=new double[0];
        //stDev=new double[0];
    }
    /**
     * Calculates an average run with CO2 values cooresponding to the average at each timestep
     * of all CO2 runs added up to this point.
     */
    /**
     * Adds a run to list of runs included in calculation of the average run.
     */
    public boolean addRun(CO2Run run)
    {
        boolean addbool = runs.add(run);
        if (addbool == false)
            return false;
        if (runs.size()==1)   //first time a run is being added
        {
            sum=new double[run.size()];
            //stDev=new double[run.size()];
            Arrays.fill(sum, 0);
            //Arrays.fill(stDev, 0);
        }
        for (int i=0; i<run.size(); i++)
        {
            sum[i]=sum[i]+run.getCO2(i);
        }
        //recalculateMean();
        //recalculateStDev();
        return true;
    }
    public void recalculateMean()
    {
        CO2.clear();
        for (int i=0; i<sum.length; i++)
        {
            CO2.add(sum[i]/runs.size());
        }
    }
    public double getCO2(int i)
    {
        recalculateMean();
        //recalculateStDev();
        return super.getCO2(i);
    }
    public ArrayList<Double> getAllCO2()
    {
        recalculateMean();
        //recalculateStDev();
        return super.getAllCO2();
    }
    
    /*
    public void recalculateStDev()
    {
        //if (CO2==null){ } throw error
        //if (CO2==null){ } throw error
        doCO2Calc();
        stDev = new ArrayList(this.size());
        stDev.clear();

        for (CO2Run r : runs)
        {
            for (int t=0; t<sumCO2.size(); t++)
            {
                stDev.add((r.getCO2(t)-this.getCO2(t))*(r.getCO2(t)-this.getCO2(t)));
            }
        }
        for (int t=0; t<stDev.size(); t++)
        {
            stDev.set(t,Math.sqrt(stDev.get(t)/runs.size()));
        }
    }
    */
    /*
    public void addAllRuns(Collection<CO2Run> initialRuns)
    public void removeRun(CO2Run run)
    public ArrayList<Double> getAllStandardDev()
    public Double getStandardDev(int i)
    */

   
   
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
}