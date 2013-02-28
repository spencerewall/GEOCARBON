package utilities;
import factorinput.GaussianFactor;
import factorinput.LognormalFactor;
import java.io.File; 
import java.util.Date; 

import jxl.*; 
import jxl.write.*; 


public class PrimitiveGeneratorTest
{
    public static void main(String[] args)
    {
        GaussianFactor ACTBell = new GaussianFactor(.03,.13,2);
        GaussianFactor FERTBell = new GaussianFactor(.2,.8,2);
        GaussianFactor LIFEBell = new GaussianFactor(.125,.5,2);
        GaussianFactor GYMBell = new GaussianFactor(.5,1.2,2);
        GaussianFactor GLACBell = new GaussianFactor(1.5,2.5,1); GLACBell.setMax(3.0); GLACBell.setMin(1.0);
        LognormalFactor dTln = new LognormalFactor(Math.log(3), Math.log(1.5));
        dTln.setMin(1.5);
        
        double[] act =  ACTBell.getNexts(50000);
        double[] fert = FERTBell.getNexts(50000);
        double[] life = LIFEBell.getNexts(50000);
        double[] gym = GYMBell.getNexts(50000);
        double[] glac = GYMBell.getNexts(50000);
        
        //double[] delT = utilities.Arrays.roundDecimal(deltaTFactor.getValueList(50000, 1.5, 6.35), 4);
        double[] delT = dTln.getNexts(50000);
        double[][] M = matCat(delT, act, fert, life, gym, glac);
        printToTxt(M);
        
    }
    public static double[][] matCat(double[]... args) {
        return args;
    }

    public static void printToExcel(double[][] m) {
        try
        {
            WritableWorkbook w = Workbook.createWorkbook(new File("sv generator.xls"));
            WritableSheet s = w.createSheet("First Sheet", 0);
            for (int i=0;i<3;i++) {
                Label l1 = new Label(0, 0, "delT");
                s.addCell(l1);
                Label l2 = new Label(0, 0, "ACT");
                s.addCell(l2);
                Label l3 = new Label(0, 0, "FERT");
                s.addCell(l3);
                Label l4 = new Label(0, 0, "LIFE");
                s.addCell(l4);
                Label l5 = new Label(0, 0, "GYM");
                s.addCell(l5);
                Label l6 = new Label(0, 0, "GLAC");
                s.addCell(l6);
            }
            
            for (int i=1; i<m.length; i++) {
                for (int j=0; j<m[1].length; j++) {
                    jxl.write.Number number = new jxl.write.Number(3, 4, 3.1459); 
                    s.addCell(number);
                }
            }
            
            
        }
        catch(java.io.IOException e)
        {
            System.out.println("IOException: " + e.getMessage());
        }
        catch(jxl.write.WriteException e)
        {
            System.out.println("IOException: " + e.getMessage());
        }
        finally
        {
            System.exit(0);
        }
    }
    public static void printToTxt(double[][] M)
    {
        try
        {
            java.io.PrintWriter output = new java.io.PrintWriter(new java.io.FileWriter("tests/Singles.dat"));
            output.println("delT\tact\tfert\tlife\tgym\tglac");
            for (int i=0; i<50000; i++)
            {
                output.println(M[0][1]+"\t"+M[1][i]+"\t"+M[2][i]+"\t"+M[3][i]+"\t"+M[4][i]+"\t"+M[5][i]);
            }
            output.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
    
}
