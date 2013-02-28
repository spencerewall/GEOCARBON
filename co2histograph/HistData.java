package co2histograph;
/**
 * __________ is 
 */
public interface HistData
{
    public double getCO2(int i);
    public double[] getCO2();
    public int size();
    public boolean equals(Object other);
    public int hashCode();
}