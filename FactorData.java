//import java.util.ArrayList;
import java.util.Arrays;
import dataHistographs.*;
/**
 * This class will run a simulation for a specific set of values for act, fert, life, gym, and glac.
 */
public class FactorData implements HistData
{
    private double deltaT;
    private double ACT;
    private double FERT;
    private double LIFE;
    private double GYM;
    private double GLAC;
    private GCSVData factorArrays;
    private float[] CO2;

    

    public FactorData(double deltat, double act, double fert, double life, double gym, double glac, GCSVData arrDat)
    {
        deltaT = deltat;
        ACT = act;
        FERT = fert;
        LIFE = life;
        GYM = gym;
        GLAC = glac;
        factorArrays=arrDat;
        
        CO2 = CO2Calculator.doCO2Calc(deltat, act, fert, life, gym, glac, arrDat);
    }
    public double getDeltaT(){ return deltaT; }
    public double getACT() { return ACT; }
    public double getFERT() { return FERT; }
    public double getLIFE() { return LIFE; }
    public double getGYM() { return GYM; }
    public double getGLAC() { return GLAC; }
    public String toString()
    {
        return Arrays.toString(CO2);
    }
    public float getCO2(int i)
    {
        return CO2[i];
    }
    public float[] getAllCO2()
    {
        return CO2;
    }
    public int size()
    {
        return CO2.length;
    }
    public boolean equals(Object other)
    {
        if (this==other)
            return true;
        if (!(other instanceof FactorData))
            return false;
        FactorData o = (FactorData) other;
        return ((this.getDeltaT()==o.getDeltaT())&&
                (this.getACT()==o.getACT())&&
                (this.getFERT()==o.getFERT())&&
                (this.getLIFE()==o.getLIFE())&&
                (this.getGYM()==o.getGYM())&&
                (this.getGLAC()==o.getGLAC()));
    }
    /**
     * Returns a hash code for this <code>CustomFVR</code> object.  The factor values from the construction of this Object are placed
     * in an array: {deltaT, ACT, FERT, LIFE, GYM, GLAC}.  The hashCode of this object is calculated from the result
     * of java.util.Arrays.hashCode(double[]), such that the input is the previously defined array.
     */
    public int hashCode()
    {
        double[] factors = {deltaT, ACT, FERT, LIFE, GYM, GLAC};
        return Arrays.hashCode(factors);
    }
}