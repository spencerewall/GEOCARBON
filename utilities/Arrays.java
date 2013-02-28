package utilities;

public class Arrays
{
    public static double[] average(double[] arr1, double[] arr2)
    {
        if (arr1.length!=arr2.length)
        {
            System.out.println("Error: Array.getAverage: array lengths unequal");
        }
        double[] avg = new double[arr1.length];
        for (int i=0; i<arr1.length; i++)
        {
            avg[i]=arr1[i]+arr2[i];
            avg[i]=avg[i]/2;
        }
        return avg;
    }
    public static int minLength(double[]... args)
    {
        int min=(int) Double.POSITIVE_INFINITY;
        for(double[] a:args)
        {
            min = Math.min(min, a.length);
        }
        return min;
    }
    public static double[] roundDecimal(double[] arr, int decimalPlaces)
    {
        for (int i = 0; i<arr.length; i++)
        {
            String temp = arr[i]+"";
            int decI = temp.indexOf(".");
            arr[i] = Double.parseDouble(temp.substring(0, decI+decimalPlaces+1));
        }
        return arr;
    }
}
