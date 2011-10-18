import java.util.Random;

/**
 * This class imitates a bell curve
 */
public class BellCurve
{
    private double mean; //mu
    private double stDev; // theta
    private Random r;
    
    /**
     * Constructs a BellCurve Object centered around the given average with a given standardDeviation.
     */
    public BellCurve(double average,  double standardDeviation)
    {
        mean = average;
        stDev = standardDeviation;
        r = new Random();
    }
    /**
     * Constructs a BellCurve Object over a finite data set with a given high and low value.
     */
    public BellCurve(double low, double high, boolean range)
    {
        mean = (low+high)/2;
        stDev = (high-low)/2;
        r = new Random();
    }
    /**
     * Returns the mean value of this BellCurve.
     */
    public double getMean()
    {
        return mean;
    }
    /**
     * Returns the standard deviation of this BellCurve.
     */
    public double getStandardDeviation()
    {
        return stDev;
    }
    public double getRandomValue()
    {
        double gauss = r.nextGaussian();
        //return ((gauss*stDev)+mean)+"\t"+gauss;
        return ((gauss*stDev)+mean);
    }
    public double[] getRandomArray(int len)
    {
        double[] arr = new double[len];
        for (int i = 0; i< arr.length; i++)
        {
            arr[i]=getRandomValue();
        }
        return arr;
    }
}
