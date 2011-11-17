import dataHistographs.*;

public class DataComparator implements java.util.Comparator<HistData>
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
