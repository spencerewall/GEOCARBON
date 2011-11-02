import java.util.Random;

/**
 * This class imitates a bell curve.
 * 
 * Possible alternative names:
 * GaussianFactor
 * 
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
    public BellCurve(double low, double high, boolean wat)
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
    /**
     * Returns a positively limited pseudorandom <code>double</code> from this Gaussian distribution.  
     * This method will only generate values between 0 and 2xMean (inclusive).  The upper end is limited
     * by 2xMean so that values will still be evenly distributed.
     */
    public double getPositiveRandom()
    {
        double val=-1;
        while ((val<0) || (val>getMean()*2))
        {
            val = getRandom();
        }
        return val;
    }
    /**
     * Returns a pseudorandom <code>double</code> from this Gaussian distribution.
     */
    public double getRandom()
    {
        double gauss = r.nextGaussian();
        return ((gauss*stDev)+mean);
    }
    /**
     * Returns an array of pseudorandom <code>doubles</code> as defined by getRandom()
     * 
     * @param len 
     * @return an array of pseudorandom doubles from this gaussian distribution
     */
    public double[] getRandomArray(int len)
    {
        double[] arr = new double[len];
        for (int i = 0; i< arr.length; i++)
        {
            arr[i]=getRandom();
        }
        return arr;
    }
    public double[] getPositiveRandomArray(int len)
    {
        double[] arr = new double[len];
        for (int i = 0; i< arr.length; i++)
        {
            arr[i]=getPositiveRandom();
        }
        return arr;
    }
}
