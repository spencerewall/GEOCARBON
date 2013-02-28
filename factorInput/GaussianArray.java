package factorinput;

import java.util.Arrays;

public class GaussianArray
{
    private GaussianFactor g;
    private double[] center;
    private double[] scale;
    private double[] min;
    private double[] max;
    
    public GaussianArray(double[] center,  double[] scale)
    {
        this.center = center;
        this.scale = scale;
        this.min = new double[center.length];
        this.max = new double[center.length];
        Arrays.fill(min, Double.NEGATIVE_INFINITY);
        Arrays.fill(max, Double.POSITIVE_INFINITY);
    }
    public void setMax(double[] max){
        this.max=max;
    }
    public void setMin(double[] min){
        this.min=min;
    }
    public void setCenter(double[] center){
        this.center=center;
    }
    public void setScale(double[] scale){
        this.scale=scale;
    }
    
    public double[] getCenter(){
        return center;
    }
    public double[]getMax(){
        return max;
    }
    public double[]getMin(){
        return min;
    }
    public double[]getNext(){
        double[] t = new double[center.length];
        for (int i=0; i<center.length; i++){
            g.setCenter(center[i]);
            g.setScale(scale[i]);
            g.setMax(max[i]);
            g.setMin(min[i]);
            t[i] = g.getNext();
        }
        return t;
    }
    public double[]getScale(){
        return scale;
    }
}
