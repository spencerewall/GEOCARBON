import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ArrayList;
import factorInput.GaussianFactor;
import dataHistographs.*;

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
        
        worsleyTest(act, fert, life, gym, glac);
    }
    public static RunList performTests(int numTests, double[] ACT, double[] FERT, double[] LIFE, double[] GYM, double[] GLAC, GCSVData arrIn)
    {
        int ndel=28;
        double deltaT=.28;
        RunList tempList = new RunList();
        for (int iiit=0; iiit<ndel; iiit++)
        {
            boolean b;
            deltaT = deltaT+0.12*(1.0+4.0*((float) (iiit))/((float) (ndel-1)));
            for (int c=0; c<10000; c++)
            {
                b=tempList.add(new FactorData(deltaT, ACT[c], FERT[c], LIFE[c], GYM[c], GLAC[c], arrIn));
                if (!b)
                    System.out.println("why won't you be added goddamn you?!");
            } 
            System.out.println("done"+iiit+"\tTime = "+System.currentTimeMillis());
        }
        return tempList;
    }
    public static void prokophTest(double[] act, double[] fert, double[] life, double[] gym, double[] glac)
    {
        double[] percentiles = {.025,.975, .5};
        
        RunList defaultRuns = performTests(10000, act, fert, life, gym, glac, new GCSVData());
        
        
        System.out.println("Default Runs finished calculating.");
        ArrayList<HistData> aList = new ArrayList();
        aList.add(defaultRuns.getMean());
        CommonData[] acom = defaultRuns.selectManyPercentileRuns(percentiles);
        aList.add(acom[0]);
        aList.add(acom[1]);
        aList.add(acom[2]);
        System.out.println("Now writing default runs.");
        Util.writeRuns("prokophDefault.dat", aList);
        
        System.out.println("Whew, writing all that was sure tough, I think I could use a break");
        GCSVData prokophData = new GCSVData();
        prokophData.setDLC0(GCSVData.DLC0_PROKOPH);
        RunList prokoph = performTests(10000, act, fert, life, gym, glac, prokophData);
        System.out.println("Alternative tests finished calculating.");
        ArrayList<HistData> bList = new ArrayList();
        bList.add(prokoph.getMean());
        CommonData[] bcom = prokoph.selectManyPercentileRuns(percentiles);
        bList.add(bcom[0]);
        bList.add(bcom[1]);
        bList.add(bcom[2]);
        System.out.println("Begin write.");
        Util.writeRuns("prokophAltDLC0.dat",bList);
    }
    public static void worsleyTest(double[] act, double[] fert, double[] life, double[] gym, double[] glac)
    {
        double[] percentiles = {.025,.975, .5};
        
        RunList defaultRuns = performTests(10000, act, fert, life, gym, glac, new GCSVData());
        System.out.println("Default Runs finished calculating.");
        ArrayList<HistData> aList = new ArrayList();
        aList.add(defaultRuns.getMean());
        CommonData[] acom = defaultRuns.selectManyPercentileRuns(percentiles);
        aList.add(acom[0]);
        aList.add(acom[1]);
        aList.add(acom[2]);
        System.out.println("Now writing default runs.");
        Util.writeRuns("worsleyDefault.dat", aList);
        
        System.out.println("Whew, writing all that was sure tough, I think I could use a break");
        GCSVData worsleyData = new GCSVData();
        worsleyData.setFA0(GCSVData.fA0_WORSLEY);
        RunList worsley = performTests(10000, act, fert, life, gym, glac, worsleyData);
        System.out.println("Alternative tests finished calculating.");
        ArrayList<HistData> bList = new ArrayList();
        bList.add(worsley.getMean());
        CommonData[] bcom = worsley.selectManyPercentileRuns(percentiles);
        bList.add(bcom[0]);
        bList.add(bcom[1]);
        bList.add(bcom[2]);
        System.out.println("Begin write.");
        Util.writeRuns("worsleyAltFA0.dat",bList);
        
        System.out.println("Whew, writing all that was sure tough, I think I could use a break");
        GCSVData worsleyPostData = new GCSVData();
        worsleyPostData.setFA0(GCSVData.fA0_WORSLEYPOST);
        RunList worsleyAlt = performTests(10000, act, fert, life, gym, glac, worsleyPostData);
        System.out.println("Alternative tests finished calculating.");
        ArrayList<HistData> cList = new ArrayList();
        cList.add(worsleyAlt.getMean());
        CommonData[] ccom = worsleyAlt.selectManyPercentileRuns(percentiles);
        cList.add(ccom[0]);
        cList.add(ccom[1]);
        cList.add(ccom[2]);
        System.out.println("Begin write.");
        Util.writeRuns("worsleyPostFA0.dat",cList);
    }
    public static RunList getDefaultList()
    {
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
        
        RunList defaultRuns = performTests(10000, act, fert, life, gym, glac, new GCSVData());
        System.out.println("Default Runs finished calculating.");
        return defaultRuns;
    }
}
