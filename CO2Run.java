import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public interface CO2Run
{
    public double getCO2(int i);
    public double[] getAllCO2();
    public int size();
    public boolean equals(Object other);
    public abstract int hashCode();
}