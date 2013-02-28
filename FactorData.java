import co2histograph.*;
import arrayinput.*;

import java.util.Arrays;

/**
 * This class will run a simulation for a specific set of values for act, fert, life, gym, and glac.
 */
public class FactorData implements HistData
{
    private static int runid=-1;

    private double act, fert, life, gym, glac, deltat;
    private double nv, vnv, fb0;
    private double pl, gas;
    
    private double[] co2, xvolc, oxy;
    private float errorThreshold;
    private int myRunID;
    
    private double[] fA, fD, fL, fSR, Sr, temp, DLS, DLC;
    
    public FactorData(double deltat, double act, double fert, double life, double gym, double glac, 
        double nv, double vnv, double fb0, double pl, double gas,
        ArrayData arrDat)
    {
        myRunID = runid;
        runid++;
        
        this.deltat = deltat;
        this.act = act;
        this.fert = fert;
        this.life = life;
        this.gym = gym;
        this.glac = glac;
        this.nv=nv;
        this.vnv=vnv;
        this.fb0=fb0;
        
        this.fA = arrDat.getFA0();
        this.fD = arrDat.getFD0();
        this.fL = arrDat.getFL0();
        this.DLS = arrDat.getDLS0();
        this.DLC = arrDat.getDLC0();
        
        this.temp = arrDat.getTemp0();
        this.fSR = arrDat.getFSR0();
        this.Sr = arrDat.getSr0();
        
        this.pl = pl;
        this.gas = gas;
        
        GCResults r = CO2Simulate.doCO2Calc(deltat, act, fert, life, gym, glac, 
            nv, vnv, fb0, pl, gas,
            fA, fD, fL, fSR, Sr, temp, DLS, DLC);
        this.co2=r.getCO2();
        this.xvolc=r.getXVolc();
        this.oxy=r.getOxy();
    }
    public int getRunID()
    {
        return myRunID;
    }
    
    public int size()
    {
        return co2.length;
    }

    public boolean equals(Object other)
    {
        if (this==other)
            return true;
        if (!(other instanceof FactorData))
            return false;
        FactorData o = (FactorData) other;
        return ((this.getDELTAT()==o.getDELTAT())&&
                (this.getACT()==o.getACT())&&
                (this.getFERT()==o.getFERT())&&
                (this.getLIFE()==o.getLIFE())&&
                (this.getGYM()==o.getGYM())&&
                (this.getGLAC()==o.getGLAC()));
    }

    /**
     * Returns a hash code for this <code>CustomFVR</code> object.  The factor values from the construction of this Object are placed
     * in an array: {deltaT, ACT, FERT, LIFE, GYM, GLAC}.  The hashCode of this object is calculated from the result
     * of java.util.Arrays.hashCode(double[]), such that the input is the previously defined array.
     */
    public int hashCode()
    {
        double[] factors = {deltat, act, fert, life, gym, glac, nv, vnv, fb0};
        return Arrays.hashCode(factors);
    }
    
    
    /*
     * Getter methods for factors
     */
    public double getACT() {
        return act;
    }
    public double getFERT() {
        return fert;
    }
    public double getLIFE() {
        return life;
    }
    public double getGYM() {
        return gym;
    }
    public double getGLAC() {
        return glac;
    }
    public double getDELTAT() {
        return deltat;
    }
    
    public double getNV() {
        return nv;
    }
    public double getVNV() {
        return vnv;
    }
    public double getFB0() {
        return fb0;
    }
    
    public double getPL() {
        return pl;
    }
    public double getGAS() {
        return gas;
    }

    
    public double getCO2(int i){
        return co2[i];
    }
    public double[] getCO2(){
        return co2;
    }
    public double getOxy(int i){
        return oxy[i];
    }
    public double[] getOxy(){
        return oxy;
    }
    
    public double getXVolc(int i){
        return xvolc[i];
    }
    public double[] getXVolc(){
        return xvolc;
    }
    
    public double getFA(int i){
        return fA[i];
    }
    public double[] getFA(){
        return fA;
    }
    public double getFD(int i){
        return fD[i];
    }
    public double[] getFD(){
        return fD;
    }
    public double getFL(int i){
        return fL[i];
    }
    public double[] getFL(){
        return fL;
    }
    public double getTemp(int i){
        return temp[i];
    }
    public double[] getTemp(){
        return temp;
    }
    public double getFSR(int i){
        return fSR[i];
    }
    public double[] getFSR(){
        return fSR;
    }
    public double getSr(int i){
        return Sr[i];
    }
    public double[] getSr(){
        return Sr;
    }
    public double getDLS(int i){
        return DLS[i];
    }
    public double[] getDLS(){
        return DLS;
    }
    public double getDLC(int i){
        return DLC[i];
    }
    public double[] getDLC(){
        return DLC;
    }
}