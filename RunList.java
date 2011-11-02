import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class RunList
{
    
    ArrayList <CO2Run> allRuns;
    ArrayList <ArrayList<PPMRunPair>> timebuckets;
    MeanRun mean = new MeanRun();
    
    public RunList()
    {
        mean = new MeanRun();
        allRuns = new ArrayList<CO2Run>();
        timebuckets = new ArrayList<ArrayList<PPMRunPair>>();
    }
    /**
     * Adds the given run to the this RunList.
     * 
     * @param run the CO2Run to be included
     * @return {<code>true} if this list changed as a result of the call
     */
    public boolean add(CO2Run run)
    {
        if (allRuns.isEmpty())
        {
            for (int i=0; i<run.size(); i++)
                timebuckets.add(new ArrayList<PPMRunPair>());
        }
        
        boolean rAdd = allRuns.add(run);
        if (rAdd==false)
            return rAdd;
        
        boolean mAdd = mean.addRun(run);
        if (mAdd==false)
        {
            allRuns.remove(run);
            return mAdd;
        }
        
        for(int i=0; i<run.size(); i++)
        {
            ArrayList<PPMRunPair> currentBucket = timebuckets.get(i);
            currentBucket.add(new PPMRunPair(run.getCO2(i),run));
        }
        return true;
    }
    
    public int size()
    {
        return allRuns.size();
    }
    
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
    }
    public CO2Run get(int i)
    {
        CO2Run r = allRuns.get(i);
        return r;
    }
    public MeanRun getMean()
    {
        return mean;
    }
    
    public class PPMRunPair implements Comparable{
        private double pp; private CO2Run rn;
        public PPMRunPair(double ppm, CO2Run r) {
            pp=ppm; rn=r;
        }
        public int compareTo(Object other) throws ClassCastException{
            if (this == other)
                return 0;
            if (!(other instanceof PPMRunPair))
                throw new ClassCastException("PPMRunPair Object was expected.");
            double otherPP = ((PPMRunPair)other).getPPM();
            return (int) Math.signum(pp-otherPP);
        }
        public boolean equals(Object other) {
            if (this==other)
                return true;
            if (!(other instanceof PPMRunPair))
                return false;
            PPMRunPair o = (PPMRunPair) other;
            return ((pp==o.getPPM()));
            
        }
        public double getPPM() {   
            return pp; 
        }
        public CO2Run getRun() {
            return rn; 
        }
    }
}
