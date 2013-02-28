------------------------------------------------------------------------
This is a Java version of Jefferey Park's GEOCARBSULF fortran code 
converted in turn from Bob Burner's basic code of the same name.  The 
file name "gcsv" stands for GeoCarbSulfVolc.
------------------------------------------------------------------------
PROJECT TITLE: GEOCARBSULF
AUTHORS: SPENCER J. EWALL

When running it is reccomended that javas heap size be increased.  This can be achieved
with the command:
	java -Xmx512m -cp .:Libraries/jsc.jar GCSVDriver






The following is the original fortran code:

c program gcsv10_export.f  
c fortran version of bob berner's GEOCARBSULF BASIC code 
c gcsv10 stands for GeoCarbSulfVolc algorithm for 2010
c  THIS CODE CAN READ EITHER THE 2006/7 proxy-CO2 data file
c  OR THE 2010 DATA FILE.
c  8/17/2010 --  confirmed the VNV parameter as 4, though Berner asked for 5, 
c  as motivated by Aaron Taylors thesis -- but other Taylor source is vnv=3
c  this code developed in MacOS 10.4, using gcc compiler, 
c  takes 90minutes to run on a 2006-vintage Macbook Pro with 2 GHz Intel Core Duo processor
c  takes 45minutes to run on a 2009-vintage Macbook Pro with 2.53 GHz Intel Core 2 Duo processor
c  -- looped over range of input parameters, set up to fit proxy data for CO2 ppm
c  11/22/06 JJP
c  proxy data has uncertainties that are sometimes large.  To make the data-fitting robust, 
c  compute chi-squared using an uncertainty in the log-domain.  
c  xf77 -o /Users/jjpark/bin/gcsv10_export gcsv10_export.f /Users/jjpark/Plotxy/plotlib.a
c
c   code uses plotit, a standalone fortran/C plotting subroutine in Xwindows
c  this code can be found on JPark's website http://earth.geology.yale.edu/~jjpark/Code/plotit.tar
c
c  code updated from the code used for the paper
c  
c  Royer, D. L., Berner, R. A., and Park, J., 2007, Climate sensitivity constrained by carbon 
c  dioxide concentrations over the last 420 million years: Nature, v.446, p.530-532.
c
c  and used for this paper
c   
c  Park, J., and D. L. Royer, Geologic constraints on the glacial amplification of Phanerozoic 
c  climate sensitivity, American J. Science, V.311, 2011, P. 1-26, DOI 10.2475/01.2011.01
c  
c  there are many test computations in Park and Royer (2011) that were effected 
c  with modified versions of this gcsv10_export.f?code
c
c  Output of this code is written to two files
c  outoutout.dat is a simple table of CO2proxy data values averaged in 10My intervals
c  out_gcsv10.dat is a large ASCII file (>50 Mb) that contains the GEOCARB carbon-cycle simulations
c  for all parameter choices.  This allows the user to examine the statistical behavior of the
c  carbon-cycle simulations offline this code (e.g. in gcsv_ppdf.f)
c
c  code converted from BASIC to run grid search over parameters
c  Newton-Raphson damping applied here, to avoid NaN.
c  divergent transition over 380-350My is interpolated better, 
c  correcting earlier convergence error
c  the code reads a file proxydata.txt that contains proxy CO2 PPM data
c  the GEOCARBSULF carbon-cycle model is computed at time steps of 1-My from 570Ma to present
c  the proxy data ranges only from 420Ma to the present
c  
c  Five nested loops comprise the heart of the code, from outermost to innermost
c  DeltaT = temp increase with CO2 doubling
c  ACT = dimensionless activation-energy for silicate weathering
c  FERT = plant CO2-fertilization coefficient
c  LIFE = liverwort-based weathering factor -- less than vascular plants
c  GYM = gymnosperm-based weathering factor (1=angiosperm weathering parameter)
c  gymnosperms (e.g. conifers) might be better at weathering than angiosperms
c
c  for each choice of parameters, we compute the carbon flux balance between terms 
c  that depend on silicate weathering (fBBS) and carbon burial/degassing (fB), 
c  each normalized with common factors derived by Bob Berner many years ago
c  The carbon-cycle balance depends on CO2 level, 
c  because the weathering of silicates accelerates with warmer temps induced by greenhouse gases
c
c  the code solves for the CO2 level that achieves the balance of carbon fluxes
c  The CO2-dependent parameter fB is calculated at each time step from carbon and
c  carbon isotope mass balance and values of all other parameters, based on
c  geological and biological data, that affect the carbon cycle. Then the value of
c  RCO2 (CO2 concentration normalized to the mean value for the past 1 million
c  years = 250 ppm) is calculated by inversion from a complex expression for fB based on the
c  greenhouse effect, CO2 fertilization of plant weathering, solar evolution, and
c  changes in land temperature).
c  
c  for detailed exposition of the theoretical GEOCARB models, see
c
c  Berner, R. A., The Phanerozoic Carbon Cycle: CO2 and O2, 150pp. Oxford University Press, 2004.
c
c  and a collection of GEOCARB and GEOCARBSULF papers published over the past decade by
c  Berner and Colleagues.
c
c  Berner, R. A., 2006a, GEOCARBSULF: A combined model for Phanerozoic atmospheric O2 and CO2: 
c  Geochimica et Cosmochimica Acta, v.70, p.5653-5664. 
c
c  Berner, R. A., 2006b, Inclusion of the weathering of volcanic rocks in the GEOCARBSULF model: 
c  American Journal of Science, v.306, p.295-302, doi:10.2475/052006.01. 
c
c  Berner, R. A., 2008, Addendum to Inclusion of the weathering of volcanic rocks in the 
c  GEOCARBSULF model (Berner, R. A., 2006, v.306, p.295-302) American Journal of Science, 
c  v.308, p.100-103, doi:10.2475/01.2008.04. 
c
c  Berner, R. A., 2009, Phanerozoic atmospheric oxygen: New results using the GEOCARBSULF Model: 
c  American Journal of Science, v.309, p.603-606, doi:10.2475/07.2009.03. 
c
c  The code computes the aggregate data fit of GEOCARBSULF for each choice 
c  of the combined parameters, expressed as the fractional variance residual when expressed
c  in the log domain without uncertainty weighting.  
c  description of the proxy data can be found in 
c
c  Royer et al, CO2 as a primary driver of Phanerozoic climate, GSA Today, March 2004.
c
c  The data variance is expressed as the log-domain deviation from pre-industrial CO2-ppm 
c  the code stores all the variance residuals for all parameter combinations and computes the
c  percentiles of the model runs at each value of DeltaT.  The code also computes the fraction
c  of parameter space that achieves at most a chosen variance residual -- these fractions
c  are used to generate Bayesian PDFs.
c
c  percentile curves and empirical Bayesian PDFs are written in out1.dat and out2.dat
c
      implicit real*4 (a-h,o-z)
      implicit integer*4 (i-n)
      real*4 kwpy,kwgy,kwsy,kwcy,LIFE
      character*80 title,name   
      common/gcs1/DLSOC(600),DLCOC(600),alphac(600), GEOG(600)
      common/gcs2/fA(600),fSR(600),fD(600),fL(600),S(600)
      common/gcs3/temp(600),fAD(600),xx(600)
      common/gcs20/fA0(58),fSR0(72),fD0(58),fL0(58),S0(60) 
      common/gcs30/temp0(58),fAD0(60),DLS0(58),DLC0(58),al0(58)
      common/gcs4/Sr(600),R(600),DELTOT(600),DELRIV(600),Bas(600),
     x DELBAS(600),fR(600),SRBAS(600),Sr0(68),fRT(600)
      common/loop/pp(10,10,10,10,60),dum(10000),weight(10000),wt(10)
      common/percento/p01(90),p10(90),p33(90),p50(90),
     x p66(90),p90(90),p99(90),ama(90)
      common/percentp/q25(90),q30(90),q35(90),q40(90),qq25(90),qq30(90),
     x qq35(90),jkk(10,4,2)
      common/proxy/age0(800),iage0(800),ppm0(800),dppm0(800),aage(800)
      common/dataf/gcsppm(800),pppm(80),iage(80),age(80),dppm(80)
c  fA is the factor of past continental area, relative to modern (10My time steps)
      data fA0/1.0,1.0,1.0,1.0,0.91,0.92,0.94,0.87,0.79,0.81,
     x 0.82,0.85,0.88,0.86,0.84,0.83,0.85,0.87,0.89,0.91,
     x 0.91,0.90,0.90,0.88,0.89,0.86,0.84,0.81,0.81,0.80,
     x 0.80,0.80,0.79,0.77,0.76,0.73,0.70,0.69,0.69,0.72,
     x 0.74,0.74,0.74,0.62,0.63,0.67,0.62,0.62,0.62,0.65,
     x 0.65,0.66,0.64,0.66,0.85,0.90,0.95,1.00/
c  data based on Christie, suggested by BobBerner  --  gives bad results
c      data fA0/1.0,1.0,1.0,1.0,1.12,1.24,1.15,1.07,1.10,1.10,1.10,
c     x 1.10,1.10,1.10,1.10,1.10,1.10,1.10,1.10,1.10,1.10,1.10,1.10,
c     x 1.10,1.10,1.10,1.10,1.10,1.08,1.07,1.06,1.05,1.02,0.97,0.94,
c     x 0.90,0.85,0.80,0.75,0.70,0.65,0.60,0.55,0.50,0.46,0.46,0.46,
c     x 0.46,0.46,0.46,0.46,0.46,0.46,0.46,0.62,0.79,0.90,1.0/
c  fD is the factor of water runoff per unit continental area, relative to modern 
       data fD0/1.00,1.02,1.04,1.06,1.08,1.10,1.15,1.20,1.19,1.19,
     x 1.18,1.18,1.18,1.17,1.15,1.13,1.12,1.10,1.05,1.03,
     x 1.02,1.01,0.98,0.96,0.94,0.96,0.97,1.02,1.08,1.10,
     x 1.13,1.14,1.15,1.19,1.21,1.21,1.21,1.21,1.21,1.22,
     x 1.23,1.25,1.28,1.31,1.10,1.00,0.90,0.92,0.96,1.03,
     x 1.07,1.10,1.12,1.12,1.12,1.12,1.12,1.12/
c  data based on Christie, suggested by BobBerner  --  gives bad results
c      data fD0/1.00,1.02,1.04,1.06,1.08,1.16,1.16,1.17,1.18,1.18,
c     x 1.18,1.18,1.16,1.14,1.12,1.10,1.05,1.02,1.00,0.95,0.90,0.85,
c     x 0.80,0.75,0.70,0.65,0.68,0.72,0.75,0.78,0.81,0.83,0.85,0.87,
c     x 0.90,0.92,0.94,0.96,0.98,1.00,1.02,1.04,1.06,1.08,1.10,1.00,
c     x 0.90,0.92,0.96,1.03,1.07,1.10,1.12,1.12,1.12,1.12,1.12,1.12/
c  temp0 is the average land temperature in past times, degrees C
c  used in defining the GEOG variable
       data temp0/12.4,12.4,12.4,12.5,12.5,12.5,11.6,11.0,11.2,11.3,
     x 11.5,11.7,11.9,12.0,12.4,12.8,13.1,13.4,13.7,14.0,
     x 14.2,14.2,14.1,14.1,14.1,14.3,14.2,13.9,13.7,13.5,
     x 13.3,13.1,13.0,13.0,12.9,13.0,13.1,13.3,13.4,13.5,
     x 12.5,11.5,10.5,12.5,15.0,17.0,18.2,17.0,16.3,15.5,
     x 14.5,13.5,13.0,13.0,13.0,12.0,12.0,12.0/
c  data based on Christie, suggested by BobBerner  --  gives bad results
c      data temp0/11.2,11.5,11.5,11.5,12.3,13.0,12.2,11.4,10.4,9.4,8.4,
c     x 8.4,8.4,8.4,8.4,8.4,8.4,8.5,8.6,8.7,8.8,8.9,9.0,9.1,9.2,9.3,9.6,
c     x 9.9,10.2,10.5,10.8,11.3,11.6,11.9,12.2,12.5,12.8,13.1,13.4,
c     x 13.7,14.0,14.2,14.5,14.7,15.5,15.5,15.5,15.5,15.5,15.5,
c     x 14.5,13.5,13.0,13.0,13.0,12.0,12.0,12.0/ 
c  for the most recent 140My, the fSR parameter is given every 5My, not 10My
c  fSR is the factor twixt Sr isotopes and silicate weathering
       data fSR0/1.00,0.98,0.98,0.98,0.98,1.02,1.04,1.11,1.22,1.41,
     x 1.46,1.41,1.35,1.30,1.35,1.37,1.33,1.30,1.37,1.52,
     x 1.65,1.76,1.74,1.67,1.63,1.67,1.52,1.26,1.27,1.27,
     x 1.27,1.21,1.14,1.09,1.10,1.14,1.12,1.10,1.09,1.11,
     x 1.14,1.10,1.07,1.21,1.21,1.14,1.07,1.07,1.28,1.43,
     x 1.43,1.40,1.38,1.36,1.38,1.48,1.53,1.55,1.55,1.52,
     x 1.52,1.50,1.49,1.50,1.52,1.62,1.70,1.59,1.35,1.20,1.04,1.00/
c FOR T= 0 TO 579
c IF T=<140 AND  INT(T/5)=T/5 THEN READ fSR(T)
c IF T>140 AND INT(T/10)=T/10 THEN READ fSR(T)
c IF T=<140 AND INT(T/5)<>T/5 THEN fSR(T)=fSR(T-1)
c IF T>140 AND INT(T/10)<>T/10 THEN fSR(T)=fSR(T-1)
c
c Sulfur isotope data (d34S) below are from Kampschulte and Strauss (2004)
       data DLS0/20.0,21.0,21.0,21.0,21.0,19.0,18.0,17.5,17.8,17.5,
     x 15.0,15.0,15.3,16.3,16.2,16.0,17.0,17.0,18.0,18.5,
     x 16.0,16.0,17.5,18.0,22.0,20.0,12.0,12.5,12.5,12.2,
     x 13.5,14.8,15.5,14.5,15.5,17.0,25.0,24.5,20.0,19.0,
     x 22.0,27.0,26.5,26.0,26.7,25.5,25.0,24.3,26.0,30.0,
     x 27.8,35.3,36.4,33.0,30.3,32.0,34.4,35.2/
c FOR T = 0 TO 579
c IF INT(T/10)=T/10 THEN READ DLSOC(T)
c IF INT (T/10)<>T/10 THEN DLSOC(T) =DLSOC(T-1)
c NEXT T      
c Carbon isotope data (d13C)
c  following array is smoothed fit TO maximum Veizer data with smoothed Silurian bump
c  also data of Korte (Tr),Grossman et al Permo-Carb,Krischvink(Camb), Saltzman (Ord, Sil),  
c  Van Geld (Devonian), Lindh (Jurassic to 0)
c  line 485 is 260-390 Ma
c  several changes to values in this array between 2006 and 2010 GEOCARBSULF versions
      data DLC0/1.50,1.70,2.00,2.20,2.20,2.20,2.30,2.40,2.40,2.50,
     x 2.60,2.70,2.50,2.00,1.20,1.70,1.54,1.38,1.22,1.06,
     x 2.00,2.50,3.50,2.50,2.00,1.00,4.00,4.70,4.40,4.70,
     x 4.40,4.60,3.20,2.50,2.90,2.80,1.40,1.00,1.00,1.50,
     x 0.50,2.00,1.50,1.00,0.70,0.00,0.00,-1.0,-1.0,1.00,
     x -1.0,-0.5,0.00,0.00,-2.0,0.00,0.00,0.00/
c data are from Hayes ey al (1999) 
c  is difference in d13C between coexisting marine orgC and marine carbonate
      data al0/22.5,25.9,27.5,30.0,29.6,30.5,30.2,28.5,27.8,29.7,
     x 28.8,30.0,31.5,29.3,30.3,31.0,31.3,34.0,33.7,32.0,
     x 30.4,30.7,31.0,31.5,32.1,32.6,33.0,33.0,32.9,32.8,
     x 32.7,32.6,32.6,32.5,32.4,32.1,31.8,31.0,30.3,29.4,
     x 28.6,28.8,29.0,30.3,31.5,31.0,30.6,29.8,29.0,29.0,
     x 29.0,29.2,29.4,29.2,29.0,30.0,30.0,30.0/
c FOR T=  0 TO 579
c IF INT(T/10) =T/10 THEN READ alphac(T)
c IF INT(T/10)<>T/10 THEN  alphac(T)=alphac(T-1)
c  fL is the fraction of land area covered by carbonate rocks, normalized to modern
      data fL0/1.00,1.00,0.99,1.29,1.10,1.10,1.26,0.88,0.88,0.88,
     x 1.04,1.04,1.04,1.04,1.04,1.24,1.25,1.25,1.25,1.36,
     x 1.36,1.23,1.23,1.39,1.36,1.43,1.31,1.31,1.31,1.45,
     x 1.45,1.45,1.41,1.41,1.41,1.41,1.40,1.47,1.47,1.47,
     x 1.63,1.63,1.54,1.43,1.43,1.15,1.07,1.07,0.99,0.99,
     x 0.99,0.97,1.05,1.05,0.63,0.63,0.63,0.63/
c FOR T = 0 TO 579
c IF INT (T/10)=T/10 THEN READ fL(T)
c IF INT (T/10)<>T/10 THEN fL(T) = fL(T-1)
c Sr0 is specified every 5my to 100Ma, prior times is 10My spacing
c Sr0 is the normalized difference of basalt-predicted and measured Sr isotope ratios
c  measured every 10 My
      data Sr0/92.0,90.5,89.5,89.0,85.0,82.5,81.0,79.0,78.0,78.0,
     x 78.0,78.0,78.5,79.0,78.0,77.0,76.0,75.5,75.0,75.0,
     x 74.0,73.0,72.0,73.0,71.0,69.0,68.0,69.0,75.0,76.0,
     x 75.0,78.0,78.0,78.0,75.0,73.0,71.0,77.0,82.0,83.0,
     x 82.0,82.0,81.0,78.0,75.0,78.0,82.0,82.0,78.0,80.0,
     x 85.0,87.0,86.0,81.0,79.0,78.0,80.0,82.0,85.0,87.0,
     x 89.0,89.0,89.0,89.0,90.0,90.0,90.0,90.0/
c FOR T=0 TO 579
c IF T >100 AND INT(T/10)=T/10 THEN READ Sr(T)
c IF T>100 AND INT(T/10)<>T/10 THEN Sr(T)=Sr(T-1)
c IF T=<100 AND INT(T/5)=T/5 THEN READ Sr(T)
c IF T=<100 AND INT(T/5)<>T/5 THEN Sr(T)=Sr(T-1)
c
c  here is a change in the weatherable land area in the Eocene (30-50 Ma)
c  and the Jurassic (150-200 Ma).  fA0(1) is 0 Ma, fA0(2) is 10 Ma
c      reduc=0.5
c      fA0(4)=fA0(4)*(1.0-reduc)
c      fA0(5)=fA0(5)*(1.0-reduc)
c      fA0(6)=fA0(6)*(1.0-reduc)
c      fA0(16)=fA0(16)*(1.0-reduc)
c      fA0(17)=fA0(17)*(1.0-reduc)
c      fA0(18)=fA0(18)*(1.0-reduc)
c      fA0(19)=fA0(19)*(1.0-reduc)
c      fA0(20)=fA0(20)*(1.0-reduc)
c      fA0(21)=fA0(21)*(1.0-reduc)
c  read proxydata file
c  uncertainties provided by Dana Royer are in a range, a max and min.  
c  I compute a factor-of-X uncertainty by dividing the upper bound by the value
c  and convert the 1-sigma into logarithmic misfit
c  2006 data set
      name='proxydata.txt'
      nscan=488
c  2010 data set
      name='co2proxy_2010.txt'
      nscan=635
c      name='co2proxy_2010_nostomata.txt'
c      nscan=438
c      name='co2proxy_2010_nopaleosol.txt'
c      nscan=420
c      name='co2proxy_2010_noplankton.txt'
c      nscan=451
c  we have data from 0Ma to 419 Ma
c  so we make averages over 10 Ma intervals, and set the ages to 5Ma, 15Ma etc
      mdat=42
      open(8,file=name,form='formatted')
      read(8,101) name
  101 format(a)
      do i=1,nscan
        read(8,*) age0(i),ppm0(i),plo,phi
	dppm0(i)=alog(phi/ppm0(i))
	dum(i)=alog(ppm0(i))
	iage0(i)=1+(age0(i)+0.5001)
c	print *,age0(i),ppm0(i),plo,phi,dum(i),dppm0(i)
      end do
      close(8)
      blob=0.
      call plotit(age0,dum,dppm0,nscan,'proxydata','age(Ma)',
     x 'log\\sub{e} CO\\sub{2} ppm',
     x  3,0,0.1,1,21)
      call plotit_axes(0.,75.,0.,0.)
      call plotit(age0,dum,dppm0,nscan,'proxydata','age(Ma)',
     x 'log\\sub{e} CO\\sub{2} ppm',
     x  3,0,0.1,1,22)
c      pause
      call plotit_axes(0.,0.,0.,0.)
c      if(blob.eq.1.0) then
c  compute 10Ma moving average of CO2 PPM estimates
c  the average is weighted by inverse-variance in the log domain.
c It is interesting that the PPM estimates since the Eocene are so much
c lower than GEOCARB.  If the proxy data are unbiased, this implies that
c  there are more factors that control CO2 than GEOCARB includes -- an
c inference that we probably would not dismiss.  Short-term disagreement
c doesnt invalidate the argument about the longer-term changes.
      jdat=0
      do i=1,mdat
        sum=0.
	nsum=0
	dsum=0
	do j=1,nscan
	  if((iage0(j)-1)/10+1.eq.i) then
	    nsum=nsum+1
	    sum=sum+alog(ppm0(j))/(dppm0(j)**2)
	    dsum=dsum+1.0/(dppm0(j)**2)
	  endif
	end do
	if(nsum.gt.0) then
	  jdat=jdat+1
	  age(jdat)=10.*i-5.
	  iage(jdat)=10*i-4
  	  dum(jdat)=exp(sum/dsum)
	  dum(mdat+jdat)=sqrt(1.0/dsum)
	  dum(jdat+3*mdat)=10.*i-4.
c  compute the chi-square of the moving average in log domain
c  can scale up the variance of the moving-average for large chi-square
c        sum=0.
c	do j=1,nscan
c	  if((iage0(j)-1)/10+1.eq.i) then
c            sum=sum+(alog(dum(jdat)/ppm0(j))/dppm0(j))**2
c	  endif
c	end do
c	if(nsum.gt.0) then
c          sum=sum/nsum
c          dum(2*mdat+jdat)=sum
c	  if(nsum.eq.1) dum(2*mdat+jdat)=sum+1.
cc	  print *,dum(jdat),dum(mdat+jdat),sum
c	  if(sum.gt.1.0) dum(mdat+jdat)=dum(mdat+jdat)*sqrt(sum)
c	endif
c  INSTEAD: compute the sample variance of the PPM estimates within the moving average 
c  in log domain -- dont use uncertainty weighting, so we are using log-ppm units
c  for the case where nsum=1 (one data point in sum) the uncertainty is the sample uncertainty
          sum=0.
          do j=1,nscan
	    if((iage0(j)-1)/10+1.eq.i) then
              sum=sum+(alog(dum(jdat)/ppm0(j)))**2
	    endif
	  end do
	  if(nsum.gt.1) then
            sum=sum/(nsum-1)
            dum(mdat+jdat)=sqrt(sum)
	  endif
c	  if(nsum.gt.0) print *,age(jdat),dum(jdat),dum(mdat+jdat),nsum
	endif
      end do
      print *,mdat,jdat
      ndat=jdat
      ppmin=1000.
      ppmax=1.
      do i=1,nscan
        ppmin=amin1(ppmin,ppm0(i))
	ppmax=amax1(ppmax,ppm0(i))
      end do
      ppmin=ppmin*0.8
      ppmax=ppmax*1.5
      call plotit_axes(0.,0.,10.,ppmax)
      call plotit(age0,ppm0,dm,nscan,'proxydata','age(Ma)',
     x 'CO\\sub{2} ppm',
     x  2,2,0.1,1,0)
      open(14,file='outoutout.dat',form='formatted')
      do i=1,ndat
	pppm(i)=dum(i)
	dppm(i)=dum(mdat+i)
	dum(i)=pppm(i)*exp(dppm(i))
	dum(i+ndat)=pppm(i)*exp(-dppm(i))
        agee=-age(i)
	print *,agee,pppm(i),dum(i),dum(i+ndat),dppm(i)
	write(14,111) agee,pppm(i),dum(i),dum(i+ndat),dppm(i)
      end do
  111 format(f8.1,3f10.1,f7.3)
      close(14)
      call plotit(age,dum,dm,ndat,'proxydata','age(Ma)',
     x 'CO\\sub{2} ppm',
     x  2,2,0.0,0,0)
      call plotit(age,dum(ndat+1),dm,ndat,'proxydata','age(Ma)',
     x 'CO\\sub{2} ppm',
     x  2,2,0.0,0,0)  
      call plotit(age,pppm,dm,ndat,'proxydata','age(Ma)',
     x 'CO\\sub{2} ppm',
     x  2,2,0.0,0,21)
      call plotit_axes(0.,0.,10.,ppmax)
      call plotit(age0,ppm0,dm,nscan,'proxydata','age(Ma)',
     x 'CO\\sub{2} ppm',
     x  2,0,0.1,1,0)
      call plotit(age,dum,dm,ndat,'proxydata','age(Ma)',
     x 'CO\\sub{2} ppm',
     x  2,0,0.0,0,0)
      call plotit(age,dum(ndat+1),dm,ndat,'proxydata','age(Ma)',
     x 'CO\\sub{2} ppm',
     x  2,0,0.0,0,0)  
      call plotit(age,pppm,dm,ndat,'proxydata','age(Ma)',
     x 'CO\\sub{2} ppm',
     x  2,0,0.0,0,22)
      print *,'go on with program? (0=no)'
      read (5,*) ick
      if(ick.eq.0) stop
c      endif
c GEOCARB III + Rapid  Recyc, Hayes alphac, n for alphas, 
c NO MIMI. Korte Triassic
c  McDougall 87Sr at P/YTr boundary
c vary delta T from 0.4 to 10.1 for each value of ACT, FERT,LIFE and GYM
c      ndel=82
c  the baseline CO2 level is 250 ppm, the average for the last million years.
      open(14,file='out_gcsv10.dat',form='unformatted')
      sumsq0=0.0
      do i=1,ndat
        diff=alog(250.0/pppm(i))/dppm(i)
	sumsq0=sumsq0+(diff)**2
      end do
      stdev_dof=sqrt(sumsq0/ndat)
      write(14) sumsq0,stdev_dof
      print *,'baseline variance, stdev/dof',sumsq0,stdev_dof
c  time step 1 million years
      Dt=1
c  total cooling due to weaker solar radiation at 570Ma (degrees C)
      Ws=7.4
      St=600
      dlst=4
      CT=6252
      dlct=-3.5
      kwpy=0.01							
      kwsy=0.01							
      kwgy=0.018							
      kwcy=0.018							
      Fwpa1=0.25
c Fws is the Cflux of weathering silicates
      Fwsa1=0.5
c  Fwg is carbon flux from sedimentary organic matter
      Fwga1=0.5
c  Fwc is carbon flux from Ca and Mg carbonates
      Fwca1=2
c  Fmg is C-degassing from volcanism, metamorphism and diagenesis of organic matter
      Fmg1=1.25
c  Fmc is C-degassing from volcanism, metamorphism and diagenesis of carbonate
      Fmc1=6.67
      Fmp1=0.25
      Fms1=0.5
      ZM=12.5							
      RBAS=.703
      FBASO=.92
      RRIV=.711
      FRIV=3.37
c  fill in the Sr array
c  Sr is the normalized difference of basalt-predicted and measured Sr isotope ratios
      do i=1,20
        jj=1+(i-1)*5
	do j=0,4
	  Sr(jj+j)=(j*Sr0(i+1)+(5-j)*Sr0(i))/5.
	end do
      end do
      do i=21,67
        jj=101+(i-21)*10
	do j=0,9
	  Sr(jj+j)=(j*Sr0(i+1)+(10-j)*Sr0(i))/10.
	end do
      end do
      Sr(571)=Sr0(68)
c  fill in the fSR array
      do i=1,28
        jj=1+(i-1)*5
	do j=0,4
	  fSR(jj+j)=(j*fSR0(i+1)+(5-j)*fSR0(i))/5.
	end do
      end do
      do i=29,71
        jj=141+(i-29)*10
	do j=0,9
	  fSR(jj+j)=(j*fSR0(i+1)+(10-j)*fSR0(i))/10.
	end do
      end do
      fSR(571)=fSR0(72)
      do it=571,1,-1
c test case would be fSR(T)=1
c  Bas calculated oceanic Sr-isotope ratio for basalt-seawater reactions
c  starts with Bas(571)=0.709
        FBAS=FBASO*fSR(it)
        IF(it.lt.571) Bas(it)=Bas(it+1)+DELTOT(it+1)
        IF(it.eq.571) Bas(it)=0.709
c  fR is dimensionless effect of mountain uplift on CO2 uptake by silicate weathering
        IF(it.eq.571) fR(it)=1
        DELRIV(it)=((RRIV-Bas(it))/ZM)*FRIV
        DELBAS(it)=((RBAS-Bas(it))/ZM)*FBAS
        DELTOT(it)=DELBAS(it)+DELRIV(it)
c  R is the actual measured Sr-isotope ratio
        R(it)=0.7+Sr(it)/10000.
c  SRBAS is basalt-seawater Sr-isotope normalization, see Berner (2004) eq 2.1
        SRBAS(it)=(Bas(it)-0.7)*10000
c  PL is Berner's adjustable empirical parameter L for Sr-ratio vs. silcate weathering
c  PL=2 obtains approximate fit between Sr-isotopes and physical erosion estimates 
c  from siliclastic sediments, see Berner (2004)
        PL=2
c  Sr is the normalized difference of basalt-predicted and measured Sr isotope ratios
c  fR is dimensionless effect of mountain uplift on CO2 uptake by silicate weathering
        xx(it)=1-it
        fR(it)=1-PL*(1-(Sr(it)/SRBAS(it)))
      end do
c      call plotit(xx,fR,dum,571,' ',' ',' ',2,0,0.0,0,0)
c  fill in the other arrays
      do i=1,57
        jj=1+(i-1)*10
	do j=0,9
	  fA(jj+j)=(j*fA0(i+1)+(10-j)*fA0(i))/10.
	  fD(jj+j)=(j*fD0(i+1)+(10-j)*fD0(i))/10.
	  temp(jj+j)=(j*temp0(i+1)+(10-j)*temp0(i))/10.
	  DLSOC(jj+j)=(j*DLS0(i+1)+(10-j)*DLS0(i))/10.
	  DLCOC(jj+j)=(j*DLC0(i+1)+(10-j)*DLC0(i))/10.
	  alphac(jj+j)=(j*al0(i+1)+(10-j)*al0(i))/10.
	  fL(jj+j)=(j*fL0(i+1)+(10-j)*fL0(i))/10.
	end do
      end do
      fA(571)=fA0(58)
      fD(571)=fD0(58)
      temp(571)=temp0(58)
      DLSOC(571)=DLS0(58)
      DLCOC(571)=DLC0(58)
      alphac(571)=al0(58)
      fL(571)=fL0(58)
c  OK, we finally have all initialized
      do i=1,579
        xx(i)=1-i
c cubic fit to Ronov sediment data
        fRT(i)=25.269*((1-i)/1000.)**3+26.561*((1-i)/1000.)**2
     x                                 +6.894*((1-i)/1000.)+1.063
        fR(i)=(fRT(i)/1.063)**(0.67)
      end do
      ndel=28
      deltaT=0.28
c      deltaT=1.88
c      deltaT=0.88
      do iiit=1,ndel
c      deltaT = 0.28+iiit*0.12
      deltaT = deltaT+0.12*(1.0+4.0*float(iiit-1)/float(ndel-1))
c  GCM is the DT for CO2-doubling, divided by ln 2, so when you mult by log(RCO2)
c  you obtain deltaT when RCO2=2
      GCM0 = deltaT/alog(2.0)
      varredmin=1000.
      do k1=10,1,-1
        ACT=0.03+(k1-1)*0.10/9.
      do k2=10,1,-1
c  limit plant response to CO2 enrichment to range of 20% to 80% efficiency
c  in this loop (over counter k2) we place 
c  the alternate variable GLAC for glacial amplification of climate response
c  also included a test loopp on the variable fob0
c  for these alternate loops, FERT=0.5 -- because the standard computations showed that
c  this seemed a most-probable value, with weak dependence on deviations from FERT=0.5
        FERT=0.2+(k2-1)*0.2/3.
c        FERT=0.5
c	GLAC=2.0**(float(k2-1)/3.0-1.0)
c        GLAC=2.0
        GLAC=1.0
c	FOBO=float(k2-1)/2.25+1.
      do k3=10,1,-1
c  assume vascular-plant acceleration of weathering lies between factor of 2 and 10
        LIFE=0.1+(k3-1)*0.4/9.
      do k4=10,1,-1
c  assume gymnosperms accelerate weathering by 0.5 to 1.2, relative to angiosperms
        GYM=0.5+(k4-1)*0.7/9.
c  oxy is oxygen level in atmosphere, in percent mass (Berner 2009)
      oxy=25.
c  RCO2 is ratio of CO2 to pre-industrial level, 
c  initial RCO2 level in the calculation is moot, because it will be solved for
      RCO2 =10.
      Spy=20.
      Spa=280.
      Ssy=150.
      Ssa=150.
      Gy=250.
      Ga=1000.
      Cy=1000.
      Ca=4000.
      dlsy=35.
      dlcy=3.
      dlpy=-10.
      dlpa=-10.
      dlsa= (dlst*St-(dlpy*Spy+dlsy*Ssy+dlpa*Spa))/Ssa
      dlgy= -23.5
      dlga=-23.5
      dlca= (dlct*CT-(dlgy*Gy  +dlcy*Cy  +dlga*Ga))/Ca
      Rcy=0.7095
      Rca=0.709
c  iberner is a counter for correspondence to earlier codes
c  I decimate by 10 to match the 10My-averaged CO2 values
      do iberner=571,1,-1
        i=iberner
	GCM=GCM0
c  Dana Royer's suggested glacial intervals
c	if(i.le.363.and.i.ge.346) GCM=GCM0*GLAC
c	if(i.le.30.and.i.ge.0) GCM=GCM0*GLAC
c	if(i.le.327.and.i.ge.290) GCM=GCM0*GLAC
c glacial intervals in Bob's GEOCARB code
	if(i.le.340.and.i.ge.260) GCM=GCM0*GLAC
	if(i.le.40.and.i.ge.0) GCM=GCM0*GLAC
c glacial intervals in Bob's GEOCARB code --  ALTERNATE GLACIAL INTERVALS
c	if(i.le.340.and.i.ge.240) GCM=GCM0*GLAC
c	if(i.le.30.and.i.ge.0) GCM=GCM0*GLAC
c glacial intervals in Bob's GEOCARB code -- reverse Glacial and interglacial
c	if(i.gt.340) GCM=GCM0/GLAC
c	if(i.le.260.and.i.ge.40) GCM=GCM0/GLAC
	fac=(i-1)/570.
c  RT is Y in Berner (2004), & RUN in Berner and Kothavala (2001)
c  is the coefficient of continental runoff versus temperature change
c  that is, runoff/runoff0 = 1 + RT*(T-T0), gotten from GCM runs
        if(i.gt.341) then
	  RT=0.025
        elseif(i.le.341.and.i.gt.261) then 
	  RT=0.045
        elseif(i.le.261.and.i.gt.41) then 
          RT=0.025
        else
	  RT=0.045
	endif
        GAS =0.75
        if(i.gt.151) then 
          Fc=GAS
        else
	  Fc=(GAS)+((1.0-GAS)/150.)*(151-i)
	endif  
c  fE is the factor of plant-assisted silicate weathering, relative to modern angiosperms
        if(i.gt.381) then  
	  fE=LIFE
        elseif(i.le.381.and.i.gt.351) then  
	  fE=(GYM-LIFE)*((381.-i)/30.)+LIFE
        elseif(i.le.351.and.i.gt.131) then   
	  fE=GYM
        elseif(i.le.131.and.i.gt.81) then  
	  fE=(1.-GYM)*((131-i)/50.)+GYM
        else
	  fE=1
	endif
c  GEOG is the change in avg land temp due to geography only, obtained via GCM runs
        GEOG(i) = temp(i)-12.4
c  fBB is the CO2-assisted acceleration of silicate weathering 
c  (looks like time-stepped value??? CHECK!)
c  in Berner (2004) fbb is biological negative feedbacks, but in this code
c  fbb includes other terms and factors
c
c  (2.0*RCO2/(1.0+RCO2))**FERT -- CO2-fertilization of plant growth
c  GCM*alog(RCO2) - Ws*fac + GEOG(i) -- the change in temperature relative to modern
c  includes CO2-warming, solar waxing, and geographic effect, equation 2.28
c
c Berner's GEOCARB modelling assumes a long-term balance of carbon fluxes, 
c  in two terms, fBB and fB
c
        if(i.eq.571) then
	  fBB=1
        elseif(i.le.570.and.i.gt.381) then  
          fBB=(1+0.087*GCM*alog(RCO2)-0.087*Ws*fac
     x                        +0.087*GEOG(i))*sqrt(RCO2)
        
        elseif(i.le.381.and.i.gt.351) then  
           fBB=((381.-i)/30.)*((1.0+0.087*GCM*alog(RCO2)-0.087*Ws*fac
     x  +0.087*GEOG(i))*(2.0*RCO2/(1.0+RCO2))**FERT)
     x  +((i-351)/30.)*(1.0+0.087*GCM*alog(RCO2)
     x  -0.087*Ws*fac+0.087*GEOG(i))*sqrt(RCO2)
        else 
c	  print *,fBB,GCM,RCO2,Ws,fac,FERT,GEOG(i)
          fBB=(1.0+0.087*GCM*alog(RCO2)-0.087*Ws*fac
     x 	    +0.087*GEOG(i))*(2.0*RCO2/(1.0+RCO2))**FERT
	endif
	Fwpy=fA(i)*fR(i)*kwpy*Spy
	Fwsy=fA(i)*fD(i)*kwsy*Ssy
	Fwgy=fA(i)*fR(i)*kwgy*Gy
	Fwcy=fA(i)*fD(i)*fL(i)*fE*fBB*kwcy*Cy
	Fwpa=fR(i)*Fwpa1
	Fwsa=fA(i)*fD(i)*Fwsa1
	Fwga=fR(i)*Fwga1
	Fwca=fA(i)*fD(i)*fL(i)*fE*fBB*Fwca1
	Fmp=fSR(i)*Fmp1
	Fms=fSR(i)*Fms1
	Fmg=fSR(i)*Fmg1
	Fmc=fSR(i)*Fc*Fmc1
	Fyop=Fwpa+Fmp
	Fyos=Fwsa+Fms
	Fyog=Fwga+Fmg
	Fyoc=Fwca+Fmc
	zzn=1.5
	aaJ=4.
	alphas =35.0*((oxy/38.0)**zzn)
c  start test
	alphac(i)=27.0+aaJ*(oxy/38.0-1.0)
c	print *,i,oxy,zzn,aaJ,alphas,alphacc,alphac(i)
c	if(i.eq.1) then
c	  pause
c	endif
c  end test
	Fbp = (1/alphas)*((DLSOC(i)-dlsy)*Fwsy+(DLSOC(i)-dlsa)*Fwsa 
     x  +(DLSOC(i)-dlpy)*Fwpy+(DLSOC(i)-dlpa)*Fwpa +(DLSOC(i)-dlsa)*Fms 
     x  +(DLSOC(i)-dlpa)*Fmp)
c  Fbg is burial Cflux of organic sediments	
        Fbg=(1/alphac(i))*((DLCOC(i)-dlcy)*Fwcy+(DLCOC(i)-dlca)*Fwca 
     x	+(DLCOC(i)-dlgy)*Fwgy+(DLCOC(i)-dlga)*Fwga +(DLCOC(i)-dlca)*Fmc 
     x  +(DLCOC(i)-dlga)*Fmg)
        Fbs=Fwpy+Fwpa+Fwsy+Fwsa +Fms +Fmp-Fbp
c  Fbc is burial Cflux of carbonate sediments	
        Fbc=Fwgy+Fwga+Fwcy+Fwca +Fmc +Fmg-Fbg
c  the following few statements were necessary because the commented line 
c  just below (if(i.lt.571) . . . ) would not compile correctly.  
        aaa1=(Fbg+(15./8.)*Fbp)*Dt
	aaa2=(Fwgy+Fwga+Fmg)*Dt
	aaa3=(15./8.)*(Fwpy+Fwpa+Fmp)*Dt
	aaa4=aaa1-aaa2-aaa3
        if(i.lt.571) oxy = oxy + aaa4
c        if(i.lt.571) oxy = oxy +(Fbg+(15./8.)*Fbp)*Dt -(Fwgy+Fwga+Fmg)*Dt 
c     x   -(15./8.)*(Fwpy+Fwpa+Fmp)*Dt

        Spy=Spy+(Fbp-Fwpy-Fyop)*Dt
        Ssy =Ssy +(Fbs-Fwsy-Fyos)*Dt
        Gy= Gy+ (Fbg-Fwgy-Fyog)*Dt
        Cy=Cy + (Fbc-Fwcy-Fyoc)*Dt
c  CT is set near the start of the program
        Ca = CT-Gy - Ga - Cy -2.0
        dlpy = dlpy +((DLSOC(i)-dlpy-alphas)*Fbp/Spy)*Dt
        dlpa = dlpa + (Fyop*(dlpy-dlpa)/Spa)*Dt
        dlsy = dlsy+((DLSOC(i)-dlsy)*Fbs/Ssy)*Dt
        dlsa = dlsa + (Fyos*(dlsy-dlsa)/Ssa)*Dt
        dlgy=dlgy+((DLCOC(i)-dlgy-alphac(i))*Fbg/Gy)*Dt
        dlga =dlga+(Fyog*(dlgy-dlga)/Ga)*Dt
        dlcy=dlcy+((DLCOC(i)-dlcy)*Fbc/Cy)*Dt
        dlca=dlca+(Fyoc*(dlcy-dlca)/Ca)*Dt
c  commented out: print out some values for diagnostics
c	if(k1.eq.10.and.k2.eq.9.and.k3.eq.1.and.k4.eq.1) 
c     x  print *, i,fBB,ewfBB,oldfBB,Fwcy,Fwpa,Fwsa,Fwga,Fwca,Fmp,
c     x    Fms,Fmg,Fmc
c	if(k1.eq.10.and.k2.eq.9.and.k3.eq.1.and.k4.eq.1) 
c     x  print *, i,DLSOC(i),dlsy,dlsa,dlpy,dlpa,RCO2
c  STARTING HERE IS NEW CODE FOR geocarbsulf volc
c  fAD is total runoff factor for silicate weathering
c  obtained with GCM simulations and continental reconstructions
        Roc = (Sr(i)/10000.)+0.7
c  vary anv from 0 to 0.015, is Berner parameter NV 
c  anv is a parameter that scales the influence of volcanic weathering on Sr-87/86 ratios.
c  anv=0 means no accelerated weathering of volcanic rocks, as opposed to plutonic/metamorphic
        anv=0.015
        Rg =0.722-anv*(1-fRT(i)/1.063)
        Rv =0.704
c  avnv is the ratio of volcanic weathering CO2-consumption rate (basalts) 
c                    to plutonic weathering CO2-consumption rate (granites)
c  avnv=4 in the version of code used for 2010 GoldSchmidt conference poster.
c  obtained by averaging values from Taylor's dissertation (5) and Taylor et al 1999 (3)
c  avnv=5 is Berner's preferred value for GEOCARBSULF,
c                 gotten from Aaron Taylor's dissertation
	avnv=4.0
c Fv0 =.3 of total Fwsi0=6.67 at x=0; Fob0 is basalt-seawater 
c flux =1/3 of total ocean imbalance  with Fv0 = 2/3
c        Fob0=0.75
c  for GEOCARBSULF volcanic, there are two flavors of code.  The first code uses BoB's 2007
c  algorithm, enshrined in a BASIC code he gave me in 2007.  These codes have the letter "v" in the
c  filename, in place of "l".  For the newer 2010 GEOCARBSULF-volcanic algorithm, based on a 2010
c  BBerner Basic code, the letters "v10" are in the filename.  The new-improved aspects of the 2010
c  code includes slightly different carbon-isotope data-inputs, one change in the strontium
c  data-input time series, a change in the formula for fR, involving a power to the 0.67(!), and the
c  parameters VNV (increase from 2 to 4) and NV (increase from 0.008 to 0.015), and the parameter
c  Fob0 (from 0.75 to 4), and switching alphac(i) from a table of values to being computed from the
c  model-generated oxygen time series.  The Newton-Raphson rootfinding algorithm 
c  works OK with estimating the alphac(i) parameter from model-derived oxygen, but 
c  the quasi-bisection routine had difficulty with this last feature.  For this reason, and the previous
c  experience that the bisection and Newton-Raphson results were not dramatically different (largest
c  differences were for unlikely parameter choices where the datafit was already poor.),
c  I retain Newton-Raphson for the export codes.
        Fob0=4.0
c	Fob0=FOBO  --- this was an option to sample a range of values in the K2 loop
	Xvolc0=0.35
	Fwsi0 =6.67
        Rcy = Rcy +((Roc-Rcy)*Fbc/Cy)*Dt
        Rca = Rca +((Rcy-Rca)*Fyoc/Ca)*Dt
        Avlc =((Rv-Roc)*fSR(i)*Fob0)/(Fbc-Fwcy-Fwca)
        Bvlc = Fwcy/(Fbc-Fwcy-Fwca)
	Dvlc = Fwca/(Fbc-Fwcy-Fwca)
	Evlc = Fbc/(Fbc-Fwcy-Fwca)
        Xvolc= (Avlc +Bvlc*Rcy+Dvlc*Rca -Evlc*Roc+Rg)/(Rg-Rv)
        fAD(i)=fA(i)*fD(i)
	fvolc=(avnv*Xvolc+1.0-Xvolc)/(avnv*Xvolc0+1.0-Xvolc0)
        fB=(Fbc-Fwcy-Fwca)/((fAD(i)**(0.65))*fE*fR(i)*fvolc*Fwsi0)
c  ENDING HERE IS NEW CODE FOR geocarbsulf volc
c	print *,fB,Fbc,Fwcy,Fwca,fAD(i),fE,fR(i),Fwsi0
c	print *,Xvolc,Avlc,Bvlc,Dvlc,Evlc,Rg,Rv
c	pause
c        print *,'GYM,LIFE,ACT,FERT'
        n=0
        if(i.gt.381) then 
c  this first step is an initialization kluge to ensure that the convergence condition 
c  is not satisfied accidentally at the first step
          RCO2old=RCO2*2.
c	  if((i/10)*10.eq.i) print *,'in loop ',i
          do while(abs(RCO2/RCO2old-1.0).gt.0.001)
	    RCO2old=RCO2
            Fbbs= (RCO2**(0.5+ACT*GCM))*((1.0+RT*GCM*alog(RCO2)
     x        -RT*Ws*fac+RT*GEOG(i))**0.65)
     x        *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
            W=((0.5+ACT*GCM)*RCO2**(-0.5+ACT*GCM))*((1+RT*GCM*alog(RCO2)
     x       -RT*Ws*(fac)+RT*GEOG(i))**0.65)
     x       *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
            V=(RCO2**(0.5+ACT*GCM))*0.65*((1.0+RT*GCM*alog(RCO2)
     x        -RT*Ws*(fac)+RT*GEOG(i))**(-0.35))*(RT*GCM/RCO2)
     x         *exp(-ACT*Ws*fac)*exp(ACT*GEOG(i))
            fPBS = W + V
            if(RCO2.gt.((Fbbs-fB)/fPBS)) then
c damp the iteration to avoid overshoot
              RCO2=RCO2-0.9*((Fbbs-fB)/fPBS)
	    else
c convert the iteration to geometric shrinkage to avoid nonpositive value in overshoot
              RCO2=RCO2*0.2
	    endif
	  end do
  777 format(i5,8g13.5)
        elseif(i.le.381.and.i.gt.349) then
          RCO2old=RCO2*2.
	  n=0
          do while(abs(RCO2/RCO2old-1.0).gt.0.001)
            RCO2old=RCO2
            oldfBBS=(RCO2**(0.5+ACT*GCM))*((1+RT*GCM*alog(RCO2)
     x        -RT*Ws*(fac)+RT*GEOG(i))**0.65)
     x        *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
            oldW=(0.5+ACT*GCM)*RCO2**(-0.5+ACT*GCM)
     x          *((1.0+RT*GCM*alog(RCO2)-RT*Ws*(fac)
     x          +RT*GEOG(i))**0.65)
     x          *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
            oldV=RCO2**(0.5+ACT*GCM)*0.65*((1.0+RT*GCM*alog(RCO2)
     x       -RT*Ws*(fac)+RT*GEOG(i))**(-0.35))*(RT*GCM/RCO2)
     x       *exp(-ACT*Ws*fac)*exp(ACT*GEOG(i))
            oldfPBS = oldW + oldV
	    oldX=0.
            ewfBBS=((2.0**FERT)*RCO2**(FERT+ACT*GCM))
     x       *((1+RCO2)**(-FERT))
     x   *((1.0+RT*GCM*alog(RCO2)-RT*Ws*fac+RT*GEOG(i))**0.65)
     x       *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
           ewW=(2.0**FERT)*(FERT+ACT*GCM)*(RCO2**(FERT+ACT*GCM-1.0))
     x 	    *((1.0+RCO2)**(-FERT))*((1.0+RT*GCM*alog(RCO2)
     x 	    -RT*Ws*(fac)+RT*GEOG(i))**0.65)
     x 	    *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
            ewV=(-FERT*(1.0+RCO2)**(-(1.0+FERT)))
     x	  *((2**FERT)*RCO2**(FERT+ACT*GCM))*((1.0+RT*GCM*alog(RCO2)
     x	  -RT*Ws*(fac)+RT*GEOG(i))**0.65)
     x	  *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
            ewX=0.65*((1.0+RT*GCM*alog(RCO2)-RT*Ws*(fac) 
     x     +RT*GEOG(i))**(-0.35))*(RT*GCM/RCO2)
     x	 *(2**FERT*RCO2**(FERT+ACT*GCM))*((1+RCO2)**(-FERT))
     x	 *exp(-ACT*Ws*fac)*exp(ACT*GEOG(i))
            ewfPBS=ewW+ewV+ewX
	    fBBS=((i-349)/32.)*oldfBBS + ((381-i)/32.)*ewfBBS
	    fPBS=((i-349)/32.)*oldfPBS + ((381-i)/32.)*ewfPBS
c	  oldfBBs=(float(i-349)/32.)*oldfBBs
c	  ewfBBs=(float(381-i)/32.)*ewfBBs
            if(RCO2.gt.((fBBS-fB)/fPBS)) then
c damp the iteration to avoid overshoot
              RCO2=RCO2-0.9*((fBBS-fB)/fPBS)
	    else
c convert the iteration to geometric shrinkage to avoid nonpositive value in overshoot
              RCO2=RCO2*0.2
	    endif
            n=n+1
          end do
        else	
          RCO2old=RCO2*2.
c	  if((i/10)*10.eq.i) print *,'in loop ',i
          do while(abs(RCO2/RCO2old-1.0).gt.0.001)
	    RCO2old=RCO2
            Fbbs=((2**FERT)*RCO2**(FERT+ACT*GCM))*((1+RCO2)**(-FERT))
     x	         *((1+RT*GCM*alog(RCO2)-RT*Ws*(fac)+RT*GEOG(i))**0.65)
     x	         *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
            W=(2**FERT)*(FERT+ACT*GCM)*(RCO2**(FERT+ACT*GCM-1))
     x	       *((1+RCO2)**(-FERT))*((1+RT*GCM*alog(RCO2)
     x	       -RT*Ws*(fac)+RT*GEOG(i))**0.65)
     x	       *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
       V=(-FERT*(1+RCO2)**(-(1.+FERT)))*((2**FERT)*RCO2**(FERT+ACT*GCM))
     x	    *((1+RT*GCM*alog(RCO2)-RT*Ws*(fac)+RT*GEOG(i))**0.65)
     x	    *exp(-ACT*Ws*(fac))*exp(ACT*GEOG(i))
            X=0.65*((1+RT*GCM*alog(RCO2)-RT*Ws*(fac) 
     x         +RT*GEOG(i))**(-0.35))*(RT*GCM/RCO2)
     x	       *(2**FERT*RCO2**(FERT+ACT*GCM))*((1+RCO2)**(-FERT))
     x	       *exp(-ACT*Ws*fac)*exp(ACT*GEOG(i))
            fPBS=W+V+X
            if(RCO2.gt.((Fbbs-fB)/fPBS)) then
c damp the iteration to avoid overshoot
              RCO2=RCO2-0.9*((Fbbs-fB)/fPBS)
	    else
c convert the iteration to geometric shrinkage to avoid nonpositive value in overshoot
              RCO2=RCO2*0.2
	    endif
          end do 
        endif
c	if(k1.eq.10.and.k2.eq.9.and.k3.eq.1.and.k4.eq.1) 
c     x	  print *,i,' fB, fBBs ',fB, fBBs,ewfBBs,oldfBBs,RCO2
c      if(k1.eq.10.and.k2.eq.9.and.k3.eq.1.and.k4.eq.1.and.i.eq.346)then
c     	  print *,i,' fB, fBBs ',fB, fBBs,ewfBBs,oldfBBs,RCO2
c	  stop
c      endif
c  the CO2 ppm is converted from RCO2 by last My average value = 250 ppm
        tau=15 + 6*alog(RCO2)-12.8*fac+GEOG(i)
        oxy2 =100*(oxy/(oxy+143.))
        ppm=250.*RCO2
c  the indexing here is time equals (i-1)Ma, save the ppm values at 10-Ma intervals,
c    K=1 --> 0Ma
	if(((i-1)/10)*10.eq.i-1) then
	  Ma=-i+1
c          print *, Ma, ppm
c	  write(8,*) -i,ppm
          k=1+i/10
c  save CO2 level (ppm) or oxygen (mass percent)
          pp(k1,k2,k3,k4,k)=ppm
c          pp(k1,k2,k3,k4,k)=oxy2
	endif
        gcsppm(i)=ppm
	aage(i)=i-1
      end do
c  compute variance reduction -- compare variance of residual to variance of data
c  normalized -- sum(pred-data)^2/sum(data)^2
c  two quantities saved:  
c  sqrt(chisq/dof) for datafit of the ndat (40?) interval averaged PPM values
c  misfit variance normalized by variance explained by constant ppm=250
c  variance ratio is not square-rooted. 
      sumsq=0.
c      sumdatsq=0.
c      do i=1,ndat
c        diff=alog(gcsppm(iage(i))/pppm(i))
c	sumsq=sumsq+(diff)**2
c        diff=alog(pppm(i)/250.)
c	sumdatsq=sumdatsq+(diff)**2
c      end do
c      varred=sumsq/sumdatsq
c  note that the array iage() holds the times at which the data-averages have been centered
c  this enables the code to skip over the gap in the CO2-proxy data at roughly 255Ma.
      do i=1,ndat
        diff=alog(gcsppm(iage(i))/pppm(i))/dppm(i)
	sumsq=sumsq+(diff)**2
      end do
      chisq_dof=sqrt(sumsq/ndat)
      varred=sumsq/sumsq0
c      print *, varred
      pp(k1,k2,k3,k4,59)=chisq_dof
      pp(k1,k2,k3,k4,60)=varred
      varredmin=amin1(varredmin,pp(k1,k2,k3,k4,60))
	end do
	end do
c        print *,'GYM,LIFE,ACT,FERT,deltaT'
c        print *,GYM,LIFE,ACT,FERT,deltaT
c        print *,'varredmin ',varredmin
c        pause
	end do
c        print *,'GYM,LIFE,ACT,FERT'
c        print *,GYM,LIFE,ACT,FERT
        if(k1.eq.10) print *,'GYM,LIFE,ACT,FERT,deltaT,varredmin'
        print *,GYM,LIFE,ACT,FERT,deltaT,varredmin
	end do
c        print *,'GYM,LIFE,ACT,FERT,deltaT'
c        print *,GYM,LIFE,ACT,FERT,deltaT
c      close(8)
c  sift through the runs to find acceptable data fits  (chiquare <= NDAT; bias < 0.3)
        do k=1,2
          do j=1,4
            do i=1,10
              jkk(i,j,k)=0
            end do
          end do
        end do
        do k1=1,10
        do k2=1,10
        do k3=1,10
        do k4=1,10
          if(pp(k1,k2,k3,k4,60).le.varredmin*1.5) then
		  jkk(k1,1,1)=jkk(k1,1,1)+1
		  jkk(k2,2,1)=jkk(k2,2,1)+1
		  jkk(k3,3,1)=jkk(k3,3,1)+1
		  jkk(k4,4,1)=jkk(k4,4,1)+1
	  endif
	  if(pp(k1,k2,k3,k4,60).le.varredmin*1.2) then
		  jkk(k1,1,2)=jkk(k1,1,2)+1
		  jkk(k2,2,2)=jkk(k2,2,2)+1
		  jkk(k3,3,2)=jkk(k3,3,2)+1
		  jkk(k4,4,2)=jkk(k4,4,2)+1
	  endif
        end do
        end do
        end do
        end do
	print *,'deltaT=',deltaT,iiit,ndel
	print *,'minimum chi-squared misfit',varredmin
	print *,'parameters for runs that fit with varred 50% from min'
	print *,'ACT'
	print *,(jkk(i,1,1),i=1,10)	  
	print *,'FERT'
	print *,(jkk(i,2,1),i=1,10)	  
	print *,'LIFE'
	print *,(jkk(i,3,1),i=1,10)	  
	print *,'GYM'
	print *,(jkk(i,4,1),i=1,10)	  
	print *,'   '
	print *,'parameters for runs that fit with varred 20% from min'
	print *,'ACT'
	print *,(jkk(i,1,2),i=1,10)	  
	print *,'FERT'
	print *,(jkk(i,2,2),i=1,10)	  
	print *,'LIFE'
	print *,(jkk(i,3,2),i=1,10)	  
	print *,'GYM'
	print *,(jkk(i,4,2),i=1,10)
c      	  pause
        print *,'writing chisq to disk',deltaT
        write(14) deltaT
        write(14)((((pp(k1,k2,k3,k4,59),k4=1,10),k3=1,10)
     x                                     ,k2=1,10),k1=1,10)
        do k1=1,10
          do k2=1,10
	    do k3=1,10
	      do k4=1,10
	        write(14)(pp(k1,k2,k3,k4,j),j=1,58)
	      end do
	    end do
	  end do
        end do
      end do
      close(14)
      stop
      end