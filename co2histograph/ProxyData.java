package co2histograph;
import java.util.Scanner;
import java.io.File;
/**
 * The class Proxy_Data interacts with a file containing proxy data over the given time interval
 * 
 * @author Spencer Ewall 
 */
public class ProxyData implements HistData
{
    public static final String proxy2010="co2proxy_2010.dat";
    public static final String proxy2007="co2proxy_2007.dat";
    
    private String file_name;
    private float[][] proxy_data;
    private int lines;

    /**
     * Constructor for objects of class Proxy_Scanner
     */
    public ProxyData()
    {
        file_name=proxy2010;
        lines=countLines()-1;
        proxy_data=new float[4][lines];
        proxy_data=file2Array(proxy2010);
    }
    
    public ProxyData(String filePath)
    {
        file_name=filePath;
        lines=countLines()-1;
        proxy_data=file2Array(filePath);
    }
    public boolean equals(Object other)
    {
        if (this==other)
            return true;
        if (!(other instanceof ProxyData))
            return false;
        ProxyData o = (ProxyData) other;
        return this.getProxyArray().equals(o.getProxyArray());
    }
    public int hashCode()
    {
        return java.util.Arrays.hashCode(this.getProxyArray());
    }
    public float[][] getProxyArray()
    {
        return proxy_data;
    }
    public float getAge(int i)
    { 
        return proxy_data[0][i]; 
    }
    public float getCO2(int i)
    {
        return proxy_data[1][i];
    }
    public float[] getCO2()
    {
        return proxy_data[1];
    }
    public float getPLo(int i)
    { 
        return proxy_data[2][i]; 
    }
    public float[] getAllPLo(int i)
    { 
        return proxy_data[2]; 
    }
    public float getPHi(int i)
    { 
        return proxy_data[3][i]; 
    }
    public float[] getAllPHi(int i)
    { 
        return proxy_data[3]; 
    }
    public String getFileName()
    {
        return file_name;
    }
    public int size()
    {
        return lines;
    }
    
    
    
    
   /**
    * private methods.  These aid in the use of the structure, mostly in construction.
    */
    private float[][] file2Array(String fileName)
    {
        float[][] data=new float[4][lines];
        try {
            Scanner arrConverter=new Scanner(new File(fileName));
            
            int line=0;
            arrConverter.nextLine();
            while (arrConverter.hasNextLine())
            {
                int row=0;
                while (row<4 && arrConverter.hasNextFloat())
                {
                    data[row][line] = arrConverter.nextFloat();
                    row++;
                }
                line++;
            }
        }
        catch (java.io.FileNotFoundException e) {
            System.err.println("FileNotFoundException in array conversion"+e);
        }
        return data;
    }
    private int countLines()
    {
        int count=0;
        try {
            Scanner lineCounter=new Scanner(new File(file_name));
            while (lineCounter.hasNextLine())
            {
                lineCounter.nextLine();
                count++;
            }
        }
        catch (java.io.FileNotFoundException e) {
            System.err.println("FileNotFoundException "+e);
        }
        return count;
    }
    
    
    
    
    
    
    public GCDataError[] getErrors(){
        return new GCDataError[0];
    }
    public int countErrors(){
        return -1;
    }
    public float getErrorThreshold(){
        return -1;
    }
    public void setErrorThreshold(float errorThreshold){}
}
