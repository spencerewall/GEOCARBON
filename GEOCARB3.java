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
        GaussianFactor ACTBell = new GaussianFactor(.03,.13,true);
        GaussianFactor FERTBell = new GaussianFactor(.2,.8,true);
        GaussianFactor LIFEBell = new GaussianFactor(.125,.5,true);
        GaussianFactor GYMBell = new GaussianFactor(.5,1.2,true);
        
        double[] act = ACTBell.getPositiveValueList(10000);
        double[] fert = FERTBell.getPositiveValueList(10000);
        double[] life = LIFEBell.getPositiveValueList(10000);
        double[] gym = GYMBell.getPositiveValueList(10000);
        double[] glac = new double[10000];
        Arrays.fill(glac, 1.0);
        
        //DataWriter output = new DataWriter("out_gcsv10.dat", cols, "'0'.000000E00");
        
        int ndel=28;
        double deltaT=.28;
        
        CO2Run[] tests = new CO2Run[270000];
        MeanRun m = new MeanRun();
        for (int iiit=0; iiit<ndel; iiit++)
        {
            deltaT = deltaT+0.12*(1.0+4.0*((float) (iiit))/((float) (ndel-1)));
            System.out.println("ACT\tFERT\tLIFE\tGYM\tGLAC\tWhoah");
            for (int c=0; c<10000; c++)
            {
                FactorValuedRun thisRun = new FactorValuedRun(deltaT, act[c], fert[c], life[c], gym[c], glac[c]);
                tests[c+10000*iiit]=thisRun;
                //System.out.print("e");
                //System.out.println();
            }
            // sift through the runs to find acceptable data fits  (chiquare <= NDAT; bias < 0.3)
            // write to disk
            System.out.println("done"+iiit+"\tTime = "+System.currentTimeMillis());
        } 
        System.out.println("Done calculating.  Begin write.");
        
        for (int i=0; i<tests.length; i++)
        {
            m.include(tests[i]);
        }
        
        double[] arr = m.getAllCO2();
        for (double d: arr)
            System.out.print(d);
        /*
        try
        {
            java.io.PrintWriter output = new java.io.PrintWriter(new java.io.FileWriter("output.dat"));
            output.println("age\tmean\thigh\tlow");
            //MeanRun m = tests.getMean();
            int highIndex=(int) (tests.length*.975);
            int lowIndex=(int)(tests.length*.025);
            CO2Run highRun, lowRun;
            for(int i = 0; i<m.size(); i++)
            {
                highRun = tests[highIndex];
                lowRun = tests[lowIndex];
                output.println(m.getCO2(i)+"/t"+highRun.getCO2(i)+"/t"+lowRun.getCO2(i));
                System.out.println(i*5+"/t"+m.getCO2(i)+"/t"+highRun.getCO2(i)+"/t"+lowRun.getCO2(i));
            }
            output.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }*/
    }
}
