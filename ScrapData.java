import factorinput.GaussianFactor;

public class ScrapData extends ArrayData
{
    public static final double[] DLC0_PROKOPH={2.19727,2.41789,1.78769,0.71326,2.59558,2.69832,2.54320,2.37403,2.33513,2.64514,2.03761,2.68880,/**Double.NaN*/2.5,0.00435,-0.50200,1.36000,0.83512,1.15636,1.15707,-0.22265,1.64161,3.48000,3.27462,1.83773,0.73323,3.05531,4.17575,4.71883,3.00737,3.56500,3.81472,4.39249,3.33513,2.32810,3.02595,3.21703,1.46195,0.63680,0.84605,0.93036,0.31511,1.79215,1.65918,2.54509,2.05391,0.56955,-0.18493,-1.24453,-1.28242,/**Double.NaN*/1.0,-0.10750,-0.72000,-0.89091,/**Double.NaN*/0.0,/**Double.NaN*/-2.0,/**Double.NaN*/0.0,/**Double.NaN*/0.0,/**Double.NaN*/0.0};
    public static final double[] DLC0Dev_PROKOPH={0.69191,0.90256,1.08284,0.37044,0.93034,1.24451,1.34046,0.35329,0.51412,0.86102,0.67322,0.59580,Double.NaN,0.73834,1.03749,Double.NaN,0.87404,0.87695,0.80196,1.03743,0.52196, Double.NaN,0.54590,1.06876,0.68391,1.35992,0.93948,1.83520,1.35181,1.65847,1.03057,1.44759,1.73197,1.51404,1.46782,1.57262,1.48498,0.75028,1.31378,1.35351,0.93554,0.94225,2.27731,1.77342,2.19584,0.73294,0.75281,0.90894,0.68785,Double.NaN,0.30724, Double.NaN,1.06990, 0, 0, 0, 0, 0};
    public static final double[] Sr0_PROKOPH = {91.30154728, 90.09370629, 88.92372414, 87.50480176, 84.33259259, 81.96585366, 79.67361111, 78.2452381, 77.58571429, 77.787, 77.49, 77.49, 77.80076923, 78.38858824, 77.37311475, 76.06555556, 75.09071429, 74.298125, 73.395, 74.18448276, 74.46717949, 73.7718, 73.30852021, 74.70576072, 73.50880346, 70.35895363, 68.83167619, 71.75636364, 72.03226415, 73.726, 77.75122806, 79.43993357, 76.67679409, 76.66476826, 78.36991742, 76.37427319, 71.61086309, 70.64757882, 71.94485718, 76.6338641, 80.86379295, 82.87434783, 81.34081114, 78.67437392, 78.05838853, 79.97784768, 83.88026316, 81.54832006, 81.06723254, 78.60431028, 79.15513398, 86.14037037, 86.2786099, 83.28819365, 80.21572845, 79.05755338, 81.5513119, 88.5833503, 89.61191093, 90.70766435, 90.58986697, 91.18987373, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN};
    public static final double[] Sr0Dev_PROKOPH = {0.553116834, 0.350570951, 0.594612525, 1.289415964, 0.686829665, 0.704746491, 0.496986486, 0.452597721, 0.646895941, 0.251588288, 0.426399656, 0.534400421, 0.519597656, 0.716702419, 0.737417552, 0.79495424, 0.539981481, 0.515473488, 0.924459688, 0.889045056, 0.611077984, 0.691094045, 0.776479168, 0.322159639, 0.711093209, 1.437003119, 0.988915142, 1.02586802, 0.95878457, 1.20534452, 1.052067441, 1.530028573, 0.492869678, 0.820773844, 1.31149982, 4.055954001, 2.23267574, 1.281272304, 1.86958056, 2.255023586, 1.602746168, 0.757444384, 1.145303398, 1.920654285, 1.679413431, 1.860388687, 1.042353073, 0.904766586, 0.772619055, 0.523496296, 0.954978983, 1.679245438, 1.135491107, 0.967896645, 1.393593346, 0.760895298, 2.795025904, 1.807750273, 1.719802226, 0.578335042, 1.694211779, 0, 0, 0, 0, 0, 0, 0};
    private static final double avgDLC0Dev = 1.0655;
    //average stDev value is used when stDev can't be calculated. This issue arises during intervals
    //where prokoph has >=1 data point.
    
    
    /**
     * TIMING IS FROM 520 ONWARDS
     */
    //public static final double[] Sr0_PROKOPH={0.709089986, 0.708899137, 0.708492492, 0.707985696, 0.70776348, 0.707756167, 0.707801222, 0.707771766, 0.707550054, 0.707373712, 0.7074534, 0.70737718, 0.707330852, 0.707470576, 0.70735088, 0.707035895, 0.706883168, 0.707175636, 0.707203226, 0.7073726, 0.707775123, 0.707943993, 0.707667679, 0.707666477, 0.707836992, 0.707637427, 0.707161086, 0.707079071, 0.707194486, 0.707663386, 0.708086379, 0.708287435, 0.708134081, 0.707867437, 0.707805839, 0.707997785, 0.708388026, 0.708154832, 0.708106723, 0.707860431, 0.707915513, 0.708614037, 0.708630437, 0.708328819, 0.708021573, 0.707905755, 0.708155131, 0.708858335, 0.708961191, 0.709070766, 0.709058987, 0.709118987};
    //public static final double[] Sr0_PARK={92.0,90.5,89.5,89.0,85.0,82.5,81.0,79.0,78.0,78.0,78.0,78.0,78.5,79.0,78.0,77.0,76.0,75.5,75.0,75.0,74.0,73.0,72.0,73.0,71.0,69.0,68.0,69.0,75.0,76.0,75.0,78.0,78.0,78.0,75.0,73.0,71.0,77.0,82.0,83.0,82.0,82.0,81.0,78.0,75.0,78.0,82.0,82.0,78.0,80.0,85.0,87.0,86.0,81.0,79.0,78.0,80.0,82.0,85.0,87.0,89.0,89.0,89.0,89.0,90.0,90.0,90.0,90.0};
    //public static final double Sr0_Vary = .5;
    //public static final double[] Sr0Dev_PROKOPH={0.005531168, 0.007890779, 0.018698418, 0.010048873, 0.005687861, 0.00346773, 0.005968816, 0.009331113, 0.00816136, 0.008230487, 0.006882039, 0.00691094, 0.007764792, 0.003221596, 0.007110932, 0.014370031, 0.009889151, 0.01025868, 0.009587846, 0.012053445, 0.010520674, 0.015300286, 0.004928697, 0.008207738, 0.013114998, 0.04055954, 0.022326757, 0.012812723, 0.018695806, 0.022550236, 0.016027462, 0.007574444, 0.011453034, 0.019206543, 0.016794134, 0.018603887, 0.010423531, 0.009047666, 0.007726191, 0.005234963, 0.00954979, 0.016792454, 0.011354911, 0.009678966, 0.013935933, 0.007608953, 0.027950259, 0.018077503, 0.017198022, 0.00578335, 0.016942118, 0/***/, 0/***/};
    
    /**
     * TIMING IS FROM 520 ONWARDS
     */
    public static final double[] DLS0_PROKOPH={21.64, 22.07, 21.84, 21.80, 22.25, 19.33, 18.27, 18.80, 18.29, 18.74, 17.80, 15.75, 16.24, 17.11, 17.26, 16.00, 15.94, 17.88, 19.48, 14.73, 17.87, 18.34, 18.93, 18.13, 23.04, 20.47, 11.00, 14.19, 12.58, (12.58+12.14)/2, 12.14, 14.03, 15.37, 14.00, 17.20, 19.50, 19.70, (19.70+24.02)/2.0, 24.02, 20.87, 15.48, 22.57, 26.27, 27.68, 27.91, 24.60, 24.83, 25.65, 30.07, 31.43, /**Double.NaN*/27.8, /**Double.NaN*/35.3, /**Double.NaN*/36.4, /**Double.NaN*/33.0, /**Double.NaN*/30.3, /**Double.NaN*/32.0, /**Double.NaN*/34.4, /**Double.NaN*/35.2};
    public static final double[] DLS0Dev_PROKOPH={0.4651, 0.2478, 0.1784, 0.4308, 0.1795, 1.5208, 0.52086966, 1.0419, 0.7174, 0.3505, 1.5173, 0.6694, 1.4621, 1.9351, 3.8785, 0, 0.4159, 2.0017, 2.8420, 0.8386, 6.6063, 0.7987, 0.6658, 0.9713, 4.6961, 5.9285, 0.0000, 3.5881, 1.0889, 0.0000, 0.5983, 0.9429, 0.8795, 0.7263, 2.8891, 2.5456, 0, 0, 1.9992, 3.9004, 2.1685, 2.2480, 3.2314, 4.3601, 5.2277, 4.7455, 2.2811, 4.5007, 2.1825, 5.8106, 7.5751, 4.4049, 3.3738, 0, 0, 0, 0, 0};
    private static final double avgDLS0Dev = 2.3401;
    
    /**
     * Variation in percent defines one standard deviation
     */
    public static final double fA0_Vary = .05;
    /**
     * Variation in percent defines one standard deviation
     */
    public static final double fSR0_Vary = .25;
    /**
     * Variation in percent defines one standard deviation
     */
    public static final double fD0_Vary = .25;
    /**
     * Variation in percent defines one standard deviation
     */
    public static final double temp0_Vary = .25;
    /**
     * Variation in percent defines one standard deviation
     */
    public static final double fL0_Vary = .25;
    
    
    private boolean faOn, fdOn, tempOn, fsrOn, dlsOn, dlcOn, alOn, flOn, srOn, fSROn;
    private GaussianFactor g;
    public ScrapData()
    {
        super();
        g=new GaussianFactor();
        faOn=false;
        fdOn=false;
        tempOn=false;
        fsrOn=false;
        dlsOn=false;
        dlcOn=false;
        flOn=false;
        srOn=false;
        super.setDLS0(DLS0_PROKOPH);
        super.setDLC0(DLC0_PROKOPH);
    }
    /**
     * 
     */
    public double getFA0(int i){
        if (Double.isNaN(super.getFA0(i)))
                return ArrayData.PARK_DATA.getFA0(i);
        else if(faOn){
            g.setCenter(super.getFA0(i));
            g.setScale(Math.abs(fA0_Vary*super.getFA0(i)));
            g.setMax(super.getFA0(i) + 2*fA0_Vary*super.getFA0(i));
            g.setMin(super.getFA0(i) - 2*fA0_Vary*super.getFA0(i));
            
            double val = g.getNext();
            
            return val;
        }
        else
            return super.getFA0(i);
    }
    public double[] getFA0(){
        double[] t = new double[super.getFA0().length];
        for (int i=0; i<super.getFA0().length; i++){
            t[i]=getFA0(i);
        }
        return t;
    }
    public double getFD0(int i){
        if (Double.isNaN(super.getFD0(i)))
            return ArrayData.PARK_DATA.getFD0(i);
        else if(fdOn){
            g.setCenter(super.getFD0(i));
            g.setScale(Math.abs(fD0_Vary*super.getFD0(i)));
            g.setMax(super.getFD0(i) + 2*fD0_Vary*super.getFD0(i));
            g.setMin(super.getFD0(i) - 2*fD0_Vary*super.getFD0(i));
        
            double val = g.getNext();
            
            return val;
        }
        else
            return super.getFD0(i);
    }
    public double[] getFD0(){
        double[] t = new double[super.getFD0().length];
        for (int i=0; i<super.getFD0().length; i++){
            t[i]=getFD0(i);
        }
        return t;
    }
    
    public double getTemp0(int i){
        if (Double.isNaN(super.getTemp0(i)))
            return ArrayData.PARK_DATA.getTemp0(i);
        else if(tempOn){
            g.setCenter(super.getTemp0(i));
            g.setScale(1.5);
            g.setMax(Double.POSITIVE_INFINITY);
            g.setMin(super.getTemp0(i) - 3.0);
        
            double val = g.getNext();
            
            return val;
        }
        else
            return super.getTemp0(i);
    }
    public double[] getTemp0(){
        double[] t = new double[super.getTemp0().length];
        for (int i=0; i<super.getTemp0().length; i++){
            t[i]=getTemp0(i);
        }
        return t;
    }
    
    public double getFSR0(int i){
        if (Double.isNaN(super.getFSR0(i)))
            return ArrayData.PARK_DATA.getFSR0(i);
        else if(fsrOn){
            g.setCenter(super.getFSR0(i));
            g.setScale(Math.abs(fSR0_Vary*super.getFSR0(i)));
            g.setMax(super.getFSR0(i) + 2*fSR0_Vary*super.getFSR0(i));
            g.setMin(super.getFSR0(i) - 2*fSR0_Vary*super.getFSR0(i));
        
            double val = g.getNext();
            
            return val;
        }
        else
            return super.getFSR0(i);
    }
    public double[] getFSR0(){
        double[] t = new double[super.getFSR0().length];
        for (int i=0; i<super.getFSR0().length; i++){
            t[i]=getFSR0(i);
        }
        return t;
    }
    
    public double getFL0(int i)
    {
        if (Double.isNaN(super.getFL0(i)))
            return ArrayData.PARK_DATA.getFL0(i);
        else if(flOn){
            g.setCenter(super.getFL0(i));
            g.setScale(fL0_Vary *super.getFL0(i));
            g.setMin(super.getFL0(i) - 2*fL0_Vary*super.getFL0(i));
            g.setMax(super.getFL0(i) + 2*fL0_Vary*super.getFL0(i));
            
            double val = g.getNext();
            
            return val;
        }
        return super.getFL0(i);
    }
    public double[] getFL0(){
        double[] t = new double[super.getFL0().length];
        for (int i=0; i<super.getFL0().length; i++){
            t[i]=getFL0(i);
        }
        return t;
    }
    public double getDLS0(int i)
    {
        if (dlsOn) {
            //is DLS NaN?
            double thismean;
            if (Double.isNaN(super.getDLS0(i)))
                thismean = ArrayData.PARK_DATA.getDLS0(i);
            else
                thismean = super.getDLS0(i);
                
            //is deviation NaN?
            double thisdev;
            if (Double.isNaN(DLS0Dev_PROKOPH[i]))
                thisdev=avgDLS0Dev;
            else
                thisdev=DLS0Dev_PROKOPH[i];
            
            g.setCenter(thismean);
            g.setScale(thisdev);
            g.setMin(thismean - 2*thisdev);
            g.setMax(thismean + 2*thisdev);
            
            return g.getNext();
        }
        else if (Double.isNaN(super.getDLS0(i))) {
            return ArrayData.PARK_DATA.getDLS0(i);
        }
        else {
            return super.getDLS0(i);
        }
    }
    public double[] getDLS0(){
        double[] t = new double[super.getDLS0().length];
        for (int i=0; i<super.getDLS0().length; i++){
            t[i]=getDLS0(i);
        }
        return t;
    }
    public double getDLC0(int i)
    {
        if (dlcOn) {
            //is DLC NaN?
            double thismean;
            if (Double.isNaN(super.getDLC0(i)))
                thismean = ArrayData.PARK_DATA.getDLC0(i);
            else
                thismean = super.getDLC0(i);
            
            //is deviation NaN?
            double thisdev=getDLCDev(i);
            
            g.setCenter(thismean);
            g.setScale(Math.abs(thisdev));
            g.setMin(thismean - Math.abs(2*thisdev));
            g.setMax(thismean + Math.abs(2*thisdev));
            
            return g.getNext();
        }
        else if (Double.isNaN(super.getDLC0(i))) {
            return ArrayData.PARK_DATA.getDLC0(i);
        }
        else {
            return super.getDLC0(i);
        }
    }
    public double[] getDLC0(){
        double[] t = new double[super.getDLC0().length];
        for (int i=0; i<super.getDLC0().length; i++){
            t[i]=getDLC0(i);
        }
        return t;
    }
    public static double getDLCDev(int i)
    {
        if (Double.isNaN(DLC0Dev_PROKOPH[i]))
            return avgDLC0Dev;
        else
            return DLC0Dev_PROKOPH[i];
    }
    
    public double getSr0(int i)
    {
        if (srOn) {
            //is DLC NaN?
            double thismean;
            if (Double.isNaN(super.getSr0(i)))
                thismean = ArrayData.PARK_DATA.getSr0(i);
            else
                thismean = super.getSr0(i);
            
            //is deviation NaN?
            double thisdev=getSrDev(i);
            
            g.setCenter(thismean);
            g.setScale(Math.abs(thisdev));
            g.setMin(thismean - Math.abs(2*thisdev));
            g.setMax(thismean + Math.abs(2*thisdev));
            
            return g.getNext();
        }
        else if (Double.isNaN(super.getSr0(i))) {
            return ArrayData.PARK_DATA.getSr0(i);
        }
        else {
            return super.getSr0(i);
        }
    }
    public double[] getSr0(){
        double[] t = new double[super.getSr0().length];
        for (int i=0; i<super.getSr0().length; i++){
            t[i]=getSr0(i);
        }
        return t;
    }
    public static double getSrDev(int i)
    {
        if (Double.isNaN(Sr0Dev_PROKOPH[i]))
            return 0;
        else
            return Sr0Dev_PROKOPH[i];
    }
    
    /**
     * Toggles variation on or off in all of the parameters at once. If given <code>false</code>, variation
     * will be turned off in each of <code>getFA0, getFD0, getFL0, getDLC0</code>, and <code>getDLS0</code>.
     * Conversly, if given <code>true</code>, variation will be turned for all factors.  After this method 
     * has been called, its effects may be overrridden for a specific factor by calling that factor's 
     * <code>toggle<FACTOR>Variance</code>, or for all factors by calling this method again.
     * 
     * @param toggle value to set all individual factor variance toggles to.
     */
    public void toggleAllVariance(boolean on) {
        toggleFA0Variance(on);
        toggleFD0Variance(on);
        toggleFL0Variance(on);
        toggleDLC0Variance(on);
        //toggleDLS0Variance(on);
        toggleSr0Variance(on);
        toggleFSR0Variance(on);
        toggleTemp0Variance(on);
    }
    public void toggleFA0Variance(boolean on) {
        faOn=on;
    }
    public void toggleFD0Variance(boolean on) {
        fdOn=on;
    }
    public void toggleFL0Variance(boolean on) {
        flOn=on;
    }
    public void toggleDLC0Variance(boolean on) {
        dlcOn=on;
    }
    public void toggleDLS0Variance(boolean on) {
        dlsOn=on;
    }
    public void toggleSr0Variance(boolean on) {
        srOn=on;
    }
    public void toggleFSR0Variance(boolean on) {
        fsrOn=on;
    }
    public void toggleTemp0Variance(boolean on) {
        tempOn=on;
    }
}