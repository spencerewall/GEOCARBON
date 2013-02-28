package factorinput; 

import java.util.Arrays;

/**
 * The GaussianFactor class simulates a factor whose most probable values are distributed along 
 * a normal distribution.  The bound toggle is set to on by default.
 */
public class GaussianFactor implements VariedFactor
{
    /* Instance Variables */
    private double mean;
    private double stDev;
    private java.util.Random r;
    private ValueLimit lim;
    private final String type="Gaussian";
    private String var;
    
    /*
     * Constructors and Instantiation Method.
     */
    /**
     * Constructs a new instance of GaussianFactor centered around the given average with a given standardDeviation.
     * The maximum and minimum of this 
     */
    public GaussianFactor()
    {
        this.var="";
        instantiate(0,1,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY);
    }
    public GaussianFactor(double mean,  double stDev, String var)
    {
        this.var=var;
        instantiate(mean,stDev,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY);
    }
    public GaussianFactor(double mean,  double stDev, double min, double max, String var)
    {
        this.var=var;
        instantiate(mean,stDev,min,max);
    }
    /**
     * Constructs a new instance of GaussianFacto defined by two values on the normal distribution which are located
     * n standard deviations above/below the mean.  This allows for designation of initial maximum
     * and minimum boundaries for the force and try methods.
     * 
     * @param min the value located nDevs below the mean
     * @param max the value located nDevs below the mean
     * @param n the number of standard deviations that high and low are from the mean
     */
    public GaussianFactor(double min, double max, double n, String var)
    {
        this.var=var;
        instantiate((min+max)/2,((max-min)/2)/n, min, max);
    }
    private void instantiate(double mean, double stDev, double min, double max)
    {
        this.r = new java.util.Random();
        this.mean=mean;
        this.stDev=stDev;
        lim=new ValueLimit(min, max, true);
    }
    public String getType() {
        return type;
    }
    public String getVariableName() {
        return var;
    }
   
    
    
    
    
    
    
    /*
     * Following methods defined by parent interface (VariedFactor)
     */
    public double getMin() {
        return lim.getMin();
    }
    public double getMax() {
        return lim.getMax();
    }
    public void setMin(double min) {
        lim.setMin(min);
    }
    public void setMax(double max) {
        lim.setMax(max);
    }
    /** Returns a pseudorandom <code>double</code> from this Gaussian distribution. */
    public double getNext()
    {
        double g=transformNormal(r.nextGaussian());
        while(!lim.passesBoundTest(g))
            g=transformNormal(r.nextGaussian());
        return g;
    }
    /**
     * Returns an array containing n <code>doubles</code> pseudorandomly distributed doubles along the 
     * gaussian distribution defined by this GaussianFactor object.
     * @param n the number of values to generate for the array
     * @return an array of pseudorandom doubles from this gaussian distribution
     */
    public double[] getNexts(int n)
    {
        double[] g = new double[n];
        for (int i=0; i<n; i++)
        {
            g[i]=this.getNext();
        }
        return g;
    }
    public void toggleBounds(boolean useBounds)
    {
        lim.toggleBounds(useBounds);
    }
    /**
     * Sets the mean value of this gaussian factor
     */
    public void setCenter(double center)
    {
        this.mean=center;
    }
    /**
     * Returns the mean value of this gaussian factor
     */
    public double getCenter()
    {
        return mean;
    }
    /**
     * Sets the standard deviation of this gaussian factor
     */
    public void setScale(double scale)
    {
        this.stDev=scale;
    }
    /**
     * Returns the standard deviation of this gaussian factor
     */
    public double getScale()
    {
        return stDev;
    }
    public void setValueLimit(ValueLimit lim)
    {
        this.lim = lim;
    }
    public ValueLimit getValueLimit()
    {
        return lim;
    }
    
    /* Gaussian Factor Specific Methods */
    /**
     * Takes a number from a normal distribution (-1 to 1) and transforms it to a value on
     * the distribution defined by this GaussianFactor object
     * 
     * @param n the normal value to be transformed
     * @param the transformation of n onto the gaussian distribution defined by this Object
     */
    private double transformNormal(double n)
    {
        n = (n*stDev)+mean;
        return n;
    }
}
