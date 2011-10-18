import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
/**
 * Average run 
 * 
 * @author (Spencer Ewall) 
 */
public class MeanRun extends CalculatedRun
{
    private ArrayList <CO2Run> runs;
    private ArrayList<Double> sumCO2;
    private ArrayList<Double> stDev;
    
    /**
     * 
     */
    public MeanRun()
    {
        super();
        runs = new ArrayList<CO2Run>();
        stDev = new ArrayList<Double>();
        sumCO2 = new ArrayList<Double>();
    }
    /**
     * 
     */
    public MeanRun(Collection<CO2Run> initialRuns)
    {
        super();
        stDev = new ArrayList<Double>();
        sumCO2 = new ArrayList<Double>();
        runs = new ArrayList<CO2Run>();
        this.addAllRuns(initialRuns);
    }
    /**
     * Calculates an average run with CO2 values cooresponding to the average at each timestep
     * of all CO2 runs added up to this point.
     */
    protected void doCO2Calc()
    {
        //if sumCO2 = null then
        if (CO2.isEmpty())
        {
            CO2.clear();
            for(int i=0; i<sumCO2.size(); i++)
            {
                CO2.add(sumCO2.get(i)/runs.size());
            }
            return;
        }
        for(int i=0; i<sumCO2.size(); i++)
            CO2.set(i,sumCO2.get(i)/runs.size());
        
        
    }
    private void doStandardDevCalc()
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
    /**
     * Adds a run to be included in the calculation of CO2 for the average run.
     */
    public void addRun(CO2Run run)
    {
        runs.add(run);
        
        if (sumCO2.isEmpty())
        {
            for (int i=0; i<run.size(); i++)
            {
                sumCO2.add(0.0);
            }
        }
        System.out.print("a");
        //sumCO2.clear();
        System.out.print("b");
        System.out.print(run.size());
        for(int i=0; i<run.size(); i++)
        {
            sumCO2.add((run.getCO2(i)+ sumCO2.get(i) ));
        }
        System.out.print("c");
        /*forceNextCalculate();*/
    }
    public void addAllRuns(Collection<CO2Run> initialRuns)
    {
        for (CO2Run i: initialRuns)
            this.addRun(i);
    }
    public void removeRun(CO2Run run)
    {
        runs.add(run);
        for(int i=0; i<run.size(); i++)
            sumCO2.set(i,sumCO2.get(i)+run.getCO2(i));
        forceNextCalculate();
    }
    public ArrayList<Double> getAllStandardDev()
    {
        doStandardDevCalc();
        return stDev;
    }
    public Double getStandardDev(int i)
    {
        doStandardDevCalc();
        return stDev.get(i);
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