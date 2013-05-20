
package gpfinance.datatypes;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public enum Fund {
    RAD(0, "Research and Development as a % of Sales YoY");
    
    private int code;
    private String label;

    private Fund(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
