import factorinput.*;
import java.io.File;

import jxl.*; 
import jxl.write.*;
/**
 * Write a description of class DistributionTests here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class DistributionTests
{
    /**
     * Constructor for objects of class DistributionTests
     */
    public static void main(String[] args)
    {
        String[] label = {"deltaT", "ACT", "FERT", "LIFE", "GYM", "GLAC"};
        VariedFactor gens[]  = {
            new LognormalFactor(Math.log(3), Math.log(1.5)), new GaussianFactor(.03,.13,2), 
            new GaussianFactor(.2,.8,2), new GaussianFactor(.125,.5,2), 
            new GaussianFactor(.5,1.2,2), new GaussianFactor(1.5,2.5,1)};
        gens[gens.length-1].setMax(3.0); gens[gens.length-1].setMin(1.0);
        
        double[][] all = new double[6][10000];
        double[][] in = new double[6][10000];
        double[][] out = new double[6][10000];
        
        for (int i=0; i<label.length; i++)
        {
            gens[i].toggleBounds(false);
            all[i] = gens[i].getNexts(10000);
            in[i] = gens[i].getValueLimit().getInlist(all[i]);
            out[i] = gens[i].getValueLimit().getOutlist(all[i]);
        }
        try{
            WritableWorkbook w = Workbook.createWorkbook(new File("distributions.xls"));
            WritableSheet s = w.createSheet("dist", 0);
            for (int i=0; i<label.length; i++)
            {
                Label l1 = new Label(i*3, 0, label[i]+" all");
                Label l2 = new Label(i*3+1, 0, label[i]+" in");
                Label l3 = new Label(i*3+2, 0, label[i]+" out");
                s.addCell(l1); s.addCell(l2); s.addCell(l3);
            }
            
            for (int i=0; i<label.length; i++)
            {
                for (int j=0; j<all[i].length; j++)
                {
                    jxl.write.Number n = new jxl.write.Number(i*3,j+1,all[i][j]);
                    s.addCell(n);
                }
                for (int j=0; j<in[i].length; j++)
                {
                    jxl.write.Number n1 = new jxl.write.Number(i*3+1,j+1,in[i][j]);
                    s.addCell(n1);
                }
                for (int j=0; j<out[i].length; j++)
                {
                    jxl.write.Number n2 = new jxl.write.Number(i*3+2,j+1,out[i][j]);
                    s.addCell(n2);
                }
            }
            w.write();
            w.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
                        
                        
                
        
    }
}
