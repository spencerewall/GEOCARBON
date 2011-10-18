import java.util.Scanner;
import java.io.File;
/**
 * The class Proxy_Data interacts with a file containing proxy data over the given time interval
 * 
 * @author Spencer Ewall 
 */
public class Proxy_Data
{
    public static final String proxy2010="co2proxy_2010.dat";
    public static final String proxy2007="co2proxy_2007.dat";
    
    private String file_name;
    private int lines;
    private double[][] proxy_data;

    /**
     * Constructor for objects of class Proxy_Scanner
     */
    public Proxy_Data()
    {
        file_name=proxy2010;
        lines=countLines()-1;
        proxy_data=file2Array(proxy2010);
    }
    
    public Proxy_Data(String filePath)
    {
        file_name=filePath;
        lines=countLines()-1;
        proxy_data=file2Array(filePath);
    }
    
    public double[][] getProxyArray()
    {
        return proxy_data;
    }
    public double getAge(int index)
    { 
        return proxy_data[0][index]; 
    }
    public double getPPM(int index)
    { 
        return proxy_data[1][index];
    }
    public double getPLo(int index)
    { 
        return proxy_data[2][index]; 
    }
    public double getPHi(int index)
    { 
        return proxy_data[3][index]; 
    }
    
    public int getLines()
    {
        return lines;
    }
    public String getFileName()
    {
        return file_name;
    }
    
    
    
    
   /**
    * private methods.  These aid in the use of the structure, mostly in construction.
    */
    private double[][] file2Array(String fileName)
    {
        double[][] data=new double[4][lines];
        try {
            Scanner arrConverter=new Scanner(new File(fileName));
            int line=0;
            arrConverter.nextLine();
            while (line<lines && arrConverter.hasNextLine())
            {
                int row=0;
                while (row<4 && arrConverter.hasNextDouble())
                {
                    data[row][line] = arrConverter.nextDouble();
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
}
