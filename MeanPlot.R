library(MASS)

out <- read.table("output.dat")
kde2d(out)

library(KernSmooth)
fit = bkde2D(out$V1, out$V2)
persp(fit$x1, fit$x2, fit$fhat)
library(rgl)


myCols <- colorRampPalette(c("white", "green", "red"))
smoothScatter(out$V1, out$V2 , ylim=c(0,3000), xlim=c(0, -300), colramp = myCols)