import java.util.Collection;

public class UtilityMethods
{
    public static void writeRuns(String fileName, RunList runs)
    {
        try
        {
            java.io.PrintWriter output = new java.io.PrintWriter(new java.io.FileWriter(fileName));
            for (int t=0; t<runs.size(); t++)
            {
                output.print(runPrintString(runs.get(t)));
            }
            output.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
    public static void writeRuns(String fileName, Collection<HistData> runs)
    {
        try
        {
            java.io.PrintWriter output = new java.io.PrintWriter(new java.io.FileWriter(fileName));
            for (HistData r: runs)
            {
                output.print(runPrintString(r));
            }
            output.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
    public static String runPrintString(HistData r)
    {
        float[] arr = r.getAllCO2();
        
        String out="";
        int age=0;
        for (float d: arr)
        {
            out+=age+"\t"+d+"\n";
            age-=5;
        }
        return out;
    }
}