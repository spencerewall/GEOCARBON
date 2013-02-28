import factorinput.*;
/**
 * Write a description of class EwallSVgens here.
 * 
 * @author (sjewall)
 */
public class SVGens_ewall
{
    public static GaussianFactor actDist = new GaussianFactor(0.03,0.13,2);
    public static GaussianFactor fertDist = new GaussianFactor(0.2,0.8,2);
    public static GaussianFactor lifeDist = new GaussianFactor(0.125,0.5,2);
    public static GaussianFactor gymDist = new GaussianFactor(0.5,1.2,2);
    public static GaussianFactor glacDist = new GaussianFactor(1.0,3.0,2);
    public static LognormalFactor dtDist = (new LognormalFactor(Math.log(3), Math.log(1.5), 1.5, Double.POSITIVE_INFINITY));
    
    public static GaussianFactor nvDist = new GaussianFactor(0.0,.015,2);
    public static GaussianFactor vnvDist = new GaussianFactor(4.0,1.0,2.0, 10.0);
    public static GaussianFactor fb0Dist = new GaussianFactor(3.0,1.0,.75, Double.POSITIVE_INFINITY);
    
    private static VariedFactor[] dist={dtDist, actDist, fertDist, lifeDist, gymDist, glacDist};
    
    public SVGens_ewall()
    {
        VariedFactor[] d = {dtDist, actDist, fertDist, lifeDist, gymDist, glacDist, nvDist, vnvDist, fb0Dist};
        this.dist=d;
    }
    public static double[][] allDefault(int i)
    {
        double[] dt = new double[i]; java.util.Arrays.fill(dt, dist[0].getCenter());
        double[] act =  new double[i]; java.util.Arrays.fill(act, dist[1].getCenter());
        double[] fert = new double[i]; java.util.Arrays.fill(fert, dist[2].getCenter());
        double[] life = new double[i]; java.util.Arrays.fill(life, dist[3].getCenter());
        double[] gym = new double[i]; java.util.Arrays.fill(gym, dist[4].getCenter());
        double[] glac = new double[i]; java.util.Arrays.fill(glac, dist[5].getCenter());
        
        double[] nv = new double[i]; java.util.Arrays.fill(nv, dist[6].getCenter());
        double[] vnv = new double[i]; java.util.Arrays.fill(vnv, dist[7].getCenter());
        double[] fb0 = new double[i]; java.util.Arrays.fill(fb0, dist[8].getCenter());
        
        double[][] d = {dt, act, fert, life, gym, glac, nv, vnv, fb0};
        return d;
    }
    public static double[][] allVaried(int i)
    {
        double[] delT = dist[0].getNexts(i);
        double[][] d = new double[dist.length][i];
        
        for (int j= 0;j<dist.length;j++)
        {
            d[j]=dist[j].getNexts(i);
        }
        return d;
    }
}
