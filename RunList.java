import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class RunList
{
    
    ArrayList <HistData> allRuns;
    MeanRun mean;
    
    public RunList()
    {
        mean = new MeanRun();
        allRuns = new ArrayList<HistData>();
    }
    public RunList(int initCapacity)
    {
        mean = new MeanRun();
        allRuns = new ArrayList<HistData>(initCapacity);
    }
    /**
     * Adds the given run to the this RunList.
     * 
     * @param run the CO2Run to be added
     * @return {<code>true} if this list changed as a result of the call
     */
    public boolean add(HistData run)
    {
        boolean rAdd = allRuns.add(run);
        if (rAdd==false)
            return rAdd;
        
        boolean mAdd = mean.include(run);
        if (mAdd==false)
        {
            allRuns.remove(run);
            return mAdd;
        }
        return true;
    }
    
    public int size()
    {
        return allRuns.size();
    }
    /*
    public RunList selectPercentRange(double minPercentile, double maxPercentile)
    {
        HashSet<CO2Run> goodR = new HashSet<CO2Run>();
        for (ArrayList<PPMRunPair> time : timebuckets)
        {
            Collections.sort(time);
            int upperCell = (int) (time.size()*maxPercentile/100.0);
            int lowerCell = (int) (time.size()*minPercentile/100.0);
            ArrayList<PPMRunPair> limitT = (ArrayList)time.subList(lowerCell, upperCell);
            for (PPMRunPair r : limitT)
                goodR.add(r.getRun());
        }
        RunList finalLst = new RunList();
        for (CO2Run r: goodR)
        {
            finalLst.add(r);
        }
        return finalLst;
    }*/
    public HistData get(int i)
    {
        HistData r = allRuns.get(i);
        return r;
    }
    public MeanRun getMean()
    {
        return mean;
    }
    
    
}
