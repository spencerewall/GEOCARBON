import java.text.DecimalFormat;

public class GcsvFormat 
{
    private DecimalFormat formatter;
    //private int leadingZeros;
    //private int trailingZeros;
    
    /**
     * Input string form has the same typology as DecimalFormat
     * Leading zeros are represented by the letter Z
     */
    public GcsvFormat(String form)
    {
        formatter = new DecimalFormat(form);
    }
    
    public String format(double n)
    {
        /*String leadZ = "";
        String trailZ = "";
        while (leadZ.length()<leadingZeros)
            leadZ+="0";
        while (trailZ.length()<trailingZeros)
            trailZ+="0";*/
        
        String result = formatter.format(n);
        if (result.equals("0°"))
        {
        }
        if (n<1&&n>0)
            result = result.replace("E","E-");
        else if (n>=1)
            result = result.replace("E","E+");
        
        return result;
    }
}
