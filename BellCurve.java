import java.util.Random;

/**
 * This class imitates a bell curve
 */
public class BellCurve
{
    private double mean; //mu
    private double stDev; // theta
    private Random r;
    
    public BellCurve(double average,  double standardDeviation)
    {
        mean = average;
        stDev = standardDeviation;
        r = new Random();
    }
    public BellCurve(double low, double high, boolean range)
    {
        mean = (low+high)/2;
        stDev = (high-low)/2;
        r = new Random();
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
