/**
 * Drives all tests performed in the generation of results for ewall2012.
 * 
 * @author (sjewall) 
 */
public class Driver
{
    public static void main(String[] args)
    {
        CompositeTests.doAllSingles("");
        VariableTests.standardTests("");
        //SoloTests.doDefaultPark("");
        //CompositeTests.doLIFEGYM("");
        //CompositeTests.doFAL("");
        //CompositeTests.doFAD("");
        //CompositeTests.doAllxDLC("");
    }
}
