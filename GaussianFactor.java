import java.util.Random;
import java.util.Arrays;

/**
 * This class imitates a bell curve.
 * 
 * Possible alternative names:
 * GaussianFactor
 * 
 */
public class GaussianFactor implements FactorGenerator
{
	private double max;
    private double min;
    private double mean;
    private double stDev;
    private Random r;
    /**
     * Constructs a GaussianFactor Object centered around the given average with a given standardDeviation.
     */
    public GaussianFactor(double average,  double standardDeviation)
    {
        mean = average;
        stDev = standardDeviation;
        max=Double.POSITIVE_INFINITY;
        max=Double.NEGATIVE_INFINITY;
        r = new Random();
    }
    /**
     * Constructs a GaussianFactor Object 
     * 
     * 
     * @param high2StDev the value located 2 standard deviations above the mean
     * @param low2StDev the value located 2 standard deviations below the mean
     * @param devNum the number of standard deviations that high and low are from the mean
     */
    public GaussianFactor(double lowVal, double highVal, double devNum)
    {
    	mean = (lowVal+highVal)/2;
        stDev = ((highVal-lowVal)/2)/devNum; //(newRange/initRange) /devNum
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
