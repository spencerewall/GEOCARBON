public class Time implements Comparable<Time>
{
    /** Billion Years Ago.  Ga short for Gigannum */
    public static final Time Ga = new Time(-1000000000);
    /** Million years ago.  Ma short for Megannum */
    public static final Time Ma = new Time(-1000000);
    /** Thousand years ago.  ka short for kiloannum */
    public static final Time ka = new Time(-1000);
    /** Hundred years ago.  ha short for hectoannum */
    public static final Time ha = new Time(-100);
    /** Standard year ago.  bp short for before present */
    public static final Time bp = new Time(-1);
    
    int time;
    Time myScale;
    /**
     * Constructor for the time object.
     */
    public Time(int year)
    {
        time = year;
        myScale = bp;
    }
    public Time(int scaledYear, Time scale)
    {
        time = scaledYear;
        myScale=scale;
    }
    public int getYearBP()
    {
        return time*myScale.getYearBP();
    }
    public int getScaledYear()
    {
        return time;
    }
    
    public int getScaleInt()
    {
        return myScale.getYearBP();
    }
    public String getScaleString()
    {
        if (myScale.equals(Ga))
            return "Ga";
        if (myScale.equals(Ma))
            return "Ma";
        if (myScale.equals(ka))
            return "ka";
        if (myScale.equals(ha))
            return "ha";
        if (myScale.equals(bp))
            return "bp";
        return myScale.getYearBP()+"yrs";
    }
    
    public boolean equals(Object other)
    {
        if (other instanceof Time)
        {
            return (getYearBP() == ((Time) other).getYearBP());
        }
        return false;
    }
    public int compareTo(Time other)
    {
        return getYearBP()-other.getYearBP();
    }
}
