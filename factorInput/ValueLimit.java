package factorinput;

/**
 * The ValueLimiter class a helper to the many subclasses of varied factor, which all feature very 
 * similar code for imposing upper and lower boundaries on their data. An instance of this class
 * represents a range of numbers defined by the endpoints max and min. The <code>passesBoundTest</code>
 * method is used by the factor classes to make certain that a value passes 
 * 
 * @author (Spencer Ewall) 
 * @version (1.0)
 */
public class ValueLimit
{
    private double max;
    private double min;
    private boolean toggle;
    
    /* Constructors */
    public ValueLimit() {
        this.max = Double.POSITIVE_INFINITY;
        this.min = Double.NEGATIVE_INFINITY;
        this.toggle = true;
    }
    public ValueLimit(double min, double max, boolean toggle)
    {
        this.max=max;
        this.min=min;
        this.toggle=toggle;
    }
    
    
    /* Accessor Methods */
    /**
     * Returns the value of the upper limit.  This is the highest possible value that 
     * will still pass <code>boundTest(double)</code>.
     * @return the highest value 
     */
    public double getMax() {
        return this.max;
    }
    /**
     * Returns the value of the lower limit.
     * @return the minimum value that will pass boundTest(double)
     */
    public double getMin() {
        return this.min;
    }
    /**
     * Returns true, if boundary checking 
     */
    public boolean getToggleState(){
        return this.toggle;
    }
    
    
    /* Mutator Methods */
    public void setMax(double max) {
        this.max=max;
    }
    public void setMin(double min) {
        this.min=min; 
    }
    /**
     * Boolean input turns value checking on or off.  If this is set to false then all parameter
     * choices will pass <code>passesBoundTest</code>.
     */
    public void toggleBounds(boolean toggle) {
        this.toggle=toggle;
    }
    
    /* Methods That Do Stuff */
    /**
     * Checks if a value will pass the boundary test by seeing if (A) it is within the
     * defined boundaries and (B) the boundaries are currently active.  If the toggle is set to false, 
     * then all tests will pass.  This is the method that should be used by factor generators.
     */
    public boolean passesBoundTest(double n){
        if (inBounds(n) || !toggle) 
            return true;
        else return false;
    }
    private boolean inBounds(double n){
        return (n>=this.min)&&(n<=this.max);
    }
    /**
     * This method takes an array and returns a new array containing only the elements of the input
     * that within in the bounds of this ValueLimit object.
     * 
     * @param a 
     * @return an array containg all values
     */
    public double[] getInlist(double[] a){
        int len=1;
        for (int i=0; i<a.length; i++) {
            if (inBounds(a[i])) len++;
        }
        
        double[] in = new double[len];
        int c=0;
        for (int i=0; i<a.length; i++) {
            if (inBounds(a[i])) {
                in[c]=a[i];
                c++;
            }
        }
        return in;
    }
    public double[] getOutlist (double[] a){
        int len=0;
        for (int i=0; i<a.length; i++) {
            if (!inBounds(a[i])) len++;
        }
        
        double[]ex = new double[len];
        int c=0;
        for (int i=0; i<a.length; i++) {
            if (!inBounds(a[i])) {
                ex[c]=a[i];
                c++;
            }
        }
        return ex;
    }
    /* Private Helper Methods */
}
