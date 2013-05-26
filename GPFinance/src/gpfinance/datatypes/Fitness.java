
package gpfinance.datatypes;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Fitness {
    public double returnValue;
    
    public Fitness(double returnValue){
        this.returnValue = returnValue;
    }
    
    public double getFitness(){
        return returnValue;
    }
    
    @Override
    public Fitness clone(){
        return new Fitness(this.returnValue);
    }
}
