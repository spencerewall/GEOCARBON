package factorinput;


/**
 * A framework for all factors that go into the model. Intended to be the most general qualifier 
 * for a model input.
 * 
 * @author (Spencer Ewall) 
 * @version (4/10/2012)
 */
public interface InputFactor
{
    double getNext();
    double[] getNexts(int n);
}
