import java.sql.*;
import com.mysql.jdbc.jdbc2.optional.*;
import factorinput.*;


public class SQLWriter
{
    Connection con;
    
    private static final String headConstant= "INSERT INTO gcRuns(run_id, exp_id, act, fert, life, gym, glac, deltat, nv, vnv, fb0, pl, gas) VALUES ";
    private static final String headArray = "INSERT INTO gcRunArrays(run_id, time_id, CO2, oxy, xvolc, fA, fD, fL, dlS, dlC, fSR, Sr, temp) VALUES ";
    
    private StringBuffer bufferConstant, bufferArray;
    
    private static int exp_id = 0;
    private static int param_id = 0;
    private int bufferlim;
    private int buffercount;
    /**
     * Default Constructor
     */
    public SQLWriter()
    {
        System.out.println("constructed");
        buffercount=0;
        bufferlim=10000;
        
        bufferConstant = new StringBuffer(headConstant);
        bufferArray = new StringBuffer(headArray);
        
        String url = "jdbc:mysql://localhost:3306/geocarb";
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url,"root","");
            System.out.println("Connected to the database");
        } catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
    public void newExperiment(ExperimentData defaults, String name, String description, int exp_id)
    {
        try {
            this.exp_id=exp_id-1;
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery("select max(run_id) as mx from gcRuns");
            int max=0;
            while (rs.next())
            {
                max = rs.getInt("mx");
            }
            this.param_id=max+1;
            newExperiment(defaults, name, description);
        }
        catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
    public void newExperiment(ExperimentData defaults, String name, String description)
    {
        System.out.println("new experiment");
        
        exp_id++;
        
        try {
            //add header
            Statement st = con.createStatement();
            String str = "INSERT INTO experiments(exp_id, exp_name, center_id, scale_id, max_id, min_id) VALUES ("+exp_id+", '"+name+"', "+param_id+","+(param_id+1)+","+(param_id+2)+","+(param_id+3)+")";
            System.out.println(str);
            st.executeUpdate(str);
            
            
            //constants
            StringBuffer s = new StringBuffer("");
            /*
             * s.append("INSERT INTO gcRuns(run_id, act, fert, life, gym, glac, deltat, nv, vnv, fb0) VALUES("+param_id+","+defaults.strConstantCenters()+","+defaults.strVolcCenters()+"), ");
             * s.append("("+(param_id+1)+","+defaults.strConstantDevs()+","+defaults.strVolcDevs()+"), ");
             * s.append("("+(param_id+2)+","+defaults.strConstantMax()+","+defaults.strVolcMax()+"), ");
             * s.append("("+(param_id+3)+","+defaults.strConstantMin()+","+defaults.strVolcMin()+")");
             * str=s.toString();
             * str=str.replaceAll("Infinity", ""+Double.MAX_VALUE);
             * str=str.replaceAll("-Infinity", ""+-1*Double.MAX_VALUE);
             * System.out.println(str);
             * st.executeUpdate(str);
             * 
             * s = new StringBuffer("");
             * s.append("INSERT INTO gcRunArrays(run_id, time_id, fA, fD, fL, dlS, dlC) VALUES");
             * for (int i=0; i<58; i++) {
             *     s.append("("+param_id+","+defaults.strArrayCenters(i)+"), ");
             *     s.append("("+(param_id+1)+","+defaults.strArrayDevs(i)+"), ");
             *     s.append("("+(param_id+2)+","+defaults.strArrayMax(i)+"), ");
             *     s.append("("+(param_id+3)+","+defaults.strArrayMin(i)+")");
             *     if (i<57)
             *      s.append(", ");
             *  }
             *  str=s.toString();
             *  str=str.replaceAll("Infinity", ""+Double.MAX_VALUE);
             *  str=str.replaceAll("-Infinity", ""+-1*Double.MAX_VALUE);
             *  System.out.println(str);
             *  st.executeUpdate(str);
             *  param_id=param_id+4;
             */
        }
        catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
    public void includeRun(FactorData f)
    {
        if (bufferlim<=buffercount)
            flushBuffer();
        
        if (buffercount > 0)
            bufferConstant.append(", ");
        bufferConstant.append("("+param_id+","+exp_id+", "+ f.getACT() +", "+f.getFERT()+", "+f.getLIFE()+", "+f.getGYM()+", "+f.getGLAC()+", "+f.getDELTAT()
            +", "+f.getNV()+", "+f.getVNV()+", "+f.getFB0()+","+f.getPL()+","+f.getGAS()+")");
        
        int len = f.size();
        for(int t = 0; t<len; t++)
        {
            if (buffercount > 0 || t>0)
                bufferArray.append(", ");
            bufferArray.append("("+param_id+", "+(t*10)+", "+f.getCO2(t)+","+f.getOxy(t)+","+f.getXVolc(t)+", "+f.getFA(t)+", "+f.getFD(t)+", "+f.getFL(t)+", "+f.getDLS(t)+", "+f.getDLC(t)+","+f.getFSR(t)+","+f.getSr(t)+","+f.getTemp(t)+")");
        }
        
        buffercount++;
        param_id++;
    }
    
    public void includeDistribution(VariedFactor v, int e_id)
    {
        try {
            Statement st = con.createStatement();
            String str = "INSERT INTO deviations VALUES("+e_id+", '"+v.getVariableName()+"' , '"+v.getType()+"' ,"+v.getCenter()+","+v.getScale()+","+v.getMax()+","+v.getMin()+")";
            str=str.replaceAll("Infinity", ""+Double.MAX_VALUE);
            str=str.replaceAll("-Infinity", ""+-1*Double.MAX_VALUE);
            System.out.println(str);
            st.executeUpdate(str);
        }
        catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
    public void flushBuffer()
    {
        System.out.println("flushBatch updating");
        try {
            Statement st = con.createStatement();
            st.executeUpdate(bufferConstant.toString());
            System.out.println("constants flushed");
            String array=bufferArray.toString();
            array=array.replaceAll("NaN","-50");
            st.executeUpdate(array);
            System.out.println("arrays flushed");
        }
        catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
        clearBuffer();
    }
    
    public void clearBuffer(){
        buffercount = 0;
        bufferConstant = new StringBuffer(headConstant);
        bufferArray = new StringBuffer(headArray);
    }
    
    public void setBufferLimit(int lim)
    {
        bufferlim = lim;
    }
    
    public void close()
    {
        try {
            flushBuffer();
            con.close();
            System.out.println("Disconnected from database");
        } catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
    public void doThing(String s)
    {
        
        try {
            //add header
            Statement st = con.createStatement();
            System.out.println(s);
        }
        catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}