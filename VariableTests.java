import arrayinput.*;
import co2histograph.*;
import utilities.*;

import java.util.ArrayList;

public class VariableTests
{
    protected static double[] percentiles = {.025, .05, .10, .15, .20, .25, .30, .35, .40, .45, .50, .55, .60, .65, .70, .75, .80, .85 ,.90, .95, .975};
    protected static double[][] vs = SVGens_ewall.allVaried(10000);
    protected static double[][] ds = SVGens_ewall.allDefault(10000);
    
    public static void standardTests(String fileinc)
    {
        SoloTests.doAll(fileinc);
        CompositeTests.doAll(fileinc);
    }
    public static void doThreeDLC(String fileinc)
    {
        ScrapData s = new ScrapData();
        s.setDLCLimit(2.0);
        SoloTests.doDLC("2.0"+fileinc, s);
        CompositeTests.doAll("-DLC-2.0"+fileinc,s);
        
        s = new ScrapData();
        s.setDLCLimit(1.5);
        SoloTests.doDLC("1.5"+fileinc, s);
        CompositeTests.doAll("-DLC-1.5"+fileinc, s);
        
        s = new ScrapData();
        s.setDLCLimit(1.0);
        SoloTests.doDLC("1.0"+fileinc, s);
        CompositeTests.doAll("-DLC-1.0"+fileinc, s);
        
        System.out.println("Three DLC done");
        System.out.println();
    }
    
    public static void writeRunsAndErrors(String label, double[] percentiles, RunList r){
        ArrayList<HistData> writing = new ArrayList<HistData>();
        writing.add(r.getMean());
        CommonData[] percentileData = r.manyRunAtPercentile(percentiles);
        for (int i=0; i<percentileData.length; i++)
        {
            writing.add(percentileData[i]);
        }
        
        System.out.println("Writing "+label+" runs.");
        Misc.writeRuns(label+"_CO2.dat", writing);
        System.out.println("Finding "+label+" errors.");
        ArrayList<HistData> errors = r.getErroredRuns(10000);
        System.out.println("Writing "+label+" errors.");
        Misc.writeFactorRuns(label+"_ERROR.dat", errors);
    }
    
    public static void writeDLCRunsAndErrors(String label, double[] percentiles, RunList r){
        ArrayList<HistData> writing = new ArrayList<HistData>();
        writing.add(r.getMean());
        CommonData[] percentileData = r.manyRunAtPercentile(percentiles);
        for (int i=0; i<percentileData.length; i++)
        {
            writing.add(percentileData[i]);
        }
        
        System.out.println("Writing "+label+" runs.");
        Misc.writeRuns(label+"_CO2.dat", writing);
        System.out.println("Finding "+label+" errors.");
        ArrayList<HistData> errors = r.getErroredRuns(10000);
        System.out.println("Writing "+label+" errors.");
        Misc.writeFactorRuns(label+"_ERROR.dat", errors);
    }
    
    public static RunList performTests(double[] deltaT, double[] ACT, double[] FERT, double[] LIFE, double[] GYM, double[] GLAC, GCSVData arrIn)
    {
        int len = utilities.Arrays.minLength(deltaT, ACT, FERT, LIFE, GYM, GLAC);
        RunList tempList = new RunList();
        for (int c=0; c<len; c++)
        {
            if (c%1000==0)
                System.out.print(c+" ");
            tempList.add(new FactorData(deltaT[c], ACT[c], FERT[c], LIFE[c], GYM[c], GLAC[c], arrIn));
        }
        System.out.println();
        return tempList;
    }
}
