import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class GEOCARB3
{
    //static DecimalFormat sixExp = new DecimalFormat("'0'.000000E00");
    //static DecimalFormat fiveExp = new DecimalFormat("'0'.00000E00");
    static DecimalFormat fiveFig = new DecimalFormat("0.####");
    
    /**
     * doStuff performs the given number of runs.  Factor values are determined intrinsicaly.
     * @param numR the number of runs to perform.
     */
    public static void main(String[] args)
    {
        String[] cols = {"Age(Ma)", "CO2 Predict..."};
        GaussianFactor ACTBell = new GaussianFactor(.03,.13,3);
        GaussianFactor FERTBell = new GaussianFactor(.2,.8,3);
        GaussianFactor LIFEBell = new GaussianFactor(.125,.5,3);
        GaussianFactor GYMBell = new GaussianFactor(.5,1.2,3);
        
        double[] act = ACTBell.forceValueList(10000);
        double[] fert = FERTBell.forceValueList(10000);
        double[] life = LIFEBell.forceValueList(10000);
        double[] gym = GYMBell.forceValueList(10000);
        double[] glac = new double[10000];
        Arrays.fill(glac, 1.0);
        
        int ndel=28;
        double deltaT=.28;
        
        RunList tests = new RunList();
        
        for (int iiit=0; iiit<ndel; iiit++)
        {
            deltaT = deltaT+0.12*(1.0+4.0*((float) (iiit))/((float) (ndel-1)));
            System.out.println("ACT\tFERT\tLIFE\tGYM\tGLAC\tWhoah");
            for (int c=0; c<10000; c++)
            {
                FactorValuedRun thisRun = new FactorValuedRun(deltaT, act[c], fert[c], life[c], gym[c], glac[c]);
                tests.add(thisRun);
            }
            // sift through the runs to find acceptable data fits  (chiquare <= NDAT; bias < 0.3)
            // write to disk
            System.out.println("done"+iiit+"\tTime = "+System.currentTimeMillis());
        } 
        System.out.println("Done calculating.  Begin write.");
        
        
        UtilityMethods.writeRuns("output.dat",tests);
        
        RunList m = new RunList();
        m.add(m.getMean());
        UtilityMethods.writeRuns("meanOut.dat",tests);
    }
}
