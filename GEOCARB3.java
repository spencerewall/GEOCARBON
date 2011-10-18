import java.io.PrintWriter;
import java.io.IOException;

public class GEOCARB3
{
    static int Dt, St, CT;
    static double Ws, dlst, dlct, kwpy, kwsy, kwgy, kwcy, Fwpa1, Fwsa1, Fwga1, Fwca1, Fmg1, Fmc1, Fmp1, Fms1, ZM, RBAS, FBASO, RRIV, FRIV;
    
    static double[] Sr, fSr;
    static double[] Bas=new double[600], fR=new double[600], DELTOT=new double[600], DELRIV=new double[600], DELBAS=new double[600], SRBAS=new double[600],
     R=new double[600], xx=new double[600], aage=new double[600], fA=new double[600], fD=new double[600], temp=new double[600], DLSOC=new double[600], 
     DLCOC=new double[600], alphac=new double[600], fL=new double[600], fAD=new double[600], fRT=new double[600];
     
    static double[] gcsppm = new double[800];
    static double[][][] jkk = new double[10][4][2];
    static double[][][][][] pp = new double[10][10][10][10][60];
    
    static GcsvFormat sixExp = new GcsvFormat("'0'.000000E00");
    static GcsvFormat fiveExp = new GcsvFormat("'0'.00000E00");
    
    public static void doStuff(int ndat, double[] pppm, double[] dppm, int[] iage)
    {
        try {
            PrintWriter output = new PrintWriter("out_gcsv10.dat");
            double sumsq0=0.0;
            double diff=0;
            double stdev_dof=0;
            for(int i=0; i<ndat; i++)
            {
                diff=Math.log(250.0/pppm[i])/dppm[i];
                sumsq0=sumsq0+Math.pow(diff,2);
            }
            stdev_dof=Math.sqrt(sumsq0/ndat);
            System.out.println("baseline variance, stdev/dof"+"\t"+sumsq0+"\t"+stdev_dof);
            System.out.println("early diff"+"\t"+diff);
            /**output.println(sixExp.format(sumsq0)+"\t"+sixExp.format(stdev_dof));*/
            output.println(sumsq0+"\t"+stdev_dof);
            
            initializeVars();
            
            
            for (int it=570; it>=0; it--)
            {
                // test case would be fSR(T)=1
                // Bas calculated oceanic Sr-isotope ratio for basalt-seawater reactions
                // starts with Bas(571)=0.709
                double FBAS=FBASO*fSr[it];
                if (it<570) 
                    Bas[it]=Bas[it+1]+DELTOT[it+1];
                else if (it==570) 
                    Bas[it]=0.709;
                
                // fR is dimensionless effect of mountain uplift on CO2 uptake by silicate weathering
                if(it==570)
                    fR[it]=1;
                DELRIV[it]=((RRIV-Bas[it])/ZM)*FRIV;
                DELBAS[it]=((RBAS-Bas[it])/ZM)*FBAS;
                DELTOT[it]=DELBAS[it]+DELRIV[it];
                
                // R is the actual measured Sr-isotope ratio
                R[it]=0.7+Sr[it]/10000.0;
                
                // SRBAS is basalt-seawater Sr-isotope normalization, see Berner (2004) eq 2.1
                SRBAS[it]=(Bas[it]-0.7)*10000;
                
                /**!!!!!!!!!!!!!!!!11111******** LOOKBAKC AT ME!!!**/
                // PL is Berner's adjustable empirical parameter L for Sr-ratio vs. silcate weathering
                // PL=2 obtains approximate fit between Sr-isotopes and physical erosion estimates 
                // from siliclastic sediments, see Berner (2004)
                int PL=2;
                
                // Sr is the normalized difference of basalt-predicted and measured Sr isotope ratios
                // fR is dimensionless effect of mountain uplift on CO2 uptake by silicate weathering
                xx[it]=1-(it+1);
                fR[it]=1-PL*(1-(Sr[it]/SRBAS[it]));
            }
            
            // fill the other arrays
            fillAccessoryArrays();
            
            for (int i=0; i<=578; i++)
            {
                xx[i]=-i;
                
                //cubic fit to Ronov sediment data
                fRT[i]=25.269*Math.pow((-i)/1000.0,3) + 26.561*Math.pow((-i)/1000.0,2) 
                       + 6.894*((-i)/1000.0) + 1.063;
                fR[i]=Math.pow(fRT[i]/1.063,0.67);
            }
            //System.out.println("fRT");
            //ArrayUtils.printArrayI(fRT);
            //System.out.println("fR");
            //ArrayUtils.printArrayI(fR);
            
            int ndel=28;
            double deltaT=.28;
            double GYM=0, LIFE=0, ACT=0, FERT=0;
            for (int iiit=0; iiit<ndel; iiit++)
            {
                deltaT = deltaT+0.12*(1.0+4.0*((float) (iiit))/((float) (ndel-1)));
                //System.out.println("DeltaT = "+deltaT);
                // GCM is the DT for CO2-doubling, divided by ln 2, so when you mult by log(RCO2)
                // you obtain deltaT when RCO2=2
                double GCM0 = deltaT/Math.log(2.0);
                //System.out.println("GCM0 = "+GCM0);
                double varredmin=1000.0;
                //System.out.println("varredmin = "+varredmin);
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
                        double GLAC=1.0;
                        for (int k3=10;k3>=1;k3--)
                        {
                            // assume vascular-plant acceleration of weathering lies between factor of 2 and 10
                            LIFE=0.1+(k3-1)*0.4/9.0;
                            for (int k4=10; k4>=1; k4--)
                            {
                                // assume gymnosperms accelerate weathering by 0.5 to 1.2, relative to angiosperms
                                GYM=0.5+(k4-1)*0.7/9.0;
                                //oxy is oxygen level in atmosphere, in percent mass (Berner 2009)
                                double oxy=25.0;
    
                                // RCO2 is ratio of CO2 to pre-industrial level.  initial RCO2 level in 
                                // the calculation is moot, because it will be solved for
                                double RCO2 =10.0;
                                double Spy=20.0;
                                double Spa=280.0;
                                double Ssy=150.0;
                                double Ssa=150.0;
                                double Gy=250.0;
                                double Ga=1000.0;
                                double Cy=1000.0;
                                double Ca=4000.0;
                                double dlsy=35.0;
                                double dlcy=3.0;
                                double dlpy=-10.0;
                                double dlpa=-10.0;
                                double dlsa= (dlst*St-(dlpy*Spy+dlsy*Ssy+dlpa*Spa))/Ssa;
                                double dlgy= -23.5;
                                double dlga=-23.5;
                                double dlca= (dlct*CT-(dlgy*Gy  +dlcy*Cy  +dlga*Ga))/Ca;
                                double Rcy=0.7095;
                                double Rca=0.709;
                                double[] GEOG=new double[600];
    
                                // iberner is a counter for correspondence to earlier codes
                                // I decimate by 10 to match the 10My-averaged CO2 values
                                //double[] fAD = new double[600];
                                //double[] gcsppm = new double[800];
                                for (int iberner=571; iberner>=1; iberner--)
                                {
                                    int i=iberner;
                                    double GCM=GCM0;
                                    
                                    // Dana Royer's suggested glacial intervals
                                    //if((i<=363)&&(i>=346)) GCM=GCM0*GLAC
                                    //if((i<=30)&&(i>=0)) GCM=GCM0*GLAC
                                    //if((i<=327)&&(i>=290)) GCM=GCM0*GLAC
    
                                    // Bob's GEOCARB GLACIAL INTERVALS
                                    if ((i<=340) && (i>=260))
                                        GCM=GCM0*GLAC;
                                    if ((i<=40) && (i>=0))
                                        GCM=GCM0*GLAC;
    
                                        // Bob's ALTERNATE GEOCARB GLACIAL INTERVALS
                                        //if((i<=340)&&(i>=240)) GCM=GCM0*GLAC
                                        //if((i<=30)&&(i>=0)) GCM=GCM0*GLAC
                                        // glacial intervals in Bob's GEOCARB code -- reverse Glacial and interglacial
                                        //if(i>340) GCM=GCM0/GLAC
                                        //if ((i<=260)&&(i>=40)) GCM=GCM0/GLAC
                                    double fac=(i-1)/570.0;
    
                                    // RT is Y in Berner (2004), & RUN in Berner and Kothavala (2001)
                                    // is the coefficient of continental runoff versus temperature change
                                    // that is, runoff/runoff0 = 1 + RT*(T-T0), gotten from GCM runs
                                    double RT;
                                    if(i>341)
                                        RT=0.025;
                                    else if((i<=341)&&(i>261)) 
                                        RT=0.045;
                                    else if((i<=261)&&(i>41))
                                        RT=0.025;
                                    else
                                        RT=0.045;
                                    double GAS=0.75;
                                    double Fc;
                                    if(i>151)
                                        Fc=GAS;
                                    else
                                        Fc=(GAS)+((1.0-GAS)/150.)*(151-i);
                                        // fE is the factor of plant-assisted silicate weathering, 
                                        // relative to modern angiosperms
                                    double fE;
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
                                    double fBB;
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
    
                                    double Fwpy=fA[i-1]*fR[i-1]*kwpy*Spy;
                                    double Fwsy=fA[i-1]*fD[i-1]*kwsy*Ssy;
                                    double Fwgy=fA[i-1]*fR[i-1]*kwgy*Gy;
                                    double Fwcy=fA[i-1]*fD[i-1]*fL[i-1]*fE*fBB*kwcy*Cy;
                                    double Fwpa=fR[i-1]*Fwpa1;
                                    double Fwsa=fA[i-1]*fD[i-1]*Fwsa1;
                                    double Fwga=fR[i-1]*Fwga1;
                                    double Fwca=fA[i-1]*fD[i-1]*fL[i-1]*fE*fBB*Fwca1;
                                    double Fmp=fSr[i-1]*Fmp1;
                                    double Fms=fSr[i-1]*Fms1;
                                    double Fmg=fSr[i-1]*Fmg1;
                                    double Fmc=fSr[i-1]*Fc*Fmc1;
                                    double Fyop=Fwpa+Fmp;
                                    double Fyos=Fwsa+Fms;
                                    double Fyog=Fwga+Fmg;
                                    double Fyoc=Fwca+Fmc;
                                    double zzn=1.5;
                                    double aaJ=4.0;
                                    double alphas =35.0*Math.pow(oxy/38.0,zzn);
                                    
                                    /**
                                      * Start Test!!!
                                      */
                                    alphac[i-1]=27.0+aaJ*(oxy/38.0-1.0);
                                    double Fbp = (1/alphas)*((DLSOC[i-1]-dlsy)*Fwsy+(DLSOC[i-1]-dlsa)*Fwsa+(DLSOC[i-1]-dlpy)*Fwpy+(DLSOC[i-1]-dlpa)*Fwpa +(DLSOC[i-1]-dlsa)*Fms +(DLSOC[i-1]-dlpa)*Fmp);
                                    
//Fbg is burial Cflux of organic sediments  
                                    double Fbg=(1/alphac[i-1])*((DLCOC[i-1]-dlcy)*Fwcy+(DLCOC[i-1]-dlca)*Fwca+(DLCOC[i-1]-dlgy)*Fwgy+(DLCOC[i-1]-dlga)*Fwga +(DLCOC[i-1]-dlca)*Fmc+(DLCOC[i-1]-dlga)*Fmg);
                                    
                                    double Fbs=Fwpy+Fwpa+Fwsy+Fwsa +Fms +Fmp-Fbp;
                                    //Fbc is burial Cflux of carbonate sediments    
                                    double Fbc=Fwgy+Fwga+Fwcy+Fwca +Fmc +Fmg-Fbg;
                                    
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
                                    double Roc = (Sr[i-1]/10000.0)+0.7;
                                    // vary anv from 0 to 0.015, is Berner parameter NV 
                                    // anv is a parameter that scales the influence of volcanic 
                                    //     weathering on Sr-87/86 ratios.
                                    // anv=0 means no accelerated weathering of volcanic rocks, 
                                    //     as opposed to plutonic/metamorphic
                                    double anv=0.015;
                                    double Rg =0.722-anv*(1-fRT[i-1]/1.063);
                                    double Rv =0.704;
                                    // avnv is the ratio of volcanic weathering CO2-consumption rate (basalts) 
                                    // to plutonic weathering CO2-consumption rate (granites)
                                    // avnv=4 in the version of code used for 2010 GoldSchmidt conference poster.
                                    // obtained by averaging values from Taylor's dissertation (5) and Taylor et al 1999 (3)
                                    // avnv=5 is Berner's preferred value for GEOCARBSULF,
                                    // gotten from Aaron Taylor's dissertation
                                    double avnv=4.0;
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
                                    double Fob0=4.0;
                                    // Fob0=FOBO  --- this was an option to sample a range of values in the K2 loop
                                    double Xvolc0=0.35;
                                    double Fwsi0 =6.67;
                                    Rcy = Rcy +((Roc-Rcy)*Fbc/Cy)*Dt;
                                    Rca = Rca +((Rcy-Rca)*Fyoc/Ca)*Dt;
                                    double Avlc =((Rv-Roc)*fSr[i-1]*Fob0)/(Fbc-Fwcy-Fwca);
                                    double Bvlc = Fwcy/(Fbc-Fwcy-Fwca);
                                    double Dvlc = Fwca/(Fbc-Fwcy-Fwca);
                                    double Evlc = Fbc/(Fbc-Fwcy-Fwca);
                                    double Xvolc= (Avlc +Bvlc*Rcy+Dvlc*Rca -Evlc*Roc+Rg)/(Rg-Rv);
                                    fAD[i-1]=fA[i-1]*fD[i-1];
                                    double fvolc=(avnv*Xvolc+1.0-Xvolc)/(avnv*Xvolc0+1.0-Xvolc0);
                                    double fB=(Fbc-Fwcy-Fwca)/(Math.pow(fAD[i-1],0.65)*fE*fR[i-1]*fvolc*Fwsi0);
                                    // ENDING HERE IS NEW CODE FOR geocarbsulf volc
                                    
                                    //System.out.println("Fb, Fbc, Fwcy, Fwca, fAD(i), fE, fR(i), fvolc, Fwsi0");
                                    //System.out.println(fB+"\t"+Fbc+"\t"+Fwcy+"\t"+Fwca+"\t"+fAD[i-1]+"\t"+fE+"\t"+fR[i-1]+"\t"+fvolc+"\t"+Fwsi0);
                                    
                                    
                                    int n=0;
                                    double RCO2old=RCO2*2.0; /** new line */
                                    //System.out.println("i: "+i);
                                    //System.out.println("RCO2, ACT, GCM, RT, Ws, fac, GEOG[i], fac");
                                    //System.out.println(RCO2+"\t"+ACT+"\t"+GCM+"\t"+RT+"\t"+Ws+"\t"+fac+"\t"+GEOG[i-1]+"\t"+fac);
                                    if (i>381) 
                                    {
                                        // this first step is an initialization kluge to ensure that the convergence 
                                        // is not satisfied accidentally at the first step
                                        //RCO2old=RCO2*2.0;
                                        //System.out.println("IfA");
                                        double Fbbs;
                                       
                                        double fPBS;
                                        while(Math.abs(RCO2/RCO2old-1.0)>0.001)
                                        {
                                            RCO2old=RCO2;
                                            Fbbs= Math.pow(RCO2,(0.5+ACT*GCM))*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*fac+RT*GEOG[i-1]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            //System.out.println("Fbbs: "+ Fbbs);
                                            
                                            double W=((0.5+ACT*GCM)*Math.pow(RCO2,(-0.5+ACT*GCM)))*Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)*Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            double V=Math.pow(RCO2,(0.5+ACT*GCM))*0.65*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),(-0.35))*(RT*GCM/RCO2)*Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i-1]);
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
                                    // What does this do?!
                                    /**777 format(i5,8g13.5)*/
                                    else if((i<=381)&&(i>349))
                                    {
                                        //System.out.println("ifB");
                                        //RCO2old=RCO2*2.0;
                                        n=0;
                                        while(Math.abs(RCO2/RCO2old-1.0)>0.001)
                                        {
                                            RCO2old=RCO2;
                                            double oldfBBS=Math.pow(RCO2,(0.5+ACT*GCM))*Math.pow((1+RT*GCM*Math.log(RCO2)
                                                -RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                                                *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            double oldW=(0.5+ACT*GCM)*Math.pow(RCO2,(-0.5+ACT*GCM))
                                                *Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)
                                                +RT*GEOG[i-1]),0.65)
                                                *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            double oldV=Math.pow(RCO2,(0.5+ACT*GCM))*0.65*Math.pow((1.0+RT*GCM*Math.log(RCO2)
                                                -RT*Ws*(fac)+RT*GEOG[i-1]),(-0.35))*(RT*GCM/RCO2)
                                                *Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i-1]);
                                            double oldfPBS = oldW + oldV;
                                            double oldX=0.0;
                                            double ewfBBS=(Math.pow(2.0,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))
                                                *Math.pow((1+RCO2),(-FERT))
                                                *Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*fac+RT*GEOG[i-1]),0.65)
                                                *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            double ewW=Math.pow(2.0,FERT)*(FERT+ACT*GCM)*Math.pow(RCO2,(FERT+ACT*GCM-1.0))
                                                *Math.pow((1.0+RCO2),(-FERT))*Math.pow((1.0+RT*GCM*Math.log(RCO2)
                                                -RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                                                *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            double ewV=(-FERT*Math.pow((1.0+RCO2),(-(1.0+FERT))))
                                                *((Math.pow(2,FERT))*Math.pow(RCO2,(FERT+ACT*GCM)))
                                                *Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                                                *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            
                                            //System.out.println("ewV\tFERT\tRCO2\tACT\tGCM\tRT\tWs\tfac\tGEOG[i-1]");
                                            //System.out.println(ewV+"\t"+FERT+" \t "+RCO2+" \t "+ACT+" \t "+GCM+" \t "+RT+" \t "+Ws+" \t "+fac+" \t "+GEOG[i-1]);
                                            
                                            double ewX=0.65*Math.pow((1.0+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),(-0.35))
                                                *(RT*GCM/RCO2)*(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))
                                                *Math.pow((1+RCO2),(-FERT))*Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i-1]);
                                            double ewfPBS=ewW+ewV+ewX;
                                            double fBBS=((i-349)/32.)*oldfBBS + ((381-i)/32.)*ewfBBS;
                                            double fPBS=((i-349)/32.)*oldfPBS + ((381-i)/32.)*ewfPBS;
                                            
                                            //System.out.println("oldfBBS\toldW\toldV\toldfPBS\toldX\tewfBBS\tewW\tewV\tewX\tewfPBS\tfBBS\tfPBS");
                                            //System.out.println(oldfBBS+"\t"+oldW+"\t"+oldV+"\t"+oldfPBS+"\t"+oldX+"\t"+ewfBBS+"\t"+ewW+"\t"+ewV+"\t"+ewX+"\t"+ewfPBS+"\t"+fBBS+"\t"+fPBS);
                                            
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
                                            double Fbbs=(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))
                                                *Math.pow((1+RCO2),(-FERT))
                                                *Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                                                *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            double W=Math.pow(2,FERT)*(FERT+ACT*GCM)*Math.pow(RCO2,(FERT+ACT*GCM-1))
                                                *Math.pow((1+RCO2),(-FERT))
                                                *Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                                                *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            double V=(-FERT*Math.pow((1+RCO2),(-(1.+FERT))))*(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))
                                                *Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)+RT*GEOG[i-1]),0.65)
                                                *Math.exp(-ACT*Ws*(fac))*Math.exp(ACT*GEOG[i-1]);
                                            double X=0.65*Math.pow((1+RT*GCM*Math.log(RCO2)-RT*Ws*(fac)
                                                +RT*GEOG[i-1]),(-0.35))*(RT*GCM/RCO2)
                                                *(Math.pow(2,FERT)*Math.pow(RCO2,(FERT+ACT*GCM)))*Math.pow((1+RCO2),(-FERT))
                                                *Math.exp(-ACT*Ws*fac)*Math.exp(ACT*GEOG[i-1]);
                                            double fPBS=W+V+X;
                                            if(RCO2>((Fbbs-fB)/fPBS))
                                                RCO2=RCO2-0.9*((Fbbs-fB)/fPBS);
                                                // damp the iteration to avoid overshoot
                                            else
                                                RCO2=RCO2*0.2;
                                                // convert the iteration to geometric shrinkage 
                                                // to avoid nonpositive value in overshoot
                                        }
                                    }
                                    // the CO2 ppm is converted from RCO2 by last My average value = 250 ppm
                                    double tau=15 + 6*Math.log(RCO2)-12.8*fac+GEOG[i-1];
                                    //double oxy2 =100*(oxy/(oxy+143.0));
                                    //System.out.println("RCO2: "+RCO2);
                                    double ppm=250.0*RCO2;
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
                                        System.out.println("DeltaT\tACT\tFERT\tLife\tGYM\tGLAC\tPPM");
                                        System.out.println(deltaT);
                                        System.out.println(ACT);
                                        System.out.println(FERT);
                                        System.out.println(LIFE);
                                        System.out.println(GYM);
                                        System.out.println(GLAC);
                                        System.out.println(ppm);
                                        pp[k1-1][k2-1][k3-1][k4-1][k-1]=ppm;
                                        // pp[k1][k2][k3][k4][k]=oxy2
                                    }
                                    gcsppm[i-1]=ppm;
                                    //System.out.println(ppm);
                                    aage[i-1]=i-1; /** WTF is this??? Is this needed??? */
                                }
                                // compute variance reduction -- compare variance of residual to variance of data
                                // normalized -- sum(pred-data)^2/sum(data)^2
                                // two quantities saved:  
                                // sqrt(chisq/dof) for datafit of the ndat (40?) interval averaged PPM values
                                // misfit variance normalized by variance explained by constant ppm=250
                                // variance ratio is not square-rooted. 
                                double sumsq=0.0;
                                
                                // note that the array iage() holds the times at which the data-averages have been centered
                                // this enables the code to skip over the gap in the CO2-proxy data at roughly 255Ma.
                                for (int i=1; i<=ndat; i++)
                                {
                                    /**
                                     * Known problem associated with gcsppm?
                                     * Definitely associated with sumsq and varred
                                     */
                                    //System.out.println(gcsppm[iage[i-1]-1]+"\t"+iage[i-1]+"\t"+pppm[i-1]+"\t"+dppm[i-1]);
                                    
                                    diff=Math.log(gcsppm[iage[i-1]-1]/pppm[i-1]) /dppm[i-1];
                                    //diff=alog(gcsppm(iage(i))/pppm(i)) /dppm(i)
                                    
                                    sumsq=sumsq+Math.pow((diff),2);
                                    //System.out.println("diff\tsumsq");
                                    //System.out.println(diff+"\t"+sumsq);
                                }
                                double chisq_dof=Math.sqrt(sumsq/ndat);
                                double varred=sumsq/sumsq0;
                                //System.out.println("sumsq+sumsq0\t"+sumsq+"\t"+sumsq0);
                                pp[k1-1][k2-1][k3-1][k4-1][58]=chisq_dof;
                                pp[k1-1][k2-1][k3-1][k4-1][59]=varred;
                                //System.out.println(""+varredmin+"\t"+pp[k1-1][k2-1][k3-1][k4-1][59]);
                                varredmin=Math.min(varredmin,pp[k1-1][k2-1][k3-1][k4-1][59]);
                                
                            } //end do
                        } //end do
                    } //end do
                    if(k1==10) 
                        System.out.println("GYM\tLIFE\tACT\tFERT\tdeltaT\tvarredmin");
                    System.out.println(GYM+"\t"+LIFE+"\t"+ACT+"\t"+FERT+"\t"+deltaT+"\t"+varredmin);
                } // end do
                // sift through the runs to find acceptable data fits  (chiquare <= NDAT; bias < 0.3)
                for (int k=0; k<2; k++) {
                    for (int j=0; j<4; j++) {
                        for(int i=0; i<10; i++) {
                            jkk[i][j][k]=0;
                        }
                    }
                }
                for (int k1=0; k1<10; k1++){
                    for (int k2=0; k2<10; k2++){
                        for (int k3=0; k3<10; k3++){
                            for (int k4=0; k4<10; k4++){
                                if(pp[k1][k2][k3][k4][59]<=varredmin*1.5)
                                {
                                    jkk[k1][0][0]++;
                                    jkk[k2][1][0]++;
                                    jkk[k3][2][0]++;
                                    jkk[k4][3][0]++;
                                }
                                if(pp[k1][k2][k3][k4][59]<=varredmin*1.2)
                                {
                                    jkk[k1][0][1]++;
                                    jkk[k2][1][1]++;
                                    jkk[k3][2][1]++;
                                    jkk[k4][3][1]++;
                                }
                            }
                        }
                    }
                }
                System.out.println("deltaT=\t"+deltaT+"\t"+iiit+"\t"+ndel);
                System.out.println("minimum chi-squared misfit"+"\t"+varredmin);
                System.out.println("parameters for runs that fit with varred 50% from min");
                System.out.println("ACT");
                for (int i=0; i<10; i++){
                    System.out.print(jkk[i][0][0]+"\t");}
                System.out.println();
                System.out.println("FERT");
                for (int i=0; i<10; i++){
                    System.out.print(jkk[i][1][0]+"\t");}
                System.out.println();
                System.out.println("LIFE");
                for (int i=0; i<10; i++){
                    System.out.print(jkk[i][2][0]+"\t");}
                System.out.println();
                System.out.println("GYM");
                for (int i=0; i<10; i++){
                    System.out.print(jkk[i][3][0]+"\t");}
                System.out.println("   ");
                System.out.println("parameters for runs that fit with varred 20% from min");
                System.out.println("ACT");
                for (int i=0; i<10; i++){
                    System.out.print(jkk[i][0][1]+"\t");}
                System.out.println();
                System.out.println("FERT");
                for (int i=0; i<10; i++){
                    System.out.print(jkk[i][1][1]+"\t");}
                System.out.println();
                System.out.println("LIFE");
                for (int i=0; i<10; i++){
                    System.out.print(jkk[i][2][1]+"\t");}
                System.out.println();
                System.out.println("GYM");
                for (int i=0; i<10; i++){
                    System.out.print(jkk[i][3][1]+"\t");}
                // pause
                System.out.println();
                System.out.println("writing chisq to disk/t"+deltaT);
                //write(14) deltaT;
                /**output.println(sixExp.format(deltaT));*/
                output.println(deltaT);
                String kStr = "";
                int line = 1;
                for (int k1=0;k1<10;k1++) {
                    for (int k2=0;k2<10;k2++) {
                        for (int k3=0;k3<10;k3++) {
                            for (int k4=0;k4<10;k4++)
                            {
                                if (line == 1001)
                                {
                                    System.out.println("k4 = " +k4);
                                    System.out.println("add = " +pp[k1][k2][k3][k4][58]);
                                    System.out.println("kSr = "+kStr);
                                }
                                /**kStr=kStr+sixExp.format(pp[k1][k2][k3][k4][58])+"\t"; */
                                kStr+=pp[k1][k2][k3][k4][58]+"\t";
                                //write(14)((((pp[k1][k2][k3][k4][59],k4=1,10),k3=1,10),k2=1,10),k1=1,10);
                            }
                            line++;
                            output.println(kStr);
                            kStr = "";
                        }
                    }
                }
                //output.println(kStr);
                
                for (int k1=0;k1<10;k1++) {
                    for (int k2=0;k2<10;k2++) {
                        for (int k3=0;k3<10;k3++) {
                            for (int k4=0;k4<10;k4++) 
                            {
                                kStr="";
                                for (int j=0; j<58; j++)
                                {    
                                    if ((j+1)%10==0)
                                    {
                                        output.println(kStr);
                                        kStr="";
                                    }
                                    /** kStr = kStr + sixExp.format(pp[k1][k2][k3][k4][j])+"\t"; */
                                    kStr = kStr + pp[k1][k2][k3][k4][j]+"\t";
                                }
                                output.println(kStr);
                            }
                        }
                    }
                }
                System.out.println("done "+iiit);
                output.flush();
            }
            output.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    
    
    private static double[] fillSr()
    {
        double []Sr = new double[600];
        for(int i=1;i<=20;i++)
        {
            int jj=1+(i-1)*5;
            for (int j=0; j<=4;j++)
            {
                Sr[jj+j-1]=(j*GCSV_Data.Sr0[i]+(5-j)*GCSV_Data.Sr0[i-1])/5.0;
            }
        }
        for (int i=21; i<=67; i++)
        {
            int jj=101+(i-21)*10;
            for (int j=0; j<=9; j++)
            {
                Sr[jj+j-1]=(j*GCSV_Data.Sr0[i]+(10-j)*GCSV_Data.Sr0[i-1])/10.0;
            }
        }
       Sr[570]=GCSV_Data.Sr0[67];
       return Sr;
    }
    private static double[] fillFSr()
    {
        double []fSR = new double[600];
        for(int i=1;i<=28;i++)
        {
            int jj=1+(i-1)*5;
            for (int j=0; j<=4;j++)
            {
                fSR[jj+j-1]=(j*GCSV_Data.fSR0[i]+(5-j)*GCSV_Data.fSR0[i-1])/5.0;
            }
        }
        for (int i=29; i<=71; i++)
        {
            int jj=141+(i-29)*10;
            for (int j=0; j<=9; j++)
            {
                fSR[jj+j-1]=(j*GCSV_Data.fSR0[i]+(10-j)*GCSV_Data.fSR0[i-1])/10.0;
            }
        }
        fSR[570]=GCSV_Data.fSR0[71];
        return fSR;
    }
    private static void fillAccessoryArrays()
    {
        for (int i=0; i<=56; i++)
        {
            //modify?
            int jj=1+(i)*10;
            //modify?
            for (int j=0;j<=9;j++)
            {
                fA[jj+j-1]=(j*GCSV_Data.fA0[i+1]+(10-j)*GCSV_Data.fA0[i])/10.0;
                fD[jj+j-1]=(j*GCSV_Data.fD0[i+1]+(10-j)*GCSV_Data.fD0[i])/10.0;
                temp[jj+j-1]=(j*GCSV_Data.temp0[i+1]+(10-j)*GCSV_Data.temp0[i])/10.0;
                DLSOC[jj+j-1]=(j*GCSV_Data.DLS0[i+1]+(10-j)*GCSV_Data.DLS0[i])/10.0;
                DLCOC[jj+j-1]=(j*GCSV_Data.DLC0[i+1]+(10-j)*GCSV_Data.DLC0[i])/10.0;
                alphac[jj+j-1]=(j*GCSV_Data.al0[i+1]+(10-j)*GCSV_Data.al0[i])/10.0;
                fL[jj+j-1]=(j*GCSV_Data.fL0[i+1]+(10-j)*GCSV_Data.fL0[i])/10.0;
            }
            fA[570]=GCSV_Data.fA0[57];
            fD[570]=GCSV_Data.fD0[57];
            temp[570]=GCSV_Data.temp0[57];
            DLSOC[570]=GCSV_Data.DLS0[57];
            DLCOC[570]=GCSV_Data.DLC0[57];
            alphac[570]=GCSV_Data.al0[57];
            fL[570]=GCSV_Data.fL0[57];
        }
    }
    public static void initializeVars()
    {
            // time step = 1mil yrs
            Dt=1;
            // total cooling due to weaker solar radiation at 570Ma
            Ws=7.4;
            St=600;
            dlst=4;
            CT=6252;
            dlct=-3.5;
            kwpy=0.01;
            kwsy=0.01;
            kwgy=0.018;
            kwcy=0.018;
            Fwpa1=0.25;
            // Fws is the Cflux of weathering silicates
            Fwsa1=0.5;
            // Fwg is carbon flux from sedimentary organic matter
            Fwga1=0.5;
            // Fwc is carbon flux from Ca and Mg carbonates
            Fwca1=2;
            // Fmg is C-degassing from volcanism, metamorphism and diagenesis of organic matter
            Fmg1=1.25;
            
            // Fmc is C-degassing from volcanism, metamorphism and diagenesis of carbonate
            Fmc1=6.67;
            Fmp1=0.25;
            Fms1=0.5;
            ZM=12.5;                     
            RBAS=.703;
            FBASO=.92;
            RRIV=.711;
            FRIV=3.37;
            
            Sr=fillSr();
            fSr=fillFSr();
    }
}
