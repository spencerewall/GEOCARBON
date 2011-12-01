import java.util.Arrays;
import java.util.Random;
import factorInput.GaussianFactor;

public class GCSVRandomData extends GCSVData
{
    public static final double[] DLC0Dev_PROKOPH={0.692, 0.903, 1.083, 0.370, 0.930, 1.245, 1.340, 0.353, 0.514, 0.861, 
        0.673, 0.596, 0/**Double.NEGATIVE_INFINITY*/, 0.738, 1.037, 0/**Double.NEGATIVE_INFINITY*/, 0.874, 0.877, 0.802, 1.037,
        0.522, 0/**Double.NEGATIVE_INFINITY*/, 0.546, 1.069, 0.684, 1.360, 0.939, 1.835, 1.352, 1.658,
        1.031, 1.448, 1.732, 1.514, 1.468, 1.573, 1.485, 0.750, 1.314, 1.354,
        0.936, 0.942, 2.277, 1.773, 2.196, 0.733, 0.753, 0.909, 0.688, 0/**Double.NEGATIVE_INFINITY*/, 0.307, 0/**Double.NEGATIVE_INFINITY*/, 1.070};
    
    Random r;
    GaussianFactor g;
    
    private double[] fA0Dev, fD0Dev, temp0Dev, fSR0Dev, DLS0Dev, DLC0Dev, al0Dev, fL0Dev, sr0Dev;
    
    public GCSVRandomData()
    {
        super();
        r=new Random();
        g=new GaussianFactor();
        
        double[] zeros = new double[58];
        Arrays.fill(zeros, 0);
        
        fA0Dev=Arrays.copyOf(zeros, super.fA0_BERNER.length);
        fD0Dev=Arrays.copyOf(zeros, super.fD0_BERNER.length);;
        temp0Dev=Arrays.copyOf(zeros, super.temp0_BERNER.length);;
        fSR0Dev=Arrays.copyOf(zeros, super.fSR0_BERNER.length);;
        DLS0Dev=Arrays.copyOf(zeros, super.DLS0_BERNER.length);;
        DLC0Dev=Arrays.copyOf(zeros, super.DLC0_BERNER.length);;
        al0Dev=Arrays.copyOf(zeros, super.al0_BERNER.length);;
        fL0Dev=Arrays.copyOf(zeros, super.fL0_BERNER.length);
        sr0Dev=Arrays.copyOf(zeros, super.Sr0_BERNER.length);
    }
    public GCSVRandomData(double[] newfA0, double[] newfD0, double[] newtemp0, double[] newfSR0,double[] newDLS0, 
                            double[] newDLC0, double[] newal0, double[] newfL0, double[] newSr0, 
                            double[] newfA0Dev, double[] newfD0Dev, double[] newtemp0Dev, double[] newfSR0Dev,double[] newDLS0Dev,
                            double[] newDLC0Dev, double[] newal0Dev, double[] newfL0Dev, double[] newSr0Dev)
    {
        super();
        r=new Random();
        g=new GaussianFactor();
        
        fA0Dev=newfA0Dev;
        fD0Dev=newfD0Dev;
        temp0Dev=newtemp0Dev;
        fSR0Dev=newfSR0Dev;
        DLS0Dev=newDLS0Dev;
        DLC0Dev=newDLC0Dev;
        al0Dev=newal0Dev;
        fL0Dev=newfL0Dev;
        sr0Dev=newSr0Dev;
    }
    public GCSVRandomData(double[] newfA0, double[] newfD0, double[] newtemp0, double[] newfSR0,double[] newDLS0, 
                            double[] newDLC0, double[] newal0, double[] newfL0, double[] newSr0, 
                            double newfA0Dev, double newfD0Dev, double newtemp0Dev, double newfSR0Dev,double newDLS0Dev,
                            double newDLC0Dev, double newal0Dev, double newfL0Dev, double newSr0Dev)
    {
        super();
        r=new Random();
        g=new GaussianFactor();
        
        fA0Dev=new double[newfA0.length];
        fD0Dev=new double[newfD0.length];
        temp0Dev=new double[newtemp0.length];
        fSR0Dev=new double[newfSR0.length];
        DLS0Dev=new double[newDLS0.length];
        DLC0Dev=new double[newDLC0.length];
        al0Dev=new double[newal0.length];
        fL0Dev=new double[newfL0.length];
        sr0Dev=new double[newSr0.length];
        
        Arrays.fill(fA0Dev,newfA0Dev);
        Arrays.fill(fD0Dev,newfD0Dev);
        Arrays.fill(temp0Dev,newtemp0Dev);
        Arrays.fill(fSR0Dev,newfSR0Dev);
        Arrays.fill(DLS0Dev,newDLS0Dev);
        Arrays.fill(DLC0Dev,newDLC0Dev);
        Arrays.fill(al0Dev,newal0Dev);
        Arrays.fill(fL0Dev,newfL0Dev);
        Arrays.fill(sr0Dev,newSr0Dev);
    }
    public boolean equals(Object other)
    {
        return false;
    }
    public double getFA0(int i)
    {
        g.setMean(super.getFA0(i));
        g.setStandardDeviation(fA0Dev[i]);
        return g.getNextValue();
    }
    public double getFD0(int i)
    {
        g.setMean(super.getFD0(i));
        g.setStandardDeviation(fD0Dev[i]);
        return g.getNextValue();
    }
    public double getTemp0(int i)
    {
        g.setMean(super.getTemp0(i));
        g.setStandardDeviation(temp0Dev[i]);
        return g.getNextValue();
    }
    public double getFSR0(int i)
    {
        g.setMean(super.getFSR0(i));
        g.setStandardDeviation(fSR0Dev[i]);
        return g.getNextValue();
    }
    public double getDLS0(int i)
    {
        g.setMean(super.getDLS0(i));
        g.setStandardDeviation(DLS0Dev[i]);
        return g.getNextValue();
    }
    public double getDLC0(int i)
    {
        g.setMean(super.getDLC0(i));
        g.setStandardDeviation(DLC0Dev[i]);
        return g.getNextValue();
    }
    public double getAl0(int i)
    {
        g.setMean(super.getAl0(i));
        g.setStandardDeviation(al0Dev[i]);
        return g.getNextValue();
    }
    public double getFL0(int i)
    {
        g.setMean(super.getFL0(i));
        g.setStandardDeviation(fL0Dev[i]);
        return g.getNextValue();
    }
    public double getSr0(int i)
    {
        g.setMean(super.getSr0(i));
        g.setStandardDeviation(sr0Dev[i]);
        return g.getNextValue();
    }
    
    
    
    
    
    public void setFA0(double[] val, double[] dev)
    {
        super.setFA0(val);
        fA0Dev=dev;
    }
    public void setFD0(double[] val, double[] dev)
    {
        super.setFD0(val);
        fD0Dev=dev;
    }
    public void setTemp0(double[] val, double[] dev)
    {
        super.setTemp0(val);
        temp0Dev=dev;
    }
    public void setFSR0(double[] val, double[] dev)
    {
        super.setFSR0(val);
        fSR0Dev=dev;
    }
    public void setDLS0(double[] val, double[] dev)
    {
        super.setDLS0(val);
        DLS0Dev=dev;
    }
    public void setDLC0(double[] val, double[] dev)
    {
        super.setDLC0(val);
        DLC0Dev=dev;
    }
    public void setAl0(double[] val, double[] dev)
    {
        super.setAl0(val);
        al0Dev=dev;
    }
    public void setFL0(double[] val, double[] dev)
    {
        super.setFL0(val);
        fL0Dev=dev;
    }
    public void setSr0(double[] val, double[] dev)
    {
        super.setSr0(val);
        sr0Dev=dev;
    }
}
