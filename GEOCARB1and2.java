//age0=data.getAge(i);
//ppm0=data.getPPM(i);
//default data is "co2proxy_2010.dat"
import java.io.PrintWriter;
import java.io.IOException;

public class GEOCARB1and2
{
    public static void main(String args[])
    {
        Proxy_Data proxy = new Proxy_Data("co2proxy_2010.dat");
        doStuff1(proxy);
    }
    public static void doStuff1(Proxy_Data data)
    {
        double[] dppm0=new double[800];
        double[] dum=new double[10000];
        int[] iage0=new int[800];
        String[] cols = {"age(Ma)", "loge(CO2 ppm)"};
        DataWriter proxyGraph = new DataWriter("proxyg.dat",cols);
        
        // we have data from 0Ma to 419 Ma
        // so we make averages over 10 Ma intervals, and set the ages to 5Ma, 15Ma etc
        for (int i=0; i<data.getLines(); i++)
        {
            dppm0[i]=Math.log(data.getPHi(i)/data.getPPM(i));
            dum[i]=Math.log(data.getPPM(i));
            iage0[i]=(int)(1+(data.getAge(i)+.5001));
            double[] lnInts = {data.getAge(i),data.getPPM(i)}; 
            proxyGraph.writeLine(lnInts);
            //iage0(i)=1.+(age0(i)+0.5001)
        }
        //System.out.println("doStuff1 complete");
        
        doStuff2(data, iage0, dppm0, dum);
    }

    public static void doStuff2(Proxy_Data data, int[] iage0, double[] dppm0, double[] dum)
    {
        int mdat = 42;
        double[] age=new double[80];
        int[] iage=new int[80];
        int jdat=0;
        for (int i=0; i<mdat; i++)
        {
            double sum=0;
            int nsum=0;
            double dsum=0;
            for (int j=0;j<data.getLines();j++)
            {
                if ((iage0[j]-1)/10==i)
                {
                    nsum++;
                    sum+=Math.log(data.getPPM(j))/(Math.pow(dppm0[j], 2));
                    dsum+=1.0/(Math.pow(dppm0[j],2.0));
                }
            }
//            System.out.println(nsum+"\t"+sum+"\t"+dsum);
            if (nsum>0)
            {
                jdat=jdat+1;
                age[jdat-1]=10.0*(i+1)-5.0;
                iage[jdat-1]=10*(i+1)-4;
                dum[jdat-1]=Math.exp(sum/dsum);
                dum[mdat+jdat-1]=Math.sqrt(1.0/dsum);
                dum[jdat+3*mdat-1]=10.0*(i+1)-4.0;
//                System.out.println((jdat-1)+"\t"+(mdat+jdat-1)+"\t"+(jdat+3*mdat-1));
//                System.out.println((Math.exp(sum/dsum))+"\t"+(Math.sqrt(1.0/dsum))+"\t"+(10.0*(i+1)-4.0));
                //System.out.println(jdat"\t"+dum[jdat]+"\t"+dum[mdat+jdat]+"\t"+dum[jdat+3*mdat]);
                sum=0.0;
                for (int j=0;j<data.getLines();j++)
                {
                    if((iage0[j]-1)/10==i)
                    {
                        sum=sum+Math.pow((Math.log(dum[jdat-1]/data.getPPM(j))),2);
//                        System.out.println(dum[jdat]+"\t"+data.getPPM(j)+"\t"+sum);
                    }
                }
//                System.out.println(sum+"\t"+nsum);
                if(nsum>1)
                {
                    sum = sum/(nsum-1);
//                    System.out.println(mdat+"\t"+jdat+"\t"+sum+"\t"+Math.sqrt(sum));
                    dum[mdat+jdat-1]=Math.sqrt(sum);
                }
            }
        }
//        System.out.println(mdat+"\t"+jdat);
        int ndat = jdat;
        double ppmin = 1000.0;
        double ppmax = 1.0;
        for (int i=0; i<data.getLines(); i++)
        {
            ppmin=Math.min(ppmin,data.getPPM(i));
            ppmax=Math.max(ppmax,data.getPPM(i));
            //System.out.println(ppmin+"\t"+ppmax);
        }
        ppmin=ppmin*0.8;
        ppmax=ppmax*1.5;
        //System.out.println("doStuff2 complete");
        doStuff3(ndat, mdat, dum, age, iage);
    }
    public static void doStuff3(int ndat, int mdat, double[] dum, double[] age, int[] iage)
    {
        DataWriter outoutout = new DataWriter("outoutout.dat");
        double[] pppm=new double[80];
        double[] dppm=new double[80];
        double agee;
//            for (int i=0; i<80; i++)
//            {
//                System.out.println(i+": "+dum[i]);
//            }
        for (int i=0; i<ndat; i++)
        {
            pppm[i]=dum[i];
            dppm[i]=dum[mdat+i];
            dum[i]=pppm[i]*Math.exp(dppm[i]);
            dum[i+ndat+1]=pppm[i]*Math.exp(-1*dppm[i]);
            agee=-age[i];
            double[] outArr={agee,pppm[i],dum[i],dum[i+ndat],dppm[i]};
            outoutout.writeLine(outArr);
            //write(14,111) agee,pppm[i],dum[i],dum[i+ndat],dppm[i];
        }
        System.out.println(ndat);
        GEOCARB3.doStuff(ndat, pppm, dppm, iage);
        //System.out.println("doStuff3 complete");
    }
}