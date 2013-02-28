package factorinput; 

import java.util.Random;
import java.util.Arrays;
//import jsc.distributions.Lognormal;

/**
 * This class imitates an Lognormal Distribution
 */
public class LognormalFactor implements VariedFactor
{
    private jsc.distributions.Lognormal lnorm;
    private ValueLimit lim;
    private double center;
    private double scale;
    private final String type = "Lognormal";
    private String var;
    
    /* LognormalFactor Constructor */
    public LognormalFactor(double location, double scale, String var)
    {
        lim = new ValueLimit();
        lnorm=new jsc.distributions.Lognormal(location, scale);
        center = Math.exp(location);
        scale = Math.exp(scale);
        this.var=var;
    }
    public LognormalFactor(double location, double scale, double min, double max, String var)
    {
        lim = new ValueLimit(min, max, true);
        lnorm=new jsc.distributions.Lognormal(location, scale);
        center = Math.exp(location);
        scale = Math.exp(scale);
        this.var=var;
    }
    
    
    /* Implemented Methods from Parent Interface (VariedFactor) */
    public String getType() {
        return type;
    }
    public String getVariableName() {
        return var;
    }
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
    public void setValueLimit(ValueLimit lim)
    {
        this.lim = lim;
    }
    public ValueLimit getValueLimit()
    {
        return lim;
    }
    
    public double getNext()
    {
        double g=lnorm.random();
        while(!lim.passesBoundTest(g))
                g=lnorm.random();
        return g;
    }
    /**
     * Returns an array of pseudorandomly <code>doubles</code> as defined by Random.nextGaussian()
     * 
     * @param len the number of values to generate for the array
     * @return an array of pseudorandom doubles from this gaussian distribution
     */
    public double[] getNexts(int len)
    {
        double[] g = new double[len];
        for (int i=0; i<len; i++)
        {
            g[i]=this.getNext();
        }
        return g;
    }
    public void toggleBounds(boolean useBounds)
    {
        lim.toggleBounds(useBounds);
    }
    
    public double getCenter() {
        return center;
    }
    public double getScale() {
        return scale;
    }
    /**
     * Not Working
     */
    public void setCenter(double center){
    }
    /**
     * Not Working
     */
    public void setScale(double scale){
    }
}
