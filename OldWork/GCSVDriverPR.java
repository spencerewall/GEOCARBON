package OldWork;

/**
 * Write a description of class ParkRoyerGCSVDrier here.
 * 
 * @author (Spencer Ewall) 
 */
public class GCSVDriverPR
{
    public static void main(String[] args)
    {
        int ndel=28;
        double deltaT=.28;
        double GYM=0, LIFE=0, ACT=0, FERT=0, GLAC=0;
        GCSVData defaultDat = new GCSVData();
        RunList r = new RunList(280000);
        for (int iiit=0; iiit<ndel; iiit++)
        {
            deltaT = deltaT+0.12*(1.0+4.0*((float) (iiit))/((float) (ndel-1)));
            
            System.out.println("GYM\tLIFE\tACT\tFERT\tdeltaT");
            for (int k1=10; k1>=1; k1--)
            {
                ACT=0.03+(k1-1)*0.10/9.0;
                for(int k2=10; k2>=1; k2--)
                {
                    // limit plant response to CO2 enrichment to range of 20% to 80% efficiency
                    // in this loop (over counter k2) we place 
                    // the alternate variable GLAC for glacial amplification of climate 
                    // also included a test loopp on the variable fob0
                    // for these alternate loops, FERT=0.5 -- because the standard computations showed that
                    // this seemed a most-probable value, with weak dependence on deviations from FERT=0.5
                    FERT=0.2+(k2-1)*0.2/3.0;
                    GLAC=1.0;
                    for (int k3=10;k3>=1;k3--)
                    {
                        // assume vascular-plant acceleration of weathering lies between factor of 2 and 10
                        LIFE=0.1+(k3-1)*0.4/9.0;
                        for (int k4=10; k4>=1; k4--)
                        {
                            // assume gymnosperms accelerate weathering by 0.5 to 1.2, relative to angiosperms
                            GYM=0.5+(k4-1)*0.7/9.0;
                    
                            //System.out.print(c+"\t");
                            //if (c%10==9)
                            //    System.out.println();
                            FactorData thisRun = new FactorData(deltaT, ACT, FERT, LIFE, GYM, GLAC, defaultDat);
                            r.add(thisRun);
                        }// end do
                        // sift through the runs to find acceptable data fits  (chiquare <= NDAT; bias < 0.3)
                        // write to disk
                    }
                }
            }
        }
        System.out.println("Done calculating.  Begin write.");
        
        //Util.writeRuns("output.dat", r);
    }

}
