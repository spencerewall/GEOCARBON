import java.io.Console;
import java.util.Scanner;
import java.io.File;
import java.text.DecimalFormat;

public class gcsv_ppdf
{
    public static void main(String args[])
    {
        //implicit real*4 (a-h,o-z)
        //implicit integer*4 (i-n)
        //character*80 title,name   
        //common/varred/vred(10,10,10,10,99),delt(99),ddel(99)
        double[][][][][]vred = new double[10][10][10][10][99]; double[] delt=new double[99];
        double[]ddel=new double[99]; //double[]
        //common/gcsv/pp(10,10,10,10,60),ppmv(100,60,3)
        double[][][][][]pp=new double[10][10][10][10][60];
        double[][][] ppmv = new double[100][60][3];
        //common/varred2/blob(10,4),params(10,4)
        double[][]blob=new double[10][4]; double[][]params=new double[10][4];
        double[] rco2 = new double[100]; double[] stdev=new double[3];
        //common/pdfs/epdf(99),epdfn(99),ecdf(99),wt_act(10)
        double[] wt_act=new double[10]; double[] epdf=new double[99]; double[] ecdf=new double[99];
        double[] epdfn=new double[99];
        DecimalFormat fourDec = new DecimalFormat("#0.0000");
        DecimalFormat threeDec = new DecimalFormat("#0.000");
        //common/stretch/sumg(100,10,4),sumgg(100,10,4)
        double[][][] sumg=new double[100][10][4]; double[][][] sumgg=new double[100][10][4];
        // dimension vvred(10000,99)
        /** equivalence (vred,vvred) */
        double theta=0;
        for (int i=1; i<=10; i++)
        {
            theta=(i-1)*Math.PI/18.0;
            wt_act[i-1]=1.0;
            System.out.println(i+"\t"+wt_act[i-1]);
        }
        
        Console console = System.console();
        /**String filename= console.readLine("Enter input file: ");*/
        String filename="out_gcsv10.dat";
        // read(5,101) name
        try{
            Scanner input=new Scanner(new File(filename));
            System.out.println("output file out_ppdf.dat");
            System.out.println("using FERT (1) or GLAC (2)??");
            
            double sumsq0 = parseNextDouble(input);
            double stdev0 = parseNextDouble(input);
            //read(14) sumsq0,stdev0
            System.out.println(sumsq0+"\t"+stdev0);
            //open(15,file=filename,form='formatted')
            DataWriter output = new DataWriter("out_ppdf.dat");
            //open(14,file=`name,form='unformatted')
            int ndel=28;
            double deltaT = .28;
            double vredmin = 1000.0;
            stdev[0]=Math.sqrt(0.15)*stdev0;    //stdev(1)=sqrt(0.15)*stdev0
            stdev[1]=Math.sqrt(0.2)*stdev0;     //stdev(2)=sqrt(0.2)*stdev0
            stdev[2]=Math.sqrt(0.25)*stdev0;    //stdev(3)=sqrt(0.25)*stdev0
            
            double alg10=Math.log(20.0);
            
            double aaa = 0;
            for (int j=1; j<=100; j++)
            {
                aaa=(((double)j)+0.5)*2.0*alg10/100.0;
                rco2[j-1]=Math.exp(aaa)*20.0;
            }
            for (int i=1;i<=3;i++)
            {
                for (int j=1; j<=60; j++)
                {
                    for (int k=1; k<=100; k++)
                    {
                        ppmv[k-1][j-1][i-1]=0.0;
                    }
                }
            }
            for (int iiit=1; iiit<=ndel; iiit++)
            {
                delt[iiit-1]=parseNextDouble(input);
                System.out.println(delt[iiit-1]);     //print *,delt(iiit)
                //read(14)((((vred(k1,k2,k3,k4,iiit),k4=1,10),k3=1,10),k2=1,10),k1=1,10)
                int counter=1;
                for(int k1=1; k1<=10; k1++){
                    for(int k2=1; k2<=10; k2++){
                        for(int k3=1; k3<=10; k3++){
                            for(int k4=1; k4<=10; k4++){
                                vred[k1-1][k2-1][k3-1][k4-1][iiit-1]=parseNextDouble(input);
                }}}}
                //do i=1,10000
                //vredmin=amin1(vredmin,vvred(i,iiit))
                /** can this be incorporated into the loop above? */
                for(int k1=1; k1<=10; k1++){
                    for(int k2=1; k2<=10; k2++){
                        for(int k3=1; k3<=10; k3++){
                            for(int k4=1; k4<=10; k4++){
                                vredmin=Math.min(vredmin,vred[k1-1][k2-1][k3-1][k4-1][iiit-1]);
                }}}}
                for(int k1=1; k1<=10; k1++){
                    for(int k2=1; k2<=10; k2++){
                        for(int k3=1; k3<=10; k3++){
                            for(int k4=1; k4<=10; k4++){
                                for (int j=1; j<=58; j++){
                                    pp[k1-1][k2-1][k3-1][k4-1][j-1]=parseNextDouble(input);
                                }
                                double std=vred[k1-1][k2-1][k3-1][k4-1][iiit-1];
                                if(std<=stdev[2]){
                                    for (int j=1; j<=58; j++)
                                    {
                                        double alogrco2=Math.log(pp[k1-1][k2-1][k3-1][k4-1][j-1]);
                                        double jj=100.0*(alogrco2-alg10)/(2.0*alg10);
                                        jj=Math.max(jj,1);
                                        jj=Math.min(jj,100);
                                        ppmv[(int)jj-1][j-1][2]=ppmv[(int)jj-1][j-1][2]+1.0;
                                        if(std<=stdev[1]) ppmv[(int)jj-1][j-1][1]=ppmv[(int)jj-1][j-1][1]+1.0;
                                        if(std<=stdev[0]) ppmv[(int)jj-1][j-1][0]=ppmv[(int)jj-1][j-1][0]+1.0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            /**int ickk=console.readLine("using FERT (1) or GLAC (2)??")*/
            int ickk=1;
            
            // make histograms of predicted values of logCO2 for fixed data fits
            // 90%,85%,80% variance reduction  
            for (int i=1; i<=3; i++){    //do i=1,3
                double sum=0.0;
                for (int jj=1; jj<=100; jj++){   //do jj=1,100
                    sum=sum+ppmv[jj-1][0][i-1];
                }
                for (int jj=1; jj<=100; jj++){
                    for(int j=1; j<=58; j++)
                    {
                        ppmv[jj-1][j-1][i-1]=ppmv[jj-1][j-1][i-1]/sum;
                    }
                }
            }
            DataWriter rco2hist = new DataWriter("out_rco2hist.dat"); //open(14,file="out_rco2hist.dat",form='formatted')
            for (int j=1; j<=58; j++)
            {
                double aj=5.-j*10.0;
                String rco2line = "";
                for (int jj=1; jj<=100; jj++)
                {
                    //write(14,104) aj,rco2(jj),(ppmv(jj,j,i),i=1,3);
                    String ppmvln="";
                    for (int i=1; i<=3;i++) { ppmvln+=fourDec.format(ppmv[jj-1][j-1][i-1])+"\t"; }
                    rco2hist.writeLine(fourDec.format(aj)+"\t"+fourDec.format(rco2[jj-1])+"\t"+ppmvln);
                }
                //rco2hist.writeLine(rco2line);
            }
            rco2hist.close();
            //close(14)
            //104 format(8f12.4)
            double ddelt;
            System.out.println(vredmin + " is min stdev misfit");
            //double vred_level=Double.parseDouble(console.readLine("choose a threshhold stdev misfit"));   //read(5,*) vred_level
            double vred_level=2.5;
            for (int j=0; j<4; j++){
                for (int k=0; k<10; k++){
                    for (int i=0; i<100; i++){
                        sumg[i][k][j]=0.0;
                        sumgg[i][k][j]=0.0;
            }}}
            int iiit=1;
            while(iiit<=ndel){
                //System.out.println("Apples are for not monkeys");
                int totruns=0;
                for(int j=1; j<=4; j++){
                    // ACT, FERT, LIFE, GYM
                    for(int i=1;i<=10;i++){
                        blob[i-1][j-1]=0.0;
                        params[i-1][0]=0.03+((i-1)*0.10/9.0);
                        
                        if(ickk==1) // FERT
                            params[i-1][1]=0.2+(i-1)*0.2/3.0;
                        else //GLAC
                            params[i-1][1]=Math.pow(2.0,((double)(i-1)/3.0-1.0));
                        params[i-1][2]=0.1+(i-1)*0.4/9.0;
                        params[i-1][3]=0.5+(i-1)*0.7/9.0;
                    }
                }
                // the ii index is intended for rescaling the deltaT ordinate for glacial deltaT
                // ignore it unless you want to try it.
                //System.out.println("Whats up with that");
                for(int k1=1; k1<=10;k1++){
                    for(int k2=1; k2<=10;k2++){
                        ddelt=params[k2-1][1]*delt[iiit-1];
                        int ii=Math.max((int)(ddelt/0.4),1);
                        ii=Math.min(ii,100);
                        for(int k3=1;k3<=10;k3++) {
                            for(int k4=1;k4<=10;k4++) {
                                sumg[ii-1][k1-1][0]=sumg[ii-1][k1-1][0]+1.0;
                                sumg[ii-1][k2-1][1]=sumg[ii-1][k2-1][1]+1.0;
                                sumg[ii-1][k3-1][2]=sumg[ii-1][k3-1][2]+1.0;
                                sumg[ii-1][k4-1][3]=sumg[ii-1][k4-1][3]+1.0;
                                //System.out.println(vred[k1-1][k2-1][k3-1][k4-1][iiit-1]);
                                if(vred[k1-1][k2-1][k3-1][k4-1][iiit-1]<=vred_level)
                                {
                                    blob[k1-1][0]=blob[k1-1][0]+1.0;
                                    blob[k2-1][1]=blob[k2-1][1]+1.0;
                                    blob[k3-1][2]=blob[k3-1][2]+1.0;
                                    blob[k4-1][3]=blob[k4-1][3]+1.0;
                                    
                                    sumgg[ii-1][k1-1][0]=sumgg[ii-1][k1-1][0]+1.0;
                                    sumgg[ii-1][k2-1][1]=sumgg[ii-1][k2-1][1]+1.0;
                                    sumgg[ii-1][k3-1][2]=sumgg[ii-1][k3-1][2]+1.0;
                                    sumgg[ii-1][k4-1][3]=sumgg[ii-1][k4-1][3]+1.0;
                                }
                            }
                        }
                    }
                }

                /*for(int j=1; j<=4; j++)
                {
                    for(int i=1; i<=10; i++)
                    {
                    }
                }*/
                //103 format(12f10.3)
                for (int i=0;i<10;i++){
                    //write(15,103)(delt(iiit),params(i,j),blob(i,j),j=1,4)
                    String wrtln = "";

                    for(int j=0;j<4;j++){
                        //wrtln+=""+delt[iiit-1]+"\t"+params[i][j]+"\t"+blob[i][j]+"\t";
                        wrtln+=threeDec.format(delt[iiit-1])+"\t"+threeDec.format(params[i][j])+"\t"+threeDec.format(blob[i][j])+"\t";
                    }
                    
                    output.writeLine(wrtln);
                    
                    totruns=totruns+(int)(blob[i][0]*wt_act[i]);
                }
                
                epdf[iiit-1]=totruns/10000.0;
                iiit++;
                
                
            }
            /**close(15)*/
            /**close(14)*/
            /**open(15,file='outs_ppdf.dat',form='formatted')*/
            output.close();
            DataWriter outsppdf = new DataWriter("outs_ppdf.dat");
            for(int j=1;j<=4;j++){
                for(int k=1;k<=10;k++){
                    for(int i=1;i<=100;i++){
                        sumgg[i-1][k-1][j-1]=sumgg[i-1][k-1][j-1]/sumg[i-1][k-1][j-1];
                    }
                }
            }
            for(int i=1;i<=100;i++){
                ddelt=0.4*i;
                for(int k=1;k<=10;k++){
                    String lnstr = "";
                    for(int j=1; j<=4;j++)
                    {
                        //lnstr+=ddelt+"\t"+params[k-1][j-1]+"\t"+sumgg[i-1][k-1][j-1]+"\t";
                        lnstr+=threeDec.format(ddelt)+"\t"+threeDec.format(params[k-1][j-1])+"\t"+threeDec.format(sumgg[i-1][k-1][j-1])+"\t";
                    }
                    outsppdf.writeLine(lnstr);
                    /**write(15,103) (ddelt,params[k-1][j-1],sumgg[i-1][k-1][j-1],j=1,4)*/
                }
            }
            outsppdf.close();
            //close(15)
            // integrate the pdf into a cdf, but must scale with the growing interval in DeltaT
            ddel[0]=delt[1]-delt[2];
            ddel[ndel-1]=delt[ndel-1]-delt[ndel-2];
            double totpdf=ddel[0]*epdf[0];
            ecdf[0]=totpdf;
            for(iiit=2;iiit<=ndel-1;iiit++){
               ddel[iiit-1]=(delt[iiit]-delt[iiit-2])/2.0;
               totpdf=totpdf+ddel[iiit-1]*epdf[iiit-1];
               ecdf[iiit-1]=totpdf;
            }
            //System.out.println("It's a dream");
            totpdf=totpdf+ddel[ndel-1]*epdf[ndel-1];
            ecdf[ndel-1]=totpdf;
            for(iiit=1;iiit<=ndel;iiit++){
                ecdf[iiit-1]=ecdf[iiit-1]/totpdf;
                epdfn[iiit-1]=epdf[iiit-1]/totpdf;
            }
            for (int i=1;i<=ndel;i++){
                System.out.println(delt[i-1]+"\t"+epdf[i-1]+"\t"+epdfn[i-1]+"\t"+ecdf[i-1]);
            }
            //rco2hist.close();
            
            //close(14)
            //close(15)
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }    
    }
    public static double parseNextDouble(Scanner s)
    {
        if (!(s.hasNext()))
        {
            System.out.println("ERROR: gcsv_ppdf.parseNextDouble(Scanner): No next element");
            System.exit(0);
        }
        if (s.hasNextDouble())
        {
            return s.nextDouble();
        }
        if (s.hasNext() && !s.hasNextDouble())
        {
            String str=s.next();
            if (str.equals("0?"))
                return Double.POSITIVE_INFINITY;
                //if (str.contains("0.455700E+"))
            System.out.print("WOAH! "+str);
            return Double.parseDouble(str);
        }
        System.out.println("ACK!");
        return -1;
    }
}