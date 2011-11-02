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
        BellCurve ACTBell = new BellCurve(.03,.13,true);
        BellCurve FERTBell = new BellCurve(.2,.8,true);
        BellCurve LIFEBell = new BellCurve(.125,.5,true);
        BellCurve GYMBell = new BellCurve(.5,1.2,true);
        
        double[] act = ACTBell.getPositiveRandomArray(10000);
        double[] fert = FERTBell.getPositiveRandomArray(10000);
        double[] life = LIFEBell.getPositiveRandomArray(10000);
        double[] gym = GYMBell.getPositiveRandomArray(10000);
        double[] glac = new double[10000];
        Arrays.fill(glac, 1.0);
        
        //DataWriter output = new DataWriter("out_gcsv10.dat", cols, "'0'.000000E00");
        
        int ndel=28;
        double deltaT=.28;
        double GYM=0, LIFE=0, ACT=0, FERT=0, GLAC=0;
        
        //RunList tests = new RunList();
        ArrayList<CO2Run> tests = new ArrayList<CO2Run>(10000);
        for (int iiit=0; iiit<ndel; iiit++)
        {
            deltaT = deltaT+0.12*(1.0+4.0*((float) (iiit))/((float) (ndel-1)));
            
            System.out.println("ACT\tFERT\tLIFE\tGYM\tGLAC");
            for (int c=0; c<10000; c++)
            {
                ACT=Math.abs(act[c]);
                FERT=Math.abs(fert[c]);
                LIFE=Math.abs(life[c]);
                GYM=Math.abs(gym[c]);
                GLAC=Math.abs(glac[c]);
                if (ACT<0 || FERT<0 || LIFE<0 || GYM<0 ||GLAC<0)
                    System.out.println(ACT+"\t"+FERT+"\t"+LIFE+"\t"+GYM+"\t"+GLAC);
                FactorValuedRun thisRun = new FactorValuedRun(deltaT, ACT, FERT, LIFE, GYM, GLAC);
                thisRun.calculateCO2();
                
                //System.out.println(c);
                tests.add(thisRun);
            }
            // sift through the runs to find acceptable data fits  (chiquare <= NDAT; bias < 0.3)
            // write to disk
            System.out.println("done"+iiit);
        } 
        System.out.println("Done calculating.  Begin write.");
        MeanRun m = new MeanRun();
        for (int i=0; i<tests.size(); i++)
        {
            m.addRun(tests.get(i));
        }
        try
        {
            java.io.PrintWriter output = new java.io.PrintWriter(new java.io.FileWriter("output.dat"));
            output.println("age/tmean/thigh/tlow");
            //MeanRun m = tests.getMean();
            int highIndex=(int) (tests.size()*.975);
            int lowIndex=(int)(tests.size()*.025);
            CO2Run highRun, lowRun;
            for(int i = 0; i<m.size(); i++)
            {
                highRun = tests.get(highIndex);
                lowRun = tests.get(lowIndex);
                output.println(m.getCO2(i)+"/t"+highRun.getCO2(i)+"/t"+lowRun.getCO2(i));
                System.out.println(i*5+"/t"+m.getCO2(i)+"/t"+highRun.getCO2(i)+"/t"+lowRun.getCO2(i));
            }
            output.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
