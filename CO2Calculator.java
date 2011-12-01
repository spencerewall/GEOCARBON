import java.util.Arrays;

public class CO2Calculator
{
    // GCM is the DT for CO2-doubling, divided by ln 2, so when you mult by log(RCO2) you obtain deltaT when RCO2=2    
    private static double GCM0;
    private static double[] GEOG=new double[600];
    private static double[] temp=new double[600];
    // fR is dimensionless effect of mountain uplift on CO2 uptake by silicate weathering
    private static double[] fR=new double[600];
    private static double[] fA=new double[600];
    private static double[] fD=new double[600];
    private static double[] DLSOC=new double[600];
    private static double[] DLCOC=new double[600];
    private static double[] alphac=new double[600];
    private static double[] fL=new double[600];
    
    private static double[] fRT = new double[600];
    private static double ZM=12.5;
    private static double RBAS=.703;
    private static double RRIV=.711;
    private static double FRIV=3.37;
    private static int Dt=1;            // time step = 1mil yrs
    
    // total cooling due to weaker solar radiation at 570Ma
    private static int St=600;
    private static int dlst=4;
    private static int CT=6252;
    private static double Ws=7.4, dlct=-3.5; 
    private static double kwpy=0.01, kwsy=0.01, kwgy=0.018, kwcy=0.018;
    private static double Fwpa1=0.25;
    
    private static double Fwsa1=0.5;    // Fws: Cflux of weathering silicates 
    private static double Fwga1=0.5;    // Fwg: Cflux from sedimentary organic matter
    private static double Fwca1=2;      // Fwc: Cflux from Ca and Mg carbonates 
    private static double Fmg1=1.25;    // Fmg: C-degassing from volcanism, metamorphism and diagenesis of organic
    private static double Fmc1=6.67;    // Fmc: C-degassing from volcanism, metamorphism and diagenesis of carbonate
    private static double Fmp1=0.25;
    private static double Fms1=0.5;
    
    private static double[] fSr;
    private static double[] Sr;
    
    private static double[] gcsppm = new double[800];
    private static double[] fAD = new double[600];
    private static double oxy, RCO2, Spy, Spa, Ssy, Ssa, Gy, Ga, Cy, Ca, dlsy, dlcy, dlpy, dlpa, dlsa, dlgy, dlga, dlca, Rcy, Rca;
    private static double GCM, fac, RT, GAS, Fc, fE, fBB;
    private static double Fwpy, Fwsy, Fwgy, Fwcy, Fwpa, Fwsa, Fwga, Fwca;
    private static double Fmp, Fms, Fmg, Fmc, Fyop, Fyos, Fyog, Fyoc, zzn, aaJ, alphas;
    private static double Fbp, Fbg, Fbs, Fbc;
    private static double Roc, anv, Rg, Rv, avnv;
    private static double Fob0, Xvolc0, Fwsi0, Avlc, Bvlc, Dvlc, Evlc, Xvolc, fvolc, fB;
    private static double RCO2old;
    private static double Fbbs, fPBS, W, V;
    private static double oldfBBS, oldW, oldV, oldfPBS, ewfBBS, ewW, ewV, ewX, ewfPBS, fBBS;
    private static double X;
    private static double tau, ppm;
    
    private static double[] oldFact = new double[6];
    private static GCSVData oldArrays;
    
    public static void handleInputs(double deltaT, double ACT, double FERT, double LIFE, double GYM, double GLAC, GCSVData arrData)
    {
        if (deltaT!=oldFact[0])
        {
            GCM0 = deltaT/Math.log(2.0);
            oldFact[0]=deltaT;
        }
        if (oldFact[1]!=ACT)
        {
            oldFact[1]=ACT;
        }
        if (oldFact[2]!=FERT)
        {
            oldFact[2]=FERT;
        }
        if (oldFact[3]!=LIFE)
        {
            oldFact[3]=LIFE;
        }
        if (oldFact[4]!=GYM)
        {
            oldFact[4]=GYM;
        }
        if (oldFact[5]!=GLAC)
        {
            oldFact[5]=GLAC;
        }
        if(!(oldArrays.equals(arrData)))
        {
            updateHistArrays(arrData);
            oldArrays=arrData;
        }
    }
    public static void updateHistArrays(GCSVData arrData)
    {
        if(oldArrays!=null)
            if(oldArrays.equals(arrData))
                return;
        oldArrays=arrData;
        System.out.println("Hey, somethings different about me");
        
        fSr = arrData.fillFSr();
        //Sr is the normalized difference of basalt-predicted and measured Sr isotope ratios
        Sr = arrData.fillSr();
        
        for (int i=0; i<=56; i++)
        {
            int jj=i*10;
            for (int j=0;j<=9;j++)
            {
                fA[jj+j]=(j*arrData.getFA0(i+1)+(10-j)*arrData.getFA0(i))/10.0;
                fD[jj+j]=(j*arrData.getFD0(i+1)+(10-j)*arrData.getFD0(i))/10.0;
                temp[jj+j]=(j*arrData.getTemp0(i+1)+(10-j)*arrData.getTemp0(i))/10.0;
                DLSOC[jj+j]=(j*arrData.getDLS0(i+1)+(10-j)*arrData.getDLS0(i))/10.0;
                DLCOC[jj+j]=(j*arrData.getDLC0(i+1)+(10-j)*arrData.getDLC0(i))/10.0;
                alphac[jj+j]=(j*arrData.getAl0(i+1)+(10-j)*arrData.getAl0(i))/10.0;
                fL[jj+j]=(j*arrData.getFL0(i+1)+(10-j)*arrData.getFL0(i))/10.0;
            }
            fA[570]=arrData.getFA0(57);
            fD[570]=arrData.getFD0(57);
            temp[570]=arrData.getTemp0(57);
            DLSOC[570]=arrData.getDLS0(57);
            DLCOC[570]=arrData.getDLC0(57);
            alphac[570]=arrData.getAl0(57);
            fL[570]=arrData.getFL0(57);
        }
    }
    
   /** The variables below are used only within the initialize_fR_fRT() method.  Bas calculated oceanic Sr-isotope
     * ratio for basalt-seawater reactions - Bas(571)=0.709.  R is the actual measured Sr-isotope ratio. */
    private static double[] Bas = new double[600], DELTOT = new double[600], DELRIV = new double[600];
    private static double[] DELBAS = new double[600],R = new double[600],SRBAS = new double[600];
    /**
     * The main purpose of this section of code seems to be to initialize values of fR and fRT.  fR is used 
     * a number of times throughout the code however the first loop seems to be entirely redundant since the second
     * loop completely overwrites those values before they are even used.
     * 
     * Additionally, the variable R appears to be redundant as it doesn't seem to appear anywhere else in the code.
     */
    private static void initialize_fR_fRT()
    {
        double FBASO=.92;
        //double[] Bas = new double[600];double[] DELTOT = new double[600];double[] DELRIV = new double[600];double[] DELBAS = new double[600];double[] R = new double[600];double[] SRBAS = new double[600];
        
        Arrays.fill(Bas, 0);
        double FBAS;
        for (int it=570; it>=0; it--)
        {
            // test case would be fSR(T)=1
            // Bas calculated oceanic Sr-isotope ratio for basalt-seawater reactions
            // starts with Bas(570)=0.709
            FBAS=FBASO*fSr[it];
            if (it<570) 
                Bas[it]=Bas[it+1]+DELTOT[it+1];
            else if (it==570)                       /**This line...*/
                Bas[it]=0.709;
            // fR is dimensionless effect of mountain uplift on CO2 uptake by silicate weathering
            if(it==570)                 /**and this line could probably be put together...*/
                fR[it]=1;               /**why don't we just say that fR[570]=1???*/
            DELRIV[it]=((RRIV-Bas[it])/ZM)*FRIV;
            DELBAS[it]=((RBAS-Bas[it])/ZM)*FBAS;
            DELTOT[it]=DELBAS[it]+DELRIV[it];
            
            // R is the actual measured Sr-isotope ratio
            R[it]=0.7+Sr[it]/10000.0;
            // SRBAS is basalt-seawater Sr-isotope normalization, see Berner (2004) eq 2.1
            SRBAS[it]=(Bas[it]-0.7)*10000;
            
            // PL is Berner's adjustable empirical parameter L for Sr-ratio vs. silcate weathering
            // PL=2 obtains approximate fit between Sr-isotopes and physical erosion estimates 
            // from siliclastic sediments, see Berner (2004)
            int PL=2;

            //fR is dimensionless effect of mountain uplift on CO2 uptake by silicate weathering
            fR[it]=1-PL*(1-(Sr[it]/SRBAS[it]));
        }
        
       /** This will generate values for cells 0-578 of fR and fRT.  Why we recalculate fR so 
         * soon is beyond my understanding. */
        for (int i=0; i<=578; i++)
        {
            //Cubic fit to Ronov sediment data
            fRT[i]=25.269*Math.pow((-i)/1000.0,3) + 26.561*Math.pow((-i)/1000.0,2) 
                   + 6.894*((-i)/1000.0) + 1.063;
            fR[i]=Math.pow(fRT[i]/1.063,0.67);
        }
    }
    /**
     * This method handles all of the variables whos values change during a single run.  These variables
     * keep track of important quantities that are needed to calculate CO2 values, but change at each timestep.
     */
    public static void resetMovingVars()
    {
        //The following variables need to be reset each time a run is performed
        oxy=25.0;    //oxy is oxygen level in atmosphere, in percent mass (Berner 2009)
        // RCO2 is ratio of CO2 to pre-industrial level.  initial RCO2 level in 
        // the calculation is moot, because it will be solved for
        RCO2=10.0;
        Spy=20.0; Spa=280.0; Ssy=150.0; Ssa=150.0;
        Gy=250.0; Ga=1000.0; Cy=1000.0; Ca=4000.0;
        dlsy=35.0; dlcy=3.0; dlpy=-10.0; dlpa=-10.0;
        dlsa= (dlst*St-(dlpy*Spy+dlsy*Ssy+dlpa*Spa))/Ssa;
        dlgy= -23.5; dlga=-23.5;
        dlca= (dlct*CT-(dlgy*Gy  +dlcy*Cy  +dlga*Ga))/Ca;
        Rcy=0.7095; Rca=0.709;
        
        Arrays.fill(gcsppm, 0);
        Arrays.fill(fAD, 0);
    }
    public static float[] doCO2Calc(double deltaT, double ACT, double FERT, double LIFE, double GYM, double GLAC, GCSVData histFactors)
    {
        //From the fortran code, it looks like this should definitely appear first
        //handleInputs(deltaT, ACT, FERT, LIFE, GYM, GLAC, histFactors);
        GCM0 = deltaT/Math.log(2.0);
        updateHistArrays(histFactors);
        initialize_fR_fRT();
        resetMovingVars();
        
        
        
        float[] CO2Temp = new float[58];
        
        //CO2.clear(); 
        // iberner is a counter for correspondence to earlier codes
        // I decimate by 10 to match the 10My-averaged CO2 values
        for (int i=571; i>=1; i--)
        {
            GCM=GCM0;

            // Bob's GEOCARB GLACIAL INTERVALS
            if ((i<=340) && (i>=260))
                GCM=GCM0*GLAC;
            if ((i<=40) && (i>=0))
                GCM=GCM0*GLAC;
            
            fac=(i-1)/570.0;

            // RT is Y in Berner (2004), & RUN in Berner and Kothavala (2001)
            // is the coefficient of continental runoff versus temperature change
            // that is, runoff/runoff0 = 1 + RT*(T-T0), gotten from GCM runs
            if(i>341)
                RT=0.025;
            else if((i<=341)&&(i>261)) 
                RT=0.045;
            else if((i<=261)&&(i>41))
                RT=0.025;
            else
                RT=0.045;
            GAS=0.75;
            if(i>151)
                Fc=GAS;
            else
                Fc=(GAS)+((1.0-GAS)/150.)*(151-i);
                // fE is the factor of plant-assisted silicate weathering, 
                // relative to modern angiosperms
            if(i>381)
                fE=LIFE;
            else if((i<=381)&&(i>351))
                fE=(GYM-LIFE)*((381.0-i)/30.0)+LIFE;
            else if((i<=351)&&(i>131))
                fE=GYM;
            else if((i<=131)&&(i>81))
                fE=(1.0-GYM)*((131-i)/50.0)+GYM;
            else
                fE=1;
            
                // GEOG is the change in avg land temp due to geography only, 
                // obtained via GCM runs
            GEOG[i-1] = temp[i-1]-12.4;

            // fBB is the CO2-assisted acceleration of silicate weathering 
            // (looks like time-stepped value??? CHECK!)
            // in Berner (2004) fbb is biological negative feedbacks, but in this code
            // fbb includes other terms and factors

            // (2.0*RCO2/(1.0+RCO2))**FERT -- CO2-fertilization of plant growth
            // GCM*alog(RCO2) - Ws*fac + GEOG[i] -- the change in temperature relative to modern
            // includes CO2-warming, solar waxing, and geographic effect, equation 2.28

            // Berner's GEOCARB modelling assumes a long-term balance of carbon fluxes, 
            // in two terms, fBB and fB
            if(i==571)
                fBB=1;
            else if((i<=570)&&(i>381))
                fBB=(1+0.087*GCM*Math.log(RCO2)-0.087*Ws*fac
                     +0.087*GEOG[i-1])*Math.sqrt(RCO2);
            else if((i<=381)&&(i>351))
                fBB=((381.-i)/30.)*((1.0+0.087*GCM*Math.log(RCO2)-0.087*Ws*fac
                    +0.087*GEOG[i-1])*Math.pow(2.0*RCO2/(1.0+RCO2),FERT))
                    +((i-351)/30.)*(1.0+0.087*GCM*Math.log(RCO2)
                    -0.087*Ws*fac+0.087*GEOG[i-1])*Math.sqrt(RCO2);
            else
                fBB=(1.0+0.087*GCM*Math.log(RCO2)-0.087*Ws*fac
                    +0.087*GEOG[i-1])*Math.pow(2.0*RCO2/(1.0+RCO2),FERT);
               
            Fwpy=fA[i-1]*fR[i-1]*kwpy*Spy;
            Fwsy=fA[i-1]*fD[i-1]*kwsy*Ssy;
            Fwgy=fA[i-1]*fR[i-1]*kwgy*Gy;
            Fwcy=fA[i-1]*fD[i-1]*fL[i-1]*fE*fBB*kwcy*Cy;
            Fwpa=fR[i-1]*Fwpa1;
            Fwsa=fA[i-1]*fD[i-1]*Fwsa1;
            Fwga=fR[i-1]*Fwga1;
            Fwca=fA[i-1]*fD[i-1]*fL[i-1]*fE*fBB*Fwca1;
            
            /**woaaaaah wrong!!!*/
            Fmp=fSr[i-1]*Fmp1;
            Fms=fSr[i-1]*Fms1;
            Fmg=fSr[i-1]*Fmg1;
            Fmc=fSr[i-1]*Fc*Fmc1;
            Fyop=Fwpa+Fmp;
            Fyos=Fwsa+Fms;
            Fyog=Fwga+Fmg;
            Fyoc=Fwca+Fmc;
            zzn=1.5;
            aaJ=4.0;
            alphas =35.0*Math.pow(oxy/38.0,zzn);
            
            alphac[i-1]=27.0+aaJ*(oxy/38.0-1.0);
            Fbp = (1/alphas)*((DLSOC[i-1]-dlsy)*Fwsy+(DLSOC[i-1]-dlsa)*Fwsa+(DLSOC[i-1]-dlpy)*Fwpy+(DLSOC[i-1]-dlpa)*Fwpa +(DLSOC[i-1]-dlsa)*Fms +(DLSOC[i-1]-dlpa)*Fmp);
            
                //Fbg is burial Cflux of organic sediments  
            Fbg=(1/alphac[i-1])*((DLCOC[i-1]-dlcy)*Fwcy+(DLCOC[i-1]-dlca)*Fwca+(DLCOC[i-1]-dlgy)*Fwgy+(DLCOC[i-1]-dlga)*Fwga +(DLCOC[i-1]-dlca)*Fmc+(DLCOC[i-1]-dlga)*Fmg);
            
            Fbs=Fwpy+Fwpa+Fwsy+Fwsa+Fms +Fmp-Fbp;
            //Fbc is burial Cflux of carbonate sediments    
            Fbc=Fwgy+Fwga+Fwcy+Fwca+Fmc +Fmg-Fbg;
            
            /**
             * 
             * IS SUPPOSED TO LOOK LIKE
             * if(i.lt.571) oxy = oxy +(Fbg+(15./8.)*Fbp)*Dt -(Fwgy+Fwga+Fmg)*Dt 
             *                    -(15./8.)*(Fwpy+Fwpa+Fmp)*Dt
             */
            if(i<571)
                oxy = oxy + ((Fbg+(15./8.)*Fbp)*Dt)-((Fwgy+Fwga+Fmg)*Dt)-((15./8.)*(Fwpy+Fwpa+Fmp)*Dt);
            
            
            Spy=Spy+(Fbp-Fwpy-Fyop)*Dt;
            Ssy =Ssy +(Fbs-Fwsy-Fyos)*Dt;
            Gy= Gy+ (Fbg-Fwgy-Fyog)*Dt;
            Cy=Cy + (Fbc-Fwcy-Fyoc)*Dt;
            // CT is set near the start of the program
            Ca = CT-Gy - Ga - Cy -2.0;
            dlpy = dlpy +((DLSOC[i-1]-dlpy-alphas)*Fbp/Spy)*Dt;
            dlpa = dlpa + (Fyop*(dlpy-dlpa)/Spa)*Dt;
            dlsy = dlsy+((DLSOC[i-1]-dlsy)*Fbs/Ssy)*Dt;
            dlsa = dlsa + (Fyos*(dlsy-dlsa)/Ssa)*Dt;
            dlgy=dlgy+((DLCOC[i-1]-dlgy-alphac[i-1])*Fbg/Gy)*Dt;
            dlga =dlga+(Fyog*(dlgy-dlga)/Ga)*Dt;
            dlcy=dlcy+((DLCOC[i-1]-dlcy)*Fbc/Cy)*Dt;
            dlca=dlca+(Fyoc*(dlcy-dlca)/Ca)*Dt;
            
            /**
             * STARTING HERE IS NEW CODE FOR geocarbsulf volc
             * fAD is total runoff factor for silicate weathering
             * obtained with GCM simulations and continental reconstructions
             */
            Roc = (Sr[i-1]/10000.0)+0.7;
            // vary anv from 0 to 0.015, is Berner parameter NV 
            // anv is a parameter that scales the influence of volcanic 
            //     weathering on Sr-87/86 ratios.
            // anv=0 means no accelerated weathering of volcanic rocks, 
            //     as opposed to plutonic/metamorphic
            anv=0.015;
            Rg =0.722-anv*(1-fRT[i-1]/1.063);
            Rv =0.704;
            // avnv is the ratio of volcanic weathering CO2-consumption rate (basalts) 
            // to plutonic weathering CO2-consumption rate (granites)
            // avnv=4 in the version of code used for 2010 GoldSchmidt conference poster.
            // obtained by averaging values from Taylor's dissertation (5) and Taylor et al 1999 (3)
            // avnv=5 is Berner's preferred value for GEOCARBSULF,
            // gotten from Aaron Taylor's dissertation
            avnv=4.0;
            // Fv0 =.3 of total Fwsi0=6.67 at x=0; Fob0 is basalt-seawater 
            /**
            * flux =1/3 of total ocean imbalance  with Fv0 = 2/3
            * for GEOCARBSULF volcanic, there are two flavors of code.  The first code 
            * uses BoB's 2007 algorithm, enshrined in a BASIC code he gave me in 2007.  
            * These codes have the letter "v" in the filename, in place of "l".  For the 
            * newer 2010 GEOCARBSULF-volcanic algorithm, based on a 2010 BBerner Basic code, 
            * the letters "v10" are in the filename.  The new-improved aspects of the 2010
            * code includes slightly different carbon-isotope data-inputs, one change in the strontium
            * data-input time series, a change in the formula for fR, involving a power to the 0.67(!), 
            * and theparameters VNV (increase from 2 to 4) and NV (increase from 0.008 to 0.015), and 
            * the parameter Fob0 (from 0.75 to 4), and switching alphac(i) from a table of values to 
            * being computed from the model-generated oxygen time series.  The Newton-Raphson rootfinding 
            * algorithm works OK with estimating the alphac(i) parameter from model-derived oxygen, but 
            * the quasi-bisection routine had difficulty with this last feature.  For this reason, 
            * and the previous experience that the bisection and Newton-Raphson results were not 
            * dramatically different (largest differences were for unlikely parameter choices where the 
            * datafit was already poor.), I retain Newton-Raphson for the export codes.
            */
            Fob0=4.0;
            // Fob0=FOBO  --- this was an option to sample a range of values in the K2 loop
            Xvolc0=0.35;
            Fwsi0 =6.67;
            Rcy = Rcy +((Roc-Rcy)*Fbc/Cy)*Dt;
            Rca = Rca +((Rcy-Rca)*Fyoc/Ca)*Dt;
            Avlc =((Rv-Roc)*fSr[i-1]*Fob0)/(Fbc-Fwcy-Fwca);
            Bvlc = Fwcy/(Fbc-Fwcy-Fwca);
            Dvlc = Fwca/(Fbc-Fwcy-Fwca);
            Evlc = Fbc/(Fbc-Fwcy-Fwca);
            Xvolc= (Avlc +Bvlc*Rcy+Dvlc*Rca -Evlc*Roc+Rg)/(Rg-Rv);
            fAD[i-1]=fA[i-1]*fD[i-1];
            fvolc=(avnv*Xvolc+1.0-Xvolc)/(avnv*Xvolc0+1.0-Xvolc0);
            fB=(Fbc-Fwcy-Fwca)/(Math.pow(fAD[i-1],0.65)*fE*fR[i-1]*fvolc*Fwsi0);
            // ENDING HERE IS NEW CODE FOR geocarbsulf volc
            
            int n=0;
            RCO2old=RCO2*2.0; /** new line */
            if (i>381) 
            {
                // this first step is an initialization kluge to ensure that the convergence 
                // is not satisfied accidentally at the first step

                while(Math.abs(RCO2/RCO2old-1.0)>0.001)
                {
                    RCO2old=RCO2;
                    Fbbs=Math.pow(RCO2,(0.5+ACT*GCM))*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT
                        *Ws*fac+RT*GEOG[i-1]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    //System.out.println("Fbbs: "+ Fbbs);
                    
                    W=((0.5+ACT*GCM)*Math.pow(RCO2,(-0.5+ACT*GCM)))
                            *Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                            *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    V=Math.pow(RCO2,(0.5+ACT*GCM))*0.65
                            *Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),(-0.35))
                            *(RT*GCM/RCO2)*Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i-1]);
                    //System.out.println("W = "+W);
                    //System.out.println("V = "+V);
                    fPBS = W + V;

                    //System.out.println("RCO2\tFbbs\tfB\tfPBS");
                    //System.out.println("["+RCO2+"\t"+Fbbs+"\t"+fB+"\t"+fPBS+"]");
                    if(RCO2>((Fbbs-fB)/fPBS))
                    {
                        // damp the iteration to avoid overshoot
                        RCO2=RCO2-(0.9*((Fbbs-fB)/fPBS));
                    }
                    else
                    {
                        // convert the iteration to geometric shrinkage to avoid nonpositive value in overshoot
                        RCO2=RCO2*0.2;
                    }
                }
                //System.out.println("RCO2: "+RCO2);
            }
            else if((i<=381)&&(i>349))
            {
                //System.out.println("ifB");
                //RCO2old=RCO2*2.0;
                n=0;
                while(Math.abs(RCO2/RCO2old-1.0)>0.001)
                {
                    RCO2old=RCO2;
                    oldfBBS=Math.pow(RCO2,(0.5+ACT*GCM))*Math.pow((1+RT*GCM*Math.log(RCO2)
                        -RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                        *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    oldW=(0.5+ACT*GCM)*Math.pow(RCO2,(-0.5+ACT*GCM))
                        *Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)
                        +RT*GEOG[i-1]),0.65)
                        *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    oldV=Math.pow(RCO2,(0.5+ACT*GCM))*0.65*Math.pow((1.0+RT*GCM*Math.log(RCO2)
                        -RT*Ws*(fac)+RT*GEOG[i-1]),(-0.35))*(RT*GCM/RCO2)
                        *Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i-1]);
                    oldfPBS = oldW + oldV;
                    ewfBBS=(Math.pow(2.0,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))
                        *Math.pow((1+RCO2),(-FERT))
                        *Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*fac+RT*GEOG[i-1]),0.65)
                        *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    ewW=Math.pow(2.0,FERT)*(FERT+ACT*GCM)*Math.pow(RCO2,(FERT+ACT*GCM-1.0))
                        *Math.pow((1.0+RCO2),(-FERT))*Math.pow((1.0+RT*GCM*Math.log(RCO2)
                        -RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                        *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    ewV=(-FERT*Math.pow((1.0+RCO2),(-(1.0+FERT))))
                        *((Math.pow(2,FERT))*Math.pow(RCO2,(FERT+ACT*GCM)))
                        *Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                        *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    
                    //System.out.println("ewV\tFERT\tRCO2\tACT\tGCM\tRT\tWs\tfac\tGEOG[i-1]");
                    //System.out.println(ewV+"\t"+FERT+" \t "+RCO2+" \t "+ACT+" \t "+GCM+" \t "+RT+" \t "+Ws+" \t "+fac+" \t "+GEOG[i-1]);
                    
                    ewX=0.65*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),(-0.35))
                        *(RT*GCM/RCO2)*(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))
                        *Math.pow((1+RCO2),(-FERT))*Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i-1]);
                    ewfPBS=ewW+ewV+ewX;
                    fBBS=((i-349)/32.)*oldfBBS + ((381-i)/32.)*ewfBBS;
                    fPBS=((i-349)/32.)*oldfPBS + ((381-i)/32.)*ewfPBS;
                    
                    
                    if(RCO2>((fBBS-fB)/fPBS))
                        RCO2=RCO2-0.9*((fBBS-fB)/fPBS);
                        // damp the iteration to avoid overshoot
                    else
                        RCO2=RCO2*0.2;
                        // convert the iteration to geometric shrinkage 
                        // to avoid nonpositive value in overshoot
                    n=n+1;
                }
            }
            else
            {
                //RCO2old=RCO2*2.0;
                //System.out.println("ifC");
                while(Math.abs(RCO2/RCO2old-1.0)>0.001)
                {
                    RCO2old=RCO2;
                    Fbbs=(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))
                        *Math.pow((1+RCO2),(-FERT))
                        *Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                        *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    W=Math.pow(2,FERT)*(FERT+ACT*GCM)*Math.pow(RCO2,(FERT+ACT*GCM-1))
                        *Math.pow((1+RCO2),(-FERT))
                        *Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                        *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    V=(-FERT*Math.pow((1+RCO2),(-(1.+FERT))))*(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))
                        *Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                        *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                    X=0.65*Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)
                        +RT*GEOG[i-1]),(-0.35))*(RT*GCM/RCO2)
                        *(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))*Math.pow((1+RCO2),(-FERT))
                        *Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i-1]);
                    fPBS=W+V+X;
                    if(RCO2>((Fbbs-fB)/fPBS))
                        RCO2=RCO2-0.9*((Fbbs-fB)/fPBS);
                        // damp the iteration to avoid overshoot
                    else
                        RCO2=RCO2*0.2;
                        // convert the iteration to geometric shrinkage 
                        // to avoid nonpositive value in overshoot
                }
                //System.out.println(elseCount);
            }
            // the CO2 ppm is converted from RCO2 by last My average value = 250 ppm
            
            tau=15 + 6*Math.log(RCO2)-12.8*fac+GEOG[i-1];
            //double oxy2 =100*(oxy/(oxy+143.0));
            //System.out.println("RCO2: "+RCO2);
            ppm=250.0*RCO2;
            // the indexing here is time equals (i-1)Ma, save the ppm values at 10-Ma intervals,
            // K=1 --> 0Ma
            if(((i-1)/10)*10 == i-1)
            {
                /**
                 * is this line needed? No, It's just for scale.
                 * Ma=-i+1;
                 * print *, Ma, ppm
                 */
                int k=1+i/10;
                
                // save CO2 level (ppm) or oxygen (mass percent)
                CO2Temp[k-1] = (float)ppm;
            }
            gcsppm[i-1]=ppm;
        }
        return CO2Temp;
    }
}