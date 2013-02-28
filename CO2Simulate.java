import java.util.Arrays;

public class CO2Simulate
{
    /*
     * Computational Fields
     * These variables are used to calibrate flux convergence and to prevent overshoot
     * and nondeterministic behavior.`
     */
    private static StringBuffer errorStr=new StringBuffer();
    private static boolean printErr=false;
    
    /*
     * Private Fields (Geologic)
     * These represent the geologic factors in the model
     */
    private static double RCO2, RCO2old;
    // GCM is the DT for CO2-doubling, divided by ln 2, so when you mult by log(RCO2) you
    //obtain deltaT when RCO2=2
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
    private static final double ZM=12.5;
    private static final double RBAS=.703;
    private static final double RRIV=.711;
    private static final double FRIV=3.37;
    private static final int Dt=1;            // time step = 1mil yrs
    
    // total cooling due to weaker solar radiation at 570Ma
    private static final int St=600;
    private static final int dlst=4;
    private static final int CT=6252;
    private static final double Ws=7.4, dlct=-3.5; 
    private static final double kwpy=0.01, kwsy=0.01, kwgy=0.018, kwcy=0.018;
    private static final double Fwpa1=0.25;
    
    // Fws: Cflux of weathering silicates
    // Fwg: Cflux from sedimentary organic matter
    // Fwc: Cflux from Ca and Mg carbonates 
    // Fmg: C-degassing from volcanism, metamorphism and diagenesis of organic
    // Fmc: C-degassing from volcanism, metamorphism and diagenesis of carbonate
    private static final double Fwsa1=0.5;
    private static final double Fwga1=0.5;
    private static final double Fwca1=2;
    private static final double Fmg1=1.25;
    private static final double Fmc1=6.67;
    private static final double Fmp1=0.25;
    private static final double Fms1=0.5;
    
    private static double[] fSr = new double[600];
    private static double[] Sr = new double[600];
    
    private static double[] gcsppm = new double[800];
    private static double[] fAD = new double[600];
    
    private static double oxy, Spy, Spa, Ssy, Ssa, Gy, Ga, Cy, Ca, dlsy, dlcy, dlpy,
        dlpa, dlsa, dlgy, dlga, dlca, Rcy, Rca;
        
    private static double GCM, fac, RT, Fc, fE, fBB;
    private static double Fwpy, Fwsy, Fwgy, Fwcy, Fwpa, Fwsa, Fwga, Fwca;
    private static double Fmp, Fms, Fmg, Fmc, Fyop, Fyos, Fyog, Fyoc, zzn, aaJ, alphas;
    private static double Fbp, Fbg, Fbs, Fbc;
    //private static double NV, VNV;
    private static double Roc, Rg, Rv;
    //private static double Fob0;
    private static double Xvolc0, Fwsi0, Avlc, Bvlc, Dvlc, Evlc, Xvolc, fvolc, fB;
    private static double Fbbs, fPBS, W, V;
    private static double oldfBBS, oldW, oldV, oldfPBS, ewfBBS, ewW, ewV, ewX,ewfPBS,fBBS;
    private static double X;
    private static double ppm, oxy2;
    
    /** 
     * The variables Bas, DELTOT, DELRIV, DELBAS, R, and SRBAS are used only within 
     * initialize_fR_fRT().  Bas calculated oceanic Sr-isotope ratio for basalt-seawater 
     * reactions - Bas(571)=0.709. R is the actual measured Sr-isotope ratio.
     */
    private static double[] Bas = new double[600], DELTOT = new double[600], 
        DELRIV = new double[600], DELBAS = new double[600], 
        R = new double[600],SRBAS = new double[600];
    
    
    public static void updateHistArrays(double PL, double[] fA0, double[] fD0, double[] fL0,
        double[] fSr0, double[] Sr0, double[] temp0, double[] DLS0, double[] DLC0)
    {
        //Fill Sr[0...570] by extrapolating from Sr0[0...57].
        for(int i=1;i<=20;i++) {
            int jj=1+(i-1)*5;
            for (int j=0; j<=4;j++) {
                Sr[jj+j]=(j*Sr0[i]+(5-j)*Sr0[i-1])/5.0; } }
        for (int i=21; i<=67; i++) {
            int jj=101+(i-21)*10;
            
            for (int j=0; j<=9; j++) {
                Sr[jj+j]=(j*Sr0[i]+(10-j)*Sr0[i-1])/10.0; } }
        Sr[571]=Sr0[67];
        
        //Fill fSr[0...570] by extrapolating from Sr0[0...57].
        for(int i=1;i<=28;i++) {
            int jj=1+(i-1)*5;
            for (int j=0; j<=4;j++){
                fSr[jj+j]=(j*fSr0[i]+(5-j)*fSr0[i-1])/5.0; } }
        for (int i=29; i<=71; i++) {
            int jj=141+(i-29)*10;
            for (int j=0; j<=9; j++) {
                fSr[jj+j]=(j*fSr0[i]+(10-j)*fSr0[i-1])/10.0; } }
        fSr[571]=fSr0[71];
        
        double FBASO=.92; //originally = .92; berner = 4
                
        Arrays.fill(Bas, 0);
        double FBAS;
        for (int it=571; it>=1; it--)
        {
            // test case would be fSR(T)=1
            // Bas calculated oceanic Sr-isotope ratio for basalt-seawater reactions
            // starts with Bas(570)=0.709
            FBAS=FBASO*fSr[it];
            if (it<571) 
                Bas[it]=Bas[it+1]+DELTOT[it+1];
            if (it==571)                       /**This line...*/
                Bas[it]=0.709;
            // fR is dimensionless effect of mountain uplift on CO2 uptake by silicate weathering
            if(it==571)                 /**and this line could probably be put together...*/
                fR[it]=1;               /**why don't we just say that fR[570]=1???*/
            DELRIV[it]=((RRIV-Bas[it])/ZM)*FRIV;
            DELBAS[it]=((RBAS-Bas[it])/ZM)*FBAS;
            DELTOT[it]=DELBAS[it]+DELRIV[it];
            
            // R is the actual measured Sr-isotope ratio
            R[it]=0.7+Sr[it]/10000.0;
            // SRBAS is basalt-seawater Sr-isotope normalization, see Berner (2004) eq 2.1
            SRBAS[it]=(Bas[it]-0.7)*10000.0;
            
            
            
            //fR is dimensionless effect of mountain uplift on CO2 uptake by silicate weathering
            fR[it]=1-PL*(1-(Sr[it]/SRBAS[it]));
        }
        
        for (int i=1; i<=57; i++)
        {
            int jj=1+(i-1)*10;
            for (int j=0;j<=9;j++)
            {
                //Sr[jj+j]=(j*Sr0[i+1]+(10-j)*Sr0[i])/10.0;
                //fSr[jj+j]=(j*fSr0[i+1]+(10-j)*fSr0[i])/10.0;
                
                fA[jj+j]=(j*fA0[i]+(10-j)*fA0[i-1])/10.0;
                fD[jj+j]=(j*fD0[i]+(10-j)*fD0[i-1])/10.0;
                temp[jj+j]=(j*temp0[i]+(10-j)*temp0[i-1])/10.0;
                DLSOC[jj+j]=(j*DLS0[i]+(10-j)*DLS0[i-1])/10.0;
                DLCOC[jj+j]=(j*DLC0[i]+(10-j)*DLC0[i-1])/10.0;
                fL[jj+j]=(j*fL0[i]+(10-j)*fL0[i-1])/10.0;
            }
        }
        //Sr[570]=Sr0[57];
        //fSr[570]=fSr0[57];
        
        fA[571]=fA0[57];
        fD[571]=fD0[57];
        temp[571]=temp0[57];
        DLSOC[571]=DLS0[57];
        DLCOC[571]=DLC0[57];
        fL[571]=fL0[57];
        
        //System.out.println("Al0:\t" + Arrays.toString(Al0));
        //System.out.println(Arrays.toString(alphac));
        
        for (int i=1; i<=579; i++)
        {
            //Cubic fit to Ronov sediment data
            fRT[i]=25.269*Math.pow((1-i)/1000.0,3.0) + 26.561*Math.pow((1-i)/1000.0,2.0)
                   + 6.894*((1-i)/1000.0) + 1.063;
            fR[i]=Math.pow(fRT[i]/1.063,0.67);
        }
        System.out.println(Arrays.toString(fR));
    }
    
    
    
    /**
     * This method handles all of the variables whos values change during a single run.  
     * These variables keep track of important quantities that are needed to calculate 
     * CO2 values, but change at each timestep.
     */
    public static void resetMovingVars()
    {
        //The following variables need to be reset each time a run is performed
        oxy=25.0;    //oxy is oxygen level in atmosphere, in percent mass (Berner 2009)
        // RCO2 is ratio of CO2 to pre-indoxyustrial level.  initial RCO2 level in 
        // the calculation is moot, because it will be solved for
        RCO2=10.0;
        Spy=20.0; Spa=280.0; Ssy=150.0; Ssa=150.0;
        Gy=250.0; Ga=1000.0; Cy=1000.0; Ca=4000.0;
        dlsy=35.0; dlcy=3.0; dlpy=-10.0; dlpa=-10.0;
        dlsa= (dlst*St-(dlpy*Spy+dlsy*Ssy+dlpa*Spa))/Ssa;
        dlgy= -23.5; dlga=-23.5;
        dlca= (dlct*CT-(dlgy*Gy  +dlcy*Cy  +dlga*Ga))/Ca;
        Rcy=0.7095; Rca=0.709;
        
        Arrays.fill(fAD, 0);
    }
    public static GCResults doCO2Calc(double deltaT, double ACT, double FERT, double LIFE, double GYM, double GLAC, double NV, double VNV, double FOB0, double PL, double GAS,
        double[] fA0,double[] fD0,double[] fL0, double[] fSR0, double[] Sr0, double[] temp0, double[] DLS0, double[] DLC0)
    {
        //From the fortran code, it looks like this should definitely appear first
        GCM0 = deltaT/Math.log(2.0);
        resetMovingVars();
        updateHistArrays(PL, fA0,fD0,fL0, fSR0, Sr0, temp0, DLS0, DLC0);
        
        double[] co2Store = new double[58];
        double[] xvolcStore = new double[58];
        double[] oxyStore = new double[58];
        
        printErr=false;
        errorStr=new StringBuffer("");
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
            //test//System.out.println(i+"\tGCM\t"+GCM);
            
            fac=(i-1.0)/570.0;
            //test//System.out.println(i+"\tfac\t"+fac);

            // RT is Y in Berner (2004), & RUN in Berner and Kothavala (2001)
            // is the coefficient fsrof continental runoff versus temperature change
            // that is, runoff/runoff0 = 1 + RT*(T-T0), gotten from GCM runs
            if(i>341)
                RT=0.025;
            else if((i<=341)&&(i>261)) 
                RT=0.045;
            else if((i<=261)&&(i>41))
                RT=0.025;
            else
                RT=0.045;
            //test//System.out.println(i+"\tRT\t"+RT);
            /**GAS=0.75;*/
            if(i>151)
                Fc=GAS;
            else
                Fc=(GAS)+((1.0-GAS)/150.0)*(151.0-i);
            //test//System.out.println(i+"\tFc\t"+Fc);

                // fE is the factor of plant-assisted silicate weathering, 
                // relative to modern angiosperms
                
                
            if(i>381)
                fE=LIFE;
            else if((i<=381)&&(i>351))
                fE=(GYM-LIFE)*( ((double)(381.0-i)) /30.0)+LIFE;
            else if((i<=351)&&(i>131))
                fE=GYM;
            else if((i<=131)&&(i>81))
                fE=(1.0-GYM)*((131.0-i)/50.0)+GYM;
            else
                fE=1.0;
            //test//System.out.println(i+"\tfE\t"+fE);
                // GEOG is the change in avg land temp due to geography only, 
                // obtained via GCM runs
            GEOG[i] = temp[i]-12.4;
            //test//System.out.println(i+"\tGEOG\t"+GEOG[i]);

            // fBB is the CO2-assisted acceleration of silicate weathering 
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
                      fBB=(1.0+0.087*GCM*Math.log(RCO2)-0.087*Ws*fac+0.087*GEOG[i])*Math.sqrt(RCO2);
            else if((i<=381)&&(i>351))
                fBB=((381.0-i)/30.0)*((1.0+0.087*GCM*Math.log(RCO2)-0.087*Ws*fac
                    +0.087*GEOG[i])*Math.pow((2.0*RCO2/(1.0+RCO2)),FERT))
                    +((i-351.0)/30.0)*(1.0+0.087*GCM*Math.log(RCO2)
                    -0.087*Ws*fac+0.087*GEOG[i])*Math.sqrt(RCO2);

            else
                fBB=(1.0+0.087*GCM*Math.log(RCO2)-0.087*Ws*fac
                    +0.087*GEOG[i])*Math.pow((2.0*RCO2/(1.0+RCO2)),FERT);
            //test//System.out.println(i+"\tfBB\t"+fBB);
            
            Fwpy=fA[i]*fR[i]*kwpy*Spy;
            //test//System.out.println(i+"\tFwpy\t"+Fwpy);
            Fwsy=fA[i]*fD[i]*kwsy*Ssy;
            //test//System.out.println(i+"\tFwsy\t"+Fwsy);
            Fwgy=fA[i]*fR[i]*kwgy*Gy;
            //test//System.out.println(i+"\tFwgy\t"+Fwgy);
            Fwcy=fA[i]*fD[i]*fL[i]*fE*fBB*kwcy*Cy;
            //test//System.out.println(i+"\tFwcy\t"+Fwcy);
            Fwpa=fR[i]*Fwpa1;
            //test//System.out.println(i+"\tFwpa\t"+Fwpa);
            Fwsa=fA[i]*fD[i]*Fwsa1;
            //test//System.out.println(i+"\tFwsa\t"+Fwsa);
            Fwga=fR[i]*Fwga1;
            //test//System.out.println(i+"\tFwga\t"+Fwga);
            Fwca=fA[i]*fD[i]*fL[i]*fE*fBB*Fwca1;
            //test//System.out.println(i+"\tFwca\t"+Fwca);
            //Fmp is sulfur degassing flux for pyrite from volcanism, metamor- phism, and diagenesis
            Fmp=fSr[i]*Fmp1;
            //test//System.out.println(i+"\tFmp\t"+Fmp);
            //Fms is sulfur degassing flux for Ca sulfates from volcanism, meta- morphism, and diagenesis
            Fms=fSr[i]*Fms1;
            //test//System.out.println(i+"\tFms\t"+Fms);
            //Fmg is degassing flux for organic matter from volcanism, metamor- phism, and diagenesis
            Fmg=fSr[i]*Fmg1;
 
            //Fmc is degassing flux for carbonates from volcanism, metamorphism, and diagenesis
            Fmc=fSr[i]*Fc*Fmc1;
            //test//System.out.println(i+"\tFmc\t"+Fmc);
            Fyop=Fwpa+Fmp;
            //test//System.out.println(i+"\tFyop\t"+Fyop);
            Fyos=Fwsa+Fms;
            //test//System.out.println(i+"\tFyos\t"+Fyos);
            Fyog=Fwga+Fmg;
            //test//System.out.println(i+"\tFyog\t"+Fyog);
            Fyoc=Fwca+Fmc;
            //test//System.out.println(i+"\tFyoc\t"+Fyoc);
            zzn=1.5;
            aaJ=4.0;
            alphas =35.0*Math.pow((oxy/38.0),zzn);
            //test//System.out.println(i+"\talphas\t"+alphas);

            
            alphac[i]=27.0+aaJ*(oxy/38.0-1.0);
            //test//System.out.println(i+"\talphac\t"+alphac[i]);
            
            //Fbp is burial flux of pyrite in sediments
            Fbp = (1/alphas)*((DLSOC[i]-dlsy)*Fwsy+(DLSOC[i]-dlsa)*Fwsa 
                +(DLSOC[i]-dlpy)*Fwpy+(DLSOC[i]-dlpa)*Fwpa +(DLSOC[i]-dlsa)*Fms 
                +(DLSOC[i]-dlpa)*Fmp);
            //test//System.out.println(i+"\tFbp\t"+Fbp);

            
            //Fbg is burial Cflux of organic sediments  
            Fbg=(1/alphac[i])*((DLCOC[i]-dlcy)*Fwcy+(DLCOC[i]-dlca)*Fwca 
                +(DLCOC[i]-dlgy)*Fwgy+(DLCOC[i]-dlga)*Fwga +(DLCOC[i]-dlca)*Fmc 
                +(DLCOC[i]-dlga)*Fmg);
            //test//System.out.println(i+"\tFbg\t"+Fbg);

            //Fbs is burial flux of Ca sulfates in sediments
            Fbs=Fwpy+Fwpa+Fwsy+Fwsa+Fms +Fmp-Fbp;
            //test//System.out.println(i+"\tFbs\t"+Fbs);
            //Fbc is burial Cflux of carbonate sediments
            Fbc=Fwgy+Fwga+Fwcy+Fwca+Fmc +Fmg-Fbg;
            //test//System.out.println(i+"\tFbc\t"+Fbc);
            
            if(i<571)
                oxy = oxy +(Fbg+(15./8.)*Fbp)*Dt -(Fwgy+Fwga+Fmg)*Dt-(15./8.)*(Fwpy+Fwpa+Fmp)*Dt;
            if (oxy<=0)
                oxy = .05;
            //test//System.out.println(i+"\t oxy \t"+oxy);
            
            
            Spy=Spy+(Fbp-Fwpy-Fyop)*Dt;
            //test//System.out.println(i+"\t Spy \t"+Spy);
            Ssy =Ssy +(Fbs-Fwsy-Fyos)*Dt;
            //test//System.out.println(i+"\t Ssy \t"+Ssy);
            Gy= Gy+ (Fbg-Fwgy-Fyog)*Dt;
            //test//System.out.println(i+"\t Gy \t"+Gy);
            Cy=Cy + (Fbc-Fwcy-Fyoc)*Dt;
            //test//System.out.println(i+"\t Cy \t"+Cy);
            // CT is set near the start of the program
            Ca = CT-Gy - Ga - Cy -2.0;
            //test//System.out.println(i+"\t Ca \t"+Ca);
            dlpy = dlpy +((DLSOC[i]-dlpy-alphas)*Fbp/Spy)*Dt;
            //test//System.out.println(i+"\t dlpy \t"+dlpy);
            dlpa = dlpa + (Fyop*(dlpy-dlpa)/Spa)*Dt;
            //test//System.out.println(i+"\t dlpa \t"+dlpa);
            dlsy = dlsy+((DLSOC[i]-dlsy)*Fbs/Ssy)*Dt;
            //test//System.out.println(i+"\t dlsy \t"+dlsy);
            dlsa = dlsa + (Fyos*(dlsy-dlsa)/Ssa)*Dt;
            //test//System.out.println(i+"\t dlsa \t"+dlsa);
            dlgy=dlgy+((DLCOC[i]-dlgy-alphac[i])*Fbg/Gy)*Dt;
            //test//System.out.println(i+"\t dlgy \t"+dlgy);
            dlga =dlga+(Fyog*(dlgy-dlga)/Ga)*Dt;
            //test//System.out.println(i+"\t dlga \t"+dlga);
            dlcy=dlcy+((DLCOC[i]-dlcy)*Fbc/Cy)*Dt;
            //test//System.out.println(i+"\t dlcy \t"+dlcy);
            dlca=dlca+(Fyoc*(dlcy-dlca)/Ca)*Dt;
            //test//System.out.println(i+"\t dlca \t"+dlca);

            
            /**
             * STARTING HERE IS NEW CODE FOR geocarbsulf volc
             * fAD is total runoff factor for silicate weathering
             * obtained with GCM simulations and continental reconstructions
             */
            Roc = (Sr[i]/10000.0)+0.7;
            //test//System.out.println(i+"\t Roc \t"+Roc);
            
            // vary NV from 0 to 0.015
            // NV is a parameter that scales the influence of volcanic weathering on Sr-87/86 ratios.
            // NV=0 means no accelerated weathering of volcanic rocks, as opposed to plutonic/metamorphic
            //     weathering on Sr-87/86 ratios.
            // NV=0 means no accelerated weathering of volcanic rocks, 
            //     as opposed to plutonic/metamorphic
            // NV=0.015; //This value is preferred by park/royer
            Rg =0.722-NV*(1.0-fRT[i]/1.063);
            //test//System.out.println(i+"\t Rg \t"+Rg);
            Rv =0.704;
            // VNV is the ratio of volcanic weathering CO2-consumption rate (basalts) 
            // to plutonic weathering CO2-consumption rate (granites)
            // VNV=4 in the version of code used for 2010 GoldSchmidt conference poster.
            // obtained by averaging values from Taylor's dissertation (5) and Taylor et al 1999 (3)
            // VNV=5 is Berner's preferred value for GEOCARBSULF,
            // gotten from Aaron Taylor's dissertation
            // VNV=4.0; //this value is preferred by park/royer
            // Fv0 =.3 of total Fwsi0=6.67 at x=0; FOB0 is basalt-seawater 
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
            * the parameter FOB0 (from 0.75 to 4), and switching alphac(i) from a table of values to 
            * being computed from the model-generated oxygen time series.  The Newton-Raphson rootfinding 
            * algorithm works OK with estimating the alphac(i) parameter from model-derived oxygen, but 
            * the quasi-bisection routine had difficulty with this last feature.  For this reason, 
            * and the previous experience that the bisection and Newton-Raphson results were not 
            * dramatically different (largest differences were for unlikely parameter choices where the 
            * datafit was already poor.), I retain Newton-Raphson for the export codes.
            */
            //FOB0=4.0; //Park/Royer preferred value
            // FOB0=FOBO  --- this was an option to sample a range of values in the K2 loop
            Xvolc0=0.35;
            Fwsi0 =6.67;
            Rcy = Rcy +((Roc-Rcy)*Fbc/Cy)*Dt;
            //test//System.out.println(i+"\t Rcy \t"+Rcy);
            Rca = Rca +((Rcy-Rca)*Fyoc/Ca)*Dt;
            //test//System.out.println(i+"\t Rca \t"+Rca);
            Avlc =((Rv-Roc)*fSr[i]*FOB0)/(Fbc-Fwcy-Fwca);
            //test//System.out.println(i+"\t Avlc \t"+Avlc);
            Bvlc = Fwcy/(Fbc-Fwcy-Fwca);
            //test//System.out.println(i+"\t Bvlc \t"+Bvlc);
            Dvlc = Fwca/(Fbc-Fwcy-Fwca);
            //test//System.out.println(i+"\t Dvlc \t"+Dvlc);
            Evlc = Fbc/(Fbc-Fwcy-Fwca);
            //test//System.out.println(i+"\t Evlc \t"+Evlc);
            Xvolc= (Avlc +Bvlc*Rcy+Dvlc*Rca -Evlc*Roc+Rg)/(Rg-Rv);
            //test//System.out.println(i+"\t Xvolc \t"+Xvolc);
            fAD[i]=fA[i]*fD[i];
            //test//System.out.println(i+"\t fAD \t"+fAD[i]);
            fvolc=(VNV*Xvolc+1.0-Xvolc)/(VNV*Xvolc0+1.0-Xvolc0);
            //test//System.out.println(i+"\t fvolc \t"+fvolc);
            fB=(Fbc-Fwcy-Fwca)/(Math.pow(fAD[i],(0.65))*fE*fR[i]*fvolc*Fwsi0);
            //test//System.out.println(i+"\t fB \t"+fB);

            // ENDING HERE IS NEW CODE FOR geocarbsulf volc
            
            int n=0;
            
            
            
            RCO2=10;
            
            //System.out.print(i+":+\t");
            if (i>381)
            {

                // this first step is an initialization kluge to ensure that the convergence 
                // is not satisfied accidentally at the first step
                RCO2old=RCO2*2.0;
                
                while(Math.abs(RCO2/RCO2old-1.0)>0.001)
                {
                    RCO2old=RCO2;
                    Fbbs= Math.pow(RCO2,(0.5+ACT*GCM))*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*fac+RT*GEOG[i]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    W=((0.5+ACT*GCM)*Math.pow(RCO2,(-0.5+ACT*GCM)))*Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    V=Math.pow(RCO2,(0.5+ACT*GCM))*0.65*Math.pow(1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i],-0.35)*(RT*GCM/RCO2)*Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i]);
                    //System.out.println("W = "+W);
                    //System.out.println("V = "+V);
                    fPBS = W + V;
                    
                    //System.out.print(RCO2+"\t");
                    if(RCO2>((Fbbs-fB)/fPBS))
                    {
                        // damp the iteration to avoid overshoot
                        //System.out.println("a1");
                        RCO2=RCO2-0.9*((Fbbs-fB)/fPBS);
                    }
                    else
                    {
                        // convert the iteration to geometric shrinkage to avoid nonpositive value in overshoot
                        //System.out.println("a2");
                        RCO2=RCO2*0.2;
                    }
                }
            }
            else if((i<=381)&&(i>349))
            {
                RCO2old=RCO2*2.0;
                n=0;
                while(Math.abs(RCO2/RCO2old-1.0)>0.001)
                {
                    RCO2old=RCO2;
                    oldfBBS=Math.pow(RCO2,(0.5+ACT*GCM))*Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    oldW=(0.5+ACT*GCM)*Math.pow(RCO2,(-0.5+ACT*GCM))*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    oldV=Math.pow(RCO2,(0.5+ACT*GCM))*0.65*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i]),(-0.35))*(RT*GCM/RCO2)*Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i]);
                    oldfPBS = oldW + oldV;
                    ewfBBS=(Math.pow(2.0,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))*Math.pow((1+RCO2),(-FERT))*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*fac+RT*GEOG[i]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    ewW=Math.pow(2.0,FERT)*(FERT+ACT*GCM)*Math.pow(RCO2,(FERT+ACT*GCM-1.0))*Math.pow((1.0+RCO2),(-FERT))*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    ewV=(-FERT*Math.pow((1.0+RCO2),(-(1.0+FERT))))*((Math.pow(2,FERT))*Math.pow(RCO2,(FERT+ACT*GCM)))*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    
                    //System.out.println("ewV\tFERT\tRCO2\tACT\tGCM\tRT\tWs\tfac\tGEOG[i-1]");
                    //System.out.println(ewV+"\t"+FERT+" \t "+RCO2+" \t "+ACT+" \t "+GCM+" \t "+RT+" \t "+Ws+" \t "+fac+" \t "+GEOG[i-1]);
                    
                    ewX=0.65*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i]),(-0.35))*(RT*GCM/RCO2)*(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))*Math.pow((1+RCO2),(-FERT))*Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i]);
                    ewfPBS=ewW+ewV+ewX;
                    fBBS=((i-349)/32.0)*oldfBBS + ((381-i)/32.0)*ewfBBS;
                    fPBS=((i-349)/32.0)*oldfPBS + ((381-i)/32.0)*ewfPBS;
                    
                    //System.out.print(RCO2+"\t");
                    if(RCO2>=((fBBS-fB)/fPBS)){
                        //System.out.println("b1");
                        RCO2=RCO2-0.9*((fBBS-fB)/fPBS);
                    }
                        // damp the iteration to avoid overshoot
                    else{
                        //System.out.println("b2");
                        RCO2=RCO2*0.2;
                    }
                        // convert the iteration to geometric shrinkage 
                        // to avoid nonpositive value in overshoot
                    n=n+1;
                }
            }
            else
            {
                RCO2old=RCO2*2.0;
                
                boolean first = true;

                //System.out.println("ifC");
                //while(Math.abs(RCO2/RCO2old-1.0)>0.001) //lowering threshold to .1 does not help (weird...)
                while(Math.abs(RCO2/RCO2old-1.0)>0.001)
                {
                    //System.out.println("Loop test = " + Math.abs(RCO2/RCO2old-1.0));
                    //System.out.println(fB);
                    //System.out.println("RCO2old = " + RCO2old);
                    //System.out.println("RCO2 = " + RCO2);
                    double older = RCO2old;
                    RCO2old=RCO2;
                    Fbbs=(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))*Math.pow((1.0+RCO2),(-FERT))*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+ RT*GEOG[i]),.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    W=Math.pow(2.0,FERT) * (FERT+ACT*GCM) * Math.pow(RCO2,(FERT+ACT*GCM-1.0)) * Math.pow((1.0+RCO2),(-FERT)) * Math.pow((1.0+RT*GCM*Math.log(RCO2) - RT*Ws*(fac)+RT*GEOG[i]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    V=(-FERT*Math.pow((1.0+RCO2),(-1.0*(1.0+FERT))))*(Math.pow(2.0,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i]),0.65)*Math.exp(-1.0*ACT*Ws*(fac))*Math.exp(ACT*GEOG[i]);
                    X=0.65 * Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i]),(-0.35)) * (RT*GCM/RCO2) * (Math.pow(2.0,FERT)*Math.pow(RCO2,(FERT+ACT*GCM))) * Math.pow((1.0+RCO2),(-1.0*FERT)) * Math.exp(-1.0*ACT*Ws*fac) * Math.exp(ACT*GEOG[i]);
                    
                    fPBS=W+V+X;
                    //System.out.println("Fbbs/fPBS/fB: "+Fbbs + "\t" + fPBS + "\t" + fB);
                    //System.out.println("W/V/X: "+W + "\t" + V + "\t" + X);
                    //errorStr.append(i+":\t"+fL[i]+" :\t"+RCO2+"\t"+((Fbbs-fB)/fPBS)+"\t"+Fbbs+"\t"+fB+"\t"+fPBS+"\n");
                    if(RCO2>((Fbbs-fB)/fPBS)){
                        //System.out.println("c1");
                        RCO2=RCO2-0.9*((Fbbs-fB)/fPBS);
                        // damp the iteration to avoid overshoot
                    }
                    else
                    {
                        RCO2=RCO2*0.2;
                        // convert the iteration to geometric shrinkage 
                        // to avoid nonpositive value in overshoot
                        //errorStr.append("c2\t");
                        //printErr=true;
                    }
                }
            }
            if (RCO2<=0)
                RCO2 = .05;
                
            //test//System.out.println(i+"RCO2: "+RCO2);
            //test//System.out.println(i+"OXY: "+oxy);
            
            
            
            
            
            
            // the CO2 ppm is converted from RCO2 by last My average value = 250 ppm
            
            //double oxy2 =100*(oxy/(oxy+143.0));
            //System.out.println("RCO2: "+RCO2);
            //System.out.println("i = "+i);
            //System.out.println("RCO2= "+RCO2);
            //the CO2 ppm is converted from RCO2 by last My average value = 250 ppm
            double tau=15 + 6*Math.log(RCO2)-12.8*fac+GEOG[i];
            oxy2 =100.0*(oxy/(oxy+143.0));
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
                
                // save CO2 level (ppm) or oxygen (oxy2) (mass percent)
                oxyStore[k-1]=oxy2;
                co2Store[k-1] = ppm;
                xvolcStore[k-1] = Xvolc;
            }
            gcsppm[i]=ppm;
            //aage[i]=i-1; //in fortran code, aage is an array of length 800
        }
        System.out.println(Arrays.toString(co2Store));
        //System.out.println();
        if (printErr)
        System.out.println(errorStr);
        
        return new GCResults(co2Store, oxyStore, xvolcStore);
    }
}