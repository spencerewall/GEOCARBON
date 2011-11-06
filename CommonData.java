import java.util.ArrayList;

public class CommonData implements HistData
{
    ArrayList<HistData> data;
    
    public CommonData()
    {
        data = new ArrayList();
    }
    public CommonData(int len)
    {
        data = new ArrayList(len);
    }
    public void nextPointFrom(HistData d)
    {
        data.add(d);
    }
    public float getCO2(int i)
    {
        return data.get(i).getCO2(i);
    }
    public HistData getDataSource(int i)
    {
        return data.get(i);
    }
    public float[] getAllCO2()
    {
        float[] returnMe = new float[data.get(0).size()];
        int c = 0;
        while ( c<returnMe.length )
        {
            HistData thisRun = data.get(c);
            
        }
        return returnMe;
    }
    public int size()
    {
        return data.size();
    }
    public boolean equals(Object other)
    {
        if (other instanceof HistData)
        {
            HistData o = (HistData) other;
            if(size() == o.size())
            {
                
            }
        }
        return false;
    }
    public int hashCode()
    {
        return data.hashCode();
    }
    
    
    
    
    
    
    public class PPMRunPair implements Comparable{
        private double pp; private HistData rn;
        public PPMRunPair(double ppm, HistData r) {
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
        public HistData getRun() {
            return rn; 
        }
    }
}
