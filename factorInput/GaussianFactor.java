package factorInput; 

import java.util.Random;
import java.util.Arrays;

/**
 * This class imitates a bell curve.
 */
public class GaussianFactor implements FactorGenerator
{
    private static Random statRan=new Random();

    private double max;
    private double min;
    private double mean;
    private double stDev;
    private Random r;
    /**
     * Constructs a GaussianFactor Object centered around the given average with a given standardDeviation.
     */
    public GaussianFactor()
    {
        mean = 0;
        stDev=1;
        max=Double.POSITIVE_INFINITY;
        max=Double.NEGATIVE_INFINITY;
        r = new Random();
    }
    public GaussianFactor(double average,  double standardDeviation)
    {
        mean = average;
        stDev = standardDeviation;
        max=Double.POSITIVE_INFINITY;
        max=Double.NEGATIVE_INFINITY;
        r = new Random();
    }
    /**
     * Constructs a new GaussianFactor object defined by two values on the normal distribution which are located
     * n standard deviations above/below the mean.  This allows for designation of initial maximum
     * and minimum boundaries for the force and try methods.
     * 
     * @param lowVal the value located nDevs below the mean
     * @param highVal the value located nDevs below the mean
     * @param nDevs the number of standard deviations that high and low are from the mean
     */
    public GaussianFactor(double lowVal, double highVal, double nDevs)
    {
    	mean = (lowVal+highVal)/2;
        stDev = ((highVal-lowVal)/2)/nDevs; //(newRange/initRange) /devNum
        max = highVal;
        min = lowVal;
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
     * Returns a pseudorandom <code>double</code> from this Gaussian distribution.
     */
    public double getNextValue()
    {
        double g = r.nextGaussian();
        return transformNormal(g);
    }
    
    /**
     * Returns an array of pseudorandom <code>doubles</code> as defined by Random.nextGaussian()
     * 
     * @param len 
     * @return an array of pseudorandom doubles from this gaussian distribution
     */
    public double[] getValueList(int len)
    {
        double[] g = new double[len];
        for (int i = 0; i< len; i++)
        {
            g[i]=getNextValue();
        }
        return g;
    }
    public double[] tryValueList(int len)
    {
        double[] g = new double[len];
        int index = 0;
        for (int i = 0; i< len; i++)
        {
            double v=getNextValue();
            if (inRange(v))
            {
                g[index]=v;
                index++;
            }
        }
        return Arrays.copyOfRange(g, 0, index+1);
    }
    public double[] forceValueList(int len)
    {
        double[] g = new double[len];
        int i=0;
        while (i< len)
        {
            double v=getNextValue();
            if (inRange(v))
            {
                g[i]=v;
                i++;
            }
        }
        for (double ga:g)
        {
            if (!inRange(ga))
                System.out.println("BAD!");
        }
        return g;
    }
    /**
     * Takes a number from a normal distribution (-1 to 1) and transforms it to a value on
     * the distribution defined by this GaussianFactor object
     * 
     * @param n the normal value to be transformed
     * @param the transformation of n onto the gaussian distribution defined by this Object
     */
    private double transformNormal(double n)
    {
        double g = (n*stDev)+mean;
        return g;
    }
    public void setMean(double n)
    {
        mean=n;
    }
    public void setStandardDeviation(double n)
    {
        stDev=n;
    }
    public void setUpperBound(double n)
    {
        max = n;
    }
    public void setLowerBound(double n)
    {
        min = n;
    }
    public boolean inRange(double n)
    {
        return (n>=min)&&(n<=max);
    }
}
