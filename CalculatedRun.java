import java.util.ArrayList;

public abstract class CalculatedRun extends CO2Run
{
    private boolean recalculate;
    /**
     * Default constructor for the CO2Run class.  Forces recalculation of CO2 values upon next call
     * to getCO2 or getAllCO2s and otherwise functions similarly to CO2Run default constructor.
     * addition.
     */
    public CalculatedRun()
    {
        super();
        forceNextCalculate();
    }
    /**
     * Overloaded constructor for the CalculatedRun class.  Forces recalculation of CO2 values upon next call
     * to getCO2 or getAllCO2s and otherwise functions similarly to CO2Run overloaded constructor of the same
     * parameter specifications.
     * 
     * @param CO2Vals a List containing the values to be included in this instance of CalculatedRun
     */
    public CalculatedRun(ArrayList<Double> CO2Vals)
    {
        super(CO2Vals);
        forceNextCalculate();
    }
    /**
     * A call to this method must be made for CO2 values to be calculated immediately.  Calls to this method
     * let other methods know that CO2 has just been recalculated.  This prevents other methods from performing
     * unnecessary recalculations for CO2.  It then recalculates values of CO2 as defined by doCO2Calc.
     */
    public void calculateCO2()
    {
        recalculate=false;
        doCO2Calc();
    }
    public int size()
    {
        if (recalculate)
            calculateCO2();
        recalculate=false;
        return super.size();
    }
    /**
     * Abstract method that tells exactly how CO2 values are calculated.  This method is typically only run
     * through calculateCO2.
     */
    protected abstract void doCO2Calc();
    /**
     * Overides getAllCO2() in parent class CO2Run in order to force CO2 values to be
     * recalculated if it is requested by an 
     */
    public ArrayList<Double> getAllCO2()
    {
        if (recalculate) calculateCO2();
        recalculate = false;
        return super.getAllCO2();
    }
    /**
     * Overides getCO2() in parent class CO2Run in order to force CO2 values to be
     * recalculated if necessary.  This method 
     */
    public double getCO2(int i)
    {
        if (recalculate) calculateCO2();
        recalculate = false;
        return super.getCO2(i);
    }
    /**
     * Forces the recalculation of CO2 values upon next attempted access of CO2 values.  If this method
     * has been called, then the next call of either getCO2 or getAllCO2 will force all CO2 values to be
     * recalculated.  This is especially useful in classes where frequent changes may be made that would
     * affect the CO2 values, as it minimizes the number of times when recalculations must be made.  Any
     * such actions performed by a subclass must make a call to this in order for CO2 values to be 
     * recalculated.
     */
    public void forceNextCalculate()
    {
        recalculate=true;
    }
}
