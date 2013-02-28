import java.util.ArrayList;
import java.util.Collections;
import co2histograph.*;

public class RunList
{
    String label;
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
            return false;
        }
        return true;
    }
    
    public int size()
    {
        return allRuns.size();
    }
    public CommonData runAtIndex(int i)
    {
        CommonData c = new CommonData();
        DataComparator comp = new DataComparator();
        int timeLen = allRuns.get(0).size();
        for(int j = 0; j<timeLen; j++)
        {
            comp.setIndex(j);
            Collections.sort(allRuns, comp);
            comp.incrementIndex();
            c.nextPointFrom(allRuns.get(i));
        }
        return c;
    }
    public RunList runsBetweenIndex(int low, int high)
    {
        RunList cRuns = new RunList(high-low);
        for(int i = low; i<high; i++)
        {
            cRuns.add(this.runAtIndex(i));
        }
        return cRuns;
    }
    public CommonData runAtPercentile(double p)
    {
        CommonData c = new CommonData();
        DataComparator comp = new DataComparator();
        int timeLen = allRuns.get(0).size();
        for(int i = 0; i<timeLen; i++)
        {
            comp.setIndex(i);
            Collections.sort(allRuns, comp);
            comp.incrementIndex();
            c.nextPointFrom(allRuns.get( (int)(allRuns.size()*p)) );
        }
        return c;
    }
    public CommonData[] manyRunAtPercentile(double[] p)
    {
        CommonData[] cRuns = new CommonData[p.length];
        for(int k=0; k<p.length; k++)
            cRuns[k]=new CommonData();
        DataComparator comp = new DataComparator();
        int timeLen = allRuns.get(0).size();
        for(int i = 0; i<timeLen; i++)
        {
            Collections.sort(allRuns, comp);
            comp.incrementIndex();
            for(int j=0; j<p.length; j++)
            {
                cRuns[j].nextPointFrom(allRuns.get( (int)(allRuns.size()*p[j])) );
            }
        }
        return cRuns;
    }
    public HistData get(int i)
    {
        HistData r = allRuns.get(i);
        return r;
    }
    public MeanRun getMean()
    {
        return mean;
    }
    public ArrayList<HistData> getErroredRuns(double threshold)
    {
        ArrayList<HistData> errors = new ArrayList<HistData>(allRuns.size());
        for(HistData h:allRuns)
        {
            h.setErrorThreshold((float)threshold);
            if(h.countErrors()>0)
                errors.add(h);
        }
        return errors;
    }
    public String getLabel(){
        return label;
    }
    public void setLabel(String label){
        label = label;
    }
    
    /**
     * Inner class implements a comparator for the HistData objects which are
     * held by this RunList
     */
    private class DataComparator implements java.util.Comparator<HistData>
    {
        int i;
        public DataComparator()
        {
            i=0;
        }
        public int compare(HistData o1, HistData o2)
        {
            return Float.compare(o1.getCO2(i),o2.getCO2(i));
        }
        public int getCurrentIndex()
        {
            return i;
        }
        public void incrementIndex()
        {
            i++;
        }
        public void setIndex(int newI)
        {
            i=newI;
        }
    }
}