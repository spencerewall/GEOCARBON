import factorinput.*;
import arrayinput.*;

/**
 * ExperimentData describes the parameters used by a group of experiments run
 * within the geocarb framework.  This includes all parameter settings for input
 * variation.
 * 
 * @author (Spencer J Ewall)
 */
public class ExperimentData
{
    VariedFactor act, fert, life, gym, glac, deltat;
    GaussianArray fa, fd, fl, dls, dlc;
    VariedFactor nv, vnv, fb0, pl, gas;
    
    boolean actBool, fertBool, lifeBool,  gymBool,  glacBool,  deltatBool;
    boolean faBool,  fdBool,  flBool,  dlsBool,  dlcBool;
    boolean nvBool, vnvBool, fb0Bool;
    
    public ExperimentData(VariedFactor act, VariedFactor fert, VariedFactor life, VariedFactor gym, VariedFactor glac, VariedFactor deltat,
        VariedFactor nv, VariedFactor vnv, VariedFactor fb0, VariedFactor pl, VariedFactor gas,
        GaussianArray fA, GaussianArray fD, GaussianArray fL, GaussianArray dlS, GaussianArray dlC)
    {
        this.act=act;
        this.fert=fert;
        this.life=life;
        this.gym=gym;
        this.glac=glac;
        this.deltat=deltat;
        this.fa=fA;
        this.fd=fD;
        this.fl=fL;
        this.dls=dlS;
        this.dlc=dlC;
        this.nv=nv;
        this.vnv=vnv;
        this.fb0=fb0;
        this.pl=pl;
        this.gas=gas;
        actBool=false;
        fertBool=false;
        lifeBool=false;
        gymBool=false;
        glacBool=false;
        deltatBool=false;
        faBool=false;
        fdBool=false;
        flBool=false;
        dlsBool=false;
        dlcBool=false;
        nvBool=false;
        vnvBool=false;
        fb0Bool=false;
    }
    public void setActive(boolean act, boolean fert, boolean life, boolean gym, boolean glac, boolean deltat, 
        boolean nv, boolean vnv, boolean fb0,
        boolean fa, boolean fd, boolean fl, boolean dls, boolean dlc)
    {
        actBool=act;
        fertBool=fert;
        lifeBool=life;
        gymBool=gym;
        glacBool=glac;
        deltatBool=deltat;
        
        faBool=fa;
        fdBool=fd;
        flBool=fl;
        dlsBool=dls;
        dlcBool=dlc;
        
        nvBool=nv;
        vnvBool=vnv;
        fb0Bool=fb0;
    }
    public double[] getCO2(){
        ArrayData aIn = new ArrayData();
        aIn.setFA0(fa.getCenter());
        aIn.setFD0(fd.getCenter());
        aIn.setFL0(fl.getCenter());
        aIn.setDLS0(dls.getCenter());
        aIn.setDLC0(dlc.getCenter());
        FactorData currRun = new FactorData(deltat.getCenter(),
            act.getCenter(),
            fert.getCenter(),
            life.getCenter(),
            gym.getCenter(),
            glac.getCenter(),
            nv.getCenter(),
            vnv.getCenter(),
            fb0.getCenter(),
            pl.getCenter(),
            gas.getCenter(),
            aIn);
        return currRun.getCO2();
    }
    public String strConstantCenters(){
        return ""+act.getCenter()+", "+fert.getCenter()+", "+ life.getCenter()+", "+ gym.getCenter()+", "+ glac.getCenter()+", "+ deltat.getCenter();
    }
    public String strConstantDevs(){
        String str = "";
        
        if (actBool)
            str+=act.getScale();
        else
            str+="0";
        str+=", ";
            
        if (fertBool)
            str+=fert.getScale();
        else
            str+="0";
        str+=", ";
        
        if (lifeBool)
            str+=life.getScale();
        else
            str+="0";
        str+=", ";
            
        if (gymBool)
            str+=gym.getScale();
        else
            str+="0";
        str+=", ";
            
        if (glacBool)
            str+=glac.getScale();
        else
            str+="0";
        str+=", ";
            
        if (deltatBool)
            str+=deltat.getScale();
        else
            str+="0";
            
        return str;
    }
    public String strConstantMax(){
        return ""+act.getMax()+", "+fert.getMax()+", "+ life.getMax()+", "+ gym.getMax()+", "+ glac.getMax()+", "+ deltat.getMax();
    }
    public String strConstantMin(){
        return ""+act.getMin()+", "+fert.getMin()+", "+ life.getMin()+", "+ gym.getMin()+", "+ glac.getMin()+", "+ deltat.getMin();
    }
    
    
    public String strArrayCenters(int i){
        String s = "";
        s+= (i*10)+", "+fa.getCenter()[i]+", "+fd.getCenter()[i]+", "+fl.getCenter()[i]+", "+dls.getCenter()[i]+", "+dlc.getCenter()[i];
        return s;
    }
    public String strArrayMax(int i){
        String s = "";
        s+= (i*10)+", "+fa.getMax()[i]+", "+fd.getMax()[i]+", "+fl.getMax()[i]+", "+dls.getMax()[i]+", "+dlc.getMax()[i];
        return s;
    }
    public String strArrayMin(int i){
        String s = "";
        s+= (i*10)+", "+fa.getMin()[i]+", "+fd.getMin()[i]+", "+fl.getMin()[i]+", "+dls.getMin()[i]+", "+dlc.getMin()[i];
        return s;
    }
    public String strArrayDevs(int i){
        String str = (i*10)+",";
        
        if (faBool)
            str+=(fa.getScale()[i]);
        else
            str+="0";
        str+=", ";
            
        if (fdBool)
            str+=(fd.getScale()[i]);
        else
            str+="0";
        str+=", ";
        
        if (flBool)
            str+=(fl.getScale()[i]);
        else
            str+="0";
        str+=", ";
            
        if (dlsBool)
            str+=(dls.getScale()[i]);
        else
            str+="0";
        str+=", ";
            
        if (dlcBool)
            str+=(dlc.getScale()[i]);
        else
            str+="0";
            
        return str;
    }
    
    public String strVolcCenters(){
        return ""+nv.getCenter()+", "+vnv.getCenter()+", "+ fb0.getCenter();
    }
    public String strVolcDevs(){
        String str = "";
        
        if (nvBool)
            str+=nv.getScale();
        else
            str+="0";
        str+=", ";
            
        if (vnvBool)
            str+=vnv.getScale();
        else
            str+="0";
        str+=", ";
        
        if (fb0Bool)
            str+=fb0.getScale();
        else
            str+="0";
        
        return str;
    }
    public String strVolcMax(){
        return ""+nv.getMax()+", "+vnv.getMax()+", "+ fb0.getMax();
    }
    public String strVolcMin(){
        return ""+nv.getMin()+", "+vnv.getMin()+", "+ fb0.getMin();
    }
}