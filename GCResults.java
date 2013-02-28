final class GCResults {
        private double[] co2, xvolc, oxy;
        public GCResults(double[] co2, double[] oxy, double[] xvolc){
            this.co2=co2; this.xvolc=xvolc; this.oxy=oxy;
        }
        public double[] getCO2() {
            return co2;
        }
        public double[] getXVolc() {
            return xvolc;
        }
        public double[] getOxy() {
            return oxy;
        }
    }