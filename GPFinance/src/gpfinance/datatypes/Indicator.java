
package gpfinance.datatypes;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public interface Indicator {
    /**
     * This class exists to abstract an Indicator type into a "enum" class
     */
    
    public int getCode();
    public String getLabel();
}
