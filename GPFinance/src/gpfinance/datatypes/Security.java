
package gpfinance.datatypes;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Security {
    public String ticker;
    public double[] values;
    
    public Security(String name, double[] values){
        this.values = values;
        ticker = name;
    }
}
