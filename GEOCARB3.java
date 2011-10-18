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
    public static void doStuff()
    {
        String[] cols = {"Age(Ma)", "CO2 Predict..."};
        BellCurve ACTBell = new BellCurve(.08, .0336405595);
        BellCurve FERTBell = new BellCurve((8.0/15.0), .221);
        BellCurve LIFEBell = new BellCurve((29.0/90.0), .147);
        BellCurve GYMBell = new BellCurve((8.0/9.0), .258);
        
        double[] act = ACTBell.getRandomArray(10000);
        double[] fert = FERTBell.getRandomArray(10000);
        double[] life = LIFEBell.getRandomArray(10000);
        double[] gym = GYMBell.getRandomArray(10000);
        double[] glac = new double[10000];
        Arrays.fill(glac, 1.0);
        
        DataWriter output = new DataWriter("out_gcsv10.dat", cols, "'0'.000000E00");
        
        int ndel=28;
        double deltaT=.28;
        double GYM=0, LIFE=0, ACT=0, FERT=0, GLAC=0;
        
        RunList r = new RunList();
        
        
        for (int iiit=0; iiit<ndel; iiit++)
        {
            deltaT = deltaT+0.12*(1.0+4.0*((float) (iiit))/((float) (ndel-1)));
            
            System.out.println("GYM\tLIFE\tACT\tFERT\tdeltaT");
            RunList tests = new RunList();
            for (int c=0; c<10000; c++)
            {
                 
                System.out.print(c+"\t");
                if (c%10==9)
                    System.out.println();
                ACT=act[c];
                FERT=fert[c];
                LIFE=life[c];
                GYM=gym[c];
                GLAC=glac[c];
                if (ACT<0 || FERT<0 || LIFE<0 || GYM<0 ||GLAC<0)
                    System.out.println("fail?");
                FactorValuedRun thisRun = new FactorValuedRun(deltaT, ACT, FERT, LIFE, GYM, GLAC);
                thisRun.calculateCO2();
                System.out.println(thisRun);
                //tests.add(thisRun);
            }// end do
            // sift through the runs to find acceptable data fits  (chiquare <= NDAT; bias < 0.3)
            // write to disk
        } 
        /**
         * 
        for (int i=0; i<r.size(); i++)
        {
            System.out.println(r.getRun(i));
        }
        */
    }
}
