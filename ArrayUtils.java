import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * This class contains a number of methods helpful to using arrays.
 * 
 * @author (Spencer Ewall) 
 */
public final class ArrayUtils
{
    /**
     * Prints the given array to the console.
     * 
     * @param the array to be printed
     */
    public static void printArray(int[] arr)
    {
        int lastI = emptyIndex(arr);
        System.out.print("["+arr[0]);
        for (int i=1; i<lastI; i++)
        {
            System.out.print("\t"+arr[i]);
        }
        System.out.print("]");
        System.out.println();
    }
    public static void printArray(double[] arr)
    {
        DecimalFormat form = new DecimalFormat();
        form.setMaximumFractionDigits(5);
        int lastI = emptyIndex(arr);
        StringBuffer result = new StringBuffer();
        result.append("[");
        result.append(form.format(arr[0]));
        for (int i=1; i<lastI; i++)
        {
            result.append("\t");
            result.append(form.format(arr[i]));
        }
        result.append("]");
        System.out.print(result);
        System.out.print("]");
        System.out.println();
    }
    /**
     * Prints the indices of the given array and the array itself.
     * The indices are printed on a first line and the array is printed below it.
     * Tabs are used to line up the indices with their appropriate elements.
     * 
     * @param the array to be printed
     */
    public static void printArrayI(int[] arr)
    {
        printArray(fillIncArray(emptyIndex(arr)));
        printArray(arr);
    }
    public static void printArrayI(double[] arr)
    {
        printArray(fillIncArray(emptyIndex(arr)));
        printArray(arr);
    }
    public static double[] merge(double[] arr1, double[] arr2)
    {
        double[] arr3 = new double[arr1.length+arr2.length];
        for (int i=0; i<arr1.length; i++)
        {
            arr3[i]=arr1[i];
        }
        for (int i=arr1.length; i<arr3.length; i++)
        {
            arr3[i]=arr2[i-arr1.length];
        }
        return arr3;
    }
    
    /**
     * Returns an array of given length whose indices are filled with incrementing integers.
     * The integers begin in cell 0 from a given start value and increment by a given
     * value.
     * 
     * @param len the length of the array to be filled.
     * @param startVal the value to start counting from
     * @param inc the value to increment by
     */
    public static int[] fillIncArray(int len,int startVal, int inc)
    {
        int[] arr = new int[len];
        for(int i=0; i<len; i++)
        {
            arr[i] = i;
        }
        return arr;
    }
    /**
     * Functions like {@link ArrayUtils#fillIncArray(int,int,int)}
     * {@code startVal} defaults to 0 and {@code inc} defaults to 1.
     */
    public static int[] fillIncArray(int len)
    {
        return fillIncArray(len, 0, 1);
    }
    /**
     * Functions like {@link ArrayUtils#fillIncArray(int,int,int)}.
     * {@code inc} defaults to 1.
     */
    public static int[] fillIncArray(int len,int startVal)
    {
        return fillIncArray(len, startVal, 1);
    }
    
    /**
     * Gives the first index of an empty tail of an array.
     * <p><p>
     * E.G. emptyIndex({.4,8.5,2.3,3.9,.7,0,0,0,0}) = 5
     * @param arr an array of doubles
     * @return the index number directly after 
     */
    public static int emptyIndex(double[] arr)
    {
        int i = arr.length-1;
        if (arr[i]!=0)
            return arr.length;
        while (arr[i]==0.0) {
            if(i==-1) return -1;
            i--;
        }
        return i+1;
    }
    
    /**
     * Typecasts an array of integers to an array of doubles.  Each element 
     * of the original array is typecasted indiviually and placed in the same
     * location in a new array of type double.
     * 
     * @param arr an int array
     * @return an array of doubles corresponding to arr
     */
    public static double[] toDouble(int[] arr)
    {
        double[] dubArr=new double[arr.length];
        for (int i=0;i<arr.length;i++) {
            dubArr[i]=(double) arr[i];
        }
        return dubArr;
    }
    /**
     * Typecasts an array of doubles to an array of integers.  Each element 
     * of the original array is typecasted indiviually and placed in the same
     * location in a new array of type int.
     * 
     * @param arr a double array
     * @return an array of ints corresponding to arr
     */
    public static int[] toInt(double[] arr)
    {
        int[] dubArr=new int[arr.length];
        for (int i=0;i<arr.length;i++) {
            dubArr[i]=(int) arr[i]; }
        return dubArr;
    }
    
    
    
    
    
    
    
    
    /**
     * Functions similarly to {@link ArrayUtils#emptyIndex(double[])}.
     * @param arr an array of ints
     * @see ArrayUtils#emptyIndex(double[])
     */
    public static int emptyIndex(int[] arr)
    {
        return emptyIndex(toDouble(arr));
    }
    /**
     * Functions similarly to {@link ArrayUtils#emptyIndex(double[])}.
     * @param arr an array of floats
     * @see ArrayUtils#emptyIndex(double[])
     */
    public static int emptyIndex(float[] arr)
    {
        return emptyIndex(toDouble(arr));
    }
    /**
     * Functions similarly to {@link ArrayUtils#emptyIndex(double[])}.
     * @param arr an array of longs
     * @see ArrayUtils#emptyIndex(double[])
     */
    public static int emptyIndex(long[] arr)
    {
        return emptyIndex(toDouble(arr));
    }
    /**
     * Functions similarly to {@link Utilities#toDouble(int[])}
     * @param arr a float array to be casted
     * @see ArrayUtils#toDouble(int[])
     */
    public static double[] toDouble(float[] arr)
    {
        double[] dubArr=new double[arr.length];
        for (int i=0;i<arr.length;i++) {
            dubArr[i]=(double) arr[i]; }
        return dubArr;
    }
    /**
     * Functions similarly to {@link Utilities#toDouble(int[])}
     * @param arr a long array to be casted
     * @see ArrayUtils#toDouble(int[])
     */
    public static double[] toDouble(long[] arr)
    {
        double[] dubArr=new double[arr.length];
        for (int i=0;i<arr.length;i++) {
            dubArr[i]=(double) arr[i]; }
        return dubArr;
    }
    /**
     * Functions similarly to {@link Utilities#toInt(double[])}
     * @param arr a float array to be casted
     * @see @link ArrayUtils#toInt(double[])
     */
    public static int[] toInt(float[] arr)
    {
        int[] dubArr=new int[arr.length];
        for (int i=0;i<arr.length;i++) {
            dubArr[i]=(int) arr[i]; }
        return dubArr;
    }
    /**
     * Functions similarly to {@link Utilities#toInt(double[])}
     * @param arr a long array to be casted
     * @see @link ArrayUtils#toInt(double[])
     */
    public static int[] toInt(long[] arr)
    {
        int[] dubArr=new int[arr.length];
        for (int i=0;i<arr.length;i++) {
            dubArr[i]=(int) arr[i]; }
        return dubArr;
    }
}
