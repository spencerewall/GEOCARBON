import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Collection;
import java.util.LinkedList;
//File;

public class TxtWriter
{
    FileWriter f;
    BufferedWriter writeTo;
    LinkedList <Collection<Number>>columns;
    LinkedList <String>colNames;
    int anonCols;
    
    
    public TxtWriter(String fileName)
    {
        try
        {
            FileWriter fstream = new FileWriter(fileName);
            writeTo = new BufferedWriter(fstream);
            columns = new LinkedList<Collection<Number>>();
            anonCols=0;
        }
        catch (java.io.IOException e)
        {
            System.out.println("Error in DataWriter[constructor]: "+e.getMessage());
            System.exit(0);
        }
    }
    public boolean addColumn(Collection<Number> c)
    {
        anonCols++;
        boolean addBool = columns.add(c);
        if (addBool == false)
        {
            return false;
        }
        addBool = colNames.add("");
        if (addBool == false)
        {
            columns.remove(c);
            return false;
        }
        return true;
            
        
    }
}
