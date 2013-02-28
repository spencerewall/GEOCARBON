import arrayinput.ScrapData;

public class CompositeTests extends VariableTests
{
    public static void doAllSingles(String fileinc)
    {
        System.out.println("Start calculating all SINGLES COVARIED.");
        
        writeRunsAndErrors("tests/S-VARIED"+fileinc, percentiles, 
            performTests(vs[0], vs[1], vs[2], vs[3], vs[4], vs[5], new ScrapData()));
    }
    public static void doAll(String fileinc)
    {
        doAll(fileinc, new ScrapData());
    }
    public static void doAll(String fileinc, ScrapData arrs)
    {
        arrs.toggleAllVariance(true);
        System.out.println("Start calculating all COVARIED.");
        
        writeRunsAndErrors("tests/ALLVARIED"+fileinc, percentiles, 
            performTests(vs[0], vs[1], vs[2], vs[3], vs[4], vs[5], arrs));
    }
    public static void doLIFEGYM(String fileinc)
    {
        System.out.println("Start calculating LIFEGYM.");
        writeRunsAndErrors("tests/LIFEGYM"+fileinc, percentiles, 
            performTests(ds[0], ds[1], ds[2], vs[3], vs[4], ds[5], new ScrapData()));
    }
    /**
     * Covaries fA and fL simultaneously.
     */
    public static void doFAL(String fileinc)
    {
        System.out.println("Start calculating fAL.");
        writeRunsAndErrors("tests/FAL"+fileinc, percentiles, 
            performTests(ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], new ScrapData()));
    }
    /**
     * Covaries fA and fD simultaneously.
     */
    public static void doFAD(String fileinc)
    {
        System.out.println("Start calculating fAD.");
        writeRunsAndErrors("tests/FAD"+fileinc, percentiles, 
            performTests(ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], new ScrapData()));
    }
    /**
     * Covaries all parameters except DLC.
     */
    public static void doAllxDLC(String fileinc)
    {
        ScrapData arrs = new ScrapData();
        arrs.toggleAllVariance(true); arrs.toggleDLC0Variance(false);
        System.out.println("Start calculating all COVARIED.");
        
        writeRunsAndErrors("tests/ALLxDLC"+fileinc, percentiles, 
            performTests(vs[0], vs[1], vs[2], vs[3], vs[4], vs[5], arrs));
    }
}
