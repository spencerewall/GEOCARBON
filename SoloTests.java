import arrayinput.*;

public class SoloTests extends VariableTests
{
    public static void doAll(String fileinc)
    {
        doDefault(fileinc);
        doDeltaT(fileinc);
        doACT(fileinc);
        doFERT(fileinc);
        doLIFE(fileinc);
        doGYM(fileinc);
        doGLAC(fileinc);
        doDeltaT(fileinc);
        doFA(fileinc);
        doFD(fileinc);
        doFL(fileinc);
        doDLS(fileinc);
        doDLC(fileinc);
    }
    public static void doDefault(String fileinc)
    {
        System.out.println("Start calculating default.");
        writeRunsAndErrors("tests/DEFAULT"+fileinc, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],ds[5],new ScrapData()));
    }
    public static void doDefaultPark(String fileinc)
    {
        System.out.println("Start calculating default.");
        writeRunsAndErrors("tests/PARKDef"+fileinc, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],ds[5],GCSVData.PARK_DATA));
    }
    public static void doDeltaT(String fileinc) 
    {
        System.out.println("Start calculating dT.");
        writeRunsAndErrors("tests/dT"+fileinc, percentiles, performTests(vs[0],ds[1],ds[2],ds[3],ds[4],ds[5],new ScrapData()));
    }
    public static void doACT(String fileinc)
    {
        System.out.println("Start calculating ACT.");
        writeRunsAndErrors("tests/ACT"+fileinc, percentiles, performTests(ds[0],vs[1],ds[2],ds[3],ds[4],ds[5],new ScrapData()));
    }
    public static void doFERT(String fileinc) 
    {
        System.out.println("Start calculating FERT.");
        writeRunsAndErrors("tests/FERT"+fileinc, percentiles, performTests(ds[0],ds[1],vs[2],ds[3],ds[4],ds[5],new ScrapData()));
    }
    public static void doLIFE(String fileinc)
    {
        System.out.println("Start calculating LIFE.");
        writeRunsAndErrors("tests/LIFE"+fileinc, percentiles, performTests(ds[0],ds[1],ds[2],vs[3],ds[4],ds[5],new ScrapData()));
    }
    public static void doGYM(String fileinc) 
    {
        System.out.println("Start calculating GYM.");
        writeRunsAndErrors("tests/GYM"+fileinc, percentiles,performTests(ds[0],ds[1],ds[2],ds[3],vs[4],ds[5],new ScrapData()));
    }
    public static void doGLAC(String fileSuffix) 
    {
        System.out.println("Start calculating GLAC.");
        writeRunsAndErrors("tests/GLAC"+fileSuffix, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],vs[5],new ScrapData()));
    }
    /**
     * Performs default testing for individual variation of the <code>DLC</code> parameter as
     * described in <code>ScrapData</code>.
     */
    public static void doDLC(String fileSuffix)
    {
        ScrapData arrs = new ScrapData();
        arrs.toggleDLC0Variance(true);
        
        writeRunsAndErrors("tests/DLC"+fileSuffix, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],ds[5],arrs));
    }
    public static void doDLS(String fileSuffix)
    {      
        ScrapData arrs = new ScrapData();
        arrs.toggleDLS0Variance(true);
        
        writeRunsAndErrors("tests/DLS"+fileSuffix, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],ds[5],arrs));
    }
    public static void doFA(String fileSuffix)
    {
        ScrapData arrs = new ScrapData();
        arrs.toggleFA0Variance(true);

        writeRunsAndErrors("tests/FA"+fileSuffix, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],ds[5],arrs));
    }
    public static void doFD(String fileSuffix)
    {
        ScrapData arrs = new ScrapData();
        arrs.toggleFD0Variance(true);
        writeRunsAndErrors("tests/FD"+fileSuffix, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],ds[5],arrs));
    }
    public static void doFL(String fileSuffix)
    {
        ScrapData arrs = new ScrapData();
        arrs.toggleFL0Variance(true);
        writeRunsAndErrors("tests/FL"+fileSuffix, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],ds[5],arrs));
    }
    
    /*
     *   Custom solotesting methods below
     *   
     *          |       |       |
     *          |       |       |
     *          V       V       V 
     *          
     *          Hardhat Required          
     */
    
    /**
     * Performs testing for individual variation of the <code>DLC</code> parameter as described
     * in <code>ScrapData</code> with data generation limited to the area within the given 
     * number of standard deviations.
     * @param n the number of standard deviations from the mean which gaussian randomization 
     * is limited to.
     */
    public static void doDLC(String fileinc, double n)
    {
        ScrapData arrs = new ScrapData();
        arrs.toggleDLC0Variance(true);
        arrs.setDLCLimit(n);

        writeRunsAndErrors("tests/DLC"+fileinc, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],ds[5],arrs));
    }
    /**
     * This method cheats.  I say that it cheats because it breaks the aesthetic of the 'SoloTests'
     * class since it allows one to vary things other than just DLC
     */
    public static void doDLC(String fileinc, ScrapData s)
    {
        s.toggleAllVariance(false);
        s.toggleDLC0Variance(true);
        writeRunsAndErrors("tests/DLC"+fileinc, percentiles, performTests(ds[0],ds[1],ds[2],ds[3],ds[4],ds[5],s));
    }
}
