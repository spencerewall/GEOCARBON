import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.DecimalFormat;
//File;

public class DataWriter
{
    BufferedWriter writeTo;
    String[] collumnTitles;
    boolean format;
    DecimalFormat myFormat;
    
    public DataWriter(String fileName)
    {
        try
        {
            FileWriter fstream = new FileWriter(fileName);
            writeTo = new BufferedWriter(fstream);
            //writeTo.write("#This is "+fileName);
            //writeTo.newLine();
            //writeTo.flush();
        }
        catch (java.io.IOException e)
        {
            System.out.println("Error in DataWriter[constructor]: "+e.getMessage());
            System.exit(0);
        }
    }
    public DataWriter(String fileName, String[] columnTitles)
    {
        try
        {
            FileWriter fstream = new FileWriter(fileName);
            writeTo = new BufferedWriter(fstream);
            //writeTo.write("#This is "+fileName);
            //writeTo.newLine();
            String cols = ""; int i = 0;
            /**for (String col:columnTitles)
                cols+=col+"\t";*/
            //writeTo.write(cols);
            //writeTo.newLine();
            //writeTo.flush();
        }
        catch (java.io.IOException e)
        {
            System.out.println("Error in DataWriter[constructor]: "+e.getMessage());
            System.exit(0);
        }
    }
    public DataWriter(String fileName, String[] columnTitles, String datFormat)
    {
        try
        {
            myFormat = new DecimalFormat(datFormat);
            FileWriter fstream = new FileWriter(fileName);
            writeTo = new BufferedWriter(fstream);
            myFormat=new DecimalFormat(datFormat);
            //writeTo.write("#This is "+fileName);
            //writeTo.newLine();
            //String cols = ""; int i = 0;
            //for (String col:columnTitles)
            //    cols+=col+"\t";
            //writeTo.write(cols);
            //writeTo.newLine();
            //writeTo.flush();
        }
        catch (java.io.IOException e)
        {
            System.out.println("Error in DataWriter[constructor]: "+e.getMessage());
            System.exit(0);
        }
    }
    public String writeLine(int[] data)
    {
        String line = "";
        for (int dat:data)
        {
            line+=""+dat+"\t";
        }
        try
        {
            writeTo.write(line);
            writeTo.newLine();
            writeTo.flush();
        }
        catch(Exception e)
        {
            System.out.println("Error in DataWriter.writeLine(int): "+e.getMessage());
            System.exit(0);
        }
        return line;
    }
    public String writeLine(double[] data)
    {
        String line = "";
        for (double dat:data)
        {
            line+=""+dat+"\t";
        }
        try
        {
            writeTo.write(line);
            writeTo.newLine();
            writeTo.flush();
        }
        catch(Exception e)
        {
            System.out.println("Error in DataWriter.writeLine(double[]): "+e.getMessage());
            System.exit(0);
        }
        return line;
    }
    public String writeLine(String data)
    {
        try
        {
            writeTo.write(data);
            writeTo.newLine();
            writeTo.flush();
        }
        catch(Exception e)
        {
            System.out.println("Error in DataWriter.writeLine(String): "+e.getMessage());
            System.exit(0);
        }
        return data;
    }
    public void newLine()
    {
        try{ writeTo.newLine(); writeTo.flush(); }
        catch(Exception e)
        { System.out.println("Error in DataWriter.newLine(): "+e.getMessage()); System.exit(0); }
    }
    public String writeToLine(double[] data)
    {
        String line = "";
        for (double dat:data)
        {
            line+=""+dat+"\t";
        }
        try
        {
            writeTo.write(line);
            writeTo.flush();
        }
        catch(Exception e)
        {
            System.out.println("Error in DataWriter.writeToLine(double[]): "+e.getMessage());
            System.exit(0);
        }
        return line;
    }
    public void applyPattern(String pattern)
    {
        myFormat=new DecimalFormat(pattern);
    }
    public String writeFormattedLine(int[] data)
    {
        double[] doubleData = new double[data.length];
        for (int i=0; i<data.length; i++)
        {
            doubleData[i]=(double) data[i];
        }
        return writeFormattedLine(doubleData);
    }
    public String writeFormattedLine(double[] data)
    {
        String line = "";
        for (double dat:data)
        {
            line+=""+myFormat.format(dat)+"\t";
        }
        try
        {
            writeTo.write(line);
            writeTo.newLine();
            writeTo.flush();
        }
        catch(Exception e)
        {
            System.out.println("Error in DataWriter.writeFormattedLine(double[]): "+e.getMessage());
            System.exit(0);
        }
        return line;
    }
    public String writeFormattedLine(int[] data, String pattern)
    {
        double[] doubleData = new double[data.length];
        for (int i=0; i<data.length; i++)
        {
            doubleData[i]=(double) data[i];
        }
        return writeFormattedLine(doubleData, pattern);
    }
    public String writeFormattedLine(double[] data, String pattern)
    {
        DecimalFormat tempFormat = new DecimalFormat(pattern);
        String line = "";
        for (double dat:data)
        {
            line+=""+tempFormat.format(dat)+"\t";
        }
        try
        {
            writeTo.write(line);
            writeTo.newLine();
            writeTo.flush();
        }
        catch(Exception e)
        {
            System.out.println("Error in DataWriter.writeFormattedLine(double[],String): "+e.getMessage());
            System.exit(0);
        }
        return line;
    }
    public String writeFormattedLine(int[] data, String[] pattern)
    {
        double[] doubleData = new double[data.length];
        for (int i=0; i<data.length; i++)
        {
            doubleData[i]=(double) data[i];
        }
        return writeFormattedLine(doubleData, pattern);
    }
    public String writeFormattedLine(double[] data, String[] pattern)
    {
        DecimalFormat tempFormat;
        String line = "";
        for (int i=0; i<data.length;i++)
        {
            tempFormat = new DecimalFormat(pattern[i]);
            line+=""+tempFormat.format(data[i])+"\t";
        }

        try
        {
            writeTo.write(line);
            writeTo.newLine();
            writeTo.flush();
        }
        catch(Exception e)
        {
            System.out.println("Error in DataWriter.writeFormattedLine(double[],String[]): "+e.getMessage());
            System.exit(0);
        }
        return line;
    }
    public void close()
    {
        try{
            writeTo.close();
        }
        catch(Exception e)
        {
            System.out.println("Error in DataWriter.close(): "+e.getMessage());
            System.exit(0);
        }
    }
}
