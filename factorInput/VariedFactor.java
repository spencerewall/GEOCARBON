package factorinput;

/**
 * The VariedFactor interface provides a framework for iterating through variations on a 
 * given factor in the GEOCARBSULF.  Every instance of a VariedFactor object is intended to 
 * represent only one factor in the model however each value obtained from the iteration is intended
 * to 
 * <p><p>
 * IF YOU INTEND TO EXTEND THIS INTERFACE DON'T FORGET THE FOLLOWING<p>
 *  1. getNext() and getNexts(int n) should default to applying boundaries.  If you do not wish
 *     for boundaries to be applied, simply set the max and min to Double.POSITIVE_INFINITY and 
 *     Double.NEGATIVE_INFINITY respectively<p>
 *  
 *  @author(Spencer Ewall)
 */
public interface VariedFactor extends InputFactor{
    String getType();
    String getVariableName();
    
    /**
     * Returns the next value from this VariedFactor object.  If maximum and minimum have been
     * specified and bound toggle is <code>true</code>, then only values within this range will be generated.
     * @return      the next value from this VariedFactor
     */
    double getNext();
    
    /**
     * Returns the next n values from this VariedFactor object.  If maximum and minimum have been
     * specified, then only values within this range will be generated.
     * @param   n   the number of values to be returned
     * @return      an array containing the next n values in this VariedFactor
     */
    double[] getNexts(int n);
    
    /**
     * Sets an upper boundary for number generation by getNext and getNexts.  All values 'n'
     * returned by these methods must fulfill (n<=max)
     * @param max the value of the upper boundary
     */
    void setMax(double max);
    /**
     * Sets an lower boundary for number generation by getNext and getNexts.  All values 'n'
     * returned by these methods must fulfill (n>=min)
     * @param min the value of the lower boundary
     */
    void setMin(double min);
    /**
     * Returns the current upper bound of this factor.
     */
    double getMax();
    /**
     * Returns the current lower bound of this factor.
     */
    double getMin();
    void setValueLimit(ValueLimit lim);
    ValueLimit getValueLimit();
    /**
     * Toggles bound usage by getNext and getNexts on and off.  Turning boundary usage off does
     * not delete bounds, but simply chooses not to use them.  This means that if this is used to 
     * turn bounds off and then back on, the boundary conditions from just prior to the toggle off
     * will reactivate.  When bound toggle is off, setMax and setMin will store values, but won't
     * do anything else until bounds toggle is turned back on.
     * @param b <code>true</code>: bounds are on and value generation is limited - 
     * <code>false</code>: bounds are off and do not apply to value generation
     */
    void toggleBounds(boolean useBounds);
    void setScale(double scale);
    double getScale();
    void setCenter(double center);
    double getCenter();
}