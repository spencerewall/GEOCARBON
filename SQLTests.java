import factorinput.*;

public class SQLTests
{
    public static GaussianFactor actD = new GaussianFactor(0.03,0.13,2, "act");
    public static GaussianFactor fertD = new GaussianFactor(0.2,0.8,2, "fert");
    public static GaussianFactor lifeD = new GaussianFactor(0.125,0.5,2, "life");
    public static GaussianFactor gymD = new GaussianFactor(0.5,1.2,2, "gym");
    public static GaussianFactor glacD = new GaussianFactor(2.0, 0.5, 1.0, Double.POSITIVE_INFINITY, "glac");
    public static LognormalFactor dtD = (new LognormalFactor(Math.log(3), Math.log(1.5), 1.5, Double.POSITIVE_INFINITY, "deltaT"));
    
    public static GaussianFactor nvD = new GaussianFactor(0.0075, 0.00375, 0.0, Double.POSITIVE_INFINITY, "nv");
    public static GaussianFactor vnvD = new GaussianFactor(5.0, 1.0, 2.0, 10.0, "vnv");
    public static GaussianFactor fb0D = new GaussianFactor(4.0, 1.0, 0.75, Double.POSITIVE_INFINITY, "fb0");
    
    // PL is Berner's adjustable empirical parameter L for Sr-ratio vs. silcate weathering
    // PL=2 obtains approximate fit between Sr-isotopes and physical erosion estimates 
    // from siliclastic sediments, see Berner (2004)
    public static GaussianFactor plD=new GaussianFactor(2.0, 0.5, 0.0, Double.POSITIVE_INFINITY, "pl");
    public static GaussianFactor gasD = new GaussianFactor(.75, .1875, 0.0, Double.POSITIVE_INFINITY, "gas");
    
    
    //ScrapData is hardwired with Spencer's preferred distributions for the array factors
    public static ScrapData aIn = new ScrapData();
    public static GaussianArray fa, fd, fl, dls, dlc;
    
    
    public static void doAll()
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
        sql.includeDistribution(actD, 1);
        sql.includeDistribution(fertD, 2);
        sql.includeDistribution(lifeD, 3);
        sql.includeDistribution(gymD, 4);
        sql.includeDistribution(glacD, 5);
        sql.includeDistribution(dtD, 6);
        sql.includeDistribution(nvD, 7);
        sql.includeDistribution(vnvD, 8);
        sql.includeDistribution(fb0D, 9);
        System.out.println("Distributions Written");

        sql.setBufferLimit(2000);

        //default run (no variance)
        ExperimentData e = new ExperimentData(actD, fertD, lifeD, gymD, glacD, dtD,
            nvD, vnvD, fb0D, plD, gasD,
            fa, fd, fl, dls, dlc);
        FactorData currRun = new FactorData(dtD.getCenter(),
                                    actD.getCenter(),
                                    fertD.getCenter(),
                                    lifeD.getCenter(),
                                    gymD.getCenter(),
                                    glacD.getCenter(),
                                    nvD.getCenter(),
                                    vnvD.getCenter(),
                                    fb0D.getCenter(),
                                    plD.getCenter(),
                                    gasD.getCenter(),
                                    aIn);
                      
           
        //constant variables varied
        e.setActive(true, false, false, false, false, false, false, false, false, false, false, false, false, false);

        //ACT
        sql.newExperiment(e, "act", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);

            currRun = new FactorData(dtD.getCenter(), actD.getNext(), fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(), glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            
            sql.includeRun(currRun);
        }
        
        //FERT
        e.setActive(false, true, false, false, false, false, false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "fert", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),   fertD.getNext(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
        //LIFE
        e.setActive(false, false, true, false, false, false, false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "life", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),   fertD.getCenter(),
                                     lifeD.getNext(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
        //GYM
        e.setActive(false, false, false, true, false, false, false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "gym", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getNext(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
        //GLAC
        e.setActive(false, false, false, false, true, false, false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "glac", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getNext(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
        //DELTAT
        e.setActive(false, false, false, false, false, true, false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "deltat", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getNext(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
        //NV
        e.setActive(false, false, false, false, false, false, 
        false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "nv", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getNext(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
        
        //VNV
        e.setActive(false, false, false, false, false, false, false, true, false, false, false, false, false, false);
        sql.newExperiment(e, "vnv", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getNext(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
        //fb0
        e.setActive(false, false, false, false, false, false, false, false, true, false, false, false, false, false);
        sql.newExperiment(e, "fb0", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getNext(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
        //GAS
        e.setActive(false, false, false, false, false, false, false, false, true, false, false, false, false, false);
        sql.newExperiment(e, "gas", "");
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getNext(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
        
        e.setActive(false, false, false, false, false, false, false, false, false, true, false, false, false, false);
        sql.newExperiment(e, "fA", "");
        //fA
        aIn.toggleFA0Variance(true);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
        
        e.setActive(false, false, false, false, false, false, false, false, false, false, true, false, false, false);
        sql.newExperiment(e, "fD", "");
        //fD
        aIn.toggleFD0Variance(true);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
        
        //fL
        e.setActive(false, false, false, false, false, false, false, false, false, false, false, true, false, false);
        sql.newExperiment(e, "fL", "");
        aIn.toggleFL0Variance(true);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
        //DLC
        e.setActive(false, false, false, false, false, false, false, false, false, false, false, false, true, false);
        sql.newExperiment(e, "dlc", "");
        aIn.toggleAllVariance(false);
        aIn.toggleDLC0Variance(true);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
        
        
        //fSR
        e.setActive(false, false, false, false, false, false, false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "fsr", "");
        aIn.toggleAllVariance(false);
        aIn.toggleFSR0Variance(true);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
        
        //Sr
        e.setActive(false, false, false, false, false, false, false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "sr", "");
        aIn.toggleAllVariance(false);
        aIn.toggleSr0Variance(true);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
        
        //temp
        e.setActive(false, false, false, false, false, false, false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "temp", "");
        aIn.toggleAllVariance(false);
        aIn.toggleTemp0Variance(true);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
        
        //All
        e.setActive(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
        sql.newExperiment(e, "all", "");
        aIn.toggleAllVariance(true);
        aIn.toggleDLS0Variance(false);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getNext(),   actD.getNext(),     fertD.getNext(),
                                     lifeD.getNext(), gymD.getNext(),   glacD.getNext(),
                                     nvD.getNext(), vnvD.getNext(), fb0D.getNext(), plD.getNext(), gasD.getNext(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
        
        sql.close();
    }
    
    public static void doDLSTests(int exp_id)
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
        
        ExperimentData e = new ExperimentData(actD, fertD, lifeD, gymD, glacD, dtD,
            nvD, vnvD, fb0D, plD, gasD,
            fa, fd, fl, dls, dlc);
        FactorData currRun = new FactorData(dtD.getCenter(),
                                    actD.getCenter(),
                                    fertD.getCenter(),
                                    lifeD.getCenter(),
                                    gymD.getCenter(),
                                    glacD.getCenter(),
                                    nvD.getCenter(),
                                    vnvD.getCenter(),
                                    fb0D.getCenter(),
                                    plD.getCenter(),
                                    gasD.getCenter(),
                                    aIn);
                                    
        aIn.toggleAllVariance(false);
                                    
        //template ends
        //dls begins
        
        //DLS
        e.setActive(false, false, false, false, false, false, false, false, false, false, false, false, false, true);
        sql.newExperiment(e, "dls", "", exp_id);
        aIn.toggleDLS0Variance(true);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        
           
        //constant variables varied
    }
    public static void doNVTests(int exp_id)
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
        
        ExperimentData e = new ExperimentData(actD, fertD, lifeD, gymD, glacD, dtD,
            nvD, vnvD, fb0D, plD, gasD,
            fa, fd, fl, dls, dlc);
        FactorData currRun = new FactorData(dtD.getCenter(),
                                    actD.getCenter(),
                                    fertD.getCenter(),
                                    lifeD.getCenter(),
                                    gymD.getCenter(),
                                    glacD.getCenter(),
                                    nvD.getCenter(),
                                    vnvD.getCenter(),
                                    fb0D.getCenter(),
                                    plD.getCenter(),
                                    gasD.getCenter(),
                                    aIn);
                                    
        aIn.toggleAllVariance(false);
        
        //NV
        e.setActive(false, false, false, false, false, false, 
        false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "nv", "", exp_id);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getNext(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
    }
    
    public static void doFB0Tests(int exp_id)
    {
        
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
        //sql.setBufferLimit(100);
        
        ExperimentData e = new ExperimentData(actD, fertD, lifeD, gymD, glacD, dtD,
            nvD, vnvD, fb0D, plD, gasD,
            fa, fd, fl, dls, dlc);
        FactorData currRun = new FactorData(dtD.getCenter(),
                                    actD.getCenter(),
                                    fertD.getCenter(),
                                    lifeD.getCenter(),
                                    gymD.getCenter(),
                                    glacD.getCenter(),
                                    nvD.getCenter(),
                                    vnvD.getCenter(),
                                    fb0D.getCenter(),
                                    plD.getCenter(),
                                    gasD.getCenter(),
                                    aIn);
                                    
        aIn.toggleAllVariance(false);
        //fb0
        e.setActive(false, false, false, false, false, false, false, false, true, false, false, false, false, false);
        sql.newExperiment(e, "fb0", "", exp_id);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getNext(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
    }
    
    public static void doGASTests(int exp_id)
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
        
        ExperimentData e = new ExperimentData(actD, fertD, lifeD, gymD, glacD, dtD,
            nvD, vnvD, fb0D, plD, gasD,
            fa, fd, fl, dls, dlc);
        FactorData currRun = new FactorData(dtD.getCenter(),
                                    actD.getCenter(),
                                    fertD.getCenter(),
                                    lifeD.getCenter(),
                                    gymD.getCenter(),
                                    glacD.getCenter(),
                                    nvD.getCenter(),
                                    vnvD.getCenter(),
                                    fb0D.getCenter(),
                                    plD.getCenter(),
                                    gasD.getCenter(),
                                    aIn);
                                    
        aIn.toggleAllVariance(false);
        
        //NV
        e.setActive(false, false, false, false, false, false, 
        false, false, false, false, false, false, false, false);
        sql.newExperiment(e, "gas", "", exp_id);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
                
            currRun = new FactorData(dtD.getCenter(),   actD.getCenter(),     fertD.getCenter(),
                                     lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
                                     nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getNext(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
    }
    
    public static void doCovariedTests(int exp_id)
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
        //sql.setBufferLimit(50);
        ExperimentData e = new ExperimentData(actD, fertD, lifeD, gymD, glacD, dtD,
            nvD, vnvD, fb0D, plD, gasD,
            fa, fd, fl, dls, dlc);
        FactorData currRun = new FactorData(dtD.getCenter(),
                                    actD.getCenter(),
                                    fertD.getCenter(),
                                    lifeD.getCenter(),
                                    gymD.getCenter(),
                                    glacD.getCenter(),
                                    nvD.getCenter(),
                                    vnvD.getCenter(),
                                    fb0D.getCenter(),
                                    plD.getCenter(),
                                    gasD.getCenter(),
                                    aIn);
                                    
        aIn.toggleAllVariance(true);
        
        e.setActive(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
        sql.newExperiment(e, "all", "", exp_id);
        aIn.toggleAllVariance(true);
        aIn.toggleDLS0Variance(false);
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getNext(),   actD.getNext(),     fertD.getNext(),
                                     lifeD.getNext(), gymD.getNext(),   glacD.getNext(),
                                     nvD.getNext(), vnvD.getNext(), fb0D.getNext(), plD.getNext(), gasD.getNext(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
    }
    
    public static void doExclusiveCovariedTests(int exp_id) //, String... vars)
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
        ExperimentData e = new ExperimentData(actD, fertD, lifeD, gymD, glacD, dtD,
            nvD, vnvD, fb0D, plD, gasD,
            fa, fd, fl, dls, dlc);
        FactorData currRun = new FactorData(dtD.getCenter(),
                                    actD.getCenter(),
                                    fertD.getCenter(),
                                    lifeD.getCenter(),
                                    gymD.getCenter(),
                                    glacD.getCenter(),
                                    nvD.getCenter(),
                                    vnvD.getCenter(),
                                    fb0D.getCenter(),
                                    plD.getCenter(),
                                    gasD.getCenter(),
                                    aIn);
                                    
        aIn.toggleAllVariance(true);
        
        /*for (String str: vars)
        {
            if (str.equalsIgnoreCase("act"))
                
            else if(str.equalsIgnoreCase("fert"))
            else if(str.equalsIgnoreCase("life"))
            else if(str.equalsIgnoreCase("gym"))
            else if(str.equalsIgnoreCase("glac"))
            else if(str.equalsIgnoreCase("deltat"))
            
        }*/
        
        e.setActive(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
        sql.newExperiment(e, "exclusive-covaried", "", exp_id);
        aIn.toggleAllVariance(true);
        aIn.toggleDLS0Variance(false);
        aIn.toggleFD0Variance(false);
        aIn.toggleFL0Variance(false);
        aIn.toggleFSR0Variance(false);
        aIn.toggleTemp0Variance(false);
        
        for (int i=0; i<10000; i++)
        {
            if (i%4000==0)
                System.out.println(i);
            
            
            currRun = new FactorData(dtD.getNext(),   actD.getNext(),     fertD.getNext(),
                                     lifeD.getNext(), gymD.getNext(),   glacD.getNext(),
                                     nvD.getNext(), vnvD.getNext(), fb0D.getNext(), plD.getCenter(), gasD.getCenter(),
                                     aIn);
            sql.includeRun(currRun);
        }
        aIn.toggleAllVariance(false);
    }
    
    public static void singleDLS(double[] newDLS)
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
                                    
        ScrapData arrs = new ScrapData();
        arrs.setDLS0(newDLS);
        FactorData currRun = new FactorData(dtD.getCenter(), actD.getCenter(), fertD.getCenter(),
            lifeD.getCenter(), gymD.getCenter(), glacD.getCenter(),
            nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
            arrs);

        System.out.println("time\tCO2\tXvolc\tDLS");
        for(int i=0;i<58;i++)
        {
            System.out.println(i*10+"\t"+currRun.getCO2(i)+"\t"+currRun.getXVolc(i)+"\t"+arrs.getDLS0(i));
        }
    }
    public static void singleDLC(double[] newDLC)
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
                                    
        ScrapData arrs = new ScrapData();
        arrs.setDLC0(newDLC);
        FactorData currRun = new FactorData(dtD.getCenter(), actD.getCenter(), fertD.getCenter(),
            lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
            nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
            arrs);

        System.out.println("time\tCO2\tXvolc\tDLC");
        for(int i=0;i<58;i++)
        {
            System.out.println(i*10+"\t"+currRun.getCO2(i)+"\t"+currRun.getXVolc(i)+"\t"+arrs.getDLC0(i));
        }
    }
    
    public static void singleNV(double newNV)
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
                                    
        ScrapData arrs = new ScrapData();
        FactorData currRun = new FactorData(dtD.getCenter(), actD.getCenter(), fertD.getCenter(),
            lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
            newNV, vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), gasD.getCenter(),
            arrs);

        System.out.println("time\tCO2\tXvolc\tDLC");
        for(int i=0;i<58;i++)
        {
            System.out.println(i*10+"\t"+currRun.getCO2(i)+"\t"+currRun.getXVolc(i)+"\t"+arrs.getDLC0(i));
        }
    }
    
    public static void singleGAS(double newGAS)
    {
        setupGArrays();
        
        SQLWriter sql = new SQLWriter();
                                    
        ScrapData arrs = new ScrapData();
        FactorData currRun = new FactorData(dtD.getCenter(), actD.getCenter(), fertD.getCenter(),
            lifeD.getCenter(), gymD.getCenter(),   glacD.getCenter(),
            nvD.getCenter(), vnvD.getCenter(), fb0D.getCenter(), plD.getCenter(), newGAS,
            arrs);

        System.out.println("time\tCO2\tXvolc\tDLC");
        for(int i=0;i<58;i++)
        {
            System.out.println(i*10+"\t"+currRun.getCO2(i)+"\t"+currRun.getXVolc(i)+"\t"+arrs.getDLC0(i));
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void setupGArrays()
    {
        aIn=new ScrapData();
        //set fa
        double[] scale=new double[58]; double[] max=new double[58]; double[] min=new double[58];
        for(int i=0; i<scale.length; i++)
        {
            scale[i] = ArrayData.fA0_PARK[i]*.05;
            max[i] = ArrayData.fA0_PARK[i] + scale[i]*2;
            min[i] = ArrayData.fA0_PARK[i] - scale[i]*2;
        }
        fa = new GaussianArray(ArrayData.fA0_PARK, scale);
        fa.setMax(max); fa.setMin(min);
        
        //set fd
        scale=new double[58]; max=new double[58]; min=new double[58];
        for(int i=0; i<scale.length; i++)
        {
            scale[i] = ArrayData.fD0_PARK[i]*.25;
            max[i] = ArrayData.fD0_PARK[i] + scale[i]*2;
            min[i] = ArrayData.fD0_PARK[i] - scale[i]*2;
        }
        fd = new GaussianArray(ArrayData.fD0_PARK, scale);
        fd.setMax(max); fd.setMin(min);

        //set fl
        scale=new double[58]; max=new double[58]; min=new double[58];
        for(int i=0; i<scale.length; i++)
        {
            scale[i] = ArrayData.fL0_PARK[i]*.25;
            max[i] = ArrayData.fL0_PARK[i] + scale[i]*2;
            min[i] = ArrayData.fL0_PARK[i] - scale[i]*2;
        }
        fl = new GaussianArray(ArrayData.fL0_PARK, scale);
        fl.setMax(max); fl.setMin(min);
        
        //set dls
        scale=new double[58]; max=new double[58]; min=new double[58];
        scale=ScrapData.DLS0Dev_PROKOPH;
        for(int i=0; i<scale.length; i++)
        {
            scale[i] = ScrapData.DLS0Dev_PROKOPH[i];
            max[i] = ScrapData.DLS0_PROKOPH[i] + scale[i]*2;
            min[i] = ScrapData.DLS0_PROKOPH[i] - scale[i]*2;
        }
        dls = new GaussianArray(ScrapData.DLS0_PROKOPH, scale);
        dls.setMax(max); dls.setMin(min);
        
        //set dlc
        scale=new double[58]; max=new double[58]; min=new double[58];
        for(int i=0; i<scale.length; i++)
        {
            scale[i] = ScrapData.getDLCDev(i);
            max[i] = ScrapData.DLC0_PROKOPH[i] + scale[i]*2;
            min[i] = ScrapData.DLC0_PROKOPH[i] - scale[i]*2;
        }
        dlc = new GaussianArray(ScrapData.DLC0_PROKOPH, scale);
        dlc.setMax(max); dlc.setMin(min);
        
        aIn.toggleAllVariance(false);
    }
}