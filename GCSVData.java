public class GCSVData
{
    // is the factor of past continental area, relative to modern (10 Ma time steps)
    public static final double[] fA0_BERNER={1.0,1.0,1.0,1.0,0.91,0.92,0.94,0.87,0.79,0.81,
        0.82,0.85,0.88,0.86,0.84,0.83,0.85,0.87,0.89,0.91,
        0.91,0.90,0.90,0.88,0.89,0.86,0.84,0.81,0.81,0.80,
        0.80,0.80,0.79,0.77,0.76,0.73,0.70,0.69,0.69,0.72,
        0.74,0.74,0.74,0.62,0.63,0.67,0.62,0.62,0.62,0.65,
        0.65,0.66,0.64,0.66,0.85,0.90,0.95,1.00};
    //Data from worsley&kidder 1991.  points are fixed at 100&110=.86, 430=.6, 510&520=.67, 570=1
    public static final double[] fA0_WORSLEY={1.00,0.99,0.97,0.96,0.94,0.93,0.92,0.90,0.89,0.87,
        0.86,0.86,0.85,0.84,0.84,0.83,0.82,0.81,0.80,0.80,
        0.79,0.78,0.77,0.76,0.75,0.75,0.74,0.73,0.72,0.71,
        0.71,0.70,0.69,0.68,0.67,0.66,0.66,0.65,0.64,0.63,
        0.62,0.62,0.61,0.60,0.61,0.62,0.63,0.64,0.64,0.65,
        0.66,0.67,0.67,0.74,0.80,0.87,0.93,1.00};
        
    //Data from worsley&kidder 1991.  Mid cretaceous point is set as .6 (postulated data). 100&110=.6
    public static final double[] fA0_WORSLEYPOST={1.00,0.96,0.92,0.88,0.84,0.80,0.76,0.72,0.68,0.64,
        0.60,0.60,0.60,0.60,0.60,0.60,0.60,0.60,0.60,0.60,
        0.60,0.60,0.60,0.60,0.60,0.60,0.60,0.60,0.60,0.60,
        0.60,0.60,0.60,0.60,0.60,0.60,0.60,0.60,0.60,0.60,
        0.60,0.60,0.60,0.60,0.61,0.62,0.63,0.64,0.64,0.65,
        0.66,0.67,0.67,0.74,0.80,0.87,0.93,1.00};
                                   
    //fD is the factor of water runoff per unit continental area, relative to modern
    public static final double[] fD0_BERNER={1.00,1.02,1.04,1.06,1.08,1.10,1.15,1.20,1.19,1.19,
        1.18,1.18,1.18,1.17,1.15,1.13,1.12,1.10,1.05,1.03,
        1.02,1.01,0.98,0.96,0.94,0.96,0.97,1.02,1.08,1.10,
        1.13,1.14,1.15,1.19,1.21,1.21,1.21,1.21,1.21,1.22,
        1.23,1.25,1.28,1.31,1.10,1.00,0.90,0.92,0.96,1.03,
        1.07,1.10,1.12,1.12,1.12,1.12,1.12,1.12};
                                   
    //temp0 is the average land temperature in past times, degrees C used in defining the GEOG variable
    public static final double[] temp0_BERNER={12.4,12.4,12.4,12.5,12.5,12.5,11.6,11.0,11.2,11.3,
        11.5,11.7,11.9,12.0,12.4,12.8,13.1,13.4,13.7,14.0,
        14.2,14.2,14.1,14.1,14.1,14.3,14.2,13.9,13.7,13.5,
        13.3,13.1,13.0,13.0,12.9,13.0,13.1,13.3,13.4,13.5,
        12.5,11.5,10.5,12.5,15.0,17.0,18.2,17.0,16.3,15.5,
        14.5,13.5,13.0,13.0,13.0,12.0,12.0,12.0};

    //for the most recent 140My, the fSR parameter is given every 5My, not 10My
    //fSR is the factor twixt Sr isotopes and silicate weathering
    //71 values
    public static final double[] fSR0_BERNER={1.00,0.98,0.98,0.98,0.98,1.02,1.04,1.11,1.22,1.41,
        1.46,1.41,1.35,1.30,1.35,1.37,1.33,1.30,1.37,1.52,
        1.65,1.76,1.74,1.67,1.63,1.67,1.52,1.26,1.27,1.27,
        1.27,1.21,1.14,1.09,1.10,1.14,1.12,1.10,1.09,1.11,
        1.14,1.10,1.07,1.21,1.21,1.14,1.07,1.07,1.28,1.43,
        1.43,1.40,1.38,1.36,1.38,1.48,1.53,1.55,1.55,1.52,
        1.52,1.50,1.49,1.50,1.52,1.62,1.70,1.59,1.35,1.20,1.04,1.00};

    //Sulfur isotope data (d34S) below are from Kampschulte and Strauss (2004)
    public static final double[] DLS0_BERNER={20.0,21.0,21.0,21.0,21.0,19.0,18.0,17.5,17.8,17.5,
        15.0,15.0,15.3,16.3,16.2,16.0,17.0,17.0,18.0,18.5,
        16.0,16.0,17.5,18.0,22.0,20.0,12.0,12.5,12.5,12.2,
        13.5,14.8,15.5,14.5,15.5,17.0,25.0,24.5,20.0,19.0,
        22.0,27.0,26.5,26.0,26.7,25.5,25.0,24.3,26.0,30.0,
        27.8,35.3,36.4,33.0,30.3,32.0,34.4,35.2};

    //Carbon isotope data (d13C) - data has been smoothed.  see jpark for documentation
    public static final double[] DLC0_BERNER={1.50,1.70,2.00,2.20,2.20,2.20,2.30,2.40,2.40,2.50,
        2.60,2.70,2.50,2.00,1.20,1.70,1.54,1.38,1.22,1.06,
        2.00,2.50,3.50,2.50,2.00,1.00,4.00,4.70,4.40,4.70,
        4.40,4.60,3.20,2.50,2.90,2.80,1.40,1.00,1.00,1.50,
        0.50,2.00,1.50,1.00,0.70,0.00,0.00,-1.0,-1.0,1.00,
        -1.0,-0.5,0.00,0.00,-2.0,0.00,0.00,0.00};
                                    
    public static final double[] DLC0_PROKOPH={2.2,2.42,1.79,0.71,2.6,2.7,2.54,2.37,2.34,2.65,
        2.04,2.69,((2.69+0.0)/2.0)/**Double.NEGATIVE_INFINITY*/,0.00,-0.5,1.36,0.84,1.16,1.16,-0.22,
        1.64,3.48,3.27,1.84,0.73,3.06,4.18,4.72,3.01,3.57,
        3.81,4.39,3.34,2.33,3.03,3.22,1.46,0.64,0.85,0.93,
        0.32,1.79,1.66,2.55,2.05,0.57,-0.18,-1.24,-1.28,((-1.28+-.11)/2.0)/**Double.NEGATIVE_INFINITY*/,
        -0.11,-0.72,-0.89,0,0,0,0,0};

    //al0 - is difference in d13C between coexisting marine orgC and marine carbonate
    public static final double[] al0_BERNER={22.5,25.9,27.5,30.0,29.6,30.5,30.2,28.5,27.8,29.7,
        28.8,30.0,31.5,29.3,30.3,31.0,31.3,34.0,33.7,32.0,
        30.4,30.7,31.0,31.5,32.1,32.6,33.0,33.0,32.9,32.8,
        32.7,32.6,32.6,32.5,32.4,32.1,31.8,31.0,30.3,29.4,
        28.6,28.8,29.0,30.3,31.5,31.0,30.6,29.8,29.0,29.0,
        29.0,29.2,29.4,29.2,29.0,30.0,30.0,30.0};

    //fL is the fraction of land area covered by carbonate rocks, normalized to modern
    public static final double[] fL0_BERNER={1.00,1.00,0.99,1.29,1.10,1.10,1.26,0.88,0.88,0.88,
        1.04,1.04,1.04,1.04,1.04,1.24,1.25,1.25,1.25,1.36,
        1.36,1.23,1.23,1.39,1.36,1.43,1.31,1.31,1.31,1.45,
        1.45,1.45,1.41,1.41,1.41,1.41,1.40,1.47,1.47,1.47,
        1.63,1.63,1.54,1.43,1.43,1.15,1.07,1.07,0.99,0.99,
        0.99,0.97,1.05,1.05,0.63,0.63,0.63,0.63};

    //Sr0 is specified every 5my to 100Ma, prior times is 10My spacing
    //Sr0 is the normalized difference of basalt-predicted and measured Sr isotope ratios (10ma)
    public static final double[] Sr0_BERNER={92.0,90.5,89.5,89.0,85.0,82.5,81.0,79.0,78.0,78.0,
        78.0,78.0,78.5,79.0,78.0,77.0,76.0,75.5,75.0,75.0,
        74.0,73.0,72.0,73.0,71.0,69.0,68.0,69.0,75.0,76.0,
        75.0,78.0,78.0,78.0,75.0,73.0,71.0,77.0,82.0,83.0,
        82.0,82.0,81.0,78.0,75.0,78.0,82.0,82.0,78.0,80.0,
        85.0,87.0,86.0,81.0,79.0,78.0,80.0,82.0,85.0,87.0,
        89.0,89.0,89.0,89.0,90.0,90.0,90.0,90.0};
    
   
    private double[] fA0, fD0, temp0, fSR0, DLS0, DLC0, al0, fL0, Sr0;    
    public GCSVData()
    {
        fA0=fA0_BERNER;
        fD0=fD0_BERNER;
        temp0=temp0_BERNER;
        fSR0=fSR0_BERNER;
        DLS0=DLS0_BERNER;
        DLC0=DLC0_BERNER;
        al0=al0_BERNER;
        fL0=fL0_BERNER;
        Sr0=Sr0_BERNER;
    }
    public GCSVData(double[] newfA0, double[] newfD0, double[] newtemp0, double[] newfSR0,double[] newDLS0,
                    double[] newDLC0, double[] newal0, double[] newfL0, double[] newSr0)
    {
        fA0=newfA0;
        fD0=newfD0;
        temp0=newtemp0;
        fSR0=newfSR0;
        DLS0=newDLS0;
        DLC0=newDLC0;
        al0=newal0;
        fL0=newfL0;
        Sr0=newSr0;
    }
    public boolean equals(Object other)
    {
        if (!(other instanceof GCSVData))
            return false;
        GCSVData o = (GCSVData) other;
        if(!(Util.arrayEquals(this.getFA0(), o.getFA0())))
            return false;
        if(!(Util.arrayEquals(this.getFD0(), o.getFD0())))
            return false;
        if(!(Util.arrayEquals(this.getTemp0(), o.getTemp0())))
            return false;
        if(!(Util.arrayEquals(this.getFSR0(), o.getFSR0())))
            return false;
        if(!(Util.arrayEquals(this.getDLS0(), o.getDLS0())))
            return false;
        if(!(Util.arrayEquals(this.getDLC0(), o.getDLC0())))
            return false;
        if(!(Util.arrayEquals(this.getAl0(), o.getAl0())))
            return false;
        if(!(Util.arrayEquals(this.getFL0(), o.getFL0())))
            return false;
        if(!(Util.arrayEquals(this.getSr0(), o.getSr0())))
            return false;
        return true;
    }
    public int minSize()
    {
        int minlen=9999999;
        double[][]lst=this.getAllArrays();
        for (double[] d: lst)
        {
            minlen=Math.min(minlen, d.length);
        }
        return minlen;
    }
    public double[] fillSr()
    {
        double []Sr = new double[600];
        for(int i=1;i<=20;i++) {
            int jj=(i-1)*5;
            for (int j=0; j<=4;j++) {
                Sr[jj+j]=(j*Sr0[i]+(5-j)*Sr0[i-1])/5.0; } }
        for (int i=21; i<=67; i++) {
            int jj=100+(i-21)*10;
            for (int j=0; j<=9; j++) {
                Sr[jj+j]=(j*Sr0[i]+(10-j)*Sr0[i-1])/10.0; } }
       Sr[570]=Sr0[67];
       return Sr;
    }
    public double[] fillFSr()
    {
        double []fSR = new double[600];
        for(int i=1;i<=28;i++) {
            int jj=1+(i-1)*5;
            for (int j=0; j<=4;j++){
                fSR[jj+j-1]=(j*fSR0[i]+(5-j)*fSR0[i-1])/5.0; } }
        for (int i=29; i<=71; i++) {
            int jj=141+(i-29)*10;
            for (int j=0; j<=9; j++)
                fSR[jj+j-1]=(j*fSR0[i]+(10-j)*fSR0[i-1])/10.0; }
        fSR[570]=fSR0[71];
        return fSR;
    }
    
    
    
    
    
    
    
    
    
    
    public double[] getFA0()
    {
        return fA0;
    }
    public double[] getFD0()
    {
        return fD0;
    }
    public double[] getTemp0()
    {
        return temp0;
    }
    public double[] getFSR0()
    {
        return fSR0;
    }
    public double[] getDLS0()
    {
        return DLS0;
    }
    public double[] getDLC0()
    {
        return DLC0;
    }
    public double[] getAl0()
    {
        return al0;
    }
    public double[] getFL0()
    {
        return fL0;
    }
    public double[] getSr0()
    {
        return Sr0;
    }
    private double[][]getAllArrays()
    {
        double[][] arr = {this.getFA0(), this.getFD0(), this.getTemp0(), this.getFSR0(), this.getDLS0(), this.getDLC0(), this.getAl0(), this.getFL0(), this.getSr0()};
        return arr;
    }
    
    
    public double getFA0(int i)
    {
        return fA0[i];
    }
    public double getFD0(int i)
    {
        return fD0[i];
    }
    public double getTemp0(int i)
    {
        return temp0[i];
    }
    public double getFSR0(int i)
    {
        return fSR0[i];
    }
    public double getDLS0(int i)
    {
        return DLS0[i];
    }
    public double getDLC0(int i)
    {
        return DLC0[i];
    }
    public double getAl0(int i)
    {
        return al0[i];
    }
    public double getFL0(int i)
    {
        return fL0[i];
    }
    public double getSr0(int i)
    {
        return Sr0[i];
    }
    
    
    
    public void setFA0(double[] newValue)
    {
        fA0=newValue;
    }
    public void setFD0(double[] newValue)
    {
        fD0=newValue;
    }
    public void setTemp0(double[] newValue)
    {
        temp0=newValue;
    }
    public void setFSR0(double[] newValue)
    {
        fSR0=newValue;
    }
    public void setDLS0(double[] newValue)
    {
        DLS0=newValue;
    }
    public void setDLC0(double[] newValue)
    {
        DLC0=newValue;
    }
    public void setAl0(double[] newValue)
    {
        al0=newValue;
    }
    public void setFL0(double[] newValue)
    {
        fL0=newValue;
    }
    public void setSr0(double[] newValue)
    {
        Sr0=newValue;
    }
}